package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Angle sprite reference base class
 * 
 * @author Ivan Pajuelo
 *
 */
public abstract class AngleAbstractSpriteReference
{
	/**
	 *  
	 * @param gl Surface where draw
	 */
	public abstract void draw(GL10 gl);
}
