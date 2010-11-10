package com.alt90.angle2;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class AngleFontTexture extends AngleTexture
{
	protected AngleFont lFont;
	
	AngleFontTexture (AngleFont font)
	{
		super();
		lFont=font;
	}

	@Override
	public Bitmap create()
	{
		Bitmap mBitmap = null;
		Paint paint = new Paint();
		paint.setTypeface(lFont.lTypeface);
		paint.setTextSize(lFont.lFontSize);
		paint.setARGB(lFont.lAlpha, lFont.lRed, lFont.lGreen, lFont.lBlue);
		paint.setAntiAlias(true);

		Rect rect = new Rect();
		int totalWidth = 0;
		lHeight = 0;
		int minTop = 1000;
		int maxBottom = -1000;
		for (int c = 0; c < lFont.lCharCount; c++)
		{
			paint.getTextBounds(new String(lFont.lCodePoints, c, 1), 0, 1, rect);
			lFont.lCharLeft[c] = (short) rect.left;
			lFont.lCharRight[c] = (short) (rect.right + lFont.lBorder);
			totalWidth += lFont.lCharRight[c] - lFont.lCharLeft[c];
			if (rect.top < minTop)
				minTop = rect.top;
			if (rect.bottom > maxBottom)
				maxBottom = rect.bottom;
		}
		lHeight = (short) ((maxBottom - minTop) + lFont.lBorder);
		int area = lHeight * totalWidth;
		int mTextSizeX = 0;
		while ((area > ((1 << mTextSizeX) * (1 << mTextSizeX))) && (mTextSizeX < 11))
			mTextSizeX++;
		if (mTextSizeX < 11)
		{
			short x = 0;
			short y = 0;
			for (int c = 0; c < lFont.lCharCount; c++)
			{
				if (x + (lFont.lCharRight[c] - lFont.lCharLeft[c]) > (1 << mTextSizeX))
				{
					x = 0;
					y += lHeight;
				}
				if (y + lHeight > (1 << mTextSizeX))
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
				lFont.lCharX[c] = x;
				lFont.lCharTop[c] = y;
				x += (lFont.lCharRight[c] - lFont.lCharLeft[c]);
			}
			paint.getTextBounds(" ", 0, 1, rect);
			lFont.lSpaceWidth = (short) (rect.right - rect.left + lFont.lBorder);
		}
		if (mTextSizeX < 11)
		{
			int mTextSizeY = 0;
			while ((lFont.lCharTop[lFont.lCharCount - 1] + lHeight) > (1 << mTextSizeY))
				mTextSizeY++;
			Bitmap paintBitmap = Bitmap.createBitmap((1 << mTextSizeX), (1 << mTextSizeY), Config.ARGB_8888);

			Canvas canvas = new Canvas(paintBitmap);

			for (int c = 0; c < lFont.lCharCount; c++)
			{
				canvas.drawText(new String(lFont.lCodePoints, c, 1), 0, 1, lFont.lCharX[c] - lFont.lCharLeft[c] + (lFont.lBorder / 2), lFont.lCharTop[c] - minTop
						+ (lFont.lBorder / 2), paint);
			}
			mBitmap=Bitmap.createBitmap(paintBitmap, 0, 0, paintBitmap.getWidth(), paintBitmap.getHeight());
			paintBitmap.recycle();
		}
		return mBitmap;
	}

}
