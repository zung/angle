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
		// TODO Auto-generated method stub
		return false;
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
