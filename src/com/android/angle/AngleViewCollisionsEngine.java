package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Engine to draw colliders
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleViewCollisionsEngine extends AngleAbstractEngine
{
	AnglePhysicsGameEngine mPhysicsEngine;

	public AngleViewCollisionsEngine(AnglePhysicsGameEngine physicsEngine)
	{
		mPhysicsEngine = physicsEngine;
	}

	@Override
	public void afterLoadTextures(GL10 gl)
	{
	}

	@Override
	public void drawFrame(GL10 gl)
	{
		for (int o = 0; o < mPhysicsEngine.mObjectsCount; o++)
			mPhysicsEngine.mObjects[o].draw(gl);
	}

	@Override
	public void loadTextures(GL10 gl)
	{
	}

	@Override
	public void onDestroy(GL10 gl)
	{
	}
}
