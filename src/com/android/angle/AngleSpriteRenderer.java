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

		AngleRenderEngine.gl.glMatrixMode(GL10.GL_MODELVIEW);
		AngleRenderEngine.gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		AngleRenderEngine.gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public void onDestroy()
	{
		AngleRenderEngine.gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		AngleRenderEngine.gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s] = null;
		mSpritesCount = 0;
	}
}
