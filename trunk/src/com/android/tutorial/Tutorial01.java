package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;
import com.android.angle.AngleSurfaceView;

/**
 * This tutorial demonstrates how to create and destroy an empty ANGLE engine.
 * The process is extremely simple. All you should do is create and destroy an
 * AngleSurfaceView.
 * 
 * We learn to: -Create the main view (AngleSurfaceView) -Override the basic
 * methods -Destroy the engine
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial01 extends Activity
{
	private AngleSurfaceView mView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mView = new AngleSurfaceView(this); // Create and set the view.
		setContentView(mView); // All the initializations are done inside
		// AngleSurfaceView.
	}

	@Override
	protected void onPause()
	{
		mView.onPause(); // onPause and onResume must be called in order to tell
		// the engine
		super.onPause(); // render when activity is active.
	}

	@Override
	protected void onResume()
	{
		mView.onResume(); // onPause and onResume must be called in order to tell
		// the engine
		super.onResume(); // render when activity is active.
	}

	@Override
	protected void onDestroy()
	{
		mView.onDestroy(); // Destroy the view and it will destroy the engine
		super.onDestroy();
	}
}
