package com.android.angle;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.util.Log;

/**
 * Sprite reference for dynamic add an remove from sprite engine
 * AngleSpritesEngine.useReferences must be set to true
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteReference extends AngleAbstractReference
{
	protected AngleSprite mSprite;
	protected float[] mTexCoordValues;
	protected int mTextureCoordBufferIndex;
	protected int mFrame;
	public float mRotation; // Rotation
	protected boolean buffersChanged;

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
		mTextureCoordBufferIndex=-1;
		mTexCoordValues=new float[8];
	}

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

			mTexCoordValues[0]= (mSprite.mCropLeft + frameLeft) / W;
			mTexCoordValues[1]= (mSprite.mCropBottom + frameTop) / H;
			mTexCoordValues[2]= (mSprite.mCropRight + frameLeft) / W;
			mTexCoordValues[3]= (mSprite.mCropBottom + frameTop) / H;
			mTexCoordValues[4]= (mSprite.mCropLeft + frameLeft) / W;
			mTexCoordValues[5]= (mSprite.mCropTop + frameTop) / H;
			mTexCoordValues[6]= (mSprite.mCropRight + frameLeft) / W;
			mTexCoordValues[7]= (mSprite.mCropTop + frameTop) / H;

			buffersChanged=true;
			AngleTextureEngine.buffersChanged=true;
		}
	}

	@Override
	public void afterLoadTexture(GL10 gl)
	{
		super.afterLoadTexture(gl);
      mTextureCoordBufferIndex=-1;
      setFrame (mFrame);
	}

	@Override
	public void createBuffers(GL10 gl)
	{
		if (buffersChanged)
		{
			buffersChanged=false;
			if (AngleRenderThread.gl instanceof GL11)
	      {
		      int[] buffer = new int[1];
		      GL11 gl11 = (GL11)AngleRenderThread.gl;
	
		      if (gl11.glIsBuffer(mTextureCoordBufferIndex))
		      {
			      Log.e("SpriteReference","buffer deleted");
		      	buffer[0] = mTextureCoordBufferIndex;
		      	gl11.glDeleteBuffers(1, buffer, 0);
		      }
	
		      // Allocate and fill the texture coordinate buffer.
		      gl11.glGenBuffers(1, buffer, 0);
		      mTextureCoordBufferIndex = buffer[0];
		      Log.e("ASR","Err="+gl11.glGetError());
		      Log.e("ASR","Idx="+mTextureCoordBufferIndex);
		      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER,  mTextureCoordBufferIndex);
		      gl11.glBufferData(GL11.GL_ARRAY_BUFFER, 8 * 4,	FloatBuffer.wrap(mTexCoordValues), GL11.GL_STATIC_DRAW);    
		      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		      Log.e("SpriteReference","buffer created");
	      }
		}
		super.createBuffers(gl);
	}

	@Override
	public void draw(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glTranslatef(mCenter.mX, mCenter.mY, mZ);
		gl.glRotatef(-mRotation, 0, 0, 1);

		AngleTextureEngine.bindTexture(gl, mSprite.mTextureID);

      if (gl instanceof GL11)
      {
	      GL11 gl11 = (GL11)gl;
	
	      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mSprite.mVertBufferIndex);
	      gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
	      
	      gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
	      gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
	      
	      gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mSprite.mIndexBufferIndex);
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
         
	      if (gl11.glIsBuffer(mTextureCoordBufferIndex))
	      {
	      	buffer[0] = mTextureCoordBufferIndex;
	      	gl11.glDeleteBuffers(1, buffer, 0);
	      	mTextureCoordBufferIndex = -1;
	      }
         
      }
		super.onDestroy(gl);
	}

}
