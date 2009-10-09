package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

public abstract class AngleRenderer
{
	public abstract void drawFrame(GL10 gl);
	public abstract void loadTextures ();
	public abstract void shutdown();
}
