package com.alt90.angle2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Have the texture and parameters to draw characters
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleFont
{
	protected static final short DEFAULT_FONT_CHARS = 93;
	protected AngleTexture lTexture;
	protected float lFontSize;
	protected Typeface lTypeface;
	protected int lBorder;
	protected int lAlpha;
	protected int lRed;
	protected int lGreen;
	protected int lBlue;

	protected int[] lCodePoints; // Unicode 'chars'
	protected short[] lCharX;
	protected short[] lCharLeft;
	protected short[] lCharTop;
	protected short[] lCharRight;
	protected short lCharCount;
	protected short lHeight;
	protected short lSpace;
	protected short lSpaceWidth;

	public AngleFont(float fontSize, Typeface typeface, int space, int red, int green, int blue, int alpha)
	{
		doInit(fontSize, typeface, DEFAULT_FONT_CHARS, (short) space, red, green, blue, alpha);
		for (int c = 0; c < lCharCount; c++)
			lCodePoints[c] = 33 + c;
		lBorder=1;
		lTexture=AngleTextureEngine.createTextureFromFont(this);
	}

	public AngleFont(float fontSize, Typeface typeface, int charCount, int border, int space, int red, int green,
			int blue, int alpha)
	{
		doInit(fontSize, typeface, (short) charCount, (short) space, red, green, blue, alpha);
		for (int c = 0; c < lCharCount; c++)
			lCodePoints[c] = 33 + c;
		lBorder=border;
		lTexture=AngleTextureEngine.createTextureFromFont(this);
	}

	public AngleFont(float fontSize, Typeface typeface, char[] chars, int border, int space, int red, int green,
			int blue, int alpha)
	{
		doInit(fontSize, typeface, (short) chars.length, (short) space, red, green, blue, alpha);
		for (int c = 0; c < chars.length; c++)
			lCodePoints[c] = (int) chars[c];
		lBorder=border;
		lTexture=AngleTextureEngine.createTextureFromFont(this);
	}

	public AngleFont(String asset, int resourceId, int space)
	{
		loadFrom(asset);
		lTexture=AngleTextureEngine.createTextureFromResourceId(resourceId);
		lSpace = (short) space;
	}

	private void doInit(short charCount)
	{
		lCharCount = charCount;
		lTexture=null;
		lCodePoints = new int[lCharCount];
		lCharX = new short[lCharCount];
		lCharLeft = new short[lCharCount];
		lCharTop = new short[lCharCount];
		lCharRight = new short[lCharCount];
		lHeight = 0;
	}

	private void doInit(float fontSize, Typeface typeface, short charCount, short space, int red, int green, int blue, int alpha)
	{
		doInit(charCount);
		lSpace = space;
		lFontSize = fontSize;
		lTypeface = typeface;
		lAlpha = alpha;
		lRed = red;
		lGreen = green;
		lBlue = blue;
	}

	public void saveTo(String fileName)
	{
		Bitmap bitmap=lTexture.create();
		if (bitmap != null)
		{
			try
			{
				FileOutputStream stream = new FileOutputStream("/sdcard/" + fileName + ".png");
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				stream.flush();
				stream.close();
				ByteBuffer out = ByteBuffer.allocate(8 + lCharCount * 12);
				out.putShort(lCharCount);
				out.putShort(lHeight);
				out.putShort(lSpace);
				out.putShort(lSpaceWidth);
				for (int c = 0; c < lCharCount; c++)
				{
					out.putInt(lCodePoints[c]);
					out.putShort(lCharX[c]);
					out.putShort(lCharLeft[c]);
					out.putShort(lCharTop[c]);
					out.putShort(lCharRight[c]);
				}
				stream = new FileOutputStream("/sdcard/" + fileName + ".fnt");
				stream.write(out.array());
				stream.flush();
				stream.close();
				out = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			bitmap.recycle();
		}
	}

	public void loadFrom(String asset)
	{
		InputStream is=null;
		try
		{
			is = AngleActivity.uInstance.getAssets().open(asset);
			ByteBuffer in = ByteBuffer.allocate(8);
			is.read(in.array());
			doInit(in.getShort(0));
			lHeight = in.getShort(2);
			lSpace = in.getShort(4);
			lSpaceWidth = in.getShort(6);
			in = ByteBuffer.allocate(lCharCount * 12);
			is.read(in.array());
			for (int c = 0; c < lCharCount; c++)
			{
				lCodePoints[c] = in.getInt(c * 12);
				lCharX[c] = in.getShort(c * 12 + 4);
				lCharLeft[c] = in.getShort(c * 12 + 6);
				lCharTop[c] = in.getShort(c * 12 + 8);
				lCharRight[c] = in.getShort(c * 12 + 10);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				Log.e("AngleFont", "loadFrom::InputStream.close error: " + e.getMessage());
			}
		}
	}

	public char getChar(char chr)
	{
		for (int c = 0; c < lCharCount; c++)
		{
			if (lCodePoints[c] == chr)
				return (char) c;
		}
		return (char) -1;
	}
}
