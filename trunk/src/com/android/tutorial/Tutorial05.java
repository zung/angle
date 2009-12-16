package com.android.tutorial;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.angle.AngleAbstractGameEngine;
import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSpriteX;
import com.android.angle.AngleSpriteXLayout;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

/**
 * 
 * Overload AngleSpritesEngine and AngleSprite to extend they functionality. To
 * put the activity in full screen set the Theme to
 * "@android:style/Theme.NoTitleBar.Fullscreen" in AndroidManifest If you also
 * want to put your activity in landscape change screenOrientation to
 * "landscape" in AndroidManifest
 * 
 * We learn to: 
 * -Extend Angle functionality 
 * -Put activity in full screen 
 * -Put activity in landscape
 * 
 * @author Ivan Pajuelo
 * 
 */

public class Tutorial05 extends Activity
{
	private MyGameEngine mGame;
	private AngleSurfaceView mView;

	class MyBlendSprite extends AngleSpriteX // Overload AngleSprite to add blend effect
	{

		public MyBlendSprite(AngleSpritesEngine engine, AngleSpriteXLayout layout)
		{
			super(engine,layout);
		}

		@Override
		public void draw(GL11 gl)
		{
			gl.glColor4f(0f, 1f, 1f, 0.8f); // Little blend effect
			super.draw(gl);
		}


	}

	class MyBlendSpritesEngine extends AngleSpritesEngine
	{

		public MyBlendSpritesEngine()
		{
			super(10, 50);
		}

		@Override
		public void afterLoadTextures(GL11 gl)
		{
			super.afterLoadTextures(gl);
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_BLEND); // Enable blend on textures
		}

	}

	class MyGameEngine extends AngleAbstractGameEngine
	{
		private static final int MAX_LOGOS = 10;
		private AngleSpritesEngine mSprites;
		private AngleSpriteXLayout mLogoLayout;
		private AngleSpriteX mLogos[]; 
		private static final int smLoad = 0;
		private static final int smRotate = 1;
		private int stateMachine = smLoad;

		// FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		// -----------

		MyGameEngine(AngleSurfaceView view)
		{
			super(view);
			mSprites = new MyBlendSpritesEngine(); // Use overloaded engine
//			mSprites = new AngleSpritesEngine(10, 10);
			mLogoLayout = new AngleSpriteXLayout(mSprites, 128, 128, R.drawable.anglelogo, 0, 0, 128, 128);
			mLogos = new AngleSpriteX[MAX_LOGOS]; 
			addEngine(mSprites);
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

			switch (stateMachine)
			// Very simple state machine
			{
				case smLoad: // Load sprite in runtime
					for (int l=0;l<MAX_LOGOS;l++)
					{
						mLogos[l] = new MyBlendSprite(mSprites, mLogoLayout); // Use the overloaded sprite
//						mLogos[l] = new AngleSpriteX(mSprites, mLogoLayout); 
						mLogos[l].mCenter.set(AngleMainEngine.mWidth / 2-100+(int)(Math.random()*200),
								AngleMainEngine.mHeight / 2-200+(int)(Math.random()*400));
					}

					stateMachine = smRotate;
					break;
				case smRotate:
					for (int l=0;l<MAX_LOGOS;l++)
					{
						mLogos[l].mRotation += 45 * AngleMainEngine.secondsElapsed;
						mLogos[l].mRotation %= 360;
					}
					break;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mView = new AngleSurfaceView(this);
		setContentView(mView);

		mGame = new MyGameEngine(mView);

		mView.setGameEngine(mGame);
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

/*
public class Tutorial05 extends Activity
{
	private MyGameEngine mGame;
	private AngleSurfaceView mView;

	class MyBlendSprite extends AngleSpriteX // Overload AngleSprite to add blend effect
	{

		public MyBlendSprite(AngleSpritesEngine engine, AngleSpriteXLayout layout)
		{
			super(engine,layout);
		}

		@Override
		public void draw(GL11 gl)
		{
			gl.glColor4f(0f, 1f, 1f, 0.8f); // Little blend effect
			super.draw(gl);
		}


	}

	class MyBlendSpritesEngine extends AngleSpritesEngine
	{

		public MyBlendSpritesEngine()
		{
			super(10, 50);
		}

		@Override
		public void afterLoadTextures(GL11 gl)
		{
			super.afterLoadTextures(gl);
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_BLEND); // Enable blend on textures
		}

	}

	class MyGameEngine extends AngleAbstractGameEngine
	{
		private static final int MAX_LOGOS = 10;
		private AngleSpritesEngine mSprites; 
		private AngleSpriteXLayout mLogoLayout;
		private AngleSpriteX[] mLogos;
		private static final int smLoad = 0;
		private static final int smRotate = 1;
		private int stateMachine = smLoad;

		// FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		// -----------

		MyGameEngine(AngleSurfaceView view)
		{
			super (view);
//			mSprites = new MyBlendSpritesEngine(); // Use overloaded engine
			mSprites = new AngleSpritesEngine(10,50);
			mLogos = new AngleSpriteX[MAX_LOGOS]; 
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

			switch (stateMachine)
			{
				case smLoad:
					mLogoLayout = new AngleSpriteXLayout(mSprites, 128, 128, R.drawable.anglelogo, 0, 0, 128, 128);
					for (int l=0;l<MAX_LOGOS;l++)
					{
//						mLogos[l] = new MyBlendSprite(mSprites, mLogoLayout); // Use the overloaded sprite
						mLogos[l] = new AngleSpriteX(mSprites, mLogoLayout); 
						mLogos[l].mCenter.set(AngleMainEngine.mWidth / 2-100+(int)(Math.random()*200),
								AngleMainEngine.mHeight / 2-200+(int)(Math.random()*400));
					}
					stateMachine = smRotate;
					break;
				case smRotate:
					for (int l=0;l<MAX_LOGOS;l++)
					{
						mLogos[l].mRotation += 45 * AngleMainEngine.secondsElapsed;
						mLogos[l].mRotation %= 360;
					}
					break;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mView = new AngleSurfaceView(this);
		setContentView(mView);

		mGame = new MyGameEngine(mView);

		mView.setGameEngine(mGame);
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
*/