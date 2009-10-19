package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class AngleCircleCollider extends AngleCollider
{
//	private float drawLineX;
//	private float drawLineY;

	public AngleCircleCollider(float x, float y, float radious)
	{
		super(x, y, radious);
	}
	
	@Override
	//La normal del choque, me la devuelve el otherCollider
	public boolean test(AngleCollider otherCollider)
	{
		float normal=(float) (otherCollider.getNormal(this));
		//Find the closest point to the center of the other collider
		float cpX=(float) (mObject.mX+mCenterX+mRadious*Math.sin(Math.PI+normal));
		float cpY=(float) (mObject.mY+mCenterY+mRadious*Math.cos(Math.PI+normal));
		//----------------------------------------------------------
	
		if (otherCollider.havePoint(cpX,cpY))
		{
			collideWith(normal,otherCollider);  
			return true;
		}
		return false;
	}

	@Override
	public boolean havePoint(float pX, float pY)
	{
		float dX=mObject.mX+mCenterX-pX;
		float dY=mObject.mY+mCenterY-pY;
		return (Math.sqrt(dX*dX+dY*dY)<=mRadious);
	}

	@Override
	protected float getNormal(AngleCollider relativeCollider)
	{
		float dX=(relativeCollider.mObject.mX+relativeCollider.mCenterX)-(mObject.mX+mCenterX);
		float dY=(relativeCollider.mObject.mY+relativeCollider.mCenterY)-(mObject.mY+mCenterY);
		float normal;
		//The normal in a circle is the direction to the center of the other collider
		if (dX>0)
			normal=(float)Math.acos(dY/Math.sqrt(dX*dX+dY*dY));
		else
			normal=(float)(Math.PI*2-Math.acos(dY/Math.sqrt(dX*dX+dY*dY)));
//		drawLineX=(float) (mRadious*Math.sin(normal));
//		drawLineY=(float) (mRadious*Math.cos(normal));
		return normal;
	}

	@Override
	protected void draw(GL10 gl)
	{
		final int segments=20;
		
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glColor4f(0f, 1f, 0f, 1f);
	 	gl.glTranslatef(mObject.mX+mCenterX, mObject.mY+mCenterY, 0.0f);
	 	FloatBuffer vertices;
		vertices = ByteBuffer.allocateDirect(segments*2*4)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();

 		int count=0;
 		for (float i = 0; i < Math.PI*2; i+=((Math.PI*2)/segments))
 		{
  			vertices.put(count++,(float) (Math.cos(i)*mRadious));
  			vertices.put(count++,(float) (Math.sin(i)*mRadious));
 		}
 		gl.glVertexPointer (2, GL10.GL_FLOAT , 0, vertices);
 		gl.glDrawArrays (GL10.GL_LINE_LOOP, 0, segments);
/*
 		gl.glLoadIdentity();
		gl.glColor4f(1f, 0f, 0f, 1f);
	 	gl.glTranslatef(mObject.mX+mCenterX, mObject.mY+mCenterY, 0.0f);
	 	vertices.clear();
	 	count=0;
		vertices.put(count++,0);
		vertices.put(count++,0);
		vertices.put(count++,drawLineX);
		vertices.put(count++,drawLineY);
 		gl.glVertexPointer (2, GL10.GL_FLOAT , 0, vertices);
		gl.glDrawArrays (GL10.GL_LINES, 0, 2);
		*/
	 	gl.glPopMatrix();

	 	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}
}
