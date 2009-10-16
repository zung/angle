package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

/**
 * Angle main engine
 * 
 * @author Ivan Pajuelo
 *
 */
public class AngleMainEngine
{
	private static final int MAX_ENGINES = 10; //Engine limit
	private static AngleAbstractEngine[] mEngines = new AngleAbstractEngine[MAX_ENGINES]; 
	private static int mEnginesCount = 0;
	public static int mWidth = 0; //Surface width
	public static int mHeight = 0; //Surface height
	public static float secondsElapsed = 0.0f; //Seconds elapsed since last frame
	public static Context mContext; //Activity context

	public AngleMainEngine()
	{
	}
	
	/**
	 * Add rendering engine to main engine
	 * 
	 * @param engine Engine to add
	 */
	public static void addEngine(AngleAbstractEngine engine)
	{
		if (mEnginesCount < MAX_ENGINES)
		{
			mEngines[mEnginesCount++] = engine;
		} else
			Log.e("AngleMainEngine", "addEngine() MAX_ENGINES reached");

	}

	/**
	 *  
	 * @param gl Surface where draw
	 */
	public static void drawFrame(GL10 gl)
	{
		gl.glLoadIdentity();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].drawFrame(gl);
	}
	
	/**
	 * Change the size of the surface
	 * 
	 * @param gl OpenGL ES surface
	 * @param width New width
	 * @param height New height
	 */
	public static void sizeChanged(GL10 gl, int width, int height)
	{
		mWidth = width;
		mHeight = height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, 0, height, 0, 1);

		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
	}

	/**
	 * Load textures of all engines
	 * @param gl OpenGL ES surface
	 */
	public static void loadTextures(GL10 gl)
	{
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].loadTextures(gl);
		AngleTextureEngine.loadTextures(gl);
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].afterLoadTextures(gl);
	}

	/**
	 * Destroy all engines
	 * @param gl OpenGL ES surface
	 */
	public static void onDestroy(GL10 gl)
	{
		for (int r = 0; r < mEnginesCount; r++)
		{
			mEngines[r].onDestroy(gl);
			mEngines[r] = null;
		}
		mEnginesCount = 0;
		AngleTextureEngine.onDestroy(gl);
		java.lang.System.gc();
	}
}
