package com.android.box2d;

public class Box2D
{ 
	public native static boolean 	createWorld (Vec2 gravity, boolean doSleep);

	public native static int 		createBody (BodyDef bodyDef);
	public native static void 		setBodyDef (int bodyId, BodyDef bodyDef);

	public native static int 		createPolygonShape	();
	public native static void 		setPolygonShapeAsBox(int shapeId, float hx, float hy);

	public native static void 		createFixture(int bodyId, int shapeId, int density);
	
   static { System.loadLibrary("box2d"); }

}
    