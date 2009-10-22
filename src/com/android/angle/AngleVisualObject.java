package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Angle sprite reference base class
 * 
 * @author Ivan Pajuelo
 *
 */
public class AngleVisualObject
{
	public AngleVector mCenter;
	public float mZ;
	
	public AngleVisualObject()
	{
		mCenter=new AngleVector();
		mZ=0;
	}
	/**
	 *  
	 * @param gl Surface where draw
	 */
	public void draw(GL10 gl){};
}
