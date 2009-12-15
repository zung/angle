package com.android.angle;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Surface view based on API demos
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	public static AngleRenderThread mRenderThread;
	public static boolean mSizeChanged = true;
	public static SurfaceHolder mSurfaceHolder;
	private static AngleMainEngine mRenderEngine;
	private PowerManager.WakeLock mWakeLock;

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
		PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "Angle");
	}

	public void setAwake(boolean awake)
	{
		if (awake)
			mWakeLock.acquire();
		else
			mWakeLock.release();
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

	public void notifyTo(Handler handler)
	{
		mRenderThread.mHandler = handler;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mRenderThread != null)
			if (mRenderThread.mGameEngine != null)
				return mRenderThread.mGameEngine.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (mRenderThread != null)
			if (mRenderThread.mGameEngine != null)
				return mRenderThread.mGameEngine.onKeyDown(keyCode, event);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		if (mRenderThread != null)
			if (mRenderThread.mGameEngine != null)
				return mRenderThread.mGameEngine.onTrackballEvent(event);
		return super.onTrackballEvent(event);
	}

	/**
	 * Set the callback function to be called before draw every frame
	 * 
	 * @param beforeDraw
	 *           Runnable derived class (usually game engine).
	 */
	public void setGameEngine(AngleAbstractGameEngine gameEngine)
	{
		if (mRenderThread != null)
			mRenderThread.setGameEngine(gameEngine);
	}

}
