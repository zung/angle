package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sprite reference for dynamic add an remove from sprite engine
 * AngleSpritesEngine.useReferences must be set to true
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteReference extends AngleAbstractReference
{
	private AngleSprite mSprite;
	protected FloatBuffer mTexCoordBuffer;
	private int mFrame;
	public float mRotation; // Rotation

	@Override
	public void afterAdd()
	{
		setFrame(mFrame);
	}

	protected void setFrame(int frame)
	{
		if (frame < mSprite.mFrameCount)
		{
			mFrame = frame;
			float W = AngleTextureEngine.getTextureWidth(mSprite.mTextureID);
			float H = AngleTextureEngine.getTextureHeight(mSprite.mTextureID);
			float frameLeft = (mFrame % mSprite.mFrameColumns)
					* (mSprite.mCropRight - mSprite.mCropLeft);
			float frameTop = (mFrame / mSprite.mFrameColumns)
					* (mSprite.mCropBottom - mSprite.mCropTop);

			mTexCoordBuffer.put(0, (mSprite.mCropLeft + frameLeft) / W);
			mTexCoordBuffer.put(1, (mSprite.mCropBottom + frameTop) / H);
			mTexCoordBuffer.put(2, (mSprite.mCropRight + frameLeft) / W);
			mTexCoordBuffer.put(3, (mSprite.mCropBottom + frameTop) / H);
			mTexCoordBuffer.put(4, (mSprite.mCropLeft + frameLeft) / W);
			mTexCoordBuffer.put(5, (mSprite.mCropTop + frameTop) / H);
			mTexCoordBuffer.put(6, (mSprite.mCropRight + frameLeft) / W);
			mTexCoordBuffer.put(7, (mSprite.mCropTop + frameTop) / H);
		}
	}

	/**
	 * 
	 * @param sprite
	 *           Sprite referenced
	 */
	public AngleSpriteReference(AngleSprite sprite)
	{
		mSprite = sprite;
		mFrame = 0;
		mRotation = 0;
		mTexCoordBuffer = ByteBuffer.allocateDirect(32).order(
				ByteOrder.nativeOrder()).asFloatBuffer();
	}

	@Override
	public void draw(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glTranslatef(mCenter.mX, mCenter.mY, mZ);
		gl.glRotatef(-mRotation, 0, 0, 1);

		AngleTextureEngine.bindTexture(gl, mSprite.mTextureID);
/*
		// Estas 3 alocatan memoria
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mSprite.mVertexValues);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, AngleSprite.sIndexValues.length,
				GL10.GL_UNSIGNED_SHORT, mSprite.mIndexBuffer);
		// ------------------------
*/
		gl.glPopMatrix();
	}

}
