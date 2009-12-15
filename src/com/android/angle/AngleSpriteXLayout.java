package com.android.angle;

import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL11;

/**
 * Sprite created with VBO. Slower than AngleSimpleSprite but supports rotation
 * and scale
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteXLayout extends AngleSpriteLayout
{
	public static final char[] sIndexValues = new char[] { 0, 1, 2, 1, 2, 3 };
	public int mIndexBufferIndex;
	public float[] mVertexValues;
	public int mVertBufferIndex;
	public boolean buffersChanged;

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
	public AngleSpriteXLayout(AngleSpritesEngine engine, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight)
	{
		super(engine, width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight);
		doInit();
	}

	public AngleSpriteXLayout(AngleSpritesEngine engine, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight, int frameCount, int frameColumns)
	{
		super(engine, width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, frameCount, frameColumns);
		doInit();

	}

	private void doInit()
	{
		buffersChanged = false;
		mVertexValues = new float[12];

		mVertexValues[0] = -mWidth / 2;
		mVertexValues[1] = mHeight / 2;
		mVertexValues[2] = 0;
		mVertexValues[3] = mWidth / 2;
		mVertexValues[4] = mHeight / 2;
		mVertexValues[5] = 0;
		mVertexValues[6] = -mWidth / 2;
		mVertexValues[7] = -mHeight / 2;
		mVertexValues[8] = 0;
		mVertexValues[9] = mWidth / 2;
		mVertexValues[10] = -mHeight / 2;
		mVertexValues[11] = 0;

		mVertBufferIndex = -1;
		mIndexBufferIndex = -1;
	}

	@Override
	public void createBuffers(GL11 gl)
	{
		if (AngleMainEngine.sUseHWBuffers)
		{
			// Allocate and fill the vertex buffer.
			mVertBufferIndex = AngleMainEngine.getHWBuffer();
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, 12 * 4, FloatBuffer.wrap(mVertexValues), GL11.GL_STATIC_DRAW);
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

			// Allocate and fill the index buffer.
			mIndexBufferIndex = AngleMainEngine.getHWBuffer();
			gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferIndex);
			gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, 6 * 2, CharBuffer.wrap(sIndexValues), GL11.GL_STATIC_DRAW);
			gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
	}

	@Override
	public void onDestroy(GL11 gl)
	{
		if (AngleMainEngine.sUseHWBuffers)
		{
			if (gl!=null)
			{
			int[] buffer = new int[1];

			if (mVertBufferIndex>-1)
			{
				buffer[0] = mVertBufferIndex;
				gl.glDeleteBuffers(1, buffer, 0);
			}

			if (mIndexBufferIndex>-1)
			{
				buffer[0] = mIndexBufferIndex;
				gl.glDeleteBuffers(1, buffer, 0);
			}
			}
		}
		super.onDestroy(gl);
	}

}
