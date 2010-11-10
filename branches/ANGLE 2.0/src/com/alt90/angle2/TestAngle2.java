package com.alt90.angle2;

import android.os.Bundle;


public class TestAngle2 extends AngleActivity
{

	private class Logo extends AngleSprite
	{

		public Logo(AngleSpriteLayout layout)
		{
			super(AngleRenderer.rViewportExtent_uu.fX/2,0,layout);
		}

		@Override
		protected void step(float secondsElapsed)
		{
			if (iPointer[0].isDown)
			{
				fPosition_uu.fX=iPointer[0].fPosition_uu.fX;
				fPosition_uu.fY=iPointer[0].fPosition_uu.fY-150;
			}
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AngleSpriteLayout slLogo=new AngleSpriteLayout(128,128,R.drawable.anglelogo);
		AngleObject myScene=new AngleObject(30);
		myScene.addObject(new AngleScreenEraser(0.2f,0.2f,0.2f));
		myScene.addObject(new Logo(slLogo));
		myScene.addObject(new AngleFPSCounter());
		AngleRenderer.setRenderTree(myScene);
	}
	
}