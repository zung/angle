package com.android.angle;

import javax.microedition.khronos.opengles.GL11;

/**
 * Base class for all visual objects
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleVisualObject
{
	public boolean mVisible;
	public AngleVector mCenter; // Central position of the object
	public float mZ; // Z position (0=Near, 1=Far)

	public AngleVisualObject()
	{
		mVisible = true;
		mCenter = new AngleVector();
		mZ = 0;
	}

	/**
	 * 
	 * @param gl
	 *           Surface where draw
	 */
	public void draw(GL11 gl)
	{
	}
}
