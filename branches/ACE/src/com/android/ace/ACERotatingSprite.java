package com.android.ace;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;

public class ACERotatingSprite extends ACEAbstractSprite
{
	protected Bitmap mBitmap;
	private int []mPixels;
	protected Matrix M;
	public float roRotation;

	public ACERotatingSprite(Context context, ACESpriteLayout layout)
	{
		super(context, layout, layout.getWidth(), layout.getHeight());
		mBitmap=Bitmap.createBitmap(roWidth, roHeight, Config.ARGB_8888);
		mPixels=new int[roWidth*roHeight];
		roRotation=0;
		M=new Matrix();
		M.preTranslate(-mPivot.mX, -mPivot.mY);
		setFrame(0);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if ((mBitmap!=null)&&(ACEMainView.mComplexPaint!=null))
		{
			ACEMainView.mComplexPaint.setAlpha(roAlpha);
			canvas.save();
			canvas.drawBitmap(mBitmap, M, ACEMainView.mComplexPaint);
			canvas.restore();
		}
	}

	private void fixMatrix()
	{
 		M.reset();
		M.preTranslate(-mPivot.mX, -mPivot.mY);
		M.postRotate(roRotation);
		M.postTranslate(roPosition.mX, roPosition.mY);
		invalidate();
	}

	public void move(float dX, float dY)
	{
		moveTo(roPosition.mX+dX, roPosition.mY+dY);
	}

	public void moveTo(float fX, float fY)
	{
		super.moveTo(fX, fY);
		fixMatrix();
	}

	public void rotate(float dA)
	{
		rotateTo(roRotation+dA);
	}

	public void rotateTo(float fA)
	{
		roRotation=fA;
		fixMatrix();
	}

	public boolean setFrame(int frame)
	{
		if (super.setFrame(frame))
		{
			mLayout.getFrame(mFrame,mBitmap,mPixels);
			return true;
		}
		return false;
	}

}
