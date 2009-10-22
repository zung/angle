package com.android.angle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class AngleAccelerometerListener implements SensorEventListener
{
	public float direction = (float) 0;
	public float inclination;
	public float rollingZ = (float) 0;

	public float kFilteringFactor = (float) 0.05;
	public float aboveOrBelow = (float) 0;

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		float vals[] = event.values;

		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
		{
			float rawDirection = vals[0];

			direction = (float) ((rawDirection * kFilteringFactor) + (direction * (1.0 - kFilteringFactor)));

			inclination = (float) ((vals[2] * kFilteringFactor) + (inclination * (1.0 - kFilteringFactor)));

			if (aboveOrBelow > 0)
				inclination = inclination * -1;

			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			{
				aboveOrBelow = (float) ((vals[2] * kFilteringFactor) + (aboveOrBelow * (1.0 - kFilteringFactor)));
			}
			Log.v("ACC","direction="+direction);
			Log.v("ACC","inclination="+inclination);
			Log.v("ACC","aob="+aboveOrBelow);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub
		
	}
}
