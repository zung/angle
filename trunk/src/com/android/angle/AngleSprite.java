package com.android.angle;

import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Sprite created with VBO. Slower than AngleSimpleSprite but supports rotation
 * and scale
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSprite extends AngleSimpleSprite
{
	protected float[] mVertexValues;
	protected float[] mTexCoordValues;
	public float mRotation; // Rotation in degrees (0º to 360º)
	private int mVertBufferIndex;
	private int mTextureCoordBufferIndex;
	private int mIndexBufferIndex;

	protected static final char[] sIndexValues = new char[] { 0, 1, 2, 1, 2, 3 };

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

		mVertexValues=new float[12];
		mTexCoordValues=new float[8];
		
		mVertexValues[0]= -mWidth / 2;
		mVertexValues[1]= mHeight / 2;
		mVertexValues[2]= 0;
		mVertexValues[3]= mWidth / 2;
		mVertexValues[4]= mHeight / 2;
		mVertexValues[5]= 0;
		mVertexValues[6]= -mWidth / 2;
		mVertexValues[7]= -mHeight / 2;
		mVertexValues[8]= 0;
		mVertexValues[9]= mWidth / 2;
		mVertexValues[10]= -mHeight / 2;
		mVertexValues[11]= 0;
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

			mTexCoordValues[0]=(mCropLeft + frameLeft) / W;
			mTexCoordValues[1]= (mCropBottom + frameTop) / H;
			mTexCoordValues[2]= (mCropRight + frameLeft) / W;
			mTexCoordValues[3]= (mCropBottom + frameTop) / H;
			mTexCoordValues[4]= (mCropLeft + frameLeft) / W;
			mTexCoordValues[5]= (mCropTop + frameTop) / H;
			mTexCoordValues[6]= (mCropRight + frameLeft) / W;
			mTexCoordValues[7]= (mCropTop + frameTop) / H;
		}
	}

	@Override
	public void afterLoadTexture(GL10 gl)
	{
      super.afterLoadTexture(gl);
      if (gl instanceof GL11)
      {
      int[] buffer = new int[1];
      GL11 gl11 = (GL11)gl;

      gl11.glGenBuffers(1, buffer, 0);
      mVertBufferIndex = buffer[0];
      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
      gl11.glBufferData(GL11.GL_ARRAY_BUFFER, 12 * 4, 
      		FloatBuffer.wrap(mVertexValues), GL11.GL_STATIC_DRAW);
      
      // Allocate and fill the texture coordinate buffer.
      gl11.glGenBuffers(1, buffer, 0);
      mTextureCoordBufferIndex = buffer[0];
      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER,  mTextureCoordBufferIndex);
      gl11.glBufferData(GL11.GL_ARRAY_BUFFER, 8 * 4, 
      		FloatBuffer.wrap(mTexCoordValues), GL11.GL_STATIC_DRAW);    
      
      // Unbind the array buffer.
      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
      
      // Allocate and fill the index buffer.
      gl11.glGenBuffers(1, buffer, 0);
      mIndexBufferIndex = buffer[0];
      gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 
              mIndexBufferIndex);
      // A char is 2 bytes.
      gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, 6 * 2, CharBuffer.wrap(sIndexValues), GL11.GL_STATIC_DRAW);
      
      // Unbind the element array buffer.
      gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
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

      if (gl instanceof GL11)
      {
      GL11 gl11 = (GL11)gl;

      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
      gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
      
      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
      gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
      
      gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferIndex);
      gl11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_SHORT, 0);
      
      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
      gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
      }
		gl.glPopMatrix();
	}

	@Override
	public void onDestroy(GL10 gl)
	{
      if (gl instanceof GL11) 
      {
         GL11 gl11 = (GL11)gl;
         int[] buffer = new int[1];
         buffer[0] = mVertBufferIndex;
         gl11.glDeleteBuffers(1, buffer, 0);
         
         buffer[0] = mTextureCoordBufferIndex;
         gl11.glDeleteBuffers(1, buffer, 0);
         
         buffer[0] = mIndexBufferIndex;
         gl11.glDeleteBuffers(1, buffer, 0);
     }
		super.onDestroy(gl);
	}
	
}
