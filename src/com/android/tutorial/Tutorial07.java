package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleCircleCollider;
import com.android.angle.AngleMainEngine;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsGameEngine;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

public class Tutorial07 extends Activity
{
	private MyGameEngine mGame;  
	private AngleSurfaceView mView;

	class MyGameEngine extends AnglePhysicsGameEngine  
	{
		//FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		//-----------
		private AngleSpritesEngine mSprites; 
		private static final int MAX_BallS = 50;
		private static final int MAX_OBJECTS = 100;
		private static final int MAX_TYPES = 10;
		private AngleSprite mBallSprite;  		
		private MyBall[] mBalls;
		private int mBallsCount;
		
		//Create new game object class overloading the AngleSpriteReference  
		class MyBall extends AnglePhysicObject
		{
			MyBall(AngleSprite sprite)
			{
				super(sprite);
				addCollider(new AngleCircleCollider(0,0,sprite.mWidth/2-4));
				mMass=10;
			}

			public void run()
			{
				mRotation+=45*AngleMainEngine.secondsElapsed;
				mRotation%=360;
			}
		}

		MyGameEngine()
		{
			super (MAX_OBJECTS, MAX_TYPES);
			mSprites = new AngleSpritesEngine(MAX_TYPES, MAX_OBJECTS); 
			AngleMainEngine.addEngine(mSprites); 

			mBalls=new MyBall[MAX_BallS];
			mBallsCount=0;

			mBallSprite = new AngleSprite(128/2, 128/2, R.drawable.ball, 0, 0, 128, 128);
			mSprites.addSprite(mBallSprite);
		}

		@Override
		public void addObject(AnglePhysicObject object)
		{
			super.addObject(object);
			mSprites.addReference(object); //Add sprite to mSprites when object is added
		}

		@Override
		public void removeObject(AnglePhysicObject object)
		{
			super.removeObject(object);
			mSprites.removeRefernece(object); //Remove sprite when object is removed
		}

		public void run()
		{
			//Add FPS record to log every 100 frames
			frameCount++;
			if (frameCount >= 100)
			{
				long CTM = System.currentTimeMillis();
				frameCount = 0;
				if (lCTM > 0)
					Log.v("FPS", "" + (100.f / ((CTM - lCTM) / 1000.f)));
				lCTM = CTM;
			}
			//--------------------------------------

			//Move all Balls
			for (int l=0;l<mBallsCount;l++)
			{
				mBalls[l].run();
				//remove Ball if is out of screen
				if ((mBalls[l].mX<-mBallSprite.mWidth/2)||(mBalls[l].mY<-mBallSprite.mWidth/2)||(mBalls[l].mX>AngleMainEngine.mWidth+mBallSprite.mWidth/2)||(mBalls[l].mY>AngleMainEngine.mHeight+mBallSprite.mWidth/2))
				{
					removeObject(mBalls[l]);
					mBallsCount--;
					for (int d=l;d<mBallsCount;d++)
						mBalls[d]=mBalls[d+1];
					mBalls[mBallsCount]=null;
				}
			}
			super.run();
		}

		//Place the input processing in to game engine
		public void onTouchEvent(MotionEvent event)
		{
			//Prevent event flooding
			//Max 20 events per second
			try
			{
				Thread.sleep(50); 
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			//-------------------------
			if (event.getAction()==MotionEvent.ACTION_DOWN)
			{
				//Add new Ball at the touch position
				if (mBallsCount<MAX_BallS)
				{
					mBalls[mBallsCount]=new MyBall(mBallSprite);
					mBalls[mBallsCount].mX=event.getX();
					mBalls[mBallsCount].mY=event.getY();
					float x=AngleMainEngine.mWidth/2-mBalls[mBallsCount].mX;
					float y=AngleMainEngine.mHeight/2-mBalls[mBallsCount].mY;
					float a=(float) Math.acos(y/Math.sqrt(x*x+y*y));
					if (x<0)
						a=(float) (Math.PI*2-a);
					float force=event.getSize()*400;
					mBalls[mBallsCount].mVelocityX=(float) (force*Math.sin(a));
					mBalls[mBallsCount].mVelocityY=(float) (force*Math.cos(a));
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

		mGame = new MyGameEngine();  

		mView = new AngleSurfaceView(this);  
		setContentView(mView);	
		mView.setBeforeDraw(mGame);  
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) //Get touch input events
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
