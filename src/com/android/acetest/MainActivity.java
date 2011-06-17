package com.android.acetest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.android.ace.ACEGameEngine;
import com.android.ace.ACEMainView;
import com.android.ace.ACERotatingSprite;
import com.android.ace.ACESpriteLayout;
import com.android.ace.ACETile;
import com.android.ace.ACEViewGroup;

public class MainActivity extends Activity
{
	ACEMainView mView;

	public class RotSel extends ACERotatingSprite
	{

		private float mRotationSpeed;

		public RotSel(Context context, ACESpriteLayout layout)
		{
			super(context, layout);
			mRotationSpeed = (float) (10 + (Math.random() * 30));
		}

		@Override
		public void step(float secondsElapsed)
		{
			super.step(secondsElapsed);
			rotate(secondsElapsed * mRotationSpeed);
		}

	};

	public class MyEngine extends ACEGameEngine
	{
		MainActivity mActivity;
		private int frameCount;
		private long lCTM;
		private ACESpriteLayout slSelection;
		private ACESpriteLayout slBackground;
		private ACEViewGroup vgSelections;
		private ACETile sprBackground;
		private ACEViewGroup vgRoot;

		public MyEngine(MainActivity activity)
		{
			super();
			mActivity = activity;

			vgRoot = new ACEViewGroup(mActivity);
			vgSelections = new ACEViewGroup(mActivity);

			slSelection = new ACESpriteLayout(mActivity, R.drawable.selection);
			slBackground = new ACESpriteLayout(mActivity, R.drawable.fondo_adeletia);

			sprBackground = new ACETile(mActivity, slBackground);
			sprBackground.addToView(vgRoot);

			for (int t = 0; t < 15; t++)
			{
				RotSel mSel = new RotSel(mActivity, slSelection);
				mSel.addToView(vgSelections);
				mSel.moveTo((int) (Math.random() * 320), (int) (Math.random() * 480));
			}
		}

		@Override
		public void linkToView(ACEMainView view)
		{
			view.removeAllViews();
			view.addView(vgRoot);
			view.addView(vgSelections);
		}

		@Override
		public void step(float secondsElapsed)
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

			vgRoot.step(secondsElapsed);
			vgSelections.step(secondsElapsed);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(1);
		mView = new ACEMainView(this);
		setContentView(mView);

		mView.setAwake(true);

		MyEngine mTest = new MyEngine(this);
		mView.setGameEngine(mTest);
	}

	@Override
	protected void onPause()
	{
		mView.pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		mView.resume();
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		boolean result = mView.onKeyDown(keyCode, event);
		if (result == false)
			return super.onKeyDown(keyCode, event);
		else
			return result;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		boolean result = mView.onTrackballEvent(event);
		if (result == false)
			return super.onTrackballEvent(event);
		else
			return result;
	}
}