package com.alt90.test;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.alt90.angle2.AngleActivity;
import com.alt90.angle2.AngleColor;
import com.alt90.angle2.AngleFPSCounter;
import com.alt90.angle2.AngleFont;
import com.alt90.angle2.AngleObject;
import com.alt90.angle2.AngleRect;
import com.alt90.angle2.AngleRenderer;
import com.alt90.angle2.AngleScreenEraser;
import com.alt90.angle2.AngleSpriteLayout;
import com.alt90.angle2.AngleSpriteRotable;
import com.alt90.angle2.AngleString;
import com.alt90.angle2.AngleTileLayer;
import com.alt90.angle2.AngleTileMap;


public class TestAngle2 extends AngleActivity
{
	private AngleTileMap tm;
	private int minDim;
	
	private class FPSDisplay extends AngleFPSCounter
	{
		private AngleString fDsp;

		public FPSDisplay()
		{
			AngleFont fntCafe=new AngleFont(AngleRenderer.rViewportExtent_uu.fY/12, Typeface.createFromAsset(getAssets(),"cafe.ttf"), 222, 0, 0, AngleColor.cLime);
			//fntCafe.saveTo("cafe");
			fDsp=new AngleString(fntCafe,"FPS",(int)AngleRenderer.rViewportExtent_uu.fX/8*1,(int)AngleRenderer.rViewportExtent_uu.fY/8*5,AngleString.aLeft);
			addObject(fDsp);
		}
		
		@Override
		protected void logFPS()
		{
			super.logFPS();
			fDsp.set(String.format("%.1f", fFPS));
		}

	}
	
	private class Controller extends AngleObject
	{
		@Override
		protected void step(float secondsElapsed)
		{
			if (iKeys[KeyEvent.KEYCODE_BACK])
				finish();
			if (iFling[0].newData)
			{
				iFling[0].newData=false;
				String aa="T="+iFling[0].fTime+", X="+iFling[0].fDelta_uu.fX+", Y="+iFling[0].fDelta_uu.fY;
				Log.d("AE2",aa);
				//fDsp.set(aa);
				if (iFling[0].fTime<0.5)
				{
					AngleTileLayer tl;
					for (int l=0;l<1;l++)
					{
						tl = tm.getLayer(l);
						tl.fTopLeft_uu.fX-=iFling[0].fDelta_uu.fX/(2+l);
						tl.fTopLeft_uu.fY-=iFling[0].fDelta_uu.fY/(2+l);
					}
				}
			}
			if (iPointer[0].isDown)
			{
				tm.fScale+=secondsElapsed;
				if (tm.fScale>3)
					tm.fScale=3;
			}
			else
			{
				tm.fScale-=secondsElapsed/2;
				if (tm.fScale<1)
					tm.fScale=1;
			}
		}
		
	}
	
	private class Logo extends AngleSpriteRotable
	{
		public Logo(AngleSpriteLayout layout)
		{
			super(AngleRenderer.rViewportExtent_uu.fX/2,0,layout);
		}

		@Override
		protected void step(float secondsElapsed)
		{
			fRotation+=90*secondsElapsed;
			if (iPointer[0].isDown)
			{
				fPosition_uu.fX=iPointer[0].fPosition_uu.fX;
				fPosition_uu.fY=iPointer[0].fPosition_uu.fY-minDim/2;
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		lGLSurfaceView
		.setRenderer(new AngleRenderer(getResources().getDimension(R.dimen.Width), getResources().getDimension(R.dimen.Height)));
		minDim=(int)(AngleRenderer.rViewportExtent_uu.fX/4);
		if (minDim>(int)(AngleRenderer.rViewportExtent_uu.fY/4))
			minDim=(int)(AngleRenderer.rViewportExtent_uu.fY/4);
		AngleSpriteLayout slLogo=new AngleSpriteLayout(minDim,minDim,R.drawable.anglelogo);

		AngleObject myScene=new AngleObject(30);
		myScene.addObject(new Controller());
		myScene.addObject(new AngleScreenEraser(AngleColor.cWhite));
	
		tm=new AngleTileMap(new AngleRect((int)AngleRenderer.rViewportExtent_uu.fX/8*1,(int)AngleRenderer.rViewportExtent_uu.fY/8*1,(int)AngleRenderer.rViewportExtent_uu.fX/8*6,(int)AngleRenderer.rViewportExtent_uu.fY/8*6),true);
//		tm=new AngleTileMap(new AngleRect(0,0,(int)AngleRenderer.rViewportExtent_uu.fX,(int)AngleRenderer.rViewportExtent_uu.fY),false);
		try
		{
			tm.loadFromAsset(this, "desert.tmx");
			tm.fScale=1.5f;
			myScene.addObject(tm);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		myScene.addObject(new Logo(slLogo));

/*		{
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
		}
	*/	
		myScene.addObject(new FPSDisplay());
		AngleRenderer.setRenderTree(myScene);
		Log.d("TestAngle2","onCreate end");
		
	}
}