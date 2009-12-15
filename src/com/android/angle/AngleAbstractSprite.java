package com.android.angle;

import javax.microedition.khronos.opengles.GL11;

/**
 * References base class
 * 
 * @author Ivan Pajuelo
 * 
 */
public abstract class AngleAbstractSprite extends AngleVisualObject
{
	public void onDestroy(GL11 gl)
	{
	}

	public void createBuffers(GL11 gl)
	{
	}

	public void afterLoadTexture(GL11 gl)
	{
	}
}
