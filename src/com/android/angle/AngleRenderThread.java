package com.android.angle;

import java.util.concurrent.Semaphore;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

class AngleRenderThread extends Thread
{
   private static final Semaphore sEglSemaphore = new Semaphore(1);
	private boolean mDone=false;
	private boolean mPaused=false;
	private boolean mHasFocus=true;
	private boolean mHasSurface;
	private boolean mContextLost;
	private int mWidth=0;
	private int mHeight=0;
	private AngleRenderEngine mRenderEngine=null;
	private Runnable mBeforeDraw=null;
	private EGLHelper mEglHelper=null;
	
	AngleRenderThread(AngleRenderEngine renderEngine)
	{
		super();
		mRenderEngine = renderEngine;
		setName("AngleRenderThread");
	}

	@Override
	public void run()
	{
		/*
		 * When the android framework launches a second instance of an
		 * activity, the new instance's onCreate() method may be called before
		 * the first instance returns from onDestroy().
		 * 
		 * This semaphore ensures that only one instance at a time accesses
		 * EGL.
		 */
		try
		{
			try
			{
				sEglSemaphore.acquire();
			} catch (InterruptedException e)
			{
				Log.e("AngleRenderThread",
						"sEglSemaphore.acquire() exception: " + e.getMessage());
				return;
			}
			guardedRun();
		} catch (InterruptedException e)
		{
			Log.e("AngleRenderThread",
					"guarded() exception: " + e.getMessage());
		} finally
		{
			sEglSemaphore.release();
		}
	}

	private void guardedRun() throws InterruptedException
	{
		mEglHelper = new EGLHelper();
		int[] configSpec = mRenderEngine.getConfigSpec();
		mEglHelper.start(configSpec);

		boolean tellRendererSurfaceCreated = true;
		boolean tellRendererSurfaceChanged = true;

		while (!mDone)
		{
			int w, h;
			boolean changed;
			boolean needStart = false;
			synchronized (this)
			{
				if (mBeforeDraw != null)
				{
					mBeforeDraw.run();
				}
				if (mPaused)
				{
					mEglHelper.finish();
					needStart = true;
				}
				if (needToWait())
				{
					while (needToWait())
					{
						wait();
					}
				}
				if (mDone)
				{
					break;
				}
				changed = AngleSurfaceView.mSizeChanged;
				w = mWidth;
				h = mHeight;
				AngleSurfaceView.mSizeChanged = false;
			}
			if (needStart)
			{
				mEglHelper.start(configSpec);
				tellRendererSurfaceCreated = true;
				changed = true;
			}
			if (changed)
			{
				AngleRenderEngine.gl = (GL10) mEglHelper.createSurface(AngleSurfaceView.mSurfaceHolder);
				tellRendererSurfaceChanged = true;
			}
			if (tellRendererSurfaceCreated)
			{
				mRenderEngine.loadTextures();
				tellRendererSurfaceCreated = false;
			}
			if (tellRendererSurfaceChanged)
			{
				mRenderEngine.sizeChanged(w, h);
				tellRendererSurfaceChanged = false;
			}
			if ((w > 0) && (h > 0))
			{
				/* draw a frame here */
				mRenderEngine.drawFrame();

				/*
				 * Once we're done with GL, we need to call swapBuffers() to
				 * instruct the system to display the rendered frame
				 */
				mEglHelper.swap();
			}
		}

		/*
		 * clean-up everything...
		 */
		mRenderEngine.shutdown();

		mEglHelper.finish();
	}

	private boolean needToWait()
	{
		return (mPaused || (!mHasFocus) || (!mHasSurface) || mContextLost)
				&& (!mDone);
	}

	public void surfaceCreated()
	{
		synchronized (this)
		{
			mHasSurface = true;
			mContextLost = false;
			notify();
		}
	}

	public void surfaceDestroyed()
	{
		synchronized (this)
		{
			mHasSurface = false;
			notify();
		}
	}

	public void onPause()
	{
		synchronized (this)
		{
			mPaused = true;
		}
	}

	public void onResume()
	{
		synchronized (this)
		{
			mPaused = false;
			notify();
		}
	}

	public void onWindowFocusChanged(boolean hasFocus)
	{
		synchronized (this)
		{
			mHasFocus = hasFocus;
			if (mHasFocus == true)
			{
				notify();
			}
		}
	}

	public void onWindowResize(int w, int h)
	{
		synchronized (this)
		{
			mWidth = w;
			mHeight = h;
			AngleSurfaceView.mSizeChanged = true;
		}
	}

	public void requestExitAndWait()
	{
		// don't call this from GLThread thread or it is a guaranteed
		// deadlock!
		synchronized (this)
		{
			mDone = true;
			notify();
		}
		try
		{
			join();
		} catch (InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
	}

	public void setBeforeDraw (Runnable beforeDraw)
	{
		mBeforeDraw=beforeDraw;
	}

}
