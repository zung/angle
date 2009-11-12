package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;

import com.android.angle.AngleSprite;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

/**
 * In this tutorial, we create a sprites engine and add an AngleSimpleSprite.
 * 
 * We learn to: -Add one rendering engine to main engine. In this case a sprites
 * engine. -Create an AngleSimpleSprite(Width, Height, Resource, Crop Left, Crop
 * Top, Crop Width, Crop Height) -Change the position of the sprite
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial02 extends Activity
{
	private AngleSurfaceView mView;
	private AngleSpritesEngine mSprites; // The engine where the sprite will be
	// added
	private AngleSprite mLogo; // A simple sprite. It doesn't supports

	// scale, rotation nor effects

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mView = new AngleSurfaceView(this);
		setContentView(mView);

		mSprites = new AngleSpritesEngine(10, 0); // Create the sprites engine
		// with maximum 10 sprites and 0
		// references (see tutorial 6)
		mView.addEngine(mSprites); // and adds it to main engine

		// Create one simple sprite with the logo and place it at position 100,100
		mLogo = new AngleSprite(128, 128, R.drawable.anglelogo, 0, 0, 128,
				128);
		mLogo.mCenter.set(100, 100); // Set position
		mSprites.addSprite(mLogo); // The sprites engine will draw all the sprites
		// added automatically
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
