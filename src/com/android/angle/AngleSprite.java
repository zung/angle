package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class AngleSprite extends AngleSimpleSprite
{
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTexCoordBuffer;
	private CharBuffer mIndexBuffer;
	private float mCropLeft;
	private float mCropRight;
	private float mCropTop;
	private float mCropBottom;
	public float mRotation;

	private static final char[] sIndexValues = new char[] 
	{
		0, 1, 2,
		1, 2, 3,
	};

	public AngleSprite(int width, int height, int resourceId, int cropLeft,
			int cropTop, int cropWidth, int cropHeight)
	{
		super(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight);

		mRotation=0;
		
		mCropLeft=cropLeft;
		mCropRight=cropLeft+cropWidth;
		mCropTop=cropTop;
		mCropBottom=cropTop+cropHeight;

		mVertexBuffer = ByteBuffer.allocateDirect(48)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTexCoordBuffer = ByteBuffer.allocateDirect(32)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mIndexBuffer = ByteBuffer.allocateDirect(12)
		.order(ByteOrder.nativeOrder()).asCharBuffer();

		for (int i = 0; i < sIndexValues.length; ++i)
			mIndexBuffer.put(i, sIndexValues[i]);

		
		mVertexBuffer.put(0,-mWidth/2);
		mVertexBuffer.put(1,-mHeight/2);
		mVertexBuffer.put(2,0);
		mVertexBuffer.put(3,mWidth/2);
		mVertexBuffer.put(4,-mHeight/2);
		mVertexBuffer.put(5,0);
		mVertexBuffer.put(6,-mWidth/2);
		mVertexBuffer.put(7,mHeight/2);
		mVertexBuffer.put(8,0);
		mVertexBuffer.put(9,mWidth/2);
		mVertexBuffer.put(10,mHeight/2);
		mVertexBuffer.put(11,0);
	}
	

	@Override
	public void afterLoadTexture()
	{
		float W=AngleTextureEngine.getTextureWidth(mTextureID);
		float H=AngleTextureEngine.getTextureHeight(mTextureID);
		
		mTexCoordBuffer.put(0,mCropLeft/W);
		mTexCoordBuffer.put(1,mCropBottom/H);
		mTexCoordBuffer.put(2,mCropRight/W);
		mTexCoordBuffer.put(3,mCropBottom/H);
		mTexCoordBuffer.put(4,mCropLeft/W);
		mTexCoordBuffer.put(5,mCropTop/H);
		mTexCoordBuffer.put(6,mCropRight/W);
		mTexCoordBuffer.put(7,mCropTop/H);
	}

	@Override
	public void draw(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glTranslatef(mX, AngleMainEngine.mHeight - mY, mZ);
		gl.glRotatef(-mRotation, 0, 0, 1);

		AngleTextureEngine.bindTexture(gl, mTextureID);
		
		//Estas 3 alocatan memoria
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, sIndexValues.length, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
		//------------------------
		
		gl.glPopMatrix();
	}
}
