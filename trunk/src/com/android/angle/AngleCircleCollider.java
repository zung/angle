package com.android.angle;

public class AngleCircleCollider extends AngleCollider
{
	protected int mRadious;

	AngleCircleCollider(int x, int y, int radious)
	{
		super(x, y);
		mRadious=radious;
	}
	
	@Override
	public boolean test(AngleCollider otherCollider)
	{
		//Find the closest point to the center of the other collider
		float dX=(mObject.mX+mCenterX)-(otherCollider.mObject.mX+otherCollider.mCenterX);
		float dY=(mObject.mY+mCenterY)-(otherCollider.mObject.mY+otherCollider.mCenterY);
		double a=Math.asin(dY/Math.sqrt(dX*dX+dY*dY));
		float cpX=(float) (mRadious*Math.sin(a));
		float cpY=(float) (mRadious*Math.cos(a));
		//----------------------------------------------------------

		if (otherCollider.havePoint(cpX,cpY))
		{
			//Collision. Modify physics
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
}
