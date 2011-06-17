package com.android.ace;

import android.content.Context;
import android.widget.FrameLayout;

public class ACEViewGroup extends FrameLayout 
{
	public ACEViewGroup(Context context)
	{
		super(context);
	}

	public void step (float secondsElapsed)
	{
		for (int c=0;c<getChildCount();c++)
		{
			if (getChildAt(c) instanceof ACEObject)
				((ACEObject)getChildAt(c)).step(secondsElapsed);
		}
	}
}
