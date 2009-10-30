package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Have the string and its position
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleString
{
	public AngleVector mPosition;
	public float mZ;
	private int mMaxLength; 
	private int mLength;
	private char[] mString;
	private AngleFont mFont;
	protected int[] mTextureIV = new int[4];
	
	public AngleString (AngleFont font, int maxLength)
	{
		mPosition=new AngleVector();
		mFont=font;
		mMaxLength=maxLength;
		mLength=0;
		mString=new char[mMaxLength];//Prevent runtime allocations
	}
	
	public void set (String src)
	{
		mLength=src.length();
		if (mLength>mMaxLength)
			mLength=mMaxLength;
		for (int c=0;c<mLength;c++)
			mString[c]=src.charAt(c);
	}

	public void draw(GL10 gl)
	{
		if (mFont!=null)
		{
			if (mFont.mHWTextureID >= 0)
			{
				gl.glBindTexture(GL10.GL_TEXTURE_2D, mFont.mHWTextureID);
/*
				mTextureIV[0]=0;
				mTextureIV[1]=256;
				mTextureIV[2]=256;
				mTextureIV[3]=-256;
				
				((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
						GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);

				((GL11Ext) gl).glDrawTexfOES(mPosition.mX,
						AngleMainEngine.mHeight - mPosition.mY-256, mZ, 256,
						256);
*/				
				float x=mPosition.mX;
				float y=mPosition.mY;
				for (int c=0;c<mLength;c++)
				{
					char chr=mString[c];
					if (chr==' ')
					{
						x+=mFont.mCharWidth['_'-33];
						continue;
					}
					if (chr=='\n')
					{
						y+=mFont.mHeight;
						continue;
					}
					chr-=33;
					mTextureIV[0]=mFont.mCharLeft[chr];
					mTextureIV[1]=mFont.mCharTop[chr]+mFont.mHeight;
					mTextureIV[2]=mFont.mCharWidth[chr];
					mTextureIV[3]=-mFont.mHeight;
					((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
							GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);

					((GL11Ext) gl).glDrawTexfOES(x,
							AngleMainEngine.mHeight - y - mFont.mHeight, mZ, mFont.mCharWidth[chr],
							mFont.mHeight);
					x+=mFont.mCharWidth[chr]+mFont.mSpace;
				}
				
			}
		}
	}
}
