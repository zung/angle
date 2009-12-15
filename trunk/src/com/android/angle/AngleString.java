package com.android.angle;

import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Have the string and its position. Length is automatically set when string
 * content is changed. But can be altered to create typing effect.
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleString extends AngleVisualObject
{
	public static final int aLeft = 0;
	public static final int aCenter = 1;
	public static final int aRight = 2;
	public AngleVector mPosition; // Position
	private int mMaxLength; // Maximum length of the string
	private int mLength; // Length to display
	private char[] mString; // characters
	private AngleFont mFont; // Font
	protected int[] mTextureIV = new int[4];
	public int mAlignment;

	/**
	 * 
	 * @param font
	 *           Font
	 * @param maxLength
	 *           Reserved length
	 */
	public AngleString(AngleTextEngine engine, AngleFont font, int maxLength)
	{
		mPosition = new AngleVector();
		mFont = font;
		mMaxLength = maxLength;
		mLength = 0;
		mString = new char[mMaxLength];// Prevent runtime allocations
		mAlignment = aLeft;
		engine.addString(this);
	}

	/**
	 * Changes the string content
	 * 
	 * @param src
	 */
	public void set(String src)
	{
		mLength = src.length();
		if (mLength > mMaxLength)
			mLength = mMaxLength;
		for (int c = 0; c < mLength; c++)
			mString[c] = src.charAt(c);
	}

	public void draw(GL11 gl)
	{
		if (mFont != null)
		{
			if (mFont.mTexture != null)
			{
				gl.glBindTexture(GL11.GL_TEXTURE_2D, mFont.mTexture.mHWTextureID);

				float x = getXPosition(0);
				float y = mPosition.mY;
				for (int c = 0; c < mLength; c++)
				{
					if (mString[c] == '\n')
					{
						y += mFont.mHeight;
						x = getXPosition(c + 1);
						continue;
					}
					char chr = mFont.getChar(mString[c]);
					if (chr == (char) -1)
					{
						x += mFont.mSpaceWidth;
						continue;
					}
					int chrWidth = mFont.mCharRight[chr] - mFont.mCharLeft[chr];
					mTextureIV[0] = mFont.mCharX[chr];
					mTextureIV[1] = mFont.mCharTop[chr] + mFont.mHeight;
					mTextureIV[2] = chrWidth;
					mTextureIV[3] = -mFont.mHeight;
					((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);

					((GL11Ext) gl).glDrawTexfOES(x + mFont.mCharLeft[chr], AngleMainEngine.mHeight - y - mFont.mHeight, mZ, chrWidth,
							mFont.mHeight);
					x += mFont.mCharRight[chr] + mFont.mSpace;
				}
			}
		}
	}

	private float getXPosition(int c)
	{
		if (mAlignment == aRight)
			return mPosition.mX - getLineWidth(c);
		else if (mAlignment == aCenter)
			return mPosition.mX - getLineWidth(c) / 2;
		return mPosition.mX;
	}

	private int getLineWidth(int c)
	{
		int ret = 0;
		for (; c < mLength; c++)
		{
			if (mString[c] == '\n')
				break;
			char chr = mFont.getChar(mString[c]);
			if (chr == (char) -1)
			{
				ret += mFont.mSpaceWidth;
				continue;
			}
			ret += mFont.mCharRight[chr] + mFont.mSpace;
			if (c == 0)
				ret -= mFont.mCharLeft[chr];
		}
		return ret;
	}

	public int getWidth()
	{
		int ret = 0;
		int maxRet = 0;
		for (int c = 0; c < mLength; c++)
		{
			if (mString[c] == '\n')
			{
				if (ret > maxRet)
					maxRet = ret;
				ret = 0;
				continue;
			}
			char chr = mFont.getChar(mString[c]);
			if (chr == (char) -1)
			{
				ret += mFont.mSpaceWidth;
				continue;
			}
			ret += mFont.mCharRight[chr] + mFont.mSpace;
			if (c == 0)
				ret -= mFont.mCharLeft[chr];
		}
		if (ret > maxRet)
			maxRet = ret;
		return maxRet;
	}

	public int getHeight()
	{
		int ret = mFont.mHeight;
		for (int c = 0; c < mLength; c++)
		{
			if (mString[c] == '\n')
			{
				ret += mFont.mHeight;
				continue;
			}
		}
		return ret;
	}

	public boolean test(float x, float y)
	{
		float left = getXPosition(0);
		if (x >= left)
			if (y >= mPosition.mY)
				if (x < left + getWidth())
					if (y < mPosition.mY + getHeight())
						return true;
		return false;

	}
}
