package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sprite created with VBO. Slower than AngleSimpleSprite but supports rotation
 * and scale
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSprite extends AngleSimpleSprite
{
	protected FloatBuffer mVertexBuffer;
	protected FloatBuffer mTexCoordBuffer;
	protected CharBuffer mIndexBuffer;
	public float mRotation; // Rotation in degrees (0º to 360º)

	protected static final char[] sIndexValues = new char[] { 0, 1, 2, 1, 2, 3, };

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
	public AngleSprite(int width, int height, int resourceId, int cropLeft,
			int cropTop, int cropWidth, int cropHeight)
	{
		super(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight);

		mRotation = 0;

		mVertexBuffer = ByteBuffer.allocateDirect(48).order(
				ByteOrder.nativeOrder()).asFloatBuffer();
		mTexCoordBuffer = ByteBuffer.allocateDirect(32).order(
				ByteOrder.nativeOrder()).asFloatBuffer();
		mIndexBuffer = ByteBuffer.allocateDirect(12).order(
				ByteOrder.nativeOrder()).asCharBuffer();

		for (int i = 0; i < sIndexValues.length; ++i)
			mIndexBuffer.put(i, sIndexValues[i]);

		mVertexBuffer.put(0, -mWidth / 2);
		mVertexBuffer.put(1, -mHeight / 2);
		mVertexBuffer.put(2, 0);
		mVertexBuffer.put(3, mWidth / 2);
		mVertexBuffer.put(4, -mHeight / 2);
		mVertexBuffer.put(5, 0);
		mVertexBuffer.put(6, -mWidth / 2);
		mVertexBuffer.put(7, mHeight / 2);
		mVertexBuffer.put(8, 0);
		mVertexBuffer.put(9, mWidth / 2);
		mVertexBuffer.put(10, mHeight / 2);
		mVertexBuffer.put(11, 0);
	}

	@Override
	protected void setFrame(int frame)
	{
		if (frame < mFrameCount)
		{
			mFrame = frame;
			float W = AngleTextureEngine.getTextureWidth(mTextureID);
			float H = AngleTextureEngine.getTextureHeight(mTextureID);
			float frameLeft = (mFrame % mFrameColumns) * (mCropRight - mCropLeft);
			float frameTop = (mFrame / mFrameColumns) * (mCropBottom - mCropTop);

			mTexCoordBuffer.put(0, (mCropLeft + frameLeft) / W);
			mTexCoordBuffer.put(1, (mCropBottom + frameTop) / H);
			mTexCoordBuffer.put(2, (mCropRight + frameLeft) / W);
			mTexCoordBuffer.put(3, (mCropBottom + frameTop) / H);
			mTexCoordBuffer.put(4, (mCropLeft + frameLeft) / W);
			mTexCoordBuffer.put(5, (mCropTop + frameTop) / H);
			mTexCoordBuffer.put(6, (mCropRight + frameLeft) / W);
			mTexCoordBuffer.put(7, (mCropTop + frameTop) / H);
		}
	}

	@Override
	public void draw(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glTranslatef(mCenter.mX, mCenter.mY, mZ);
		gl.glRotatef(-mRotation, 0, 0, 1);

		AngleTextureEngine.bindTexture(gl, mTextureID);

		// Estas 3 alocatan memoria
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, sIndexValues.length,
				GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
		// ------------------------

		gl.glPopMatrix();
	}
}
