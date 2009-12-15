package com.android.angle;

import javax.microedition.khronos.opengles.GL11;

import android.util.Log;

/**
 * Angle engines base class
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleAbstractEngine
{
	private static final int sMaxEngines = 10; // Default sub-engines slots
	private int mMaxEngines; // Engine limit
	private AngleAbstractEngine[] mEngines; // Sub-engines
	private int mEnginesCount; // Sub-engines count

	public AngleAbstractEngine()
	{
		mMaxEngines = sMaxEngines;
		mEngines = new AngleAbstractEngine[mMaxEngines];
		mEnginesCount = 0;
	}

	/**
	 * 
	 * @param maxEngines
	 *           Number of sub-engines slots
	 */
	public AngleAbstractEngine(int maxEngines)
	{
		mMaxEngines = maxEngines;
		mEngines = new AngleAbstractEngine[mMaxEngines];
		mEnginesCount = 0;
	}

	/**
	 * Add rendering engine to main engine
	 * 
	 * @param engine
	 *           Engine to add
	 */
	public void addEngine(AngleAbstractEngine engine)
	{
		if (mEnginesCount < mMaxEngines)
			mEngines[mEnginesCount++] = engine;
		else
			Log.e("AngleAbstractEngine", "addEngine() MaxEngines reached");

	}

	/**
	 * Remove rendering engine from main engine
	 * 
	 * @param engine
	 *           Engine to remove
	 */
	protected void removeEngine(AngleAbstractEngine engine)
	{
		int r;

		for (r = 0; r < mEnginesCount; r++)
			if (mEngines[r] == engine)
				break;

		if (r < mEnginesCount)
		{
			mEnginesCount--;
			for (int d = r; d < mEnginesCount; d++)
				mEngines[d] = mEngines[d + 1];
			mEngines[mEnginesCount] = null;
		}
	}

	/**
	 * 
	 * @param gl
	 *           Surface where draw
	 */
	public void drawFrame(GL11 gl)
	{
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].drawFrame(gl);
	}

	/**
	 * Engine must load its textures here
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 */
	public void beforeLoadTextures(GL11 gl)
	{
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].beforeLoadTextures(gl);
	}

	/**
	 * If engine need to do anything after load textures do it here
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 */
	public void afterLoadTextures(GL11 gl)
	{
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].afterLoadTextures(gl);
	}

	/**
	 * Buffers have changes. Check if any engine needs to do something with
	 * hardware buffers
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 */
	public void createBuffers(GL11 gl)
	{
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].createBuffers(gl);
	}

	/**
	 * Destroy engine objects here
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 */
	public void onDestroy(GL11 gl)
	{
		for (int r = 0; r < mEnginesCount; r++)
		{
			mEngines[r].onDestroy(gl);
			mEngines[r] = null;
		}
		mEnginesCount = 0;
	}
}
