package com.android.angle;

public class AngleRectCollider extends AngleCollider
{
	protected int mWidth;
	protected int mHeight;

	AngleRectCollider(int x, int y, int width, int height)
	{
		super(x, y);
		mWidth=width;
		mHeight=height;
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
}
