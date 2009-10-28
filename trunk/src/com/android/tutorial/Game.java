package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSimpleSprite;
import com.android.angle.AngleSimpleSpriteReference;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;
import com.android.angle.AngleTileEngine;

public class Game extends Activity
{
	private MyGameEngine mGame;  
	private AngleSurfaceView mView;
	
	class MyGameEngine implements Runnable  
	{
		class DashEngine extends AngleSpritesEngine
		{
			private AngleSimpleSprite mBG; 		

			public DashEngine()
			{
				super(10, 0);
				mBG=new AngleSimpleSprite(320, 64, R.drawable.tilemap, 0, 448, 320, 64);
				mBG.mCenter.mY=mBackground.mTileHeight*12+mBG.mHeight/2;
				mBG.mCenter.mX=mBG.mWidth/2;
				addSprite(mBG);
			}
		};

		//FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		//-----------
		private AngleSpritesEngine mSprites; 
		private AngleTileEngine mBackground;
		private DashEngine mDash;

		private static final int smLoad = 0;
		private static final int smRotate = 1;
		private static final int MAX_SHOTS = 50;
		private int stateMachine=smLoad;
		private AngleSimpleSprite mLogo;  		
		private AngleSimpleSprite mMiniLogo; 		
		private AngleSimpleSpriteReference mShip; 		
		private AngleSimpleSpriteReference[] mShot;
		private int mShotsCount;
		private long mShotColdDown=0; 		

		MyGameEngine()
		{
			mBackground = new AngleTileEngine(R.drawable.tilemap, 15, 15*14, 32, 32, 10, 1064);
			AngleMainEngine.addEngine(mBackground);

			mSprites = new AngleSpritesEngine(10,100); 
			AngleMainEngine.addEngine(mSprites); 

			mDash = new DashEngine(); 
			AngleMainEngine.addEngine(mDash); 

			mBackground.mViewWidth=10;
			mBackground.mViewHeight=13;

			mBackground.mTop=(mBackground.mMapHeight-mBackground.mViewHeight)*mBackground.mTileHeight;
			
			mBackground.loadMap (getResources().openRawResource(R.raw.map));

			mLogo = new AngleSimpleSprite(128, 56, R.drawable.anglelogo, 0, 25, 128, 81);
			mSprites.addSprite(mLogo);
			mMiniLogo = new AngleSimpleSprite(25, 11, R.drawable.anglelogo, 0, 25, 128, 81);
			mSprites.addSprite(mMiniLogo);
			mShip = new AngleSimpleSpriteReference(mLogo); 		
			mShip.mCenter.set(160,200);
			mShot = new AngleSimpleSpriteReference[MAX_SHOTS];
			mShotsCount=0;
		}

		//Place the input processing in to game engine
		public void onTouchEvent(MotionEvent event)
		{
			//Prevent event flooding
			//Max 33 events per second
			try
			{
				Thread.sleep(30); 
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			//-------------------------
			mShip.mCenter.set(event.getX(),event.getY()-64-mLogo.mHeight/2);
			if (mShotsCount<MAX_SHOTS)
			{
				long CTM=System.currentTimeMillis();
				if (CTM>mShotColdDown)
				{
					mShotColdDown=CTM+80;
					mShot[mShotsCount]=new AngleSimpleSpriteReference(mMiniLogo);
					mShot[mShotsCount].mCenter.set(mShip.mCenter.mX,mShip.mCenter.mY-20);
					mSprites.addReference(mShot[mShotsCount++]);
				}
			}
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
			
			switch (stateMachine) //Very simple state machine
			{
				case smLoad: //Load sprite in runtime
					mSprites.addReference(mShip);
					mShip.mCenter.set(AngleMainEngine.mWidth/2,AngleMainEngine.mHeight/2);
					mShip.mCenter.set(160,0);
					stateMachine=smRotate;
					break;
				case smRotate:
					mBackground.mTop-=200*AngleMainEngine.secondsElapsed;
					for (int s=0;s<mShotsCount;s++)
					{
						if (mShot[s]!=null)
						{
							mShot[s].mCenter.mY-=200*AngleMainEngine.secondsElapsed;
							if (mShot[s].mCenter.mY<-10)
							{
								mSprites.removeRefernece(mShot[s]);
								mShot[s]=null;
							}
						}
					}
					for (int s=0;s<mShotsCount;s++)
					{
						if (mShot[s]==null)
						{
							mShotsCount--;
							for (int d=s;d<mShotsCount;d++)
								mShot[d]=mShot[d+1];
							mShot[mShotsCount]=null;
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
		setContentView(R.layout.main);

		mGame = new MyGameEngine();  

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
