package com.android.angle;

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
}
