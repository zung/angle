package com.android.angle;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class AngleActivity extends Activity
{
	public AngleSurfaceView mGLSurfaceView;
	public AngleSoundSystem SS;
	private AngleUI mCurrentUI=null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{
			SS=new AngleSoundSystem(this);
			Thread.sleep(100);
			mGLSurfaceView = new AngleSurfaceView(this);
			mGLSurfaceView.setAwake(true);
			mGLSurfaceView.start();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setUI(AngleUI currentUI)
	{
		if (mCurrentUI!=null)
		{
			mCurrentUI.onDeactivate();
			mGLSurfaceView.removeObject(mCurrentUI);
		}
		mCurrentUI=currentUI;
		if (mCurrentUI!=null)
		{
			mCurrentUI.onActivate();
			mGLSurfaceView.addObject(mCurrentUI);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mCurrentUI!=null)
			if (mCurrentUI.onTouchEvent(event))
				return true; 
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		if (mCurrentUI!=null)
			if (mCurrentUI.onTrackballEvent(event))
				return true;
		return super.onTrackballEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (mCurrentUI!=null)
			if (mCurrentUI.onKeyDown(keyCode, event))
				return true;
		return super.onKeyDown(keyCode, event);
}
	@Override
	protected void onPause()
	{
		super.onPause();
		mGLSurfaceView.onPause();
		if (mCurrentUI!=null)
			mCurrentUI.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mGLSurfaceView.onResume();
		if (mCurrentUI!=null)
			mCurrentUI.onResume();
	}

	@Override
	public void finish()
	{
		mGLSurfaceView.delete();
		SS.delete();
		super.finish();
	}

}
