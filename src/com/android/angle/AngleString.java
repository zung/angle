package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Have the string and its position. Length is automatically set when string content is changed. But can be altered to create typing effect.
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleString extends AngleObject
{
	public static final int aLeft = 0;
	public static final int aCenter = 1;
	public static final int aRight = 2;
	protected String mString;
	public int m1stChar; // 1st char to display
	public int mLength; // Length to display
	protected AngleFont mFont; // Font
	protected int[] mTextureIV = new int[4]; // Texture coordinates
	public AngleVector mPosition; // Position
	public float mZ; // Z position (0=Near, 1=Far)
	public int mAlignment; // Text alignment
	public float mRed; // Red tint (0 - 1)
	public float mGreen; // Green tint (0 - 1)
	public float mBlue; // Blue tint (0 - 1)
	public float mAlpha; // Alpha channel (0 - 1)
	public int mDisplayWidth;
	public int mDisplayLines;
	protected boolean mIgnoreNL;
	protected int mTabLength;

	public AngleString(AngleFont font)
	{
		init(font, 3, false);
	}

	public AngleString(AngleFont font, int tabLength, boolean ignoreNL)
	{
		init(font, tabLength, ignoreNL);
	}

	private void init(AngleFont font, int tabLength, boolean ignoreNL)
	{
		mPosition = new AngleVector();
		mFont = font;
		mLength = 0;
		mAlignment = aLeft;
		mRed = 1;
		mGreen = 1;
		mBlue = 1;
		mAlpha = 1;
		mDisplayWidth = 0;
		mDisplayLines = 0;
		mTabLength = tabLength;
		mIgnoreNL = ignoreNL;
	}

	/**
	 * Changes the string content
	 * 
	 * @param src
	 */
	public void set(String src)
	{
		mLength = 0;
		if (src == null)
			return;

		String mStep1 = "";
		for (int c = 0; c < src.length(); c++)
		{
			if ((src.charAt(c) == '\n') && mIgnoreNL)
				continue;
			if (src.charAt(c) == '\t')
			{
				for (int t = 0; t < mTabLength; t++)
					mStep1.concat(" ");
			}
			else if (src.charAt(c) >= ' ')
				mStep1.concat(src.substring(c, c + 1));
		}
		if (mDisplayWidth > 0)
		{
			int lineWidth = 0;
			int flc = 0; // FirstLineChar
			for (int c = 0; c < mStep1.length(); c++)
			{
				if (mStep1.charAt(c) == '\n')
				{
					lineWidth = 0;
					flc = c + 1;
				}
				else
					lineWidth += mFont.charWidth(mStep1.charAt(c));

				if (lineWidth > mDisplayWidth)
				{
					int llc = c; // Last Line Char
					while ((lineWidth > mDisplayWidth) && (llc > flc))
					{
						while ((mStep1.charAt(llc) == ' ') && (llc > flc)) // Quita los espacios del final
						{
							lineWidth -= mFont.charWidth(mStep1.charAt(llc));
							llc--;
						}
						if (lineWidth <= mDisplayWidth)
							break;
						while ((mStep1.charAt(llc) != ' ') && (llc > flc)) // Quita la última palabra
						{
							lineWidth -= mFont.charWidth(mStep1.charAt(llc));
							llc--;
						}
					}
					if (llc == flc) //Hay una palabra + larga que la linea
					{
						mString = mStep1;
						break;
					}
					else
					{
						mString.concat(mStep1.substring(flc,llc));
						mString.concat("\n");
						c=llc;
					}
				}
			}
		}
		else
			mString = mStep1;
		mLength = mString.length();
	}

	/**
	 * Test if a point is within extent of the string
	 * 
	 * @param x
	 * @param y
	 * @return Returns true if point(x,y) is within string
	 */
	public boolean test(float x, float y)
	{
		float left = getXPosition(0);
		if (x >= left)
			if (y >= mPosition.mY + mFont.mLineat)
				if (x < left + getWidth())
					if (y < mPosition.mY + getHeight() + mFont.mLineat)
						return true;
		return false;
	}

	private void drawLine(GL10 gl, float y, int line)
	{
		if (line>=0)
		{
			int flc=firstCharOfLine(line);
			int llc=lastCharOfLine(line);
			float x=mPosition.mX;
			if (mAlignment == aRight)
				x=mPosition.mX - getWidth(flc,llc);
			else if (mAlignment == aCenter)
				x=mPosition.mX - getWidth(flc,llc) / 2;
			for (int c=flc;c<llc;c++)
			{
				char chr = mFont.getChar(mString.charAt(c));
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

				((GL11Ext) gl).glDrawTexfOES(x + mFont.mCharLeft[chr], AngleSurfaceView.roHeight - (y + mFont.mHeight + mFont.mLineat),
						mZ, chrWidth, mFont.mHeight);
				x += mFont.mCharRight[chr] + mFont.mSpace;
				
			}
		}
	}

	private int getWidth(int flc, int llc)
	{
		int result=0;
		for (int c=flc;c<llc;c++)
			result += mFont.charWidth(mString.charAt(c));
		return result;
	}

	private int lastCharOfLine(int line)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	private int firstCharOfLine(int line)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void draw(GL10 gl)
	{
		if (mFont != null)
		{
			if (mFont.mTexture != null)
			{
				if (mFont.mTexture.mHWTextureID > -1)
				{
					gl.glBindTexture(GL10.GL_TEXTURE_2D, mFont.mTexture.mHWTextureID);
					gl.glColor4f(mRed, mGreen, mBlue, mAlpha);

					int LC=linesCount();
					for (int l = LC-mDisplayLines; l < LC; l++)
						drawLine(l);

					float x = getXPosition(0);
					float y = mPosition.mY;
					for (int c = 0; c < mLength; c++)
					{
						if (mString.charAt(c) == '\n')
						{
							y += mFont.mHeight;
							x = getXPosition(c + 1);
							continue;
						}
						char chr = mFont.getChar(mString.charAt(c));
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

						((GL11Ext) gl).glDrawTexfOES(x + mFont.mCharLeft[chr], AngleSurfaceView.roHeight - (y + mFont.mHeight + mFont.mLineat),
								mZ, chrWidth, mFont.mHeight);
						x += mFont.mCharRight[chr] + mFont.mSpace;
					}
				}
				else
					mFont.mTexture.linkToGL(gl);
			}
		}
		super.draw(gl);
	}

	private int linesCount()
	{
		int result=1;
		for (int c=0;c<mLength;c++)
		{
			if (mString.charAt(c)=='\n')
				result++;
		}
		return result;
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
			if (mString.charAt(c) == '\n')
				break;
			char chr = mFont.getChar(mString.charAt(c));
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

	/**
	 * 
	 * @return String width in pixels
	 */
	public int getWidth()
	{
		int ret = 0;
		int maxRet = 0;
		for (int c = 0; c < mLength; c++)
		{
			if (mString.charAt(c) == '\n')
			{
				if (ret > maxRet)
					maxRet = ret;
				ret = 0;
				continue;
			}
			char chr = mFont.getChar(mString.charAt(c));
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

	/**
	 * 
	 * @return String height in pixels
	 */
	public int getHeight()
	{
		int ret = mFont.mHeight;
		for (int c = 0; c < mLength; c++)
		{
			if (mString.charAt(c) == '\n')
			{
				ret += mFont.mHeight;
				continue;
			}
		}
		return ret;
	}

	public int getLength()
	{
		if (mString != null)
			return mString.length();
		return 0;
	}

}
