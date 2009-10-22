package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Sprite that only supports direct texture copy to surface
 * If the dimensions are changed, the sprite will be scaled
 * The fastest one
 * 
 * @author Ivan Pajuelo
 *
 */
public class AngleSimpleSprite extends AngleAbstractSprite
{
	public int mWidth; //
	public int mHeight;// Dimensions
	
	public int mResourceID; //Resource bitmap
	public int mTextureID;  //Texture ID on AngleTextureEngine
	protected int[] mTextureCrop = new int[4]; //Cropping coordinates

	/**
	 * 
	 * @param width Width in pixels
	 * @param height Height in pixels
	 * @param resourceId Resource bitmap
	 * @param cropLeft Most left pixel in texture
	 * @param cropTop Most top pixel in texture
	 * @param cropWidth Width of the cropping rectangle in texture
	 * @param cropHeight Height of the cropping rectangle in texture
	 */
	public AngleSimpleSprite(int width, int height, int resourceId, int cropLeft,
			int cropTop, int cropWidth, int cropHeight)
	{
		mWidth = width;
		mHeight = height;
		mZ = 0.0f;
		mTextureID = -1;
		mResourceID = resourceId;
		mTextureCrop[0] = cropLeft; // Ucr
		mTextureCrop[1] = cropTop + cropHeight; // Vcr
		mTextureCrop[2] = cropWidth; // Wcr
		mTextureCrop[3] = -cropHeight; // Hcr
		loadTexture();
	}

	public void loadTexture()
	{
		mTextureID = AngleTextureEngine.createHWTextureFromResource(mResourceID);
	}

	public void afterLoadTexture()
	{
	}

	public void draw(GL10 gl)
	{
		if (mTextureID>=0)
		{
			AngleTextureEngine.bindTexture(gl, mTextureID);
	
			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureCrop, 0);
	
			((GL11Ext) gl).glDrawTexfOES(mCenter.mX-mWidth/2,
					AngleMainEngine.mHeight - mHeight/2 - mCenter.mY, mZ, mWidth, mHeight);
		}
	}
}
