package com.android.game;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleVector;

public class Field extends AngleSprite
{
	private static final float sSmileySpawn = 1.3f;
	private AngleObject ogSmileys;
	private AngleObject ogFX;
	private float smileyTO;
	private GameUI mGame;

	public Field(GameUI game)
	{
		super(game.slField);
		mGame=game;
		mPosition.set(160,240);
		mZ=1; //al fondo

		//Grupo para los smileys
		ogSmileys=addObject(new AngleObject(100));
		//Y otro para los efectos.
		//Podria usar el mismo que los smileys, pero así dispondremos de un contenedor sólo para smileys
		ogFX=addObject(new AngleObject(100));
	}

	@Override
	public void step(float secondsElapsed)
	{
		smileyTO-=secondsElapsed;
		if (smileyTO<0)
		{
			ogSmileys.addObject(new Smiley(mGame));
			smileyTO=sSmileySpawn;
		}
		super.step(secondsElapsed);
	}

	public void moveTo(AngleVector mSight)
	{
		mPosition.set(160-mSight.mX*(256-160),240-mSight.mY*(256-240));
		for (int s=0;s<ogSmileys.count();s++)
			((Scrollable)ogSmileys.childAt(s)).place();
		for (int f=0;f<ogFX.count();f++)
			((Scrollable)ogFX.childAt(f)).place();
	}

	public void shotAt(AngleVector mSight, int mWeapon)
	{
		float sX=(256-mPosition.mX)+mSight.mX;
		float sY=(256-mPosition.mY)+mSight.mY;
		for (int s=0;s<ogSmileys.count();s++)
			((Smiley)ogSmileys.childAt(s)).shotAt(sX,sY);
	}

}
