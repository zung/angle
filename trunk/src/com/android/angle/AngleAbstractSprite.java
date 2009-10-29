package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Angle sprite base class
 * 
 * @author Ivan Pajuelo
 * 
 */
public abstract class AngleAbstractSprite extends AngleVisualObject
{
	/**
	 * Sprite must load its texture here
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 */
	public abstract void loadTexture();

	/**
	 * If sprite need to do anything after load the texture do it here
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 */
	public abstract void afterLoadTexture();

	/**
	 * 
	 * @param gl
	 *           Surface where draw
	 */
	public abstract void draw(GL10 gl);

}
