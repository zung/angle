package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleAbstractGameEngine;
import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteReference;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

/**
 * 
 * Create game object called MyLogo which updates its status every frame. And
 * use references to dynamically add and remove 'sprites' from the engine
 * without need to load them in runtime.
 * 
 * We learn to: -Use references instead of sprites -Catch touch events
 * -Implement a game object
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial06 extends Activity
{
	private MyGameEngine mGame;
	private AngleSurfaceView mView;

	class MyGameEngine extends AngleAbstractGameEngine
	{
		// FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		// -----------
		private AngleSpritesEngine mSprites;
		private static final int MAX_LOGOS = 50;
		private AngleSprite mLogoSprite;
		private MyLogo[] mLogos;
		private int mLogosCount;

		// Create new game object class overloading the AngleSpriteReference
		class MyLogo extends AngleSpriteReference
		{
			MyLogo(AngleSprite sprite)
			{
				super(sprite);
			}

			public void run()
			{
				// Move up 80 pixels per second
				mCenter.mY -= 80 * AngleMainEngine.secondsElapsed;
				mRotation += 45 * AngleMainEngine.secondsElapsed;
				mRotation %= 360;
			}
		}

		MyGameEngine()
		{
			// Put the sprites engine into game engine
			mSprites = new AngleSpritesEngine(10, 100); // Tell sprites engine use
			// references (100 max)
			// instead of sprites
			mView.addEngine(mSprites);
			// Array of logos displayed
			mLogos = new MyLogo[MAX_LOGOS];
			mLogosCount = 0;
			// Create sprite in the constructor. We add references to engine later
			mLogoSprite = new AngleSprite(128, 128, R.drawable.anglelogo, 0, 0,
					128, 128);
			mSprites.addSprite(mLogoSprite);
		}

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

			// Move all logos
			for (int l = 0; l < mLogosCount; l++)
			{
				mLogos[l].run();
				// remove logo if is out of screen
				if (mLogos[l].mCenter.mY < -mLogoSprite.mWidth / 2)
				{
					mSprites.removeRefernece(mLogos[l]);
					mLogosCount--;
					for (int d = l; d < mLogosCount; d++)
						mLogos[d] = mLogos[d + 1];
					mLogos[mLogosCount] = null;
				}
			}
		}

		// Place the input processing in to game engine
		public void onTouchEvent(MotionEvent event)
		{
			// Prevent event flooding
			// Max 20 events per second
			try
			{
				Thread.sleep(50);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			// -------------------------
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				// Add new logo at the touch position
				if (mLogosCount < MAX_LOGOS)
				{
					mLogos[mLogosCount] = new MyLogo(mLogoSprite);
					mLogos[mLogosCount].mCenter.set(event.getX(), event.getY());
					mSprites.addReference(mLogos[mLogosCount++]);
				}
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mView = new AngleSurfaceView(this);
		setContentView(mView);

		mGame = new MyGameEngine();
		mView.setBeforeDraw(mGame);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) // Get touch input events
	{
		mGame.onTouchEvent(event);
		return super.onTouchEvent(event);
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
