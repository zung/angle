package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
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
	private static final int FONT_CHARS=93;
	private static final String sAvailableChars="!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
	protected int mHWTextureID;
	private float mFontSize;
	private Typeface mTypeface;
	private int mAlpha;
	private int mRed;
	private int mGreen;
	private int mBlue;
	protected int[] mCharLeft;
	protected int[] mCharTop;
	protected int[] mCharWidth;
	protected int mHeight;
	protected int mSpace;
	
	public AngleFont (float fontSize, Typeface typeface, int space, int red, int green, int blue, int alpha)
	{
		mHWTextureID=-1;
		mFontSize=fontSize;
		mSpace=space;
		mTypeface=typeface;
		mAlpha=alpha;
		mRed=red;
		mGreen=green;
		mBlue=blue;
		mCharLeft=new int[FONT_CHARS];
		mCharTop=new int[FONT_CHARS];
		mCharWidth=new int[FONT_CHARS];
		mHeight=0;
	}

	public void loadTexture(GL10 gl)
	{
		int[] mTextureIDs = new int[1];

		gl.glGenTextures(1, mTextureIDs, 0);

		mHWTextureID = mTextureIDs[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mHWTextureID);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);

		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
				GL10.GL_REPLACE);

		Paint paint = new Paint();
		paint.setTypeface(mTypeface);
		paint.setTextSize(mFontSize);
		paint.setARGB(mAlpha, mRed, mGreen, mBlue);
		paint.setAntiAlias(true);
		Rect rect = new Rect();
		int totalWidth=0;
		mHeight=0;
		int minTop=1000;
		int maxBottom=-1000;
		for (int c=0;c<FONT_CHARS;c++)
		{
			paint.getTextBounds(sAvailableChars, c, c+1, rect);
			mCharWidth[c]=rect.right;
			totalWidth += mCharWidth[c];
			if (rect.top<minTop)
				minTop=rect.top;
			if (rect.bottom>maxBottom)
				maxBottom=rect.bottom;
		}
		mHeight=(maxBottom-minTop)+1;
		int area=mHeight*totalWidth;
		int p=0;
		while ((area>((1<<p)*(1<<p)))&&(p<11))
			p++;
		if (p<11)
		{
			int x=0;
			int y=0;
			for (int c=0;c<FONT_CHARS;c++)
			{
				if (x+mCharWidth[c]>(1<<p))
				{
					x=0;
					y+=mHeight;
				}
				if (y+mHeight>(1<<p))
				{
					if (p<11)
					{
						p++;
						x=0;
						y=0;
						c=-1;
						continue;
					}
					else
						break;
				}
				mCharLeft[c]=x;
				mCharTop[c]=y;
				x+=mCharWidth[c];
			}
		}
		if (p<11)
		{
			Bitmap bitmap;
			bitmap = Bitmap.createBitmap((1<<p), (1<<p), Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmap);
/*			
			for (int y=0;y<256;y++)
			{
				paint.setARGB(255, 255, y, 30);
				canvas.drawLine(0, y, 256, y, paint);
			}
*/			
			for (int c=0;c<FONT_CHARS;c++)
				canvas.drawText(sAvailableChars.substring(c, c+1), mCharLeft[c], mCharTop[c]-minTop, paint);

			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

			int error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR)
			{
				Log.e("AngleFont", "loadTexture GLError: " + error);
			}
		}
		else
		{
			Log.e("AngleFont", "Font too large");
			gl.glDeleteTextures(1, mTextureIDs, 0);
			mHWTextureID=-1;
		}
	}
}
