package com.android.angle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AngleTest extends Activity
{
	private AngleSurfaceView mView=null;
	private AngleSpriteRenderer mSprites=null;
	private AngleGameEngine mGame=null; 
	
	class AngleGameEngine implements Runnable
	{
		private static final int smLoad = 1;
		private static final int smMove = 2;
		private int mStateMachine=0;
		private AngleSprite s;
		private int dir=1;
		private int frameCount=0;
		private long lCTM=0;
		
		AngleGameEngine ()
		{
		}

      public void SaveInstance(Bundle outState)
      {
         Bundle sav = new Bundle();

         sav.putInt("mStateMachine", Integer.valueOf(mStateMachine));
   		outState.putBundle("AngleGameEngine", sav);
   		Log.v("AngleGameEngine", "SaveInstance");
      }

      public void RestoreInstance(Bundle savedInstanceState)
      {
         Bundle sav = savedInstanceState.getBundle("AngleGameEngine");
         mStateMachine = sav.getInt("mStateMachine");
   		Log.v("AngleGameEngine", "RestoreInstance");
      }
      
      public void Load ()
      {
      	mStateMachine=smLoad;
      }
		
		public void run()
		{
			frameCount++;
			if (frameCount>=100)
			{
				long CTM = System.currentTimeMillis();
				frameCount=0;
				if (lCTM>0)
					Log.v("FPS",String.valueOf(100.f/((CTM-lCTM)/1000.f)));
				lCTM = CTM;
			}

			switch (mStateMachine)
			{
				case smLoad:
					for (int t=0;t<500;t++)
					{
						s = new AngleSprite(34,34,R.drawable.ball,0,0,34,34);
				      s.mX=(float) (Math.random()*300);
				      s.mY=(float) (Math.random()*460);
						mSprites.addSprite(s);
					}
					mStateMachine=smMove;
					break;
				case smMove:
					s.mX+=dir;
					if (s.mX>300||s.mX<20)
						dir=-dir;
					break;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d("AngleTest", "Create");
		setContentView(R.layout.main);
		if (savedInstanceState==null)
		{
			Log.e("AngleGameEngine", "New");
			mSprites=new AngleSpriteRenderer();
			mView = new AngleSurfaceView(this);
			mGame=new AngleGameEngine();
			mView.setBeforeDraw(mGame);
			AngleRenderEngine.addRenderer(mSprites);
			mGame.Load();
		}
		else
		{
			Log.e("AngleGameEngine", "SAVED!!!");
			mGame.RestoreInstance(savedInstanceState);
		}
		setContentView(mView);
	}

	@Override
   public void onSaveInstanceState(Bundle outState) 
	{
		Log.d("AngleTest", "SaveInstanceState");
      mGame.SaveInstance(outState);
   }

	@Override
   protected void onResume() 
	{
		Log.d("AngleTest", "Resume");
      mView.onResume();
      super.onResume();
   }

   @Override
	protected void onDestroy()
	{
		Log.d("AngleTest", "Destroy");
		AngleRenderEngine.shutdown();
		super.onDestroy();
	}

	@Override
	protected void onRestart()
	{
		Log.d("AngleTest", "Restart");
		super.onRestart();
	}

	@Override
	protected void onStart()
	{
		Log.d("AngleTest", "Start");
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		Log.d("AngleTest", "Stop");
		super.onStop();
	}

	@Override
   protected void onPause() 
	{
		Log.d("AngleTest", "Pause");
      mView.onPause();
      super.onPause();
   }
}