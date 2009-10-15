package com.android.tutorial;

import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSimpleSprite;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

import android.app.Activity;
import android.os.Bundle;
/**
 * In this tutorial, we create a sprites engine and add an AngleSimleSprite.
 *  
 *  We learn to:
 *  -Add one rendering engine to main engine. In this case a sprites engine.
 *  -Create an AngleSimpleSprite(Width, Height, Resource, Crop Left, Crop Top, Crop Width, Crop Height)
 *  -Change position of the sprite
 * 
 * @author Ivan Pajuelo
 *
 */
public class Tutorial02 extends Activity
{
	private AngleSurfaceView mView;
	private AngleSpritesEngine mSprites; //The engine where sprite will be added
	private AngleSimpleSprite mLogo;		//A simple sprite. It doesn't supports scale, rotation nor effects

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mSprites = new AngleSpritesEngine(); //Create the sprites engine
		AngleMainEngine.addEngine(mSprites); //and adds it to main engine
	
		//Create one simple sprite with the logo and place it at 100,100
		mLogo = new AngleSimpleSprite(128, 56, R.drawable.anglelogo, 0, 25, 128, 81);
		mLogo.mX=100;
		mLogo.mY=100;
		mSprites.addSprite(mLogo); //The sprites engine will draw all the sprites added automatically

		mView = new AngleSurfaceView(this);  
		setContentView(mView);					
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
