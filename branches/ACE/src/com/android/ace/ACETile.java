package com.android.ace;

import android.content.Context;
import android.graphics.Canvas;

public class ACETile extends ACEAbstractSprite
{
	public ACETile(Context context, ACESpriteLayout layout)
	{
		super(context, layout, layout.getWidth(), layout.getHeight());
		mPivot.set(0,0);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if ((mLayout.mBitmap!=null)&&(ACEMainView.mFastPaint!=null))
		{
			canvas.save();
			canvas.drawBitmap(mLayout.mBitmap, roPosition.mX, roPosition.mY, ACEMainView.mFastPaint);
			canvas.restore();
		}
	}

}
