package com.android.angle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AngleTest extends Activity
{
	AngleSurfaceView mView;
	AngleRenderEngine mRenderEngine;
	AngleGameEngine mGameEngine;
	AngleSpriteRenderer mSprites; 
	
	class AngleGameEngine implements Runnable
	{
		private static final int smLoad = 1;
		private static final int smMove = 2;
		private AngleRenderEngine mRenderEngine;
		private int mStateMachine=smLoad;
		private AngleSprite s;
		private int dir=1;
		
		AngleGameEngine (AngleRenderEngine renderEngine)
		{
			mRenderEngine=renderEngine;
		}
		
		public void run()
		{
			Log.v("Run", "Run");
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
		mSprites=new AngleSpriteRenderer();
		mRenderEngine = new AngleRenderEngine(this);
		mRenderEngine.addRenderer(mSprites);
		mView = new AngleSurfaceView(this);
		mView.setRenderEngine(mRenderEngine);
		mView.setBeforeDraw(new AngleGameEngine(mRenderEngine));
		setContentView(mView);
	}
}