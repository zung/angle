package com.android.ace;

import android.view.KeyEvent;
import android.view.MotionEvent;

public class ACEGameEngine
{
	public ACEGameEngine()
	{
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

	public void step(float secondsElapsed)
	{
	}

	public void linkToView(ACEMainView view)
	{
	}
}
