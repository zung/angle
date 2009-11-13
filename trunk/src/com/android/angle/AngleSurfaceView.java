package com.android.angle;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
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
	private static AngleMainEngine mRenderEngine;

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
		AngleMainEngine.mContext = context;
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		mRenderEngine = new AngleMainEngine(10);
		mRenderThread = new AngleRenderThread(mRenderEngine);
		mRenderThread.start();
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		if (mRenderThread != null)
			mRenderThread.surfaceCreated();
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{
		if (mRenderThread != null)
			mRenderThread.surfaceDestroyed();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
	{
		if (mRenderThread != null)
			mRenderThread.surfaceChanged(w, h);
	}

	/**
	 * Must be called in Activity.onPause
	 */
	public void onPause()
	{
		if (mRenderThread != null)
			mRenderThread.onPause();
	}

	/**
	 * Must be called in Activity.onResume
	 */
	public void onResume()
	{
		if (mRenderThread != null)
			mRenderThread.onResume();
	}

	/**
	 * Must be called in Activity.onDestroy
	 */
	public void onDestroy()
	{
		if (mRenderThread != null)
			mRenderThread.requestExitAndWait();
		mRenderThread = null;
		mRenderEngine = null;
		System.gc();
	}

	/**
	 * Set the callback function to be called before draw every frame
	 * 
	 * @param beforeDraw
	 *           Runnable derived class (usually game engine).
	 */
	public void setBeforeDraw(Runnable beforeDraw)
	{
		if (mRenderThread != null)
			mRenderThread.setBeforeDraw(beforeDraw);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if (mRenderThread != null)
			mRenderThread.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		if (mRenderThread != null)
			mRenderThread.requestExitAndWait();
	}

	public void addEngine(AngleAbstractEngine engine)
	{
		if (mRenderEngine != null)
			mRenderEngine.addEngine(engine);
	}

	public void notifyTo(Handler handler)
	{
		mRenderThread.mHandler=handler;
	}
}
