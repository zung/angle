package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL11;

/**
 * Sprite reference for dynamic add an remove from sprite engine
 * AngleSpritesEngine.useReferences must be set to true
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteX extends AngleAbstractSprite
{
	protected AngleSpriteXLayout mLayout;
	protected float[] mTexCoordValues;
	protected int mTextureCoordBufferIndex;
	protected int mFrame;
	public float mRotation;
	public float mScale;
	protected boolean invalidTexBuffer;

	/**
	 * 
	 * @param layout
	 *           Sprite referenced
	 */
	public AngleSpriteX(AngleSpritesEngine engine, AngleSpriteXLayout layout)
	{
		mLayout = layout;
		mRotation = 0;
		mScale = 1;
		mTextureCoordBufferIndex = -1;
		mTexCoordValues = new float[8];
		setFrame(0);
		engine.addSprite(this);
	}

	protected void setFrame(int frame)
	{
		if (frame < mLayout.mFrameCount)
		{
			mFrame = frame;
			float W = mLayout.mTexture.mWidth;
			float H = mLayout.mTexture.mHeight;
			float frameLeft = (mFrame % mLayout.mFrameColumns) * mLayout.mCropWidth;
			float frameTop = (mFrame / mLayout.mFrameColumns) * mLayout.mCropHeight;

			mTexCoordValues[0] = (mLayout.mCropLeft + frameLeft) / W;
			mTexCoordValues[1] = (mLayout.mCropTop + mLayout.mCropHeight + frameTop) / H;
			mTexCoordValues[2] = (mLayout.mCropLeft + mLayout.mCropHeight + frameLeft) / W;
			mTexCoordValues[3] = (mLayout.mCropTop + mLayout.mCropHeight + frameTop) / H;
			mTexCoordValues[4] = (mLayout.mCropLeft + frameLeft) / W;
			mTexCoordValues[5] = (mLayout.mCropTop + frameTop) / H;
			mTexCoordValues[6] = (mLayout.mCropLeft + mLayout.mCropHeight + frameLeft) / W;
			mTexCoordValues[7] = (mLayout.mCropTop + frameTop) / H;

			invalidTexBuffer = true;
		}
	}

	@Override
	public void afterLoadTexture(GL11 gl)
	{
		super.afterLoadTexture(gl);
		setFrame(mFrame);
	}

	protected void fillTexBuffer(GL11 gl)
	{
		if (AngleMainEngine.sUseHWBuffers)
		{
			if (invalidTexBuffer)
			{
				invalidTexBuffer = false;
				if (mTextureCoordBufferIndex>-1)
				{
					gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
					if (gl.glIsBuffer(mTextureCoordBufferIndex)) //Ok
					{
						gl.glBufferData(GL11.GL_ARRAY_BUFFER, 8 * 4, FloatBuffer.wrap(mTexCoordValues), GL11.GL_STATIC_DRAW);
						gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
					}
				}
			}
		}
	}

	@Override
	public void createBuffers(GL11 gl)
	{
		if (AngleMainEngine.sUseHWBuffers)
		{
			invalidTexBuffer = true;
			mTextureCoordBufferIndex = AngleMainEngine.getHWBuffer();
		}
		super.createBuffers(gl);
	}

	@Override
	public void draw(GL11 gl)
	{
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glTranslatef(mCenter.mX, mCenter.mY, mZ);
		if (mRotation != 0)
			gl.glRotatef(-mRotation, 0, 0, 1);
		if (mScale != 0)
			gl.glScalef(mScale, mScale, 1);

		gl.glBindTexture(GL11.GL_TEXTURE_2D, mLayout.mTexture.mHWTextureID);

		if (AngleMainEngine.sUseHWBuffers)
		{
			if (mLayout.mVertBufferIndex<0)
				mLayout.createBuffers(gl);
			if (mTextureCoordBufferIndex < 0)
				createBuffers(gl);
			if (invalidTexBuffer)
				fillTexBuffer(gl);

				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mLayout.mVertBufferIndex);
				gl.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
				gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);

				gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mLayout.mIndexBufferIndex);
				gl.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_SHORT, 0);

				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
				gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
		else
		{
			FloatBuffer mVertexBuffer;
			FloatBuffer mTexCoordBuffer;
			CharBuffer mIndexBuffer;

			mVertexBuffer = ByteBuffer.allocateDirect(48).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mTexCoordBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mIndexBuffer = ByteBuffer.allocateDirect(12).order(ByteOrder.nativeOrder()).asCharBuffer();

			for (int i = 0; i < AngleSpriteXLayout.sIndexValues.length; ++i)
				mIndexBuffer.put(i, AngleSpriteXLayout.sIndexValues[i]);
			for (int i = 0; i < mLayout.mVertexValues.length; ++i)
				mVertexBuffer.put(i, mLayout.mVertexValues[i]);
			for (int i = 0; i < mTexCoordValues.length; ++i)
				mTexCoordBuffer.put(i, mTexCoordValues[i]);

			gl.glVertexPointer(3, GL11.GL_FLOAT, 0, mVertexBuffer);
			gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, mTexCoordBuffer);
			gl.glDrawElements(GL11.GL_TRIANGLES, AngleSpriteXLayout.sIndexValues.length, GL11.GL_UNSIGNED_SHORT, mIndexBuffer);
		}

		gl.glPopMatrix();
	}

	@Override
	public void onDestroy(GL11 gl)
	{
		if (AngleMainEngine.sUseHWBuffers)
		{
			if (gl!=null)
			{
			int[] buffer = new int[1];

			if (mTextureCoordBufferIndex>-1)
			{
				buffer[0] = mTextureCoordBufferIndex;
				gl.glDeleteBuffers(1, buffer, 0);
				mTextureCoordBufferIndex = -1;
			}
			}
		}
		super.onDestroy(gl);
	}

}
