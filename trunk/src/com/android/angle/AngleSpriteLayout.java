package com.android.angle;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

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

	public AngleVector roPivot; //Pivot point
	
	public float[] roVertexValues;
	public int roVertBufferIndex;
	

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

		roVertexValues = new float[12];
		roPivot=new AngleVector();

		setPivot(roWidth / 2, roHeight / 2);

		roVertBufferIndex = -1;
	}

	/**
	 * Set pivot point
	 * @param x
	 * @param y
	 */
	public void setPivot(int x, int y)
	{
		roPivot.set(x,y);
		
		roVertexValues[0] = -roPivot.mX;
		roVertexValues[1] = roHeight - roPivot.mY;
		roVertexValues[2] = roWidth - roPivot.mX;
		roVertexValues[3] = roHeight - roPivot.mY;
		roVertexValues[4] = -roPivot.mX;
		roVertexValues[5] = -roPivot.mY;
		roVertexValues[6] = roWidth - roPivot.mX;
		roVertexValues[7] = -roPivot.mY;
	}
	
	/**
	 * Called when hardware buffers are invalid
	 * @param gl
	 */
	protected void invalidateHardwareBuffers(GL10 gl)
	{
		int[] hwBuffers=new int[1];
		((GL11)gl).glGenBuffers(1, hwBuffers, 0);

		// Allocate and fill the vertex buffer.
		roVertBufferIndex = hwBuffers[0];
		((GL11)gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, roVertBufferIndex);
		((GL11)gl).glBufferData(GL11.GL_ARRAY_BUFFER, 8 * 4, FloatBuffer.wrap(roVertexValues), GL11.GL_STATIC_DRAW);
		((GL11)gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Called when hardware buffers must be released
	 * @param gl
	 */
	protected void releaseHardwareBuffers(GL10 gl)
	{
		int[] hwBuffers = new int[1];
		hwBuffers[0]=roVertBufferIndex;
		((GL11) gl).glDeleteBuffers(1, hwBuffers, 0);
		roVertBufferIndex=-1;
	}
	
	public void onDestroy(GL10 gl)
	{
		mTextureEngine.deleteTexture(roTexture);
	}

}
