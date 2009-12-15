package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.angle.AngleAbstractGameEngine;
import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSpriteX;
import com.android.angle.AngleSpriteXLayout;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

/**
 * In this tutorial we will implement a little game engine with fps count
 * 
 * We learn to: 
 * -Create overloaded game engine. 
 * -Use AgleSpriteX and how to rotate it
 * -Implements a FPS counter 
 * -Make the changes consistent with the time elapsed
 *  since last frame
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial03 extends Activity
{
	private AngleSurfaceView mView;
	private MyGameEngine mGame;

	class MyGameEngine extends AngleAbstractGameEngine // Game engine class
	{
		private AngleSpritesEngine mSprites;
		private AngleSpriteXLayout mLogoLayout; 
		private AngleSpriteX mLogo; // In this sample use AmgleSpriteX (see below)

		// FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		// -----------

		MyGameEngine(AngleSurfaceView view)
		{
			super(view);
			mSprites = new AngleSpritesEngine(10, 10);
			addEngine(mSprites);

			mLogoLayout = new AngleSpriteXLayout(mSprites, 128, 128, R.drawable.anglelogo, 0, 0, 128, 128);
			// Use AngleSprite instead of AngleSimpleSprite to rotate it
			mLogo=new AngleSpriteX(mSprites, mLogoLayout);
			mLogo.mCenter.set(100, 100);
		}

		// This method will be called before draw every frame
		@Override
		public void run()
		{
			// Add FPS record to log every 100 frames
			frameCount++;
			if (frameCount >= 100)
			{
				long CTM = System.currentTimeMillis();
				frameCount = 0;
				if (lCTM > 0)
					Log.v("FPS", "" + (100.f / ((CTM - lCTM) / 1000.f)));
				lCTM = CTM;
			}
			// --------------------------------------

			// Rotate the logo at 45º per second
			mLogo.mRotation += 45 * AngleMainEngine.secondsElapsed;
			mLogo.mRotation %= 360;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mView = new AngleSurfaceView(this);
		setContentView(mView);

		mGame = new MyGameEngine(mView); 
		
		mView.setGameEngine(mGame);
	}

	@Override
	protected void onPause()
	{
		mView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		mView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		mView.onDestroy();
		super.onDestroy();
	}
}
