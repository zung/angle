package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class AngleRenderEngine
{
	private static final int MAX_RENDERERS = 10;
	private static AngleRenderer[] mRenderers = new AngleRenderer[MAX_RENDERERS];
	private static int mRenderersCount = 0;
	public static int mWidth = 0;
	public static int mHeight = 0;
	public static GL10 gl = null;
	public static float secondsElapsed = 0.0f;
	public static Context mContext;

	public AngleRenderEngine()
	{
	}

	public static void addRenderer(AngleRenderer renderer)
	{
		if (mRenderersCount < MAX_RENDERERS)
		{
			mRenderers[mRenderersCount++] = renderer;
		} else
			Log.e("AngleRendererEngine", "addRenderer() MAX_RENDERERS reached");

	}

	public static void drawFrame()
	{
/*
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, mWidth, mHeight, 0.0f, -1.0f, 1.0f);
*/
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		for (int r = 0; r < mRenderersCount; r++)
			mRenderers[r].drawFrame(gl);
	}

	public static void sizeChanged(int width, int height)
	{
		mWidth = width;
		mHeight = height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, width, height, 0.0f, -1.0f, 1.0f);

		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

	public static void loadTextures()
	{
		for (int r = 0; r < mRenderersCount; r++)
			mRenderers[r].loadTextures();
		AngleTextureEngine.loadTextures();
	}

	public static void onDestroy()
	{
		for (int r = 0; r < mRenderersCount; r++)
		{
			mRenderers[r].onDestroy();
			mRenderers[r] = null;
		}
		mRenderersCount = 0;
		AngleTextureEngine.onDestroy();
		java.lang.System.gc();
	}

}
