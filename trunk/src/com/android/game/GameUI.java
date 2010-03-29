package com.android.game;

import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleUI;
import com.android.angle.AngleVector;

public class GameUI extends AngleUI
{
	//PASO 14:
	//Este será el punto de entrada
	//Aqui inicializaremos todas la variables que necesitemos cuando se acabe de activar esta UI
	@Override
	public void onActivate()
	{
		super.onActivate();
	}

	private static final float sSightSpeed = 10;
	private Field mField;
	private Sight mSight;
	private AngleObject ogDashboard;
	private AngleSprite sprTrackPad;
	private AngleVector mAim;
	private AngleVector mTrackPadPos;
	private AngleVector mTrackPadDelta;
	
	public GameUI(AngleActivity activity)
	{
		super(activity);
		
		//PASO 15:
		//Creación de los objetos de juego
		//Aquí usamos la forma de inserción rápida
		//Para el campo de batalla,
		mField=(Field)addObject(new Field(new AngleSpriteLayout(mActivity.mGLSurfaceView,com.android.tutorial.R.drawable.fondo)));
		//El punto de mira
		mSight=(Sight)addObject(new Sight(new AngleSpriteLayout(mActivity.mGLSurfaceView,com.android.tutorial.R.drawable.mira),mField));
		//y un grupo para marcadores y demás cosas flotantes
		ogDashboard=addObject(new AngleObject());
		sprTrackPad=(AngleSprite) ogDashboard.addObject(new AngleSprite(160,380,new AngleSpriteLayout(mActivity.mGLSurfaceView,320,200,com.android.tutorial.R.drawable.panel)));
		mAim=new AngleVector();
		mTrackPadPos=new AngleVector();
		mTrackPadDelta=new AngleVector();
	}

	@Override
	public void step(float secondsElapsed)
	{
		super.step(secondsElapsed);
		mSight.moveTo(mTrackPadPos);
		mTrackPadDelta.set(mTrackPadPos);
		mTrackPadDelta.mul(sSightSpeed*secondsElapsed);
		mAim.add(mTrackPadDelta);
		if (mAim.mX>1)
			mAim.mX=1;
		if (mAim.mX<-1)
			mAim.mX=-1;
		if (mAim.mY>1)
			mAim.mY=1;
		if (mAim.mY<-1)
			mAim.mY=-1;
		mField.moveTo(mAim);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float eX=event.getX();
		float eY=event.getY();
		if (event.getAction()==MotionEvent.ACTION_UP)
		{
			mSight.fire(false);
			mTrackPadPos.set(0,0);
		}
		else if (event.getAction()==MotionEvent.ACTION_DOWN)
		{
			mSight.fire(true);
		}
		else if (event.getAction()==MotionEvent.ACTION_MOVE)
		{
			if (eX>sprTrackPad.mPosition.mX-sprTrackPad.roLayout.roWidth/2)
				if (eY>sprTrackPad.mPosition.mY-sprTrackPad.roLayout.roHeight/2)
					if (eX<sprTrackPad.mPosition.mX+sprTrackPad.roLayout.roWidth/2)
						if (eY<sprTrackPad.mPosition.mY+sprTrackPad.roLayout.roHeight/2)
							mTrackPadPos.set((eX-sprTrackPad.mPosition.mX)/(sprTrackPad.roLayout.roWidth/2),(eY-sprTrackPad.mPosition.mY)/(sprTrackPad.roLayout.roHeight/2));
		}
		return true;
	}
	
}
