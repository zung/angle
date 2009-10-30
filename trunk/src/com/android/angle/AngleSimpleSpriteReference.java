package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Sprite reference for dynamic add an remove from sprite engine
 * AngleSpritesEngine.useReferences must be set to true
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSimpleSpriteReference extends AngleAbstractReference
{
	public AngleSimpleSprite mSprite; // Sprite referenced
	protected int mFrame;
	protected int[] mTextureIV = new int[4];

	/**
	 * 
	 * @param sprite
	 *           Sprite referenced
	 */
	public AngleSimpleSpriteReference(AngleSimpleSprite sprite)
	{
		mSprite = sprite;
		mFrame = 0;
		mTextureIV[2] = mSprite.mTextureIV[2]; // Wcr
		mTextureIV[3] = mSprite.mTextureIV[3]; // Hcr
	}

	@Override
	public void afterAdd()
	{
		setFrame(mFrame);
	}

	protected void setFrame(int frame)
	{
		if (frame < mSprite.mFrameCount)
		{
			mFrame = frame;
			mTextureIV[0] = mSprite.mCropLeft
					+ ((mFrame % mSprite.mFrameColumns) * mTextureIV[2]);// Ucr
			mTextureIV[1] = mSprite.mCropBottom
					+ ((mFrame / mSprite.mFrameColumns) * -mTextureIV[3]);// Vcr
		}
	}

	@Override
	public void draw(GL10 gl)
	{
		if (mSprite.mTextureID >= 0)
		{
			AngleTextureEngine.bindTexture(gl, mSprite.mTextureID);

			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);

			((GL11Ext) gl).glDrawTexfOES(mCenter.mX - mSprite.mWidth / 2,
					AngleMainEngine.mHeight - mCenter.mY - mSprite.mHeight / 2, mZ,
					mSprite.mWidth, mSprite.mHeight);
		}
	}
}
