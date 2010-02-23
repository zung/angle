package com.android.angle;

import android.util.Log;

public class FPSCounter extends AngleObject
{
	private int frameCount;
	private long lCTM;
	public float mFPS;

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
			{
				mFPS=(100.f / ((CTM - lCTM) / 1000.f));
				Log.e("FPS","" + mFPS);
			}
			lCTM = CTM;
		}
		// --------------------------------------

		super.step(secondsElapsed);
	}
}
