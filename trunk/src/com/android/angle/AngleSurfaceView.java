package com.android.angle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

	public AngleSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		doInit(context);
	}

	private void doInit(Context context)
	{
		//new AngleRenderEngine(context);
		AngleRenderEngine.mContext=context;
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		mRenderThread = new AngleRenderThread();
		mRenderThread.start();
	}

	public void setRenderEngine(AngleRenderEngine renderEngine)
	{
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
		mRenderThread.onWindowResize(w, h);
	}

	public void onPause()
	{
		mRenderThread.onPause();
	}

	public void onResume()
	{
		mRenderThread.onResume();
	}

	public void setBeforeDraw (Runnable beforeDraw)
	{
		mRenderThread.setBeforeDraw (beforeDraw);
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

	// ----------------------------------------------------------------------

}
