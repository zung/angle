package com.android.angle;

public abstract class AngleVisible
{
   // Position.
   public float mX;
   public float mY;
   public float mZ;
   
   // Velocity.
   public float velocityX;
   public float velocityY;
   public float velocityZ;
   
   // Size.
   public int mWidth;
   public int mHeight;
   
   AngleVisible (int width, int height)
   {
   	mWidth=width;
   	mHeight=height;
   }
}
