package com.alt90.angle2;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Texture engine
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleTextureEngine
{
	private static final boolean sLogAngleTextureEngine = true;
	public static AngleTextureEngine uInstance=null;
	private static CopyOnWriteArrayList<AngleTexture> mTexturesX = new CopyOnWriteArrayList<AngleTexture>();
	private static GL10 mGl;

/*	
	public static void destroy(GL10 gl)
	{
		if (sLogAngleTextureEngine)
			Log.d("AngleTextureEngine","onSudestroyrfaceCreated");
		mGl = gl;
		onContextLost();
		mGl=null;
	}

	public static void onContextLost()
	{
		if (mGl != null)
		{
			int d = 0;
			int[] textures = new int[mTexturesX.size()];
			Iterator<AngleTexture> it = mTexturesX.iterator();
			while (it.hasNext())
				textures[d++] = it.next().lHWTextureID;

			mGl.glDeleteTextures(d, textures, 0);
			mTexturesX.clear();
		}
	}
*/
	public static void loadTextures(GL10 gl)
	{
		mGl = gl;
		if (mGl != null)
		{
			Iterator<AngleTexture> it = mTexturesX.iterator();
			while (it.hasNext())
				it.next().linkToGL(mGl);
			Log.v("TextureEngine", "loadTexture");
		}
	}

	public static AngleTexture createTextureFromFont(AngleFont font, int type)
	{
		AngleTexture tex = null;
		Iterator<AngleTexture> it = mTexturesX.iterator();
		while (it.hasNext())
		{
			tex = it.next();
			if (tex instanceof AngleFontTexture)
			{
				// Texture already exists
				if (((AngleFontTexture) tex).fontEquals(font))
				{
					tex.lRefernces++;
					return tex;
				}
			}
		}

		tex = new AngleFontTexture(font, type);
		mTexturesX.add(tex);
		Log.v("TextureEngine", "link tex to gl");
		if (mGl != null)
			tex.linkToGL(mGl);
		Log.v("TextureEngine", "--------------");
		return tex;
	}
	
	public static AngleTexture createTextureFromResourceId(int resourceId, int type)
	{
		AngleTexture tex = null;
		Iterator<AngleTexture> it = mTexturesX.iterator();
		while (it.hasNext())
		{
			tex = it.next();
			if (tex instanceof AngleResourceTexture)
			{
				// Texture already exists
				if (((AngleResourceTexture) tex).fResourceID == resourceId)
				{
					tex.lRefernces++;
					return tex;
				}
			}
		}

		tex = new AngleResourceTexture(resourceId, type);
		mTexturesX.add(tex);
		if (mGl != null)
			tex.linkToGL(mGl);
		return tex;
	}

	public static AngleTexture createTextureFromAsset(String filename, int type)
	{
		AngleTexture tex = null;
		Iterator<AngleTexture> it = mTexturesX.iterator();
		while (it.hasNext())
		{
			tex = it.next();
			if (tex instanceof AngleAssetTexture)
			{
				// Texture already exists
				if (((AngleAssetTexture) tex).fFileName.equals(filename))
				{
					tex.lRefernces++;
					return tex;
				}
			}
		}

		tex = new AngleAssetTexture(filename, type);
		mTexturesX.add(tex);
		if (mGl != null)
			tex.linkToGL(mGl);
		return tex;
	}

	public static AngleTexture createTextureFromURL(String url, int type)
	{
		AngleTexture tex = null;
		Iterator<AngleTexture> it = mTexturesX.iterator();
		while (it.hasNext())
		{
			tex = it.next();
			if (tex instanceof AngleURLTexture)
			{
				// Texture already exists
				if (((AngleURLTexture) tex).fURL.equals(url))
				{
					tex.lRefernces++;
					return tex;
				}
			}
		}

		tex = new AngleURLTexture(url, type);
		mTexturesX.add(tex);
		if (mGl != null)
			tex.linkToGL(mGl);
		return tex;
	}

	public static int generateTexture()
	{
		if (mGl != null)
		{
			int[] textureIDs = new int[1];

			mGl.glGenTextures(1, textureIDs, 0);

			int error = mGl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("TextureEngine", "generateTexture GLError: " + error);
			else
			{
				Log.v("TextureEngine", "generateTexture id="+textureIDs[0]);
				return textureIDs[0];
			}
		}
		return -1;
	}

	public static void deleteTexture(AngleTexture tex)
	{
		if (mTexturesX.indexOf(tex) > -1)
		{
			tex.lRefernces--;
			if (tex.lRefernces < 0)
				mTexturesX.remove(tex);
		}
		if (tex.lHWTextureID > -1)
		{
			Log.v("TextureEngine", "deleteTexture id="+tex.lHWTextureID);
			int[] texture = new int[1];
			texture[0] = tex.lHWTextureID;
			if (mGl != null)
				mGl.glDeleteTextures(1, texture, 0);
		}
	}

	public static int fitPow2(int size)
	{
		for (int p=0;p<32;p++)
		{
			if (size<(1<<p))
				return (1<<p);
		}
		return 0;
	}

	public static void init()
	{
		mGl=null;
	}

	public static void deinit()
	{
		if (mGl != null)
		{
			int d = 0;
			int[] textures = new int[mTexturesX.size()];
			Iterator<AngleTexture> it = mTexturesX.iterator();
			while (it.hasNext())
				textures[d++] = it.next().lHWTextureID;

			mGl.glDeleteTextures(d, textures, 0);
			mTexturesX.clear();
			mGl=null;
		}
	}

}