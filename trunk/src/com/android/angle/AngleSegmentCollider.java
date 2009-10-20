package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

public class AngleSegmentCollider extends AngleCollider
{
	protected float mNormal;

	public AngleSegmentCollider(float x1, float y1, float x2, float y2)
	{
		float dX=x2-x1;
		float dY=y2-y1;
		float diameter=(float) Math.sqrt(dX*dX+dY*dY);
		mNormal=(float)Math.asin(dY/diameter);
		mRadious=diameter/2;
		mCenterX=(float) (x1+mRadious*Math.sin(mNormal));
		mCenterY=(float) (y1+mRadious*Math.cos(mNormal));
		mNormal-=(float)Math.PI/2;
		mOnlyReceiveTest=true;
	}

	@Override
	public boolean havePoint(float pX, float pY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean test(AngleCollider otherCollider)
	{
		//mOnlyReceiveTest=true so allways return false
		return false;
	    dirX = p2X - p1X;
	    dirY = p2Y - p1Y;
			diffX = cX - p1X;
			diffY = cY - p1Y;
			
			    float t = (diffX*dirX)+(diffY*dirY) / (dirX*dirX)+(dirY*dirY);
			    if (t < 0.0f)
			        t = 0.0f;
			    if (t > 1.0f)
			        t = 1.0f;
			    closestX = p1X + t * dirX;
			    closestY = p1Y + t * dirY;
			    dX = cX - closestX;
			    dY = cY - closestY;
			    float distsqr = (dX*dX)+(dY*dY);
			    return (distsqr <= r * r);

	}
	
	

	@Override
	//devuelve su normal sin mas. pasa del relativo
	protected float getNormal(AngleCollider relativeCollider)
	{
		return mNormal;
	}

	@Override
	protected void draw(GL10 gl)
	{
		// TODO Auto-generated method stub
		
	}
}
