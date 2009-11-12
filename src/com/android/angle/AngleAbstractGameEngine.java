package com.android.angle;

/**
 * Angle game engines base class
 * 
 * @author Ivan Pajuelo
 * 
 */
public abstract class AngleAbstractGameEngine implements Runnable
{
	protected AngleSurfaceView mView;

	public AngleAbstractGameEngine (AngleSurfaceView view)
	{
		mView = view;
		mView.setBeforeDraw(this);
	}
}
