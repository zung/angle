package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Fastest sprite with no rotation support
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSprite extends AngleAbstractSprite
{
	protected int[] mTextureIV; //Texture coordinates
	
	/**
	 * 
	 * @param layout
	 *           Sprite referenced
	 */
	public AngleSprite(AngleSpriteLayout layout)
	{
		super(layout);
		mTextureIV = new int[4];
		mTextureIV[2] = mLayout.roCropWidth; // Wcr
		mTextureIV[3] = -mLayout.roCropHeight; // Hcr
		setFrame(0);
	}

	@Override
	public void setFrame(int frame)
	{
		if (frame < mLayout.mFrameCount)
		{
			roFrame = frame;
			mTextureIV[0] = mLayout.roCropLeft + ((roFrame % mLayout.mFrameColumns) * mLayout.roCropWidth);// Ucr
			mTextureIV[1] = (mLayout.roCropTop + mLayout.roCropHeight) + ((roFrame / mLayout.mFrameColumns) * mLayout.roCropHeight);// Vcr
		}
	}

	@Override
	public void draw(GL10 gl)
	{
		if (mLayout.roTexture != null)
		{
			if (mLayout.roTexture.mHWTextureID>-1)
			{
				gl.glBindTexture(GL10.GL_TEXTURE_2D, mLayout.roTexture.mHWTextureID);
			   gl.glColor4f(mRed,mGreen,mBlue,mAlpha);

				((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);
	
				((GL11Ext) gl).glDrawTexfOES(mPosition.mX - (mLayout.getPivot(roFrame).mX*mScale.mX), 
						AngleSurfaceView.roHeight-((mPosition.mY - (mLayout.getPivot(roFrame).mY*mScale.mY))+(mLayout.roHeight * mScale.mY)), mZ, mLayout.roWidth * mScale.mX, mLayout.roHeight * mScale.mY);
			}
		}
		super.draw(gl);
	}
}
