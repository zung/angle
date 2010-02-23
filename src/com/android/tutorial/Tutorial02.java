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
		public MyAnimatedSprite(AngleSpriteLayout layout)
		{
			super(layout);
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

		AngleSpriteLayout mLogoLayout = new AngleSpriteLayout(mGLSurfaceView, 128, 128, R.drawable.anglelogo);
		//Use MyAnimatedSprite so we can make it roll
		MyAnimatedSprite mLogo = new MyAnimatedSprite (mLogoLayout);
		mLogo.mPosition.set(160, 200); 
		mGLSurfaceView.addObject(mLogo);

		//Use a framelayout as main view instead of using mGLSurfaceView directly 
		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
	}
}
