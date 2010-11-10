package com.alt90.angle2;

import javax.microedition.khronos.opengles.GL10;

public class AngleScreenEraser extends AngleObject
{

	@Override
	public void draw(GL10 gl)
	{
      gl.glClearColor(0,0,0,1);
      gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	}

}
