package com.android.angle;

public abstract class AngleVisible
{
	public float mX;
	public float mY;
	public float mZ;

	public int mWidth;
	public int mHeight;

	AngleVisible(int width, int height)
	{
		mWidth = width;
		mHeight = height;
		mX = 0.0f;
		mY = 0.0f;
		mZ = 0.0f;
	}
}
