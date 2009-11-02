package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Base class for all visual objects
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleVisualObject
{
	public AngleVector mCenter; // Central position of the object
	public float mZ; // Z position (0=Near, 1=Far)

	public AngleVisualObject()
	{
		mCenter = new AngleVector();
		mZ = 0;
	}

	/**
	 * 
	 * @param gl
	 *           Surface where draw
	 */
	public void draw(GL10 gl)
	{
	}
}
