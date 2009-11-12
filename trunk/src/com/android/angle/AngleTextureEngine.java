package com.android.angle;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Texture that uses AngleTextureEngine
 * 
 * @author Ivan Pajuelo
 * 
 */
class AngleTexture
{
	public int mHWTextureID = -1;
	public int mResourceID = -1;
	public int mWidth = 0;
	public int mHeight = 0;
};

/**
 * Texture engine
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleTextureEngine
{
	private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	private static final int MAX_TEXTURES = 16;
	private static AngleTexture[] mTextures = new AngleTexture[MAX_TEXTURES];
	private static int mTextureCount = 0;
	public static boolean hasChanges = false;
	public static boolean buffersChanged = false;

	AngleTextureEngine()
	{
	}

	public static void onDestroy(GL10 gl)
	{
		if (gl != null)
		{
			int d = 0;
			int[] textures = new int[MAX_TEXTURES];

			for (int t = 0; t < mTextureCount; t++)
			{
				if (mTextures[t].mWidth > 0)
					textures[d++] = mTextures[t].mHWTextureID;
			}
			gl.glDeleteTextures(d, textures, 0);
		}
		for (int t = 0; t < mTextureCount; t++)
			mTextures[t] = null;
		mTextureCount = 0;
	}

	/**
	 * 
	 * @param resourceId
	 *           Drawable
	 * @return Hardware texture Id
	 */
	public static int createHWTextureFromResource(int resourceId)
	{
		for (int t = 0; t < mTextureCount; t++)
		{
			if (mTextures[t].mResourceID == resourceId) // Texture already loaded
				return t;
		}
		if (mTextureCount < MAX_TEXTURES)
		{
			hasChanges = true;
			mTextures[mTextureCount] = new AngleTexture();
			mTextures[mTextureCount].mResourceID = resourceId;
			return mTextureCount++;
		}
		Log.e("AngleTextureEngine",
				"createHWTextureFromResource() MAX_TEXTURES reached");
		return 0;
	}

	public static void loadTextures(GL10 gl)
	{
		if (gl != null)
		{
			Log.e("Textures","LOADED");
			sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;

			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

			gl.glShadeModel(GL10.GL_FLAT);
			gl.glDisable(GL10.GL_DEPTH_TEST);
			gl.glDisable(GL10.GL_DITHER);
			gl.glDisable(GL10.GL_LIGHTING);
			gl.glEnable(GL10.GL_TEXTURE_2D);

			gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

			for (int t = 0; t < MAX_TEXTURES; t++)
			{
				if (mTextures[t] != null)
					loadTexture(gl, t);
			}
			hasChanges = false;
		}
	}

	private static void loadTexture(GL10 gl, int textureId)
	{
		if (AngleMainEngine.mContext != null)
		{
			{
				int[] mTextureIDs = new int[1];

				gl.glGenTextures(1, mTextureIDs, 0);

				mTextures[textureId].mHWTextureID = mTextureIDs[0];
				gl.glBindTexture(GL10.GL_TEXTURE_2D,
						mTextures[textureId].mHWTextureID);

				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
						GL10.GL_NEAREST);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
						GL10.GL_LINEAR);

				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
						GL10.GL_CLAMP_TO_EDGE);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
						GL10.GL_CLAMP_TO_EDGE);

				gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
						GL10.GL_REPLACE);

				InputStream is = AngleMainEngine.mContext.getResources()
						.openRawResource(mTextures[textureId].mResourceID);
				Bitmap bitmap;
				try
				{
					bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
				} finally
				{
					try
					{
						is.close();
					} catch (IOException e)
					{
						Log.e("AngleTextureEngine",
								"loadTexture::InputStream.close error: "
										+ e.getMessage());
					}
				}

				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

				mTextures[textureId].mWidth = bitmap.getHeight();
				mTextures[textureId].mHeight = bitmap.getWidth();

				bitmap.recycle();

				int error = gl.glGetError();
				if (error != GL10.GL_NO_ERROR)
				{
					Log.e("AngleTextureEngine", "loadTexture GLError: " + error);
				}
			}
		}
	}

	public static void bindTexture(GL10 gl, int mTextureID)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextures[mTextureID].mHWTextureID);
	}

	public static float getTextureWidth(int mTextureID)
	{
		return mTextures[mTextureID].mWidth;
	}

	public static float getTextureHeight(int mTextureID)
	{
		return mTextures[mTextureID].mHeight;
	}
}