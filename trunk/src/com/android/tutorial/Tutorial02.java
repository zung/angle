package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;

import com.android.angle.AngleAbstractGameEngine;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

/**
 * In this tutorial, we create a sprites engine and add an AngleSprite.
 * 
 * We learn to:
 * -Create a basic game engine 
 * -Add one rendering engine to main game engine. In this case a sprites engine. 
 * -Create an AngleSprite(Engine, Width, Height, ResourceId, 
 *                        Crop Left, Crop Top, Crop Width, Crop Height) 
 * -Change the position of the sprite
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial02 extends Activity
{
	private AngleSurfaceView mView; //Main view
	private AngleAbstractGameEngine mGame; //Simplest game engine. Only renders
	private AngleSpritesEngine mSprites; //The engine where the sprite will be added
	private AngleSpriteLayout mLogoLayout; //The layout (see below)
	private AngleSprite mLogo; //The sprite (see below)

	// scale, rotation nor effects

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mView = new AngleSurfaceView(this);
		setContentView(mView);
		
		//1st we need a game engine that contains the rendering engines tree
		mGame=new AngleAbstractGameEngine(mView);

		// Create a sprites engine with maximum of 10 layouts and 10 sprites
		mSprites = new AngleSpritesEngine(10, 10); 
		
		// and adds it to our game engine
		mGame.addEngine(mSprites); 

		
		// Create a layout that describes how the sprite will be rendered (the layout will be automatically added to mSprites)
		mLogoLayout = new AngleSpriteLayout(mSprites, 128, 128, R.drawable.anglelogo, 0, 0, 128, 128);
		// Create a sprite that use this layout (the sprite will be automatically added to mSprites and render if visible)
		mLogo = new AngleSprite (mSprites, mLogoLayout);
		// Set position
		mLogo.mCenter.set(100, 100); 

		//Set the game engine to run and render
		//In this sample (AngleAbstractGameEngine) only render
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
