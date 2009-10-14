package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

public class AngleSpriteRenderer extends AngleRenderer
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
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s].draw(gl);
	}

	public void loadTextures()
	{
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s].loadTexture();
	}

	public void afterLoadTextures()
	{
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s].afterLoadTexture();
	}

	public void onDestroy()
	{
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s] = null;
		mSpritesCount = 0;
	}
}
