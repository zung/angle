package com.android.angle;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class AngleTextureEngine
{
	private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	private static final int MAX_TEXTURES = 16;
	public static AngleTexture[] mTextures = new AngleTexture[MAX_TEXTURES];
	private static int mTextureCount=0; 

	AngleTextureEngine()
	{
		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
	}

	public static void shutdown()
	{
		if (AngleRenderEngine.gl != null)
		{
			int d = 0;
			int[] textures = new int[MAX_TEXTURES];

			for (int t=0;t<mTextureCount;t++)
			{
				if (mTextures[t].mWidth>0)
					textures[d++] = mTextures[t].mHWTextureID;
			}
			AngleRenderEngine.gl.glDeleteTextures(d, textures, 0);
		}
	}
	
	public static int createHWTextureFromResource (int resourceId)
	{
		for (int t=0;t<mTextureCount;t++)
		{
			if (mTextures[t].mResourceID==resourceId) //Texture already loaded
				return t;
		}
		if (mTextureCount<MAX_TEXTURES)
		{
			mTextures[mTextureCount]=new AngleTexture();
			mTextures[mTextureCount].mResourceID=resourceId;
			return mTextureCount++;
		}
		Log.e("AngleTextureEngine",
				"createHWTextureFromResource() MAX_TEXTURES reached");
		return 0;
	}
	
	public static void loadTextures()
	{
		AngleRenderEngine.gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		AngleRenderEngine.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		AngleRenderEngine.gl.glShadeModel(GL10.GL_FLAT);
		AngleRenderEngine.gl.glDisable(GL10.GL_DEPTH_TEST);
		AngleRenderEngine.gl.glEnable(GL10.GL_TEXTURE_2D);
		AngleRenderEngine.gl.glDisable(GL10.GL_DITHER);
		AngleRenderEngine.gl.glDisable(GL10.GL_LIGHTING);

		AngleRenderEngine.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		for (int t = 0; t < MAX_TEXTURES; t++)
		{
			if (mTextures[t]!=null)
				loadTexture(t);
		}
	}
	private static void loadTexture(int textureId)
	{
		if (AngleRenderEngine.mContext != null)
		{
			if (AngleRenderEngine.gl != null)
			{
				int[] mTextureIDs = new int[1];

				AngleRenderEngine.gl.glGenTextures(1, mTextureIDs, 0);

				mTextures[textureId].mHWTextureID = mTextureIDs[0];
				AngleRenderEngine.gl.glBindTexture(GL10.GL_TEXTURE_2D,
						mTextures[textureId].mHWTextureID);

				AngleRenderEngine.gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
				AngleRenderEngine.gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

				AngleRenderEngine.gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
				AngleRenderEngine.gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

				AngleRenderEngine.gl.glTexEnvf(GL10.GL_TEXTURE_ENV,
						GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

				InputStream is = AngleRenderEngine.mContext.getResources().openRawResource(mTextures[textureId].mResourceID);
				Bitmap bitmap;
				try
				{
					bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
				} 
				finally
				{
					try
					{
						is.close();
					} 
					catch (IOException e)
					{
						Log.e("AngleTextureEngine",
								"loadTexture::InputStream.close error: " + e.getMessage());
					}
				}

				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

				mTextures[textureId].mWidth = bitmap.getHeight();
				mTextures[textureId].mHeight = bitmap.getWidth();

				bitmap.recycle();

				int error = AngleRenderEngine.gl.glGetError();
				if (error != GL10.GL_NO_ERROR)
				{
					Log.e("AngleTextureEngine", "loadTexture GLError: " + error);
				}
			}
		}
	}
}