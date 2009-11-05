package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * References base class
 * 
 * @author Ivan Pajuelo
 * 
 */
public abstract class AngleAbstractReference extends AngleVisualObject
{
	/**
	 * Method called after adding this reference to an engine
	 */
	public abstract void afterAdd();

	public void onDestroy(GL10 gl)
	{
	}
}
