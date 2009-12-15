package com.android.angle;

import javax.microedition.khronos.opengles.GL11;

import android.util.Log;

/**
 * Texture engine
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleTextureEngine
{
	private static final int MAX_TEXTURES = 64;
	private static AngleTexture[] mTextures = new AngleTexture[MAX_TEXTURES];
	private static GL11 mGL;

	AngleTextureEngine()
	{
	}

	public static void onDestroy()
	{
		onContextLost();
	}

	public static void onContextLost()
	{
		if (mGL != null)
		{
			int d = 0;
			int[] textures = new int[MAX_TEXTURES];

			for (int t = 0; t < MAX_TEXTURES; t++)
			{
				if (mTextures[t] != null)
					textures[d++] = mTextures[t].mHWTextureID;
			}
			mGL.glDeleteTextures(d, textures, 0);
		}
//		for (int t = 0; t < MAX_TEXTURES; t++)
//			mTextures[t] = null;
	}

	public static void genTextures(GL11 gl)
	{
		mGL = gl;
		if (mGL != null)
		{
			mGL.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST);

			mGL.glShadeModel(GL11.GL_FLAT);
			mGL.glDisable(GL11.GL_DEPTH_TEST);
			mGL.glDisable(GL11.GL_DITHER);
			mGL.glDisable(GL11.GL_LIGHTING);
			mGL.glEnable(GL11.GL_TEXTURE_2D);

			mGL.glClearColor(0.0f, 0.0f, 0.0f, 1);
			mGL.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			int[] mTextureIDs = new int[MAX_TEXTURES];
	
			mGL.glGenTextures(MAX_TEXTURES, mTextureIDs, 0);
	
			int error = mGL.glGetError();
			if (error != GL11.GL_NO_ERROR)
				Log.e("AngleTexture", "generate GLError: " + error);
			else
				Log.e("Textures", "Generated");
		}
	}
	
	public static void loadTextures()
	{
		for (int t = 0; t < MAX_TEXTURES; t++)
		{
			if (mTextures[t] != null)
				mTextures[t].load(t,mGL);
		}
		AngleMainEngine.mTexturesLost = false;
		Log.e("Textures", "Loaded");
	}

	public static AngleTexture createTextureFromFont(AngleFont font)
	{
		int t;
		for (t = 0; t < MAX_TEXTURES; t++)
		{
			if (mTextures[t] != null)
			{
				if (mTextures[t] instanceof AngleFontTexture)
				{
					//Texture already exists
					if (((AngleFontTexture) mTextures[t]).mFont == font)
					{
						mTextures[t].mRefernces++;
						return mTextures[t];
					}
				}
			}
		}
		for (t = 0; t < MAX_TEXTURES; t++)
		{
			if (mTextures[t] == null)
			{
				mTextures[t] = new AngleFontTexture(font);
				return mTextures[t];
			}
		}
		Log.e("AngleTextureEngine", "createTextureFromFont() MAX_TEXTURES reached");
		return null;
	}
	public static AngleTexture createTextureFromResourceId(int resourceId)
	{
		int t;
		for (t = 0; t < MAX_TEXTURES; t++)
		{
			if (mTextures[t] != null)
			{
				if (mTextures[t] instanceof AngleResourceTexture)
				{
					//Texture already exists
					if (((AngleResourceTexture) mTextures[t]).mResourceID == resourceId)
					{
						mTextures[t].mRefernces++;
						return mTextures[t];
					}
				}
			}
		}
		for (t = 0; t < MAX_TEXTURES; t++)
		{
			if (mTextures[t] == null)
			{
				mTextures[t] = new AngleResourceTexture(resourceId);
				return mTextures[t];
			}
		}
		Log.e("AngleTextureEngine", "createTextureFromResourceId() MAX_TEXTURES reached");
		return null;
	}

	public static void deleteTexture(AngleTexture mTexture)
	{
		for (int t = 0; t < MAX_TEXTURES; t++)
		{
			if (mTextures[t] == mTexture)
			{
				mTextures[t].mRefernces--;
				if (mTextures[t].mRefernces < 0)
				{
					if (mTextures[t].mHWTextureID>-1)
					{
						int[] texture = new int[1];
						texture[0] = mTextures[t].mHWTextureID;
						mGL.glDeleteTextures(1, texture, 0);
					}
					mTextures[t] = null;
					break;
				}
			}
		}
	}
}