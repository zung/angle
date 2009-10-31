package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

/**
 * Add a state machine to our new game engine so we can load the sprites after
 * the "game" is started.
 * 
 * We learn to: -Load sprites 'in runtime' -Get view extents
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial04 extends Activity
{
	private MyGameEngine mGame;
	private AngleSurfaceView mView;
	private AngleSpritesEngine mSprites;

	class MyGameEngine implements Runnable
	{
		// FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		// -----------
		private static final int smLoad = 0;
		private static final int smRotate = 1;
		private int stateMachine = smLoad;
		private AngleSprite mLogo; // Declare mLogo in MyGameEngine (better place)

		MyGameEngine()
		{
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

			switch (stateMachine)
			// Very simple state machine
			{
				case smLoad: // Load sprite in runtime
					mLogo = new AngleSprite(128, 128, R.drawable.anglelogo, 0, 0,
							128, 128);
					// Cause the engine is already initialized, we can consult its
					// extents
					mLogo.mCenter.set(AngleMainEngine.mWidth / 2,
							AngleMainEngine.mHeight / 2);
					mSprites.addSprite(mLogo);

					stateMachine = smRotate;
					break;
				case smRotate:
					mLogo.mRotation += 45 * AngleMainEngine.secondsElapsed;
					mLogo.mRotation %= 360;
					break;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mGame = new MyGameEngine();

		mSprites = new AngleSpritesEngine(10, 0);
		AngleMainEngine.addEngine(mSprites);

		mView = new AngleSurfaceView(this);
		setContentView(mView);
		mView.setBeforeDraw(mGame);
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
