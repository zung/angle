package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class AngleMainEngine
{
	private static final int MAX_ENGINES = 10;
	private static AngleAbstractEngine[] mEngines = new AngleAbstractEngine[MAX_ENGINES];
	private static int mEnginesCount = 0;
	public static int mWidth = 0;
	public static int mHeight = 0;
	public static float secondsElapsed = 0.0f;
	public static Context mContext;

	public AngleMainEngine()
	{
	}

	public static void addEngine(AngleAbstractEngine renderer)
	{
		if (mEnginesCount < MAX_ENGINES)
		{
			mEngines[mEnginesCount++] = renderer;
		} else
			Log.e("AngleMainEngine", "addEngine() MAX_ENGINES reached");

	}

	public static void drawFrame(GL10 gl)
	{
		gl.glLoadIdentity();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].drawFrame(gl);
	}

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

	
	public static void loadTextures(GL10 gl)
	{
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].loadTextures(gl);
		AngleTextureEngine.loadTextures(gl);
		for (int r = 0; r < mEnginesCount; r++)
			mEngines[r].afterLoadTextures(gl);
	}

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
