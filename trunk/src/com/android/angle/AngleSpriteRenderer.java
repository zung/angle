package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

public class AngleSpriteRenderer extends AngleRenderer
{
	private static final int MAX_SPRITES = 100;
	private static AngleSprite[] mSprites=new AngleSprite[MAX_SPRITES];
	private static int mSpritesCount=0;

	public void addSprite(AngleSprite sprite)
	{
		mSprites[mSpritesCount++] = sprite;
	}

	public void drawFrame(GL10 gl)
	{
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s].draw(gl);
	}

	public void loadTextures ()
	{
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s].mTextureID=AngleTextureEngine.createHWTextureFromResource(mSprites[s].mResourceID);
	}
	
	public void shutdown()
	{
		
	}
}
