package com.android.ace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

public class ACESprite extends ACEAbstractSprite
{
	protected RectF mDst;

	public ACESprite(Context context, ACESpriteLayout layout, int width, int height)
	{
		super(context, layout, width, height);
		mDst=new RectF(-mPivot.mX,-mPivot.mY,roWidth-mPivot.mX,roHeight-mPivot.mY);
	}

	public ACESprite(Context context, ACESpriteLayout layout)
	{
		super(context, layout, layout.getWidth(), layout.getHeight());
		mDst=new RectF(-mPivot.mX,-mPivot.mY,roWidth-mPivot.mX,roHeight-mPivot.mY);
	}
	
	public void setExtent (int width, int height)
	{
		roWidth=width;
		roHeight=height;
		mDst=new RectF(0,0,roWidth,roHeight);
		moveTo(roPosition.mX,roPosition.mY);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if ((mLayout.mBitmap!=null)&&(ACEMainView.mFastPaint!=null))
		{
			ACEMainView.mFastPaint.setAlpha(roAlpha);
			canvas.save();
			canvas.drawBitmap(mLayout.mBitmap, mLayout.getSrcRect(mFrame), mDst, ACEMainView.mFastPaint);
			canvas.restore();
		}
	}

	public void move(float dX, float dY)
	{
		mDst.offset(dX, dY);
		super.move(dX, dY);
		invalidate();
	}

	public void moveTo(float fX, float fY)
	{
		mDst.offsetTo(fX-mPivot.mX, fY-mPivot.mY);
		super.moveTo(fX, fY);
		invalidate();
	}
}
