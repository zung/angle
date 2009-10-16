package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Angle engines base class
 * 
 * @author Ivan Pajuelo
 *
 */
public abstract class AngleAbstractEngine
{
	/**
	 *  
	 * @param gl Surface where draw
	 */
	public abstract void drawFrame(GL10 gl);
	
	/**
	 * Engine must load its textures here
	 * 
	 * @param gl OpenGL ES surface
	 */
	public abstract void loadTextures(GL10 gl);

	/**
	 * If engine need to do anything after load textures
	 * do it here
	 * 
	 * @param gl OpenGL ES surface
	 */
	public abstract void afterLoadTextures(GL10 gl);

	/**
	 * Destroy engine objects here
	 * 
	 * @param gl OpenGL ES surface
	 */
	public abstract void onDestroy(GL10 gl);
}
