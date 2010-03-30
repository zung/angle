package com.android.game;

import android.graphics.Typeface;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
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
	Field mField;
	private Sight mSight;
	private AngleObject ogDashboard;
	private AngleSprite sprTrackPad;
	private AngleVector mAim;
	private AngleVector mTrackPadPos;
	private AngleVector mTrackPadDelta;
	private boolean mInverted;
	private int mScore;
	private int mLifes;
	private AngleString strScore;
	private AngleString strLifes;
	public AngleSpriteLayout slSmiley;
	public AngleSpriteLayout slField;
	
	public GameUI(AngleActivity activity)
	{
		super(activity);
		
		slSmiley=new AngleSpriteLayout(mActivity.mGLSurfaceView,32,45,com.android.tutorial.R.drawable.salto,0,0,32,45,8,8); 
		slField=new AngleSpriteLayout(mActivity.mGLSurfaceView,com.android.tutorial.R.drawable.fondo);

		//PASO 15:
		//Creación de los objetos de juego
		//Aquí usamos la forma de inserción rápida
		//Para el campo de batalla,
		mField=(Field)addObject(new Field(this));
		//El punto de mira
		mSight=(Sight)addObject(new Sight(new AngleSpriteLayout(mActivity.mGLSurfaceView,32,32,com.android.tutorial.R.drawable.mira,0,0,32,32,2,2),mField));
		//y un grupo para marcadores y demás cosas flotantes
		ogDashboard=addObject(new AngleObject());
		sprTrackPad=(AngleSprite) ogDashboard.addObject(new AngleSprite(160,380,new AngleSpriteLayout(mActivity.mGLSurfaceView,320,200,com.android.tutorial.R.drawable.panel)));

		AngleFont fntBazaronite=new AngleFont(mActivity.mGLSurfaceView, 18, Typeface.createFromAsset(mActivity.getAssets(),"bazaronite.ttf"), 222, 0, 2, 255, 100, 255, 255);

		strScore = (AngleString) ogDashboard.addObject(new AngleString(fntBazaronite, "0", 310, 30, AngleString.aRight));
		strLifes = (AngleString) ogDashboard.addObject(new AngleString(fntBazaronite, "0", 60, 30, AngleString.aRight));
		
		mAim=new AngleVector();
		mTrackPadPos=new AngleVector();
		mTrackPadDelta=new AngleVector();
		mInverted=false;
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
						{
							if (mInverted)
								mTrackPadPos.set(-(eX-sprTrackPad.mPosition.mX)/(sprTrackPad.roLayout.roWidth/2),-(eY-sprTrackPad.mPosition.mY)/(sprTrackPad.roLayout.roHeight/2));
							else
								mTrackPadPos.set((eX-sprTrackPad.mPosition.mX)/(sprTrackPad.roLayout.roWidth/2),(eY-sprTrackPad.mPosition.mY)/(sprTrackPad.roLayout.roHeight/2));
						}
		}
		return true;
	}

	public void updateLifes(int lifes)
	{
		mLifes+=lifes;
		strLifes.set(""+mLifes);
	}

	public void updateScore(int score)
	{
		mScore+=score;
		strScore.set(""+mScore);
	}
	
}
