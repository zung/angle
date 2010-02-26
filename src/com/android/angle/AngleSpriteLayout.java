package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Stores all the information about how to draw a sprite
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteLayout
{
	public int roWidth; //
	public int roHeight;// Dimensions (ReadOnly)

	public AngleTexture roTexture; //Texture (ReadOnly)
	public int roCropLeft;
	public int roCropTop;
	public int roCropWidth;
	public int roCropHeight; //Crop information (ReadOnly) 
	protected int mFrameCount;
	protected int mFrameColumns;
	private AngleTextureEngine mTextureEngine;

	private AngleVector[] mPivot; //Pivot point
	
	/**
	 * 
	 * @param view			Main AngleSurfaceView
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
	public AngleSpriteLayout(AngleSurfaceView view, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight)
	{
		doInit(view, width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, 1, 1);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
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
	public AngleSpriteLayout(AngleSurfaceView view, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight, int frameCount, int frameColumns)
	{
		doInit(view, width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, frameCount, frameColumns);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param width
	 *           Width in pixels
	 * @param height
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 */
	public AngleSpriteLayout(AngleSurfaceView view, int width, int height, int resourceId)
	{
		doInit(view, width, height, resourceId, 0, 0, width, height, 1, 1);
	}

	private void doInit(AngleSurfaceView view, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth, int cropHeight, int frameCount,
			int frameColumns)
	{
		mTextureEngine=view.getTextureEngine();
		roWidth = width;
		roHeight = height;
		roTexture = mTextureEngine.createTextureFromResourceId(resourceId);
		roCropLeft = cropLeft;
		roCropWidth = cropWidth;
		roCropTop = cropTop;
		roCropHeight = cropHeight;
		mFrameCount = frameCount;
		mFrameColumns = frameColumns;

		mPivot=new AngleVector[mFrameCount];
		for (int f=0;f<mFrameCount;f++)
			mPivot[f]=new AngleVector(roWidth / 2, roHeight / 2);

	}

	/**
	 * Set pivot point
	 * @param x
	 * @param y
	 */
	public void setPivot(int frame, float x, float y)
	{
		if (frame<mFrameCount)
			mPivot[frame].set(x,y);
	}

	public void setPivot(float x, float y)
	{
		for (int f=0;f<mFrameCount;f++)
			mPivot[f].set(x,y);
	}

	public AngleVector getPivot(int frame)
	{
		if (frame<mFrameCount)
			return mPivot[frame];
		return null;
	}

	public void fillVertexValues(int frame, float[] vertexValues)
	{
		if (frame<mFrameCount)
		{
			vertexValues[0] = -mPivot[frame].mX;
			vertexValues[1] = roHeight - mPivot[frame].mY;
			vertexValues[2] = roWidth - mPivot[frame].mX;
			vertexValues[3] = roHeight - mPivot[frame].mY;
			vertexValues[4] = -mPivot[frame].mX;
			vertexValues[5] = -mPivot[frame].mY;
			vertexValues[6] = roWidth - mPivot[frame].mX;
			vertexValues[7] = -mPivot[frame].mY;
		}
	}
	
	public void onDestroy(GL10 gl)
	{
		mTextureEngine.deleteTexture(roTexture);
	}
}
