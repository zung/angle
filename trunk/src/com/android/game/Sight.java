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
	private float mShotTO;

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

	private void shot()
	{
		setFrame(1);
		mShotTO=0.05f;
		mField.shotAt(mPosition,mWeapon);
		mReloadTO=mReload[mWeapon];
	}

	public void fire(boolean isDown)
	{
		mAutofire=isDown;
		if ((!isDown)&&mOneShot[mWeapon])
		{
			if (mReloadTO<=0)
				shot();
		}
	}

	@Override
	public void step(float secondsElapsed)
	{
		mReloadTO-=secondsElapsed;
		if (mShotTO>0)
		{
			mShotTO-=secondsElapsed;
			if (mShotTO<0)
			{
				setFrame(0);
				mShotTO=0;
			}
		}
		if (mAutofire)
		{
			if (!mOneShot[mWeapon])
			{
				if (mReloadTO<=0)
					shot();
			}
		}
		super.step(secondsElapsed);
	}

}
