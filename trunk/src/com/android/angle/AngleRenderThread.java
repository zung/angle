package com.android.angle;

import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Main render thread based on API demos render thread
 * 
 * @author Ivan Pajuelo
 * 
 */
class AngleRenderThread extends Thread
{
	private static final int[] configSpec = { EGL10.EGL_DEPTH_SIZE, 0,
			EGL10.EGL_NONE };
	private static final Semaphore sEglSemaphore = new Semaphore(1);
	private boolean mDone = false;
	private boolean mPaused = true;
	private boolean mHasFocus = false;
	private boolean mHasSurface = false;
	private boolean mContextLost = true;
	private int mWidth = 0;
	private int mHeight = 0;
	private Runnable mBeforeDraw = null;
	private EGLHelper mEglHelper = null;
	private static GL10 gl = null;
	private boolean needStartEgl = true;
	private boolean needCreateSurface = false;
	private boolean needResize = false;

	AngleRenderThread()
	{
		super();
		setName("AngleRenderThread");
	}

	@Override
	public void run()
	{
		try
		{
			try
			{
				sEglSemaphore.acquire();
			} catch (InterruptedException e)
			{
				Log.e("AngleRenderThread", "sEglSemaphore.acquire() exception: "
						+ e.getMessage());
				return;
			}
			guardedRun();
		} catch (InterruptedException e)
		{
			Log.e("AngleRenderThread", "guarded() exception: " + e.getMessage());
		} finally
		{
			sEglSemaphore.release();
		}
	}

	private void guardedRun() throws InterruptedException
	{
		mEglHelper = new EGLHelper();
		mEglHelper.start(configSpec);

		long lCTM = 0;

		mDone = false;

		Log.d("AngleRenderThread", "run init");

		while (!mDone)
		{

			synchronized (this)
			{
				if (mPaused)
				{
					mEglHelper.finish();
					needStartEgl = true;
					Log.d("AngleRenderThread", "PAUSED...");
				}
				if (needToWait())
				{
					Log.d("AngleRenderThread", "Waiting...");
					while (needToWait())
					{
						wait();
					}
					Log.d("AngleRenderThread", "End wait!");
					lCTM = 0;
				}
				if (mDone)
					break;

				// Captures variables values synchronized
				needCreateSurface = AngleSurfaceView.mSizeChanged;
				AngleSurfaceView.mSizeChanged = false;
			}
			if (needStartEgl)
			{
				needStartEgl = false;
				Log.d("AngleRenderThread", "Need Start EGL");
				mEglHelper.start(configSpec);
				needCreateSurface = true;
				AngleTextureEngine.hasChanges = true;
			}
			if (needCreateSurface)
			{
				needCreateSurface = false;
				Log.d("AngleRenderThread", "needCreateSurface");
				gl = (GL10) mEglHelper
						.createSurface(AngleSurfaceView.mSurfaceHolder);
				needResize = true;
			}
			if (AngleTextureEngine.hasChanges)
			{
				Log.d("AngleRenderThread", "needLoadTextures");
				AngleMainEngine.loadTextures(gl);
			}
			if (needResize)
			{
				needResize = false;
				Log.d("AngleRenderThread", "needResize");
				AngleMainEngine.sizeChanged(gl, mWidth, mHeight);
			}
			if (mHasSurface && (AngleMainEngine.mWidth > 0)
					&& (AngleMainEngine.mHeight > 0))
			{
				AngleMainEngine.secondsElapsed = 0.0f;
				long CTM = System.currentTimeMillis();
				if (lCTM > 0)
					AngleMainEngine.secondsElapsed = (CTM - lCTM) / 1000.f;
				lCTM = CTM;

				if (mBeforeDraw != null)
					mBeforeDraw.run();

				AngleMainEngine.drawFrame(gl);

				mEglHelper.swap();
			}
		}

		mEglHelper.finish();
	}

	private boolean needToWait()
	{
		return (mPaused || (!mHasFocus) || (!mHasSurface) || mContextLost)
				&& (!mDone);
	}

	protected void surfaceCreated()
	{
		synchronized (this)
		{
			mHasSurface = true;
			mContextLost = false;
			notify();
		}
	}

	protected void surfaceDestroyed()
	{
		synchronized (this)
		{
			AngleMainEngine.onDestroy(gl);
			mContextLost = true;
			mHasSurface = false;
			notify();
		}
	}

	protected void surfaceChanged(int width, int height)
	{
		synchronized (this)
		{
			mWidth = width;
			mHeight = height;
			AngleSurfaceView.mSizeChanged = true;
		}
	}

	protected void onPause()
	{
		synchronized (this)
		{
			Log.d("AngleRenderThread", "PAUSED!!!");
			mPaused = true;
		}
	}

	protected void onResume()
	{
		synchronized (this)
		{
			Log.d("AngleRenderThread", "RESUMED!!!");
			mPaused = false;
			notify();
		}
	}

	protected void onWindowFocusChanged(boolean hasFocus)
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

	protected void requestExitAndWait()
	{
		if (!mDone)
		{
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
	}

	protected void setBeforeDraw(Runnable beforeDraw)
	{
		mBeforeDraw = beforeDraw;
	}

}
