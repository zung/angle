package com.alt90.angle2;

public class AngleVectorF
{
	public float fX;
	public float fY;

	public AngleVectorF()
	{
		set(0,0);
	}

	public AngleVectorF(float x, float y)
	{
		set(x,y);
	}

	public AngleVectorF(AngleVectorF src)
	{
		set(src);
	}

	public AngleVectorF(AngleVectorI src)
	{
		set(src);
	}

	public void set(AngleVectorF src)
	{
		fX = src.fX;
		fY = src.fY;
	}

	public void set(AngleVectorI src)
	{
		fX = src.fX;
		fY = src.fY;
	}

	public void set(float x, float y)
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
	public void add(AngleVectorF vector)
	{
		fX += vector.fX;
		fY += vector.fY;
	}

	public void add(AngleVectorI vector)
	{
		fX += vector.fX;
		fY += vector.fY;
	}

	/*
	 * public void add(float x, float y) { x += x; y += y; }
	 */
	public void sub(AngleVectorF vector)
	{
		fX -= vector.fX;
		fY -= vector.fY;
	}

	public void sub(AngleVectorI vector)
	{
		fX -= vector.fX;
		fY -= vector.fY;
	}

	public void subAt(AngleVectorF vector)
	{
		fX = vector.fX - fX;
		fY = vector.fY - fY;
	}

	public void mul(AngleVectorF vector)
	{
		fX *= vector.fX;
		fY *= vector.fY;
	}

	public void mul(AngleVectorI vector)
	{
		fX *= vector.fX;
		fY *= vector.fY;
	}

	public void div(AngleVectorF vector)
	{
		fX /= vector.fX;
		fY /= vector.fY;
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

	public float dot(AngleVectorF vector)
	{
		return (fX * vector.fX) + (fY * vector.fY);
	}

	public void rotate(float dAlfa)
	{
		float nCos = (float) Math.cos(dAlfa);
		float nSin = (float) Math.sin(dAlfa);

		float iX = fX * nCos - fY * nSin;
		float iY = fY * nCos + fX * nSin;

		fX = iX;
		fX = iY;
	}

	public boolean isZero()
	{
		return ((fX==0)&&(fY==0));
	}
}
