package com.android.tutorial;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleRotatingSprite;
import com.android.angle.AngleSpriteLayout;

/**
 * Override Angle class and create our first animated object
 * 
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial02 extends AngleActivity
{
	private class MyAnimatedSprite extends AngleRotatingSprite
	{
		public MyAnimatedSprite(int x, int y, AngleSpriteLayout layout)
		{
			super(x, y, layout);
		}

		//Override step function to implement animations and user logic
		@Override
		public void step(float secondsElapsed)
		{
			mRotation+=secondsElapsed*10;//10º per second
			super.step(secondsElapsed);
		}
		
	};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//Now we will insert our sprite with only one line of code
		//Use MyAnimatedSprite so we can make it roll
		mGLSurfaceView.addObject(new MyAnimatedSprite (160, 200, new AngleSpriteLayout(mGLSurfaceView, R.drawable.anglelogo)));

		//Use a framelayout as main view instead of using mGLSurfaceView directly 
		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
	}
}
