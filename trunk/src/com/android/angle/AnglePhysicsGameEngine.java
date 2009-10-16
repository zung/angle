package com.android.angle;

public class AnglePhysicsGameEngine extends AngleAbstractGameEngine
{
	private AngleSpritesEngine mSprites; 
	private int mMaxObjects;
	private int mObjectsCount;
	AnglePhysicObject[] mObjects;
	public float mGravityX;
	public float mGravityY;
	public float mViscosity;
	
	AnglePhysicsGameEngine (int maxObjects, int maxTypes)
	{
		mMaxObjects=maxObjects;
		mObjectsCount=0;
		mObjects = new AnglePhysicObject[mMaxObjects]; 
		mSprites = new AngleSpritesEngine(maxTypes, maxObjects); 
		AngleMainEngine.addEngine(mSprites); 
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
	
	@Override
	public void run()
	{
		for (int o=0;o<mObjectsCount;o++)
		{
			//Gravity
			mObjects[o].mVelocityX+=mGravityX*AngleMainEngine.secondsElapsed;
			mObjects[o].mVelocityY+=mGravityY*AngleMainEngine.secondsElapsed;
			//Air viscosity
			if (mViscosity>0)
			{
				float decay=mViscosity*AngleMainEngine.secondsElapsed;
				if (mObjects[o].mVelocityX>decay)
					mObjects[o].mVelocityX-=decay;
				else if (mObjects[o].mVelocityX<-decay)
					mObjects[o].mVelocityX+=decay;
				else
					mObjects[o].mVelocityX=0;
				if (mObjects[o].mVelocityY>decay)
					mObjects[o].mVelocityY-=decay;
				else if (mObjects[o].mVelocityY<-decay)
					mObjects[o].mVelocityY+=decay;
				else
					mObjects[o].mVelocityY=0;
			}
			//Velocity
			float dX=mObjects[o].mVelocityX*AngleMainEngine.secondsElapsed;
			float dY=mObjects[o].mVelocityY*AngleMainEngine.secondsElapsed;
			//Collision
			int steps=(int) ((dX>dY)?dX:dY);
			if (steps<1)
				steps=1;
			for (int s=0;s<steps;s++)
			{
				mObjects[o].mX+=dX/steps;
				mObjects[o].mY+=dY/steps;
				for (int c=0;c<mObjectsCount;c++)
				{
					if (c!=o)
						mObjects[o].test(mObjects[c]);
				}
			}
		}
	}
}
