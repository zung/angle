package com.android.angle;

public class AnglePhysicObject extends AngleSpriteReference
{
	private static final int MAX_COLLIDERS = 0;
	public float mMass;
	public float mVelocityX;
	public float mVelocityY;
	protected AngleCollider mColliders[];
	protected int mCollidersCount;
	
	public AnglePhysicObject(AngleSprite sprite)
	{
		super(sprite);
		mColliders=new AngleCollider[MAX_COLLIDERS];
		mCollidersCount=0;
	}
	
	public void addCollider (AngleCollider collider)
	{
		if (mCollidersCount<MAX_COLLIDERS)
		{
			collider.mObject=this;
			mColliders[mCollidersCount++]=collider;
		}
	}

	public void test(AnglePhysicObject other)
	{
		for (int mc=0;mc<mCollidersCount;mc++)
		{
			for (int oc=0;oc<other.mCollidersCount;oc++)
			{
				if (mColliders[mc].test(other.mColliders[oc]))
					return;
			}
		}
	}
}
