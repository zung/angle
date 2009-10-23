package com.android.angle;

public class AnglePhysicsGameEngine extends AngleAbstractGameEngine
{
	protected int mMaxObjects;
	protected int mObjectsCount;
	protected AnglePhysicObject[] mObjects;
	public AngleVector mGravity;
	public float mViscosity;
	
	public AnglePhysicsGameEngine (int maxTypes, int maxObjects)
	{
		mMaxObjects=maxObjects;
		mObjectsCount=0;
		mObjects = new AnglePhysicObject[mMaxObjects];
		mGravity=new AngleVector();
		mViscosity=0;
	}

	public void addObject(AnglePhysicObject object)
	{
		if (mObjectsCount<mMaxObjects)
			mObjects[mObjectsCount++]=object;
	}
	
	public void removeObject (AnglePhysicObject object)
	{
		int o;
		
		for (o = 0; o < mObjectsCount; o++)
			if (mObjects[o] == object)
				break;

		if (o < mObjectsCount)
		{
			mObjectsCount--;
			for (int d=o;d<mObjectsCount;d++)
				mObjects[d]=mObjects[d+1];
			mObjects[mObjectsCount]=null;
		}
		
	}

	protected void physics()
	{
		for (int o=0;o<mObjectsCount;o++)
		{
			//Gravity
			mObjects[o].mVelocity.mX+=mObjects[o].mMass*mGravity.mX*AngleMainEngine.secondsElapsed;
			mObjects[o].mVelocity.mY+=mObjects[o].mMass*mGravity.mY*AngleMainEngine.secondsElapsed;
			if ((mObjects[o].mVelocity.mX!=0)||(mObjects[o].mVelocity.mY!=0))
			{
				//Air viscosity
				if (mViscosity>0)
				{
					float surface=mObjects[o].getSurface();
					if (surface>0)
					{
					float decay=surface*mViscosity*AngleMainEngine.secondsElapsed;
					if (mObjects[o].mVelocity.mX>decay)
						mObjects[o].mVelocity.mX-=decay;
					else if (mObjects[o].mVelocity.mX<-decay)
						mObjects[o].mVelocity.mX+=decay;
					else
						mObjects[o].mVelocity.mX=0;
					if (mObjects[o].mVelocity.mY>decay)
						mObjects[o].mVelocity.mY-=decay;
					else if (mObjects[o].mVelocity.mY<-decay)
						mObjects[o].mVelocity.mY+=decay;
					else
						mObjects[o].mVelocity.mY=0;
					}
				}
			}
			//Velocity
			mObjects[o].mDelta.mX=mObjects[o].mVelocity.mX*AngleMainEngine.secondsElapsed;
			mObjects[o].mDelta.mY=mObjects[o].mVelocity.mY*AngleMainEngine.secondsElapsed;
		}
	}
/*	
	protected void kynetics ()
	{
		int steps=1;
		
		for (int o=0;o<mObjectsCount;o++)
		{
			int dX=(int) Math.abs(mObjects[o].mDelta.mX);
			int dY=(int) Math.abs(mObjects[o].mDelta.mX);
			if (dX>steps) steps=dX;
			if (dY>steps) steps=dY;
		}
		
		for (int s=0;s<steps;s++)
		{
			for (int o=0;o<mObjectsCount;o++)
			{
				if ((mObjects[o].mDelta.mX!=0)||(mObjects[o].mDelta.mY!=0))
				{
					//Collision
					mObjects[o].mVisual.mCenter.mX+=mObjects[o].mDelta.mX/steps;
					mObjects[o].mVisual.mCenter.mY+=mObjects[o].mDelta.mY/steps;
					for (int c=0;c<mObjectsCount;c++)
					{
						if (c!=o)
						{
							if (mObjects[o].collide(mObjects[c]))
							{
								mObjects[o].mVisual.mCenter.mX-=mObjects[o].mDelta.mX/steps;
								mObjects[o].mVisual.mCenter.mY-=mObjects[o].mDelta.mY/steps;
								mObjects[o].mDelta.mX=mObjects[o].mVelocity.mX*AngleMainEngine.secondsElapsed;
								mObjects[o].mDelta.mY=mObjects[o].mVelocity.mY*AngleMainEngine.secondsElapsed;
								mObjects[c].mDelta.mX=mObjects[c].mVelocity.mX*AngleMainEngine.secondsElapsed;
								mObjects[c].mDelta.mY=mObjects[c].mVelocity.mY*AngleMainEngine.secondsElapsed;
								break;
							}
						}
					}
				}
			}
		}
	}
*/	
	protected void kynetics ()
	{
		for (int o=0;o<mObjectsCount;o++)
		{
			if ((mObjects[o].mDelta.mX!=0)||(mObjects[o].mDelta.mY!=0))
			{
				//Collision
				mObjects[o].mVisual.mCenter.mX+=mObjects[o].mDelta.mX;
				mObjects[o].mVisual.mCenter.mY+=mObjects[o].mDelta.mY;
				for (int c=0;c<mObjectsCount;c++)
				{
					if (c!=o)
					{
						if (mObjects[o].collide(mObjects[c]))
						{
							mObjects[o].mVisual.mCenter.mX-=mObjects[o].mDelta.mX;
							mObjects[o].mVisual.mCenter.mY-=mObjects[o].mDelta.mY;
							mObjects[c].mDelta.mX=mObjects[c].mVelocity.mX*AngleMainEngine.secondsElapsed;
							mObjects[c].mDelta.mY=mObjects[c].mVelocity.mY*AngleMainEngine.secondsElapsed;
							break;
						}
					}
				}
			}
		}
	}
	@Override
	public void run()
	{
		physics();
		kynetics();
	}
}
