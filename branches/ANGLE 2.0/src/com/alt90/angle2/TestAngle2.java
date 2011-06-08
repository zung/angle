package com.alt90.angle2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;


public class TestAngle2 extends AngleActivity
{
	private AngleString fDsp;
	private AngleFPSCounter fFPS;
	
	private class Logo extends AngleSpriteRotable
	{
		public Logo(AngleSpriteLayout layout)
		{
			super(AngleRenderer.rViewportExtent_uu.fX/2,0,layout);
		}

		@Override
		protected void step(float secondsElapsed)
		{
			//fDsp.set(String.format("%.3f", fFPS.fFPS));
			if (iKeys[KeyEvent.KEYCODE_BACK])
				finish();
			if (iFling[0].newData)
			{
				iFling[0].newData=false;
				String aa="T="+iFling[0].fTime+", X="+iFling[0].fDelta_uu.fX+", Y="+iFling[0].fDelta_uu.fY;
				Log.d("AE2",aa);
				fDsp.set(aa);
			}
			fRotation+=90*secondsElapsed;
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
		AngleFont fntCafe=new AngleFont(25, Typeface.createFromAsset(getAssets(),"cafe.ttf"), 222, 0, 0, 30, 200, 255, 255);
		fntCafe.saveTo("cafe.fnt");

		AngleObject myScene=new AngleObject(30);
		myScene.addObject(new AngleScreenEraser());
		myScene.addObject(new AngleLine(AngleRenderer.rViewportExtent_uu.fX/8,0,AngleRenderer.rViewportExtent_uu.fX/8,AngleRenderer.rViewportExtent_uu.fY,AngleColor.cMaroon));
		myScene.addObject(new AngleLine(0,AngleRenderer.rViewportExtent_uu.fY/8,AngleRenderer.rViewportExtent_uu.fX,AngleRenderer.rViewportExtent_uu.fY/8,AngleColor.cMaroon));
		myScene.addObject(new AngleLine(AngleRenderer.rViewportExtent_uu.fX/8*3,0,AngleRenderer.rViewportExtent_uu.fX/8*3,AngleRenderer.rViewportExtent_uu.fY,AngleColor.cMaroon));
		myScene.addObject(new AngleLine(0,AngleRenderer.rViewportExtent_uu.fY/8*3,AngleRenderer.rViewportExtent_uu.fX,AngleRenderer.rViewportExtent_uu.fY/8*3,AngleColor.cMaroon));
		myScene.addObject(new AngleLine(AngleRenderer.rViewportExtent_uu.fX/8*5,0,AngleRenderer.rViewportExtent_uu.fX/8*5,AngleRenderer.rViewportExtent_uu.fY,AngleColor.cMaroon));
		myScene.addObject(new AngleLine(0,AngleRenderer.rViewportExtent_uu.fY/8*5,AngleRenderer.rViewportExtent_uu.fX,AngleRenderer.rViewportExtent_uu.fY/8*5,AngleColor.cMaroon));
		myScene.addObject(new AngleLine(AngleRenderer.rViewportExtent_uu.fX/8*7,0,AngleRenderer.rViewportExtent_uu.fX/8*7,AngleRenderer.rViewportExtent_uu.fY,AngleColor.cMaroon));
		myScene.addObject(new AngleLine(0,AngleRenderer.rViewportExtent_uu.fY/8*7,AngleRenderer.rViewportExtent_uu.fX,AngleRenderer.rViewportExtent_uu.fY/8*7,AngleColor.cMaroon));
		myScene.addObject(new AngleLine(AngleRenderer.rViewportExtent_uu.fX/4,0,AngleRenderer.rViewportExtent_uu.fX/4,AngleRenderer.rViewportExtent_uu.fY,AngleColor.cNavy));
		myScene.addObject(new AngleLine(0,AngleRenderer.rViewportExtent_uu.fY/4,AngleRenderer.rViewportExtent_uu.fX,AngleRenderer.rViewportExtent_uu.fY/4,AngleColor.cNavy));
		myScene.addObject(new AngleLine(AngleRenderer.rViewportExtent_uu.fX/4*3,0,AngleRenderer.rViewportExtent_uu.fX/4*3,AngleRenderer.rViewportExtent_uu.fY,AngleColor.cNavy));
		myScene.addObject(new AngleLine(0,AngleRenderer.rViewportExtent_uu.fY/4*3,AngleRenderer.rViewportExtent_uu.fX,AngleRenderer.rViewportExtent_uu.fY/4*3,AngleColor.cNavy));
		myScene.addObject(new AngleLine(AngleRenderer.rViewportExtent_uu.fX/2,0,AngleRenderer.rViewportExtent_uu.fX/2,AngleRenderer.rViewportExtent_uu.fY,AngleColor.cBlue));
		myScene.addObject(new AngleLine(0,AngleRenderer.rViewportExtent_uu.fY/2,AngleRenderer.rViewportExtent_uu.fX,AngleRenderer.rViewportExtent_uu.fY/2,AngleColor.cBlue));
		myScene.addObject(new AngleLine(100,100,300,100,AngleColor.cGreen));
		myScene.addObject(new Logo(slLogo));
		fFPS=new AngleFPSCounter();
		myScene.addObject(fFPS);
		AngleTileMap tm=new AngleTileMap(new AngleRect(100,100,400,400));
		try
		{
			//tm.loadFromAsset(this, "desert.tmx");
			//myScene.addObject(tm);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		fDsp=new AngleString(fntCafe,"Hola",100,400,AngleString.aLeft);
		myScene.addObject(fDsp);
		AngleRenderer.setRenderTree(myScene);
		
	}
}