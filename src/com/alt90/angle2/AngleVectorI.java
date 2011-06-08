package com.alt90.angle2;

public class AngleVectorI
{
	public int fX;
	public int fY;

	public AngleVectorI()
	{
		set(0,0);
	}

	public AngleVectorI(int x, int y)
	{
		set (x,y);
	}

	public AngleVectorI(AngleVectorI src)
	{
		set(src);
	}

	public AngleVectorI(AngleVectorF src)
	{
		set(src);
	}

	public void set(AngleVectorI src)
	{
		fX = src.fX;
		fY = src.fY;
	}

	public void set(AngleVectorF src)
	{
		fX = (int) src.fX;
		fY = (int) src.fY;
	}

	public void set(int x, int y)
	{
		fX = x;
		fY = y;
	}

	/*
	 * 
	 * public float length() { return (float) Math.sqrt((mX * mX) + (mY * mY)); }
	 * 
	 * public void normalize() { float len = length();
	 * 
	 * if (len != 0.0f) { mX /= len; mY /= len; } else { mX = 0.0f; mY = 0.0f; }
	 * }
	 */
	public void add(AngleVectorI vector)
	{
		fX += vector.fX;
		fY += vector.fY;
	}

	/*
	 * public void add(float x, float y) { x += x; y += y; }
	 */
	public void sub(AngleVectorI vector)
	{
		fX -= vector.fX;
		fY -= vector.fY;
	}

	public void subAt(AngleVectorI vector)
	{
		fX = vector.fX - fX;
		fY = vector.fY - fY;
	}

	public void mul(AngleVectorI vector)
	{
		fX *= vector.fX;
		fY *= vector.fY;
	}

	public void div(AngleVectorI vector)
	{
		fX /= vector.fX;
		fY /= vector.fY;
	}

	/*
	 * public void sub(float x, float y) { x -= x; y -= y; }
	 * 
	 * public void mul(AngleVector vector) { mX *= vector.mX; mY *= vector.mY; }
	 * 
	 * public void mul(float x, float y) { x += x; y += y; }
	 */
	public void mul(float scalar)
	{
		fX *= scalar;
		fY *= scalar;
	}

	public void div(float scalar)
	{
		fX /= scalar;
		fY /= scalar;
	}

	public float dot(AngleVectorI vector)
	{
		return (fX * vector.fX) + (fY * vector.fY);
	}

	public boolean isZero()
	{
		return ((fX==0)&&(fY==0));
	}
}
