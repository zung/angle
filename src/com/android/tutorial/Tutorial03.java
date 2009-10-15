package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

/**
 * In this tutorial we use a Runnable to implement a little game engine
 * 
 *  We learn to:
 *  -Add a game engine.
 *  -Use AgleSprite and its rotation
 *  -Implements a FPS counter
 *  -Do the changes consistent with the time elapsed since last frame
 *  
 * @author Ivan Pajuelo
 *
 */
public class Tutorial03 extends Activity
{
	private MyGameEngine mGame; //Independent game engine 
	private AngleSurfaceView mView;
	private AngleSpritesEngine mSprites; 
	private AngleSprite mLogo; //In this sample use AmgleSprite (see below)		

	class MyGameEngine implements Runnable //Game engine class 
	{
		//FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		//-----------

		MyGameEngine()
		{
		}

		//The game engine must be a Runnable to callback his method run before draw every frame 
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
			
			//Rotate the logo at 45º per second
			mLogo.mRotation+=45*AngleMainEngine.secondsElapsed;
			mLogo.mRotation%=360;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mGame = new MyGameEngine(); //Instantiation 

		mSprites = new AngleSpritesEngine(); 
		AngleMainEngine.addEngine(mSprites); 
	
		//Use AngleSprite instead of AngleSimpleSprite to rotate it
		mLogo = new AngleSprite(128, 56, R.drawable.anglelogo, 0, 25, 128, 81);
		mLogo.mX=100;
		mLogo.mY=100;
		mSprites.addSprite(mLogo); 

		mView = new AngleSurfaceView(this);  
		setContentView(mView);	
		mView.setBeforeDraw(mGame); //Tells view what method must call before draw every frame 
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
