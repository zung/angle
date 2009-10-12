package com.android.angle;

import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

class AngleRenderThread extends Thread
{
	private static final int[] configSpec = { EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE };
	private static final Semaphore sEglSemaphore = new Semaphore(1);
	private boolean mDone=false;
	private boolean mPaused=true;
	private boolean mHasFocus=false;
	private boolean mHasSurface;
	private boolean mContextLost;
	private int mWidth=0;
	private int mHeight=0;
	private Runnable mBeforeDraw=null;
	private EGLHelper mEglHelper=null;
	
	AngleRenderThread()
	{
		super();
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
		mEglHelper.start(configSpec);

		boolean tellRendererSurfaceCreated = false;
		boolean tellRendererSurfaceChanged = false;

		while (!mDone)
		{
			int w, h;
			boolean changed=false;
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
					Log.d("AngleRenderThread","Waiting...");
					while (needToWait())
					{
						wait();
					}
					Log.d("AngleRenderThread","End wait!");
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
				Log.d("AngleRenderThread","Need Start");
				mEglHelper.start(configSpec);
				tellRendererSurfaceCreated = true;
				changed = true;
			}
			if (changed)
			{
				Log.d("AngleRenderThread","Changed");
				AngleRenderEngine.gl = (GL10) mEglHelper.createSurface(AngleSurfaceView.mSurfaceHolder);
				tellRendererSurfaceChanged = true;
			}
			if (tellRendererSurfaceCreated)
			{
				AngleRenderEngine.loadTextures();
				tellRendererSurfaceCreated = false;
			}
			if (tellRendererSurfaceChanged)
			{
				AngleRenderEngine.sizeChanged(w, h);
				tellRendererSurfaceChanged = false;
			}
			if ((w > 0) && (h > 0))
			{
				AngleRenderEngine.drawFrame();

				mEglHelper.swap();
			}
		}

		AngleRenderEngine.shutdown();

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
			Log.d("AngleRenderThread","PAUSED!!!");
			mPaused = true;
		}
	}

	public void onResume()
	{
		synchronized (this)
		{
			Log.d("AngleRenderThread","RESUME!!!");
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
