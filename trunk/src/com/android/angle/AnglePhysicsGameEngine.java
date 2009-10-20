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
	
	public AnglePhysicsGameEngine (int maxObjects, int maxTypes)
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
			mObjects[o].mVelocityX+=mObjects[o].mMass*mGravityX*AngleMainEngine.secondsElapsed;
			mObjects[o].mVelocityY+=mObjects[o].mMass*mGravityY*AngleMainEngine.secondsElapsed;
			if ((mObjects[o].mVelocityX!=0)||(mObjects[o].mVelocityY!=0))
			{
				//Air viscosity
				if (mViscosity>0)
				{
					float surface=mObjects[o].getSurface();
					if (surface>0)
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
				}
			}
			//Velocity
			mObjects[o].dX=mObjects[o].mVelocityX*AngleMainEngine.secondsElapsed;
			mObjects[o].dY=mObjects[o].mVelocityY*AngleMainEngine.secondsElapsed;
		}
		int steps=1;
		for (int o=0;o<mObjectsCount;o++)
		{
			int dX=(int) Math.abs(mObjects[o].dX);
			int dY=(int) Math.abs(mObjects[o].dX);
			if (dX>steps) steps=dX;
			if (dY>steps) steps=dY;
		}
		for (int s=0;s<steps;s++)
		{
			for (int o=0;o<mObjectsCount;o++)
			{
				if ((mObjects[o].dX!=0)||(mObjects[o].dY!=0))
				{
					//Collision
					mObjects[o].mX+=mObjects[o].dX/steps;
					mObjects[o].mY+=mObjects[o].dY/steps;
					for (int c=0;c<mObjectsCount;c++)
					{
						if (c!=o)
						{
							if (mObjects[o].test(mObjects[c]))
							{
								mObjects[o].mX-=mObjects[o].dX/steps;
								mObjects[o].mY-=mObjects[o].dY/steps;
								mObjects[o].dX=mObjects[o].mVelocityX*AngleMainEngine.secondsElapsed;
								mObjects[o].dY=mObjects[o].mVelocityY*AngleMainEngine.secondsElapsed;
								mObjects[c].dX=mObjects[c].mVelocityX*AngleMainEngine.secondsElapsed;
								mObjects[c].dY=mObjects[c].mVelocityY*AngleMainEngine.secondsElapsed;
								break;
							}
						}
					}
				}
			}
		}
	}
}
