package com.android.angle;

import javax.microedition.khronos.opengles.GL11;

/**
 * Sprite that only supports direct texture copy to surface If the dimensions
 * are changed, the sprite will be scaled The fastest one
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteLayout
{
	public int mWidth; //
	public int mHeight;// Dimensions

	public AngleTexture mTexture;
	public int mCropLeft;
	public int mCropTop;
	public int mCropWidth;
	public int mCropHeight;
	protected int mFrameCount;
	protected int mFrameColumns;
	protected int mFrame;

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
	public AngleSpriteLayout(AngleSpritesEngine engine, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight)
	{
		doInit(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, 1, 1);
		engine.addLayout(this);
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
	public AngleSpriteLayout(AngleSpritesEngine engine, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight, int frameCount, int frameColumns)
	{
		doInit(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, frameCount, frameColumns);
		engine.addLayout(this);
	}

	private void doInit(int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth, int cropHeight, int frameCount,
			int frameColumns)
	{
		mWidth = width;
		mHeight = height;
		mTexture = AngleTextureEngine.createTextureFromResourceId(resourceId);
		mCropLeft = cropLeft;
		mCropWidth = cropWidth;
		mCropTop = cropTop;
		mCropHeight = cropHeight;
		mFrame = 0;
		mFrameCount = frameCount;
		mFrameColumns = frameColumns;
	}

	public void createBuffers(GL11 gl)
	{
	}

	public void onDestroy(GL11 gl)
	{
		AngleTextureEngine.deleteTexture(mTexture);
	}
}
