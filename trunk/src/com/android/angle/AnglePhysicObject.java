package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class AnglePhysicObject extends AngleSpriteReference
{
	private static final int MAX_COLLIDERS = 10;
	public float mMass; //Masa
	public float mBounce; //Coefficient of restitution
	public float mVelocityX;
	public float mVelocityY;
	protected AngleCollider mColliders[];
	protected int mCollidersCount;
	public float dX; //Delta X
	public float dY; //Delta Y
	private int mFriction;
	
	public AnglePhysicObject(AngleSprite sprite)
	{
		super(sprite);
		mColliders=new AngleCollider[MAX_COLLIDERS];
		mCollidersCount=0;
		mBounce=1;
		mFriction=1;
		mMass=0; //Infinite mass
		mVelocityX=0;
		mVelocityY=0;
	}

	@Override
	public void draw(GL10 gl)
	{
		super.draw(gl);
		for (int mc=0;mc<mCollidersCount;mc++)
			mColliders[mc].draw(gl);
	}

	public void addCollider (AngleCollider collider)
	{
		if (mCollidersCount<MAX_COLLIDERS)
		{
			collider.mObject=this;
			mColliders[mCollidersCount++]=collider;
		}
	}

	public boolean test(AnglePhysicObject other)
	{
		for (int mc=0;mc<mCollidersCount;mc++)
		{
			if (!mColliders[mc].mOnlyReceiveTest)
			{
				for (int oc=0;oc<other.mCollidersCount;oc++)
				{
					if (mColliders[mc].test(other.mColliders[oc]))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public float getSurface()
	{
		return 0;
	}
	
	// |
	// |
	// | /
	// |/
	// |-------->
	// |\
	// | \
	// |  \
	// |   O

	public void kynetics(AnglePhysicObject other, float normal)
	{
		// TODO rotar el sistema -normal, hacer los cálculos (invertir vY) y volver a rotar +normal
	   float nCos=(float)Math.cos(normal);
	   float nSin=(float)Math.sin(normal);
	   
	   Log.v("Kynetics","Normal="+normal);
	   
	   float mVelX=mVelocityX*nCos-mVelocityY*nSin;
	   float mVelY=mVelocityY*nCos+mVelocityX*nSin;
	   float oVelX=other.mVelocityX*nCos-other.mVelocityY*nSin;
	   float oVelY=other.mVelocityY*nCos+other.mVelocityX*nSin;
	   
 	   float e=mBounce*other.mBounce;
	   float f=mFriction*other.mFriction;
 	   float momentum=mVelY*mMass+oVelY*other.mMass;
 	   float totalMass=mMass+other.mMass;
 	   float mFinalVelX=0f;
 	   float mFinalVelY=0f;
 	   float oFinalVelX=0f;
 	   float oFinalVelY=0f;
 	   if (mMass>0) //Mass is not infinite
 	   { 
 	   	//mFinalVelX=(momentum+other.mMass*e*(oVelX-mVelX))/totalMass; //wiki
 	   	mFinalVelY=((mMass-other.mMass*e)*mVelY+other.mMass*(1+e)*oVelY)/totalMass;
			//mFinalVelY=mVelY*(1/f)-oVelY*(1-(1/f)); //Friction
			mFinalVelX=mVelX;
 	   }
 	   
//	sX=((s->Mass-d->Mass*e)*sVel.X+d->Mass*(1+e)*dVel.X)/(s->Mass+d->Mass);
//   dX=(s->Mass*(1+e)*sVel.X+(d->Mass-s->Mass*e)*dVel.X)/(s->Mass+d->Mass);
   
 	   if (other.mMass>0) //Other mass is not infinite
 	   {
 	   	//oFinalVelX=(momentum+mMass*e*(mVelX-oVelX))/totalMass; //wiki
 	   	oFinalVelY=(mMass*(1+e)*mVelY+(other.mMass-mMass*e)*oVelY)/totalMass;
			//oFinalVelY=oVelY*(1/f)+mVelY*(1-(1/f)); //Friction
			oFinalVelX=oVelX;
 	   }

 	   Log.v("Kynetics","mFVX="+mFinalVelX);
 	   Log.v("Kynetics","mFVY="+mFinalVelY);
 	   Log.v("Kynetics","oFVX="+oFinalVelX);
 	   Log.v("Kynetics","oFVY="+oFinalVelY);

	   nCos=(float)Math.cos(-normal);
	   nSin=(float)Math.sin(-normal);
 	   
	   //devuelve el sistema a su sitio
	   mVelocityX=mFinalVelX*nCos-mFinalVelY*nSin;
	   mVelocityY=mFinalVelY*nCos+mFinalVelX*nSin;
	   other.mVelocityX=oFinalVelX*nCos-oFinalVelY*nSin;
	   other.mVelocityY=oFinalVelY*nCos+oFinalVelX*nSin;

	   Log.v("Kynetics","mVX="+mVelocityX);
 	   Log.v("Kynetics","mVY="+mVelocityY);
 	   Log.v("Kynetics","oVX="+other.mVelocityX);
 	   Log.v("Kynetics","oVY="+other.mVelocityY);
	}
}
