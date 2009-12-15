package com.android.angle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Have the texture and parameters to draw characters
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleFont
{
	private static final short DEFAULT_FONT_CHARS = 93;
//	protected Bitmap mBitmap;
//	protected int mHWTextureID;
	protected AngleTexture mTexture;
	protected float mFontSize;
	protected Typeface mTypeface;
	protected int mBorder;
	protected int mAlpha;
	protected int mRed;
	protected int mGreen;
	protected int mBlue;

	protected int[] mCodePoints; // Unicode 'chars'
	protected short[] mCharX;
	protected short[] mCharLeft;
	protected short[] mCharTop;
	protected short[] mCharRight;
	protected short mCharCount;
	protected short mHeight;
	protected short mSpace;
	protected short mSpaceWidth;

	public AngleFont(AngleTextEngine engine, float fontSize, Typeface typeface, int space, int red, int green, int blue, int alpha)
	{
		doInit(fontSize, typeface, DEFAULT_FONT_CHARS, (short) space, red, green, blue, alpha);
		for (int c = 0; c < mCharCount; c++)
			mCodePoints[c] = 33 + c;
		mBorder=1;
		mTexture=AngleTextureEngine.createTextureFromFont(this);
		//calculeTexture(1);
		engine.addFont(this);
	}

	public AngleFont(AngleTextEngine engine, float fontSize, Typeface typeface, int charCount, int border, int space, int red, int green,
			int blue, int alpha)
	{
		doInit(fontSize, typeface, (short) charCount, (short) space, red, green, blue, alpha);
		for (int c = 0; c < mCharCount; c++)
			mCodePoints[c] = 33 + c;
		mBorder=border;
		mTexture=AngleTextureEngine.createTextureFromFont(this);
		//calculeTexture(border);
		engine.addFont(this);
	}

	public AngleFont(AngleTextEngine engine, float fontSize, Typeface typeface, char[] chars, int border, int space, int red, int green,
			int blue, int alpha)
	{
		doInit(fontSize, typeface, (short) chars.length, (short) space, red, green, blue, alpha);
		for (int c = 0; c < chars.length; c++)
			mCodePoints[c] = (int) chars[c];
		mBorder=border;
		mTexture=AngleTextureEngine.createTextureFromFont(this);
		//calculeTexture(border);
		engine.addFont(this);
	}

	public AngleFont(AngleTextEngine engine, String asset, int resourceId, int space)
	{
		loadFrom(asset);
		mTexture=AngleTextureEngine.createTextureFromResourceId(resourceId);
		mSpace = (short) space;
		engine.addFont(this);
	}

	private void doInit(short charCount)
	{
		mCharCount = charCount;
		//mHWTextureID = -1;
		mTexture=null;
		mCodePoints = new int[mCharCount];
		mCharX = new short[mCharCount];
		mCharLeft = new short[mCharCount];
		mCharTop = new short[mCharCount];
		mCharRight = new short[mCharCount];
		mHeight = 0;
	}

	private void doInit(float fontSize, Typeface typeface, short charCount, short space, int red, int green, int blue, int alpha)
	{
		doInit(charCount);
		mSpace = space;
		mFontSize = fontSize;
		mTypeface = typeface;
		mAlpha = alpha;
		mRed = red;
		mGreen = green;
		mBlue = blue;
	}
/*
	private void calculeTexture(int mBorder)
	{
		Paint paint = new Paint();
		paint.setTypeface(mTypeface);
		paint.setTextSize(mFontSize);
		paint.setARGB(mAlpha, mRed, mGreen, mBlue);
		paint.setAntiAlias(true);

		Rect rect = new Rect();
		int totalWidth = 0;
		mHeight = 0;
		int minTop = 1000;
		int maxBottom = -1000;
		for (int c = 0; c < mCharCount; c++)
		{
			paint.getTextBounds(new String(mCodePoints, c, 1), 0, 1, rect);
			mCharLeft[c] = (short) rect.left;
			mCharRight[c] = (short) (rect.right + mBorder);
			totalWidth += mCharRight[c] - mCharLeft[c];
			if (rect.top < minTop)
				minTop = rect.top;
			if (rect.bottom > maxBottom)
				maxBottom = rect.bottom;
		}
		mHeight = (short) ((maxBottom - minTop) + mBorder);
		int area = mHeight * totalWidth;
		int mTextSizeX = 0;
		while ((area > ((1 << mTextSizeX) * (1 << mTextSizeX))) && (mTextSizeX < 11))
			mTextSizeX++;
		if (mTextSizeX < 11)
		{
			short x = 0;
			short y = 0;
			for (int c = 0; c < mCharCount; c++)
			{
				if (x + (mCharRight[c] - mCharLeft[c]) > (1 << mTextSizeX))
				{
					x = 0;
					y += mHeight;
				}
				if (y + mHeight > (1 << mTextSizeX))
				{
					if (mTextSizeX < 11)
					{
						mTextSizeX++;
						x = 0;
						y = 0;
						c = -1;
						continue;
					}
					else
						break;
				}
				mCharX[c] = x;
				mCharTop[c] = y;
				x += (mCharRight[c] - mCharLeft[c]);
			}
			paint.getTextBounds(" ", 0, 1, rect);
			mSpaceWidth = (short) (rect.right - rect.left + mBorder);
		}
		if (mTextSizeX < 11)
		{
			int mTextSizeY = 0;
			while ((mCharTop[mCharCount - 1] + mHeight) > (1 << mTextSizeY))
				mTextSizeY++;
			mBitmap = Bitmap.createBitmap((1 << mTextSizeX), (1 << mTextSizeY), Config.ARGB_8888);

			Canvas canvas = new Canvas(mBitmap);

			for (int c = 0; c < mCharCount; c++)
			{
				canvas.drawText(new String(mCodePoints, c, 1), 0, 1, mCharX[c] - mCharLeft[c] + (mBorder / 2), mCharTop[c] - minTop
						+ (mBorder / 2), paint);
			}
		}
	}
/*
	public void loadTexture(GL11 gl)
	{
		int[] mTextureIDs = new int[1];

		gl.glGenTextures(1, mTextureIDs, 0);

		mHWTextureID = mTextureIDs[0];
		gl.glBindTexture(GL11.GL_TEXTURE_2D, mHWTextureID);
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE);

		gl.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);

		if (mBitmap != null)
		{
			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, mBitmap, 0);

			int error = gl.glGetError();
			if (error != GL11.GL_NO_ERROR)
				Log.e("AngleFont", "loadTexture GLError: " + error);
		}
		else
		{
			Log.e("AngleFont", "Font too large");
			gl.glDeleteTextures(1, mTextureIDs, 0);
			mHWTextureID = -1;
		}
	}
*/
	public void saveTo(String fileName)
	{
		Bitmap bitmap=mTexture.create();
		if (bitmap != null)
		{
			try
			{
				FileOutputStream stream = new FileOutputStream("/sdcard/" + fileName + ".png");
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				stream.flush();
				stream.close();
				ByteBuffer out = ByteBuffer.allocate(8 + mCharCount * 12);
				out.putShort(mCharCount);
				out.putShort(mHeight);
				out.putShort(mSpace);
				out.putShort(mSpaceWidth);
				for (int c = 0; c < mCharCount; c++)
				{
					out.putInt(mCodePoints[c]);
					out.putShort(mCharX[c]);
					out.putShort(mCharLeft[c]);
					out.putShort(mCharTop[c]);
					out.putShort(mCharRight[c]);
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
/*		
		final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
		sBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		InputStream is = AngleMainEngine.mContext.getResources().openRawResource(resourceId);
		try
		{
			mBitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
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
*/
		InputStream is=null;
		try
		{
			is = AngleMainEngine.mContext.getAssets().open(asset);
			ByteBuffer in = ByteBuffer.allocate(8);
			is.read(in.array());
			doInit(in.getShort(0));
			mHeight = in.getShort(2);
			mSpace = in.getShort(4);
			mSpaceWidth = in.getShort(6);
			in = ByteBuffer.allocate(mCharCount * 12);
			is.read(in.array());
			for (int c = 0; c < mCharCount; c++)
			{
				mCodePoints[c] = in.getInt(c * 12);
				mCharX[c] = in.getShort(c * 12 + 4);
				mCharLeft[c] = in.getShort(c * 12 + 6);
				mCharTop[c] = in.getShort(c * 12 + 8);
				mCharRight[c] = in.getShort(c * 12 + 10);
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
		for (int c = 0; c < mCharCount; c++)
		{
			if (mCodePoints[c] == chr)
				return (char) c;
		}
		return (char) -1;
	}
}
