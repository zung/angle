package com.android.angle;

import android.content.Context;
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

	public void onPause()
	{
		mRenderThread.onPause();
	}

	public void onResume()
	{
		mRenderThread.onResume();
	}

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

	public void onDestroy()
	{
		mRenderThread.requestExitAndWait();
	}

	public void invalidateTextures()
	{
		mRenderThread.invalidateTextures();
	}
}
