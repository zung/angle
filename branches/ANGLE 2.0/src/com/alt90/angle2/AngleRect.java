package com.alt90.angle2;

public class AngleRect
{
	public AngleVectorI fSize;
	public AngleVectorI fPosition;

	public AngleRect(int left, int top, int width, int height)
	{
		fPosition=new AngleVectorI(left, top);
		fSize=new AngleVectorI(width, height);
	}
}
