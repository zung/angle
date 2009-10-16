package com.android.angle;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Surface view based on API demos
 * 
 * @author Ivan Pajuelo
 *
 */
public class AngleSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback
{
	private static AngleRenderThread mRenderThread;
	public static boolean mSizeChanged = true;
	public static SurfaceHolder mSurfaceHolder;

	public AngleSurfaceView(Context context)
	{
		super(context);
		doInit(context);
	}

	private void doInit(Context context)
	{
		AngleMainEngine.mContext = context;
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		mRenderThread = new AngleRenderThread();
		mRenderThread.start();
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		mRenderThread.surfaceCreated();
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{
		mRenderThread.surfaceDestroyed();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
	{
		mRenderThread.surfaceChanged(w, h);
	}

	/**
	 * Must be called in Activity.onPause
	 */
	public void onPause()
	{
		mRenderThread.onPause();
	}

	/**
	 * Must be called in Activity.onResume
	 */
	public void onResume()
	{
		mRenderThread.onResume();
	}

	/**
	 * Must be called in Activity.onDestroy
	 */
	public void onDestroy()
	{
		mRenderThread.requestExitAndWait();
	}

	/**
	 * Set the callback function to be called before draw
	 * every frame
	 *  
	 * @param beforeDraw Runnable derived class (usually game engine).
	 */
	public void setBeforeDraw(Runnable beforeDraw)
	{
		mRenderThread.setBeforeDraw(beforeDraw);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mRenderThread.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		mRenderThread.requestExitAndWait();
	}
}
