package com.android.ace;

/**
 * 2D vector
 * 
 * @author Ivan Pajuelo
 * 
 */
public class ACEVector
{
	public float mX;
	public float mY;

	public ACEVector()
	{
		mX = 0.0f;
		mY = 0.0f;
	}

	public ACEVector(float x, float y)
	{
		mX = x;
		mY = y;
	}

	public ACEVector(ACEVector src)
	{
		mX = src.mX;
		mY = src.mY;
	}

	public void set(ACEVector src)
	{
		mX = src.mX;
		mY = src.mY;
	}

	public void set(float x, float y)
	{
		mX = x;
		mY = y;
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
	public void add(ACEVector vector)
	{
		mX += vector.mX;
		mY += vector.mY;
	}

	/*
	 * public void add(float x, float y) { x += x; y += y; }
	 */
	public void sub(ACEVector vector)
	{
		mX -= vector.mX;
		mY -= vector.mY;
	}

	public void subAt(ACEVector vector)
	{
		mX = vector.mX - mX;
		mY = vector.mY - mY;
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
		mX *= scalar;
		mY *= scalar;
	}

	public float dot(ACEVector vector)
	{
		return (mX * vector.mX) + (mY * vector.mY);
	}

	public void rotate(float dAlfa)
	{
		float nCos = (float) Math.cos(dAlfa);
		float nSin = (float) Math.sin(dAlfa);

		float iX = mX * nCos - mY * nSin;
		float iY = mY * nCos + mX * nSin;

		mX = iX;
		mX = iY;
	}

}
