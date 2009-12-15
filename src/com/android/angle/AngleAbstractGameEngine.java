package com.android.angle;

import javax.microedition.khronos.opengles.GL11;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Angle game engines base class
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleAbstractGameEngine implements Runnable
{
	protected AngleSurfaceView mView;
	public AngleAbstractEngine mRootEngine;

	public AngleAbstractGameEngine(AngleSurfaceView view)
	{
		mView = view;
		mRootEngine=new AngleAbstractEngine();
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return false;
	}

	public boolean onTrackballEvent(MotionEvent event)
	{
		return false;
	}

	public void onDestroy(GL11 gl)
	{
		mRootEngine.onDestroy(gl);
	}

	public void addEngine(AngleAbstractEngine engine)
	{
		mRootEngine.addEngine(engine);
	}

	public void run()
	{
	}
}
