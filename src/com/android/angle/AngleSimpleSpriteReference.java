package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Sprite reference for dynamic add an remove from sprite engine
 * AngleSpritesEngine.useReferences must be set to true
 * 
 * @author Ivan Pajuelo
 *
 */
public class AngleSimpleSpriteReference extends AngleVisualObject
{
	public AngleSimpleSprite mSprite; //Sprite referenced

	/**
	 * 
	 * @param sprite Sprite referenced
	 */
	protected AngleSimpleSpriteReference (AngleSimpleSprite sprite)
	{
		mSprite=sprite;
	}
	
	@Override
	public void draw(GL10 gl)
	{
		if (mSprite.mTextureID>=0)
		{
			AngleTextureEngine.bindTexture(gl, mSprite.mTextureID);
	
			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, mSprite.mTextureIV, 0);
	
			((GL11Ext) gl).glDrawTexfOES(mCenter.mX-mSprite.mWidth/2,
					AngleMainEngine.mHeight - mSprite.mHeight/2 - mCenter.mY, mZ, mSprite.mWidth, mSprite.mHeight);
		}
	}
}
