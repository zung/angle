package com.android.tutorial;

import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSimpleSprite;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

import android.app.Activity;
import android.os.Bundle;

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
	private AngleSimpleSprite mLogo; // A simple sprite. It doesn't supports
												// scale, rotation nor effects

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mSprites = new AngleSpritesEngine(10, 0); // Create the sprites engine
																// with maximum 10 sprites and 0
																// references (see tutorial 6)
		AngleMainEngine.addEngine(mSprites); // and adds it to main engine

		// Create one simple sprite with the logo and place it at position 100,100
		mLogo = new AngleSimpleSprite(128, 128, R.drawable.anglelogo, 0, 0, 128,
				128);
		mLogo.mCenter.set(100, 100);
		mSprites.addSprite(mLogo); // The sprites engine will draw all the sprites
											// added automatically

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
