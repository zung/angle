package com.android.angle;

import javax.microedition.khronos.opengles.GL11;

/**
 * Angle sprites engine Can operate in direct mode (sprites) or in indirect mode
 * (references) If MaxReferences>0 then indirect mode is selected
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpritesEngine extends AngleAbstractEngine
{
	protected int mMaxLayouts;
	protected AngleSpriteLayout[] mLayouts;
	protected int mLayoutsCount;
	protected int mMaxSprites;
	protected AngleAbstractSprite[] mSprites;
	protected int mSpritesCount;

	/**
	 * 
	 * @param maxSprites
	 *           Max sprites available in engine
	 * @param maxReferences
	 *           Max references available in engine. If >0 references will be
	 *           used instead of sprites.
	 */
	public AngleSpritesEngine(int maxLayouts, int maxReferences)
	{
		mMaxLayouts = maxLayouts;
		mMaxSprites = maxReferences;
		mLayoutsCount = 0;
		mSpritesCount = 0;
		mLayouts = new AngleSpriteLayout[mMaxLayouts];
		mSprites = new AngleAbstractSprite[mMaxSprites];
	}

	/**
	 * Adds a sprite
	 * 
	 * @param sprite
	 *           Sprite to be added
	 */
	public synchronized void addLayout(AngleSpriteLayout layout)
	{
		if (mLayoutsCount < mMaxLayouts)
		{
			for (int s = 0; s < mLayoutsCount; s++)
				if (mLayouts[mLayoutsCount] == layout)
					return;
			mLayouts[mLayoutsCount++] = layout;
		}
	}

	/**
	 * Removes a sprite
	 * 
	 * @param sprite
	 *           Sprite to be removed
	 */
	public synchronized void removeLayout(AngleSpriteLayout layout)
	{
		int r;

		for (r = 0; r < mLayoutsCount; r++)
			if (mLayouts[r] == layout)
				break;

		if (r < mLayoutsCount)
		{
			mLayoutsCount--;
			for (int d = r; d < mLayoutsCount; d++)
				mLayouts[d] = mLayouts[d + 1];
			mLayouts[mLayoutsCount] = null;
		}
	}

	/**
	 * Adds a reference
	 * 
	 * @param sprite
	 *           Reference to be added
	 */
	public synchronized void addSprite(AngleAbstractSprite sprite)
	{
		if (mSpritesCount < mMaxSprites)
		{
			for (int s = 0; s < mSpritesCount; s++)
				if (mSprites[mSpritesCount] == sprite)
					return;
			mSprites[mSpritesCount++] = sprite;
		}
	}

	/**
	 * Removes a reference
	 * 
	 * @param sprite
	 *           Reference to be removed
	 */
	public synchronized void removeSprite(AngleAbstractSprite sprite)
	{
		int r;

		for (r = 0; r < mSpritesCount; r++)
			if (mSprites[r] == sprite)
				break;

		if (r < mSpritesCount)
		{
			mSpritesCount--;
			for (int d = r; d < mSpritesCount; d++)
				mSprites[d] = mSprites[d + 1];
			mSprites[mSpritesCount] = null;
		}
	}

	@Override
	public void drawFrame(GL11 gl)
	{
		if ((!AngleMainEngine.mTexturesLost) && (!AngleMainEngine.mBuffersLost))
		{
			for (int s = 0; s < mSpritesCount; s++)
				if (mSprites[s].mVisible)
					mSprites[s].draw(gl);
		}
		super.drawFrame(gl);
	}

	@Override
	public void afterLoadTextures(GL11 gl)
	{
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s].afterLoadTexture(gl);

		if (gl != null)
		{
			gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		}
		super.afterLoadTextures(gl);
	}

	@Override
	public void createBuffers(GL11 gl)
	{
		for (int s = 0; s < mLayoutsCount; s++)
			mLayouts[s].createBuffers(gl);
		for (int s = 0; s < mSpritesCount; s++)
			mSprites[s].createBuffers(gl);
		super.createBuffers(gl);
	}

	@Override
	public void onDestroy(GL11 gl)
	{
		if (gl != null)
		{
			gl.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		}
		for (int s = 0; s < mLayoutsCount; s++)
		{
			mLayouts[s].onDestroy(gl);
			mLayouts[s] = null;
		}
		mLayoutsCount = 0;
		for (int r = 0; r < mSpritesCount; r++)
		{
			mSprites[r].onDestroy(gl);
			mSprites[r] = null;
		}
		mSpritesCount = 0;
		super.onDestroy(gl);
	}
}
