package com.android.angle;


import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class AngleRenderEngine
{
	private static final int MAX_RENDERERS = 10;
	private static AngleRenderer[] mRenderers=new AngleRenderer[MAX_RENDERERS];
	private static int mRenderersCount=0;
	public static int mWidth=0;
	public static int mHeight=0;
	public static Context mContext=null;
	public static GL10 gl = null;


	public AngleRenderEngine(Context context)
	{
		new AngleTextureEngine();
		mContext = context;
	}
	
	public void addRenderer(AngleRenderer renderer)
	{
		if (mRenderersCount<MAX_RENDERERS)
		{
			mRenderers[mRenderersCount++]=renderer;
		}
		else
			Log.e("AngleRendererEngine",
			"addRenderer() MAX_RENDERERS reached");

	}

	public int[] getConfigSpec()
	{
		int[] configSpec = { EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE };
		return configSpec;
	}

	public void drawFrame()
	{
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, mWidth, mHeight, 0.0f, -1.0f, 1.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		for (int r = 0; r < mRenderersCount; r++)
			mRenderers[r].drawFrame(gl);
		//Log.v("Frame", "Frame");
	}

	public void sizeChanged(int width, int height)
	{
		mWidth=width;
		mHeight=height;
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
	
	public void loadTextures ()
	{
		for (int r = 0; r < mRenderersCount; r++)
			mRenderers[r].loadTextures();
		AngleTextureEngine.loadTextures();
	}

	public void shutdown()
	{
		for (int r = 0; r < mRenderersCount; r++)
			mRenderers[r].shutdown();
		AngleTextureEngine.shutdown();
	}
	
}
