package com.android.game;

import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleVector;

public class Sight extends AngleSprite
{
	private boolean mAutofire;
	private Field mField;
	private float mReloadTO;
	private int mWeapon;
	private boolean[] mOneShot={false};
	private float[] mReload={0.2f};

	public Sight(AngleSpriteLayout layout, Field field)
	{
		super(layout);
		mPosition.set(160,140);
		mZ=0; //al frente
		mField=field;
	}

	public void moveTo(AngleVector mSight)
	{
		mPosition.set(160+mSight.mX*160,140+mSight.mY*140);
	}

	public void fire(boolean isDown)
	{
		mAutofire=isDown;
		if ((!isDown)&&mOneShot[mWeapon])
		{
			if (mReloadTO<=0)
			{
				mField.shotAt(mPosition,mWeapon);
				mReloadTO=mReload[mWeapon];
			}
		}
	}

	@Override
	public void step(float secondsElapsed)
	{
		mReloadTO-=secondsElapsed;
		if (mAutofire)
		{
			if (!mOneShot[mWeapon])
			{
				if (mReloadTO<=0)
				{
					mField.shotAt(mPosition,mWeapon);
					mReloadTO=mReload[mWeapon];
				}
			}
		}
		super.step(secondsElapsed);
	}

}
