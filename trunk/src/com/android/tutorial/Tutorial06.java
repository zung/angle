package com.android.tutorial;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleCircleCollider;
import com.android.angle.AngleObject;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsEngine;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleUI;
import com.android.angle.FPSCounter;
import com.android.box2d.BodyDef;
import com.android.box2d.Box2D;
import com.android.box2d.Vec2;

public class Tutorial06 extends AngleActivity
{
	private MyDemo mDemo;
	
   private final SensorEventListener mListener = new SensorEventListener() 
   {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
		}

		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
			{
				mDemo.setGravity(-event.values[0],event.values[1]);
			}
		}
   };

	private SensorManager mSensorManager; 	

	private class Ball extends AngleSprite
	{

		public Ball(AngleSpriteLayout layout)
		{
			super(layout);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void step(float secondsElapsed)
		{
			// TODO Auto-generated method stub
			super.step(secondsElapsed);
		}
		
		
	};

	private class MyDemo extends AngleUI
	{
		AngleSpriteLayout mBallLayout;
		
		public MyDemo(AngleActivity activity)
		{
			super(activity);
 
			Box2D.createWorld(new Vec2(0f, -10f), true);
			final BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.position.y=-10;  
			int groundBody=Box2D.createBody(groundBodyDef);

			int groundBox=Box2D.createShape(Box2D.stPolygon);
			Box2D.setPolygonShapeAsBox(groundBox, 50.0f, 10.0f);
			Box2D.createFixture(groundBody, groundBox, 0);

			mBallLayout = new AngleSpriteLayout(mGLSurfaceView, 64, 64, R.drawable.ball, 0, 0, 128, 128);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			if (event.getAction()==MotionEvent.ACTION_DOWN)
			{
				if ((event.getX()>30)&&(event.getY()>30)&&(event.getX()<320-30)&&(event.getY()<480-30))
				{
					Ball mBall = new Ball (mBallLayout);
					mBall.mPosition.set(event.getX(), event.getY());
					// Ensure that there isn't any ball in this place
					// >Nos aseguramos de que ninguna pelota ocupa esta posición
					for (int b = 0; b < count(); b++)
					{
						AngleObject O=childAt(b);
						if (O instanceof Ball)
//							if (mBall.test((Ball)O))
								return true;
					}
					final BodyDef ballDef = new BodyDef();
					ballDef.type=BodyDef.b2_dynamicBody;
					ballDef.position.y=4;  
					int ballBody=Box2D.createBody(ballDef);

					int ballShape=Box2D.createShape(Box2D.stCircle);
					Box2D.setPolygonShapeAsBox(ballShape, 50.0f, 10.0f);
					Box2D.createFixture(ballBody, ballShape, 0);
					addObject(mBall);
				}
				return true;
			}
			return super.onTouchEvent(event);
		}

		public void setGravity(float x, float y)
		{
//			mPhysics.mGravity.set(x*3,y*3);
		}
		
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

      mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE); 
      
		mGLSurfaceView.addObject(new FPSCounter());

		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
		
		mDemo=new MyDemo(this);
		setUI(mDemo);
	}


	//Overload onPause and onResume to enable and disable the accelerometer
	//Sobrecargamos onPause y onResume para activar y desactivar el acelerómetro
	@Override
	protected void onPause()
	{
      mSensorManager.unregisterListener(mListener); 
      super.onPause();
	}


	@Override
	protected void onResume()
	{
      mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST); 		
		super.onResume();
	}

}
