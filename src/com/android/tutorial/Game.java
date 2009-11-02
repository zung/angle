package com.android.tutorial;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleFont;
import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSimpleSprite;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteReference;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleString;
import com.android.angle.AngleSurfaceView;
import com.android.angle.AngleTextEngine;
import com.android.angle.AngleTileEngine;

/**
 * 
 * In this sample we create a simple vertical shoot'em all
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Game extends Activity
{
	private MyGameEngine mGame;
	private AngleSurfaceView mView;

	class MyGameEngine implements Runnable
	{
		class DashboardEngine extends AngleSpritesEngine // An engine to draw the
		// dashboard
		{
			private AngleTextEngine mTexts;
			private AngleFont mArialWhite10 = null; // Our font
			private AngleString mString = null; // Our string

			private AngleSimpleSprite mBG; // Dashboard background

			public DashboardEngine()
			{
				super(10, 0);
				mBG = new AngleSimpleSprite(320, 64, R.drawable.tilemap, 0, 32,
						320, 64);
				mBG.mCenter.set(mBG.mWidth / 2, mLevel.mTileHeight * 13
						+ mBG.mHeight / 2);
				addSprite(mBG);

				// Engine for texts
				mTexts = new AngleTextEngine(2, 10);
				addEngine(mTexts);

				mArialWhite10 = new AngleFont(30, Typeface.create("Arial",
						Typeface.NORMAL), 1, 255, 255, 255, 255);
				mTexts.addFont(mArialWhite10); // Add font to text engine

				mString = new AngleString(mArialWhite10, 100);
				mString.mPosition
						.set(50, mLevel.mTileHeight * 13 + mBG.mHeight / 2);
				mTexts.addString(mString); // Add string to text engine
			}

		};

		// FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		// -----------
		private AngleSpritesEngine mSprites;
		private AngleTileEngine mLevel;
		private DashboardEngine mDash;

		private static final int smLoad = 0;
		private static final int smPlay = 1;
		private static final int MAX_SHOTS = 50;
		private static final long mShotColdDownTime = 70;
		private int stateMachine = smLoad;
		private AngleSprite sprShip;
		private AngleSprite sprShot;
		private AngleSpriteReference mShip;
		private AngleSpriteReference[] mShot;
		private int mShotsCount;
		private long mShotColdDown = 0;

		MyGameEngine()
		{
			// Create a tile engine with 8 tiles and 8 columns of tiles in the
			// texture
			// Tile size 32x32. Map size 10*1000
			mLevel = new AngleTileEngine(R.drawable.tilemap, 8, 8, 32, 32, 10,
					1000);
			mView.addEngine(mLevel);

			// Engine for game sprites
			mSprites = new AngleSpritesEngine(10, 100);
			mView.addEngine(mSprites);

			// Engine for dashboard
			mDash = new DashboardEngine();
			mView.addEngine(mDash);

			// Set the view extent in tiles
			mLevel.mViewWidth = 10;
			mLevel.mViewHeight = 14;

			sprShip = new AngleSprite(64, 64, R.drawable.anglelogo, 0, 0,
					128, 128);
			mSprites.addSprite(sprShip);
			sprShot = new AngleSprite(16, 16, R.drawable.anglelogo, 0, 0,
					128, 128);
			mSprites.addSprite(sprShot);

			mShip = new AngleSpriteReference(sprShip);
			mShot = new AngleSpriteReference[MAX_SHOTS];
			mShotsCount = 0;
		}

		public void onTouchEvent(MotionEvent event)
		{
			// Prevent event flooding
			// Max 33 events per second
			try
			{
				Thread.sleep(30);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			// -------------------------
			mShip.mCenter.set(event.getX(), event.getY() - 64 - sprShip.mHeight
					/ 2); // Position the ship
			if (mShotsCount < MAX_SHOTS)
			{
				long CTM = System.currentTimeMillis();
				if (CTM > mShotColdDown) // Prevent shoot in less than
				// mShotColdDownTime milliseconds
				{
					mShotColdDown = CTM + mShotColdDownTime;
					mShot[mShotsCount] = new AngleSpriteReference(sprShot);
					mShot[mShotsCount].mCenter.set(mShip.mCenter.mX,
							mShip.mCenter.mY - 20);
					mSprites.addReference(mShot[mShotsCount++]);
				}
			}
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
				{
					mDash.mString.set("FPS=" + (100.f / ((CTM - lCTM) / 1000.f)));
					Log.v("FPS", "" + (100.f / ((CTM - lCTM) / 1000.f)));
				}
				lCTM = CTM;
			}
			// --------------------------------------

			switch (stateMachine)
			// Very simple state machine
			{
				case smLoad: // Load sprite in runtime
					mSprites.addReference(mShip);
					mShip.mCenter.set(AngleMainEngine.mWidth / 2,
							AngleMainEngine.mHeight / 2);
					// Load the level map
					mLevel.loadMap(getResources().openRawResource(R.raw.map));
					// Put the top of the camera at the lowest part of the map
					mLevel.mTop = (mLevel.mMapHeight - mLevel.mViewHeight)
							* mLevel.mTileHeight;
					stateMachine = smPlay;
					break;
				case smPlay:
					mLevel.mTop -= 200 * AngleMainEngine.secondsElapsed; // Move the
					// camera
					for (int s = 0; s < mShotsCount; s++) // Move the shots
					{
						if (mShot[s] != null)
						{
							mShot[s].mCenter.mY -= 200 * AngleMainEngine.secondsElapsed;
							if (mShot[s].mCenter.mY < -10)
							{
								mSprites.removeRefernece(mShot[s]);
								mShot[s] = null;
							}
						}
					}
					for (int s = 0; s < mShotsCount; s++) // destroy null shots
					{
						if (mShot[s] == null)
						{
							mShotsCount--;
							for (int d = s; d < mShotsCount; d++)
								mShot[d] = mShot[d + 1];
							mShot[mShotsCount] = null;
							s--;
						}
					}
					break;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mGame.onTouchEvent(event);
		return super.onTouchEvent(event);
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
