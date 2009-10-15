package com.android.tutorial;

import com.android.angle.AngleMainEngine;
import com.android.angle.AngleSimpleSprite;
import com.android.angle.AngleSpritesEngine;
import com.android.angle.AngleSurfaceView;

import android.app.Activity;
import android.os.Bundle;

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
	
		mLogo = new AngleSimpleSprite(128, 56, R.drawable.anglelogo, 0, 25, 128, 81);
		mSprites.addSprite(mLogo);

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
