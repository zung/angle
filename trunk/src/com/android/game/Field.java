package com.android.game;

import android.util.Log;

import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleVector;

public class Field extends AngleSprite
{
	private AngleObject ogSmileys;
	private AngleObject ogFX;
	private AngleVector mShotPoint;

	public Field(AngleSpriteLayout layout)
	{
		super(layout);
		mPosition.set(160,240);
		mZ=1; //al fondo

		//Grupo para los smileys
		ogSmileys=addObject(new AngleObject());
		//Y otro para los efectos.
		//Podria usar el mismo que los smileys, pero así dispondremos de un contenedor sólo para smileys
		ogFX=addObject(new AngleObject());
		
		mShotPoint=new AngleVector();
	}

	public void moveTo(AngleVector mSight)
	{
		mPosition.set(160-mSight.mX*(256-160),240-mSight.mY*(256-240));
	}

	public void shotAt(AngleVector mSight, int mWeapon)
	{
		mShotPoint.set(256-mPosition.mX+mSight.mX,256-mPosition.mY+mSight.mY);
		Log.d("Field","shotAt "+mShotPoint.mX+", "+mShotPoint.mY);
		
	}

}
