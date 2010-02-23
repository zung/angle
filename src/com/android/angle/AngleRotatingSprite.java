package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Sprite with rotating capabilities. Uses hardware buffers if available
 * @author Ivan
 *
 */
public class AngleRotatingSprite extends AngleAbstractSprite
{

	public float mRotation;
	protected float[] mTexCoordValues;
	protected int mTextureCoordBufferIndex;

	public AngleRotatingSprite(AngleSpriteLayout layout)
	{
		super(layout);
		mRotation = 0;
		mTexCoordValues = new float[8];
		mTextureCoordBufferIndex = -1;
		setFrame(0);
	}

	@Override
	public void invalidateTexture(GL10 gl)
	{
		setFrame(roFrame);
		super.invalidateTexture(gl);
	}

	@Override
	public void setFrame(int frame)
	{
		if (frame < mLayout.mFrameCount)
		{
			roFrame = frame;
			float W = mLayout.roTexture.mWidth;
			float H = mLayout.roTexture.mHeight;
			float frameLeft = (roFrame % mLayout.mFrameColumns) * mLayout.roCropWidth;
			float frameTop = (roFrame / mLayout.mFrameColumns) * mLayout.roCropHeight;

			mTexCoordValues[0] = (mLayout.roCropLeft + frameLeft) / W;
			mTexCoordValues[1] = (mLayout.roCropTop + mLayout.roCropHeight + frameTop) / H;
			mTexCoordValues[2] = (mLayout.roCropLeft + mLayout.roCropHeight + frameLeft) / W;
			mTexCoordValues[3] = (mLayout.roCropTop + mLayout.roCropHeight + frameTop) / H;
			mTexCoordValues[4] = (mLayout.roCropLeft + frameLeft) / W;
			mTexCoordValues[5] = (mLayout.roCropTop + frameTop) / H;
			mTexCoordValues[6] = (mLayout.roCropLeft + mLayout.roCropHeight + frameLeft) / W;
			mTexCoordValues[7] = (mLayout.roCropTop + frameTop) / H;
		}
	}

	@Override
	public void invalidateHardwareBuffers(GL10 gl)
	{
		int[] hwBuffers = new int[1];
		((GL11) gl).glGenBuffers(1, hwBuffers, 0);

		// Allocate and fill the texture buffer.
		mTextureCoordBufferIndex = hwBuffers[0];
		((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
		((GL11) gl).glBufferData(GL11.GL_ARRAY_BUFFER, 8 * 4, FloatBuffer.wrap(mTexCoordValues), GL11.GL_STATIC_DRAW);
		((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

		mLayout.invalidateHardwareBuffers(gl);
		super.invalidateHardwareBuffers(gl);
	}

	@Override
	public void releaseHardwareBuffers(GL10 gl)
	{
		int[] hwBuffers = new int[1];
		hwBuffers[0]=mTextureCoordBufferIndex;
		((GL11) gl).glDeleteBuffers(1, hwBuffers, 0);
		mTextureCoordBufferIndex=-1;
		
		mLayout.releaseHardwareBuffers(gl);
		super.releaseHardwareBuffers(gl);
	}

	@Override
	public void draw(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glTranslatef(mPosition.mX, mPosition.mY, mZ);
		if (mRotation != 0)
			gl.glRotatef(-mRotation, 0, 0, 1);
		if ((mScale.mX != 1) || (mScale.mY != 1))
			gl.glScalef(mScale.mX, mScale.mY, 1);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, mLayout.roTexture.mHWTextureID);
	   gl.glColor4f(mRed,mGreen,mBlue,mAlpha);

		if (AngleSurfaceView.sUseHWBuffers)
		{
			if (mTextureCoordBufferIndex<0)
				invalidateHardwareBuffers(gl);

			((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, mLayout.roVertBufferIndex);
			((GL11) gl).glVertexPointer(2, GL10.GL_FLOAT, 0, 0);

			((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
			((GL11) gl).glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);

			((GL11) gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, AngleSurfaceView.roIndexBufferIndex);
			((GL11) gl).glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, 0);

			((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			((GL11) gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
		else
		{
			CharBuffer mIndexBuffer;
			FloatBuffer mVertexBuffer;
			FloatBuffer mTexCoordBuffer;

			mIndexBuffer = ByteBuffer.allocateDirect(AngleSurfaceView.sIndexValues.length*2).order(ByteOrder.nativeOrder()).asCharBuffer();
			mVertexBuffer = ByteBuffer.allocateDirect(mLayout.roVertexValues.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mTexCoordBuffer = ByteBuffer.allocateDirect(mTexCoordValues.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();

			for (int i = 0; i < AngleSurfaceView.sIndexValues.length; ++i)
				mIndexBuffer.put(i, AngleSurfaceView.sIndexValues[i]);
			for (int i = 0; i < mLayout.roVertexValues.length; ++i)
				mVertexBuffer.put(i, mLayout.roVertexValues[i]);
			for (int i = 0; i < mTexCoordValues.length; ++i)
				mTexCoordBuffer.put(i, mTexCoordValues[i]);

			gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
			gl.glDrawElements(GL10.GL_TRIANGLES, AngleSurfaceView.sIndexValues.length, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		}

		gl.glPopMatrix();
		super.draw(gl);
	}

}
