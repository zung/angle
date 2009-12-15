package com.android.angle;

import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Sprite reference for dynamic add an remove from sprite engine
 * AngleSpritesEngine.useReferences must be set to true
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSprite extends AngleAbstractSprite
{
	public AngleSpriteLayout mLayout; // Sprite referenced
	public int mFrame;
	public float mScale;
	protected int[] mTextureIV = new int[4];

	/**
	 * 
	 * @param layout
	 *           Sprite referenced
	 */
	public AngleSprite(AngleSpritesEngine engine, AngleSpriteLayout layout)
	{
		mLayout = layout;
		setFrame(0);
		mScale = 1;
		mTextureIV[2] = mLayout.mCropWidth; // Wcr
		mTextureIV[3] = -mLayout.mCropHeight; // Hcr
		engine.addSprite(this);
	}

	public void setFrame(int frame)
	{
		if (frame < mLayout.mFrameCount)
		{
			mFrame = frame;
			mTextureIV[0] = mLayout.mCropLeft + ((mFrame % mLayout.mFrameColumns) * mLayout.mCropWidth);// Ucr
			mTextureIV[1] = (mLayout.mCropTop + mLayout.mCropHeight) + ((mFrame / mLayout.mFrameColumns) * mLayout.mCropHeight);// Vcr
		}
	}

	@Override
	public void draw(GL11 gl)
	{
		if (mLayout.mTexture != null)
		{
			gl.glBindTexture(GL11.GL_TEXTURE_2D, mLayout.mTexture.mHWTextureID);

			gl.glTexParameteriv(GL11.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);

			((GL11Ext) gl).glDrawTexfOES(mCenter.mX - (mLayout.mWidth * mScale) / 2, AngleMainEngine.mHeight - mCenter.mY
					- (mLayout.mHeight * mScale) / 2, mZ, mLayout.mWidth * mScale, mLayout.mHeight * mScale);
		}
	}
}
