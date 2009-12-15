package com.android.angle;

import javax.microedition.khronos.opengles.GL11;

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
	public void drawFrame(GL11 gl)
	{
		for (int o = 0; o < mPhysicsEngine.mObjectsCount; o++)
			mPhysicsEngine.mObjects[o].draw(gl);
		super.drawFrame(gl);
	}
}
