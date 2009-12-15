package com.android.angle;

import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Texture that uses AngleTextureEngine
 * 
 * @author Ivan Pajuelo
 * 
 */
public abstract class AngleTexture
{
	public int mRefernces = 0;
	public int mHWTextureID = -1;
	public int mWidth = 0;
	public int mHeight = 0;

	public abstract Bitmap create();

	public void load(int textureID, GL11 gl)
	{
		if (gl!=null)
		{
			mHWTextureID=textureID;
			gl.glBindTexture(GL11.GL_TEXTURE_2D, mHWTextureID);
			int error = gl.glGetError();
			if (error != GL11.GL_NO_ERROR)
				Log.e("AngleTexture", "load Bind GLError: " + error);
	
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE);
			error = gl.glGetError();
			if (error != GL11.GL_NO_ERROR)
				Log.e("AngleTexture", "load Parameterf GLError: " + error);
	
			gl.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
			error = gl.glGetError();
			if (error != GL11.GL_NO_ERROR)
				Log.e("AngleTexture", "load Envf GLError: " + error);
	
	
			Bitmap bitmap = create();
	
			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, bitmap, 0);
			error = gl.glGetError();
			if (error != GL11.GL_NO_ERROR)
				Log.e("AngleTexture", "load Image2D GLError: " + error);
	
			mWidth = bitmap.getWidth();
			mHeight = bitmap.getHeight();
	
			bitmap.recycle();
		}
	}
};
