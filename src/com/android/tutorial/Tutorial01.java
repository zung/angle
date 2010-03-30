package com.android.tutorial;

import android.os.Bundle;

import com.android.angle.AngleActivity;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;

/**
 * This tutorial demonstrates how to create an ANGLE engine and add a sprite to render.
 * 
 * In this example we use the main GL view directly
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial01 extends AngleActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Create a sprite layout
		//In this case use drawable anglelogo cropping a 128x128 square placed at position 0,0 (See others AngleSpriteLayout constructors for more options)
		AngleSpriteLayout mLogoLayout = new AngleSpriteLayout(mGLSurfaceView, R.drawable.anglelogo);
		//Create a sprite that use this layout
		AngleSprite mLogo = new AngleSprite (mLogoLayout);
		//Set position
		mLogo.mPosition.set(160, 200); 
		//Add sprite to main view
		mGLSurfaceView.addObject(mLogo);
		//Set mGLSurfaceView as default view
		setContentView(mGLSurfaceView);
	}

}
