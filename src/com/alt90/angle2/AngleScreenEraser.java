package com.alt90.angle2;

import javax.microedition.khronos.opengles.GL10;

public class AngleScreenEraser extends AngleObject
{
	private float lR;
	private float lG;
	private float lB;
	
	public AngleScreenEraser()
	{
		super();
		lR=0;
		lG=0;
		lB=0;
	}

	public AngleScreenEraser(float r, float g, float b)
	{
		super();
		lR=r;
		lG=g;
		lB=b;
	}

	@Override
	public void draw(GL10 gl)
	{
      gl.glClearColor(lR,lG,lB,1);
      gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	}

}
