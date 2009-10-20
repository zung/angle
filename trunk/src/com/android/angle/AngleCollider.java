package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

public abstract class AngleCollider
{
	protected AnglePhysicObject mObject;
	protected float mCenterX;
	protected float mCenterY;
	protected float mRadious;
	protected boolean mOnlyReceiveTest;
	
	AngleCollider()
	{
	}
	
	AngleCollider(float x, float y, float radious)
	{
		mCenterX=x;
		mCenterY=y;
		mRadious=radious;
		mOnlyReceiveTest=false;
	}

	//La normal del choque, me la devuelve el otherCollider
	protected abstract boolean test (AngleCollider otherCollider);
	protected abstract boolean havePoint (float pX, float pY);
	//devuelve la normal en relación a otro colisionador
	protected abstract float getNormal(AngleCollider relativeCollider);
	protected abstract void draw(GL10 gl);
	protected void collideWith(float collisionNormal, AngleCollider otherCollider)
	{
		mObject.kynetics(otherCollider.mObject,collisionNormal);
	}

}

