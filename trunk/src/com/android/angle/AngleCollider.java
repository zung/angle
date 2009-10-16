package com.android.angle;

public abstract class AngleCollider
{
	protected AnglePhysicObject mObject;
	protected int mCenterX;
	protected int mCenterY;
	
	AngleCollider(int x, int y)
	{
		mCenterX=x;
		mCenterY=y;
	}

	public abstract boolean test (AngleCollider otherCollider);
	public abstract boolean havePoint (float pX, float pY);
	
}

