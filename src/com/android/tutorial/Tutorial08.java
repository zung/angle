package com.android.tutorial;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleCircleCollider;
import com.android.angle.AngleMainEngine;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsGameEngine;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;
import com.android.angle.AngleVisualObject;

/**
 * 
 * Add an environment to physics engine (gravity, air)
 * 
 * We learn to: -Use Gravity, Air viscosity and Coefficient of restitution -Use
 * accelerometer
 * 
 * @author Ivan Pajuelo
 * 
 */
@SuppressWarnings("deprecation")
public class Tutorial08 extends Activity
{
	/*
	private MyGameEngine mGame;
	private MyView mView;
	private SensorManager mSensorManager;

	class MyGameEngine extends AnglePhysicsGameEngine
	{
		// FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		// -----------
		private AngleSpritesEngine mSprites;
		private static final int MAX_BallS = 50;
		private static final int MAX_OBJECTS = 100;
		private static final int MAX_TYPES = 10;
		private static final int smLoad = 1;
		private static final int smRun = 2;
		private AngleSprite mBallSprite;
		private MyBall[] mBalls;
		private int mBallsCount;
		private int mStateMachine;

		// Create new game object class overloading the AngleSpriteReference
		class MyBall extends AnglePhysicObject
		{
			MyBall(AngleSpriteReference spriteReference)
			{
				super(spriteReference, 0, 1);
				addCircleCollider(new AngleCircleCollider(0, 0, 29));
				mMass = 10;
				mBounce = 0.8f; // Coefficient of restitution (1 return all the
				// energy)
			}

			@Override
			public float getSurface() // Return the surface of our object for air
			// friction
			{
				return 29 * 2; // Radius * 2
			}

			public void run()
			{
				AngleSpriteReference SR = (AngleSpriteReference) mVisual;
				SR.mRotation += 45 * AngleMainEngine.secondsElapsed;
				SR.mRotation %= 360;
			}

		}

		MyGameEngine(AngleSurfaceView view, Context ctx)
		{
			super(view, MAX_TYPES, MAX_OBJECTS);

			mSprites = new AngleSpritesEngine(MAX_TYPES, MAX_OBJECTS);
			mView.addEngine(mSprites);

			mBalls = new MyBall[MAX_BallS];
			mBallsCount = 0;

			mBallSprite = new AngleSprite(128 / 2, 128 / 2, R.drawable.ball, 0, 0,
					128, 128);
			mSprites.addSprite(mBallSprite);

			mGravity.mY = 9.8f;// Gravity
			mViscosity = 0.2f; // Air viscosity

			mStateMachine = smLoad;
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
			mSprites.removeRefernece((AngleAbstractReference) object.mVisual); // Remove
			// sprite
			// when
			// object
			// is
			// removed
		}

		public void run()
		{
			switch (mStateMachine)
			{
				case smLoad:
					// Add 4 segment colliders to simulate walls
					AnglePhysicObject mWall = new AnglePhysicObject(
							new AngleVisualObject(), 1, 0);
					mWall.mVisual.mCenter.set(AngleMainEngine.mWidth / 2,
							AngleMainEngine.mHeight - 1);
					mWall.addSegmentCollider(new AngleSegmentCollider(
							-AngleMainEngine.mWidth / 2, 0,
							AngleMainEngine.mWidth / 2, 0));
					mWall.mBounce = 0.5f;
					// Add object with AnglePhysicsGameEngine.addObject cause walls
					// doesn't have sprite
					super.addObject(mWall); // Down wall
					mWall = new AnglePhysicObject(new AngleVisualObject(), 1, 0);
					mWall.mVisual.mCenter.set(AngleMainEngine.mWidth / 2, 0);
					mWall.addSegmentCollider(new AngleSegmentCollider(
							AngleMainEngine.mWidth / 2, 0,
							-AngleMainEngine.mWidth / 2, 0));
					mWall.mBounce = 0.5f;
					super.addObject(mWall); // Up wall
					mWall = new AnglePhysicObject(new AngleVisualObject(), 1, 0);
					mWall.mVisual.mCenter.set(AngleMainEngine.mWidth - 1,
							AngleMainEngine.mHeight / 2);
					mWall.addSegmentCollider(new AngleSegmentCollider(0,
							AngleMainEngine.mHeight / 2, 0,
							-AngleMainEngine.mHeight / 2));
					mWall.mBounce = 0.5f;
					super.addObject(mWall); // Right wall
					mWall = new AnglePhysicObject(new AngleVisualObject(), 1, 0);
					mWall.mVisual.mCenter.set(0, AngleMainEngine.mHeight / 2);
					mWall.addSegmentCollider(new AngleSegmentCollider(0,
							-AngleMainEngine.mHeight / 2, 0,
							AngleMainEngine.mHeight / 2));
					mWall.mBounce = 0.5f;
					super.addObject(mWall); // Left wall
					mStateMachine = smRun;
					break;
				case smRun:
					simulate();
					break;
			}
			super.run();
		}

		private void simulate()
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
				// Add new Ball at the touch position
				if (mBallsCount < MAX_BallS)
				{
					mBalls[mBallsCount] = new MyBall(new AngleSpriteReference(
							mBallSprite));
					mBalls[mBallsCount].mVisual.mCenter.set(event.getX(), event
							.getY());

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

	// This way to use the sensors is depreciated
	// TODO change with the new way
	class MyView extends AngleSurfaceView implements SensorListener
	{

		public MyView(Context context)
		{
			super(context);
		}

		@Override
		public void onAccuracyChanged(int sensor, int accuracy)
		{
		}

		@Override
		public void onSensorChanged(int sensor, float[] values)
		{
			mGame.mGravity.mX = values[3] * 2;
			mGame.mGravity.mY = -values[4] * 2;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mView = new MyView(this);
		setContentView(mView);

		mGame = new MyGameEngine(mView, this);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
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
		mSensorManager
				.registerListener(mView, SensorManager.SENSOR_ACCELEROMETER);
		mView.onResume();
		super.onResume();
	}

	@Override
	protected void onStop()
	{
		mSensorManager.unregisterListener(mView);
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		mView.onDestroy();
		super.onDestroy();
	}
	*/
}
