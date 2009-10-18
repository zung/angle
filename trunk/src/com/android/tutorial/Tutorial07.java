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
		private static final int MAX_LOGOS = 50;
		private static final int MAX_OBJECTS = 100;
		private static final int MAX_TYPES = 10;
		private AngleSprite mLogoSprite;  		
		private MyLogo[] mLogos;
		private int mLogosCount;
		
		//Create new game object class overloading the AngleSpriteReference  
		class MyLogo extends AnglePhysicObject
		{
			MyLogo(AngleSprite sprite)
			{
				super(sprite);
				addCollider(new AngleCircleCollider(0,0,sprite.mWidth/2));
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

			mLogos=new MyLogo[MAX_LOGOS];
			mLogosCount=0;

			mLogoSprite = new AngleSprite(128, 56, R.drawable.anglelogo, 0, 25, 128, 81);
			mSprites.addSprite(mLogoSprite);

			mLogos[mLogosCount]=new MyLogo(mLogoSprite);
			mLogos[mLogosCount].mX=160;
			mLogos[mLogosCount].mY=100;
			addObject(mLogos[mLogosCount++]);
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

			//Move all logos
			for (int l=0;l<mLogosCount;l++)
			{
				mLogos[l].run();
				//remove logo if is out of screen
				if (mLogos[l].mY<-mLogoSprite.mWidth/2)
				{
					removeObject(mLogos[l]);
					mLogosCount--;
					for (int d=l;d<mLogosCount;d++)
						mLogos[d]=mLogos[d+1];
					mLogos[mLogosCount]=null;
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
				//Add new logo at the touch position
				if (mLogosCount<MAX_LOGOS)
				{
					mLogos[mLogosCount]=new MyLogo(mLogoSprite);
					mLogos[mLogosCount].mX=event.getX();
					mLogos[mLogosCount].mY=event.getY();
					mLogos[mLogosCount].mVelocityY=-30;
					addObject(mLogos[mLogosCount++]);
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
