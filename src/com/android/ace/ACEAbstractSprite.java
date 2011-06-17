package com.android.ace;

import android.content.Context;
import android.view.View;

public class ACEAbstractSprite extends View implements ACEObject
{
	protected ACESpriteLayout mLayout;
	protected int mFrame;
	protected ACEVector mPivot;
	public ACEVector roPosition;
	public int roWidth;
	public int roHeight;
	protected int roAlpha;
	
	public ACEAbstractSprite(Context context, ACESpriteLayout layout, int width, int height)
	{
		super(context);
		roWidth=width;
		roHeight=height;
		mLayout=layout;
		mFrame=0;
		roPosition=new ACEVector();
		mPivot=new ACEVector(roWidth/2,roHeight/2);
		roAlpha=255;
	}
	
	public void centerPivot()
	{
		mPivot.set(roWidth/2,roHeight/2);
		moveTo(roPosition.mX,roPosition.mY);
	}

	@Override
	public void step(float secondsElapsed)
	{
	}

	public void move(float dX, float dY)
	{
		roPosition.mX+=dX;
		roPosition.mY+=dY;
	}

	public void moveTo(float fX, float fY)
	{
		roPosition.set(fX,fY);
	}

	public boolean setFrame(int frame)
	{
		if ((frame>=0)&&(frame<=mLayout.mFrameCount))
		{
			mFrame=frame;
			invalidate();
			return true;
		}
		return false;
	}

	@Override
	public void addToView(ACEViewGroup view)
	{
		if (view.findViewById(getId())==null)
			view.addView(this);
	}

	@Override
	public void removeFromView()
	{
		ACEViewGroup view=(ACEViewGroup)getParent();
		if (view!=null)
			view.removeView(this);
	}
	
	public int getFrame()
	{
		return mFrame;
	}

	public void setAlpha(int Value)
	{
		roAlpha=Value;
		invalidate();
	}
}
