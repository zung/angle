package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

/**
 * Angle main engine
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleMainEngine extends AngleAbstractEngine
{
	public static int mWidth = 0; // Surface width
	public static int mHeight = 0; // Surface height
	public static float secondsElapsed = 0.0f; // Seconds elapsed since last
	// frame
	public static Context mContext; // Activity context

	/**
	 * 
	 * @param maxEngines
	 *           Sub-engines limit
	 */
	public AngleMainEngine(int maxEngines)
	{
		super(maxEngines);
	}

	/**
	 * Change the size of the surface
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 * @param width
	 *           New width
	 * @param height
	 *           New height
	 */
	public static void sizeChanged(GL10 gl, int width, int height)
	{
		mWidth = width;
		mHeight = height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, height, 0, 0, 1);

		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
	}

	@Override
	public void afterLoadTextures(GL10 gl)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawFrame(GL10 gl)
	{
		gl.glLoadIdentity();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		super.drawFrame(gl);
	}

	/**
	 * Load textures of all engines
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 */
	@Override
	public void loadTextures(GL10 gl)
	{
		super.loadTextures(gl);
		AngleTextureEngine.loadTextures(gl);
		super.afterLoadTextures(gl);
	}

	/**
	 * Destroy all engines
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 */
	@Override
	public void onDestroy(GL10 gl)
	{
		super.onDestroy(gl);
		AngleTextureEngine.onDestroy(gl);
		java.lang.System.gc();
	}
}
