package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

public class AngleSpritesEngine extends AngleAbstractEngine
{
	private static final int MAX_SPRITES = 1000;
	private static AngleSimpleSprite[] mSprites = new AngleSimpleSprite[MAX_SPRITES];
	private static int mSpritesCount = 0;

	public void addSprite(AngleSimpleSprite sprite)
	{
		mSprites[mSpritesCount++] = sprite;
	}

	public void drawFrame(GL10 gl)
	{
		if (!AngleTextureEngine.hasChanges)
		{
			for (int s = 0; s < mSpritesCount; s++)
				mSprites[s].draw(gl);
		}
	}

	public void loadTextures(GL10 gl)
	{
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s].loadTexture();
	}

	public void afterLoadTextures(GL10 gl)
	{
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s].afterLoadTexture();

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public void onDestroy(GL10 gl)
	{
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s] = null;
		mSpritesCount = 0;
	}
}
