package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Sprite that only supports direct texture copy to surface If the dimensions
 * are changed, the sprite will be scaled The fastest one
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSimpleSprite extends AngleAbstractSprite
{
	public int mWidth; //
	public int mHeight;// Dimensions

	public int mResourceID; // Resource bitmap
	public int mTextureID; // Texture ID on AngleTextureEngine
	protected int mCropLeft;
	protected int mCropRight;
	protected int mCropTop;
	protected int mCropBottom;
	protected int mFrameCount;
	protected int mFrameColumns;
	protected int mFrame;
	protected int[] mTextureIV = new int[4];

	/**
	 * 
	 * @param width
	 *           Width in pixels
	 * @param height
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 * @param cropLeft
	 *           Most left pixel in texture
	 * @param cropTop
	 *           Most top pixel in texture
	 * @param cropWidth
	 *           Width of the cropping rectangle in texture
	 * @param cropHeight
	 *           Height of the cropping rectangle in texture
	 */
	public AngleSimpleSprite(int width, int height, int resourceId,
			int cropLeft, int cropTop, int cropWidth, int cropHeight)
	{
		init(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight,
				1, 1);
	}

	/**
	 * 
	 * @param width
	 *           Width in pixels
	 * @param height
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 * @param cropLeft
	 *           Most left pixel in texture
	 * @param cropTop
	 *           Most top pixel in texture
	 * @param cropWidth
	 *           Width of the cropping rectangle in texture
	 * @param cropHeight
	 *           Height of the cropping rectangle in texture
	 * @param frameCount
	 *           Number of frames in animation
	 * @param frameColumns
	 *           Number of frames horizontally in texture
	 */
	public AngleSimpleSprite(int width, int height, int resourceId,
			int cropLeft, int cropTop, int cropWidth, int cropHeight,
			int frameCount, int frameColumns)
	{
		init(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight,
				frameCount, frameColumns);
	}

	private void init(int width, int height, int resourceId, int cropLeft,
			int cropTop, int cropWidth, int cropHeight, int frameCount,
			int frameColumns)
	{
		mWidth = width;
		mHeight = height;
		mZ = 0.0f;
		mTextureID = -1;
		mResourceID = resourceId;
		mCropLeft = cropLeft;
		mCropRight = cropLeft + cropWidth;
		mCropTop = cropTop;
		mCropBottom = cropTop + cropHeight;
		mTextureIV[2] = cropWidth; // Wcr
		mTextureIV[3] = -cropHeight; // Hcr
		mFrame = 0;
		mFrameCount = frameCount;
		mFrameColumns = frameColumns;
		loadTexture(null);
	}

	@Override
	public void loadTexture(GL10 gl)
	{
		mTextureID = AngleTextureEngine.createHWTextureFromResource(mResourceID);
	}

	public void afterLoadTexture(GL10 gl)
	{
		setFrame(mFrame);
	}

	protected void setFrame(int frame)
	{
		if (frame < mFrameCount)
		{
			mFrame = frame;
			mTextureIV[0] = mCropLeft + ((mFrame % mFrameColumns) * mTextureIV[2]);// Ucr
			mTextureIV[1] = mCropBottom
					+ ((mFrame / mFrameColumns) * -mTextureIV[3]);// Vcr
		}
	}

	public void draw(GL10 gl)
	{
		if (mTextureID >= 0)
		{
			AngleTextureEngine.bindTexture(gl, mTextureID);

			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);

			((GL11Ext) gl).glDrawTexfOES(mCenter.mX - mWidth / 2,
					AngleMainEngine.mHeight - mCenter.mY - mHeight / 2, mZ, mWidth,
					mHeight);
		}
	}
}
