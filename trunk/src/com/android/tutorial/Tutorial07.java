package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleAbstractReference;
import com.android.angle.AngleCircleCollider;
import com.android.angle.AngleMainEngine;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsGameEngine;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteReference;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;
import com.android.angle.AngleViewCollisionsEngine;

/**
 * 
 * Overload the AnglePhysicsGameEngine to create our physics game engine.
 * 
 * We learn to: -Use AnglePhysicsGameEngine and AnglePhysicsObject -Draw
 * colliders -Use AnglePhysicsObject.test to test if there will be a collision.
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial07 extends Activity
{
	private MyGameEngine mGame;
	private AngleSurfaceView mView;

	class MyGameEngine extends AnglePhysicsGameEngine // Our physics game engine
	{
		// FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		// -----------
		private AngleSpritesEngine mSprites; // Sprites engine
		private AngleViewCollisionsEngine mCollisions; // Engine to draw colliders
		private static final int MAX_BallS = 50;
		private static final int MAX_OBJECTS = 100;
		private static final int MAX_TYPES = 10;
		private AngleSprite mBallSprite;
		private MyBall[] mBalls;
		private int mBallsCount;

		// Create new game object class overloading the AnglePhysicObject
		class MyBall extends AnglePhysicObject
		{
			MyBall(AngleSpriteReference spriteReference)
			{
				super(spriteReference, 0, 1); // Init AnglePhysicObject with an
														// AngleSpriteReference, 0 segment
														// colliders and 1 circle collider
				addCircleCollider(new AngleCircleCollider(0, 0, 29)); // Add a
																						// circle
																						// collider to
																						// our object
				mMass = 10; // Set the mass
			}

			public void run()
			{
				// AnglePhysicObject.mVisual points to his visual object. In this
				// case an AngleSpriteReference
				AngleSpriteReference SR = (AngleSpriteReference) mVisual;
				SR.mRotation += 45 * AngleMainEngine.secondsElapsed;
				SR.mRotation %= 360;
			}

		}

		MyGameEngine()
		{
			super(MAX_TYPES, MAX_OBJECTS);
			mSprites = new AngleSpritesEngine(MAX_TYPES, MAX_OBJECTS);
			AngleMainEngine.addEngine(mSprites);
			// ------------------------------------------------
			mCollisions = new AngleViewCollisionsEngine(this);
			AngleMainEngine.addEngine(mCollisions);
			// Adding this engine, the collisions will be drawn
			// Use only for debug purposes. It's very slow

			mBalls = new MyBall[MAX_BallS];
			mBallsCount = 0;

			mBallSprite = new AngleSprite(128 / 2, 128 / 2, R.drawable.ball, 0, 0,
					128, 128);
			mSprites.addSprite(mBallSprite);
		}

		@Override
		public void addObject(AnglePhysicObject object)
		{
			super.addObject(object);
			mSprites.addReference((AngleAbstractReference) object.mVisual); // Add
																											// sprite
																											// to
																											// mSprites
																											// when
																											// object
																											// is
																											// added
		}

		@Override
		public void removeObject(AnglePhysicObject object)
		{
			super.removeObject(object);
			mSprites
					.removeRefernece((AngleAbstractReference) object.mVisual); // Remove
																											// sprite
																											// when
																											// object
																											// is
																											// removed
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

			// Move all Balls
			for (int l = 0; l < mBallsCount; l++)
			{
				mBalls[l].run();
				// remove Ball if is out of screen
				if ((mBalls[l].mVisual.mCenter.mX < -mBallSprite.mWidth / 2)
						|| (mBalls[l].mVisual.mCenter.mY < -mBallSprite.mWidth / 2)
						|| (mBalls[l].mVisual.mCenter.mX > AngleMainEngine.mWidth
								+ mBallSprite.mWidth / 2)
						|| (mBalls[l].mVisual.mCenter.mY > AngleMainEngine.mHeight
								+ mBallSprite.mWidth / 2))
				{
					removeObject(mBalls[l]);
					mBallsCount--;
					for (int d = l; d < mBallsCount; d++)
						mBalls[d] = mBalls[d + 1];
					mBalls[mBallsCount] = null;
				}
			}
			super.run();
		}

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
				// Add new Ball at the touch position
				if (mBallsCount < MAX_BallS)
				{
					mBalls[mBallsCount] = new MyBall(new AngleSpriteReference(
							mBallSprite));
					mBalls[mBallsCount].mVisual.mCenter.set(event.getX(), event
							.getY());
					// Point direction to screen center
					float x = AngleMainEngine.mWidth / 2
							- mBalls[mBallsCount].mVisual.mCenter.mX;
					float y = AngleMainEngine.mHeight / 2
							- mBalls[mBallsCount].mVisual.mCenter.mY;
					float a = (float) Math.acos(y / Math.sqrt(x * x + y * y));
					if (x < 0)
						a = (float) (Math.PI * 2 - a);
					// --------------------------------
					float force = 50 + event.getSize() * 400; // Use the touch area
																			// to set the start
																			// speed
					mBalls[mBallsCount].mVelocity.mX = (float) (force * Math.sin(a));
					mBalls[mBallsCount].mVelocity.mY = (float) (force * Math.cos(a));

					// Ensure that there isn't any ball in this place
					for (int b = 0; b < mBallsCount; b++)
					{
						if (mBalls[b].test(mBalls[mBallsCount]))
						{
							mBalls[mBallsCount] = null;
							return;
						}
					}
					addObject(mBalls[mBallsCount++]);
				}
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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
