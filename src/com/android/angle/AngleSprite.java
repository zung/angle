package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

public class AngleSprite extends AngleVisible
{
	public int mResourceID;
	public int mTextureID;
	private int[] mTextureCrop = new int[4];

	public AngleSprite(int width, int height, AngleTextureClip clip)
	{
		super(width, height);
		mResourceID=clip.mID;
		mTextureCrop[0] = clip.mLeft;					//Ucr
		mTextureCrop[1] = clip.mTop+clip.mHeight; //Vcr
		mTextureCrop[2] = clip.mWidth;				//Wcr
		mTextureCrop[3] = -clip.mHeight;				//Hcr
	}
	
	public AngleSprite(int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth, int cropHeight)
	{
		super(width, height);
		mResourceID=resourceId;
		mTextureCrop[0] = cropLeft;				//Ucr
		mTextureCrop[1] = cropTop+cropHeight; 	//Vcr
		mTextureCrop[2] = cropWidth;				//Wcr
		mTextureCrop[3] = -cropHeight;			//Hcr
	}
	
	public void draw(GL10 gl)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, AngleTextureEngine.mTextures[mTextureID].mHWTextureID);

		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
				GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureCrop, 0);

		((GL11Ext) gl).glDrawTexfOES(mX, AngleRenderEngine.mHeight-mHeight-mY, mZ, mWidth, mHeight);
	}
	
	
}
