package com.android.tutorial;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

/**
 *  
 *	Overload AngleSpritesEngine and AngleSprite to extend they functionality.
 * To put the activity in full screen set the Theme to "@android:style/Theme.NoTitleBar.Fullscreen"
 * in AndroidManifest
 * If you also want to put your activity in landscape change screenOrientation to "landscape" 
 * in AndroidManifest
 * 
 *  We learn to:
 *  -Extend Angle functionality
 *  -Put activity in full screen
 *  -Put activity in landscape
 * 
 * @author Ivan Pajuelo
 *
 */
public class Tutorial05 extends Activity
{
	private MyGameEngine mGame;  
	private AngleSurfaceView mView;
	private MyBlendSpritesEngine mSprites; //Use overloaded engine 
	
	class MyBlendSprite extends AngleSprite //Overload AngleSprite to add blend effect 
	{

		public MyBlendSprite(int width, int height, int resourceId, int cropLeft,
				int cropTop, int cropWidth, int cropHeight)
		{
			super(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight);
		}

		@Override
		public void draw(GL10 gl)
		{
			gl.glColor4f(1f, 1f, 0f, 0.5f); //Little blend effect	
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
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND); //Enable blend on textures
		}
		
	}

	class MyGameEngine implements Runnable  
	{
		//FPS Counter
		private int frameCount = 0;
		private long lCTM = 0;
		//-----------
		private static final int smLoad = 0;
		private static final int smRotate = 1;
		private int stateMachine=smLoad;
		private AngleSprite mLogo;  		

		MyGameEngine()
		{
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
			
			switch (stateMachine)
			{
				case smLoad: 
					mLogo = new MyBlendSprite(128, 56, R.drawable.anglelogo, 0, 25, 128, 81); //Use the overloaded sprite
					mLogo.mCenter.set(AngleMainEngine.mWidth/2,AngleMainEngine.mHeight/2);
					mSprites.addSprite(mLogo);
					
					stateMachine=smRotate;
					break;
				case smRotate:
					mLogo.mRotation+=45*AngleMainEngine.secondsElapsed;
					mLogo.mRotation%=360;
					break;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mGame = new MyGameEngine();  

		mSprites = new MyBlendSpritesEngine(); 
		AngleMainEngine.addEngine(mSprites); 
	
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
