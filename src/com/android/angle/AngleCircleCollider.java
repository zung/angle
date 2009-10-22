package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class AngleCircleCollider 
{
	protected AnglePhysicObject mObject;
	protected AngleVector mCenter;
	protected float mRadious;
	protected float mNormal;

	public AngleCircleCollider(float x, float y, float radious)
	{
		mCenter=new AngleVector (x,y);
		mRadious=radious;
	}
	
	public boolean test(AngleCircleCollider otherCollider)
	{
		float dX=(otherCollider.mObject.mVisual.mCenter.mX+otherCollider.mCenter.mX)-(mObject.mVisual.mCenter.mX+mCenter.mX);
		float dY=(otherCollider.mObject.mVisual.mCenter.mY+otherCollider.mCenter.mY)-(mObject.mVisual.mCenter.mY+mCenter.mY);
		float dist=(float)Math.sqrt(dX*dX+dY*dY);
		//The normal in a circle is the direction to the center of the other collider
		if (dX>0)
			otherCollider.mNormal=(float)Math.acos(dY/dist);
		else
			otherCollider.mNormal=(float)(Math.PI*2-Math.acos(dY/dist));

		return (dist<mRadious+otherCollider.mRadious);
	}

	public boolean test(AngleSegmentCollider otherCollider)
	{
		return otherCollider.closestDist(this)<mRadious;
	}

	protected void draw(GL10 gl)
	{
		final int segments=20;
		
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glColor4f(0f, 1f, 0f, 1f);
	 	gl.glTranslatef(mObject.mVisual.mCenter.mX+mCenter.mX, mObject.mVisual.mCenter.mY+mCenter.mY, 0.0f);
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
	 	gl.glPopMatrix();

	 	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

}
