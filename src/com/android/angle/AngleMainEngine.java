package com.android.angle;

import javax.microedition.khronos.opengles.GL11;

import android.content.Context;

/**
 * Angle main engine
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleMainEngine extends AngleAbstractEngine
{
	public static final int MSG_CONTEXT_LOST = 1;
	public static final boolean sUseHWBuffers = true;
	private static final int sMaxHWBuffers = 300;
	public static int mWidth = 0; // Surface width
	public static int mHeight = 0; // Surface height
	public static float secondsElapsed = 0.0f; // Seconds elapsed since last
	// frame
	public static Context mContext; // Activity context
	public static boolean mContextLost = false;
	public static boolean mTexturesLost = false;
	public static boolean mBuffersLost = false;
	private static int[] mHWBuffers;
	private static int mHWBuffersCount;

	/**
	 * 
	 * @param maxEngines
	 *           Sub-engines limit
	 */
	public AngleMainEngine(int maxEngines)
	{
		super(maxEngines);
		mHWBuffers=new int[sMaxHWBuffers];
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
	public static void sizeChanged(GL11 gl, int width, int height)
	{
		mWidth = width;
		mHeight = height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL11.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, height, 0, 0, 1);

		gl.glShadeModel(GL11.GL_FLAT);
		gl.glEnable(GL11.GL_BLEND);
		gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		gl.glEnable(GL11.GL_TEXTURE_2D);
		gl.glMatrixMode(GL11.GL_MODELVIEW);
	}

	@Override
	public void createBuffers(GL11 gl)
	{
		mHWBuffersCount=0;
		gl.glGenBuffers(sMaxHWBuffers, mHWBuffers, 0);
		super.createBuffers(gl);
	}

	public static int getHWBuffer()
	{
		if (mHWBuffersCount<sMaxHWBuffers)
			return mHWBuffers[mHWBuffersCount++];
		else
			return -1;
	}

	@Override
	public void drawFrame(GL11 gl)
	{
		gl.glLoadIdentity();
		gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		super.drawFrame(gl);
	}

	/**
	 * Destroy all engines
	 * 
	 * @param gl
	 *           OpenGL ES surface
	 */
	@Override
	public void onDestroy(GL11 gl)
	{
		super.onDestroy(gl);
		AngleTextureEngine.onDestroy();
		java.lang.System.gc();
	}

	public void setRootEngine(GL11 gl, AngleAbstractEngine mRootEngine)
	{
		onDestroy(gl);
		addEngine(mRootEngine);
	}
}
