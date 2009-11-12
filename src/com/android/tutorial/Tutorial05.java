package com.android.tutorial;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.angle.AngleAbstractGameEngine;
import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSprite;
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
 * We learn to: -Extend Angle functionality -Put activity in full screen -Put
 * activity in landscape
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial05 extends Activity
{
	private MyGameEngine mGame;
	private AngleSurfaceView mView;
	private MyBlendSpritesEngine mSprites; // Use overloaded engine

	class MyBlendSprite extends AngleSprite // Overload AngleSprite to add blend
	// effect
	{

		public MyBlendSprite(int width, int height, int resourceId, int cropLeft,
				int cropTop, int cropWidth, int cropHeight)
		{
			super(width, height, resourceId, cropLeft, cropTop, cropWidth,
					cropHeight);
		}

		@Override
		public void draw(GL10 gl)
		{
			gl.glColor4f(0f, 1f, 1f, 0.8f); // Little blend effect
			super.draw(gl);
		}

	}

	class MyBlendSpritesEngine extends AngleSpritesEngine
	{

		public MyBlendSpritesEngine()
		{
			super(10, 0);
		}

		@Override
		public void afterLoadTextures(GL10 gl)
		{
			super.afterLoadTextures(gl);
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_BLEND); // Enable blend on textures
		}

	}

	class MyGameEngine extends AngleAbstractGameEngine
	{
		// FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		// -----------
		private static final int smLoad = 0;
		private static final int smRotate = 1;
		private static final int MAX_LOGOS = 10;
		private int stateMachine = smLoad;
		private AngleSprite[] mLogos;

		MyGameEngine(AngleSurfaceView view)
		{
			super (view);
			mLogos = new AngleSprite[MAX_LOGOS]; 
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
					for (int l=0;l<MAX_LOGOS;l++)
					{
						mLogos[l] = new MyBlendSprite(128, 128, R.drawable.anglelogo, 0, 0,
								128, 128); // Use the overloaded sprite
						mLogos[l].mCenter.set(AngleMainEngine.mWidth / 2-100+(int)(Math.random()*200),
								AngleMainEngine.mHeight / 2-200+(int)(Math.random()*400));
						mSprites.addSprite(mLogos[l]);
					}
					stateMachine = smRotate;
					break;
				case smRotate:
					for (int l=0;l<MAX_LOGOS;l++)
					{
						mLogos[l].mRotation += 45 * AngleMainEngine.secondsElapsed;
						mLogos[l].mRotation %= 360;
					}
					if (AngleMainEngine.mDirty)
					{
						for (int l=0;l<MAX_LOGOS;l++)
						{
							mLogos[l].mCenter.set(AngleMainEngine.mWidth / 2-100,
									AngleMainEngine.mHeight / 2);
						}
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

		mSprites = new MyBlendSpritesEngine();
		mView.addEngine(mSprites);
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
