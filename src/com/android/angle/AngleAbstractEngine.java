package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

public abstract class AngleAbstractEngine
{
	public abstract void drawFrame(GL10 gl);

	public abstract void loadTextures(GL10 gl);

	public abstract void onDestroy(GL10 gl);

	public abstract void afterLoadTextures(GL10 gl);
}
