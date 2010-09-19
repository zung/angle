package com.android.angle;

public class Box2D
{ 
	public native static boolean createWorld (float gravityX, float gravityY, boolean doSleep);
	
   static { System.loadLibrary("box2d"); } 
}
