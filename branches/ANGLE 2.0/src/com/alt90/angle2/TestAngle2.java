package com.alt90.angle2;

import android.os.Bundle;


public class TestAngle2 extends AngleActivity
{

	private class Logo extends AngleSprite
	{

		public Logo(AngleSpriteLayout layout)
		{
			super(240,160,layout);
		}

		@Override
		protected void step(float secondsElapsed)
		{
			fPosition.fX+=10*secondsElapsed;
			fPosition.fY+=5*secondsElapsed;
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AngleSpriteLayout slLogo=new AngleSpriteLayout(128,128,R.drawable.anglelogo);
		AngleObject myScene=new AngleObject();
		myScene.addObject(new AngleScreenEraser());
		myScene.addObject(new Logo(slLogo));
		AngleRenderer.setRenderTree(myScene);
	}
	
}