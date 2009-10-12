package com.android.angle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AngleTest extends Activity
{
	private AngleSurfaceView mView;
	private AngleSpriteRenderer mSprites;
	private AngleGameEngine mGame; 
	
	class AngleGameEngine implements Runnable
	{
		private static final int smLoad = 1;
		private static final int smMove = 2;
		private int mStateMachine=smLoad;
		private AngleSprite s;
		private int dir=1;
		
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
		
		public void run()
		{
			switch (mStateMachine)
			{
				case smLoad:
					s = new AngleSprite(34,34,R.drawable.ball,0,0,34,34);
			      s.mX=30;
			      s.mY=0;
					mSprites.addSprite(s);
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
		setContentView(R.layout.main);
		if (savedInstanceState==null)
		{
			Log.e("CREATE!!!!!!!!!", "New");
			mSprites=new AngleSpriteRenderer();
			mView = new AngleSurfaceView(this);
			mGame=new AngleGameEngine();
			mView.setBeforeDraw(mGame);
			AngleRenderEngine.addRenderer(mSprites);
		}
		else
		{
			Log.e("CREATE!!!!!!!!!", "SAVED!!!");
			mGame.RestoreInstance(savedInstanceState);
		}
		setContentView(mView);
	}

	@Override
   public void onSaveInstanceState(Bundle outState) 
	{
       mGame.SaveInstance(outState);
   }

	@Override
   protected void onResume() 
	{
       // Ideally a game should implement onResume() and onPause()
       // to take appropriate action when the activity looses focus
       super.onResume();
       mView.onResume();
   }

   @Override
   protected void onPause() {
       // Ideally a game should implement onResume() and onPause()
       // to take appropriate action when the activity looses focus
       super.onPause();
       mView.onPause();
   }
}