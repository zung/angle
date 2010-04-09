package com.android.angle;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Main Activity
 * @author Ivan Pajuelo
 *
 */
public class AngleActivity extends Activity
{
	public AngleSurfaceView mGLSurfaceView; //The main GL View
	public AngleSoundSystem SS; //Sounds and music manager
	public XmlPullParser xmlParser;
	protected AngleUI mCurrentUI=null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{
			SS=new AngleSoundSystem(this);
			Thread.sleep(100);
			mGLSurfaceView = new AngleSurfaceView(this);
			mGLSurfaceView.setAwake(true);
			mGLSurfaceView.start();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Set the current user interface
	 * @param currentUI
	 */
	public void setUI(AngleUI currentUI)
	{
		if (mCurrentUI!=null)
		{
			mCurrentUI.onDeactivate();
			mGLSurfaceView.removeObject(mCurrentUI);
		}
		mCurrentUI=currentUI;
		if (mCurrentUI!=null)
		{
			mCurrentUI.onActivate();
			mGLSurfaceView.addObject(mCurrentUI);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mCurrentUI!=null)
			if (mCurrentUI.onTouchEvent(event))
				return true; 
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		if (mCurrentUI!=null)
			if (mCurrentUI.onTrackballEvent(event))
				return true;
		return super.onTrackballEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (mCurrentUI!=null)
			if (mCurrentUI.onKeyDown(keyCode, event))
				return true;
		return super.onKeyDown(keyCode, event);
}
	@Override
	protected void onPause()
	{
		super.onPause();
		mGLSurfaceView.onPause();
		if (mCurrentUI!=null)
			mCurrentUI.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mGLSurfaceView.onResume();
		if (mCurrentUI!=null)
			mCurrentUI.onResume();
	}

	@Override
	public void finish()
	{
		mGLSurfaceView.delete();
		SS.delete();
		super.finish();
	}
	
	public boolean executeXML (int resId)
	{
		xmlParser=getResources().getXml(resId);
		Log.d("XML","executeXML "+resId);
		return nextXMLCommand();
	}

	public boolean nextXMLCommand()
	{
		if (XMLHelper.nextXMLTag(xmlParser, 2))
		{
			Log.d("XML","nextXMLCommand");
			executeXMLCommand(xmlParser.getName().toLowerCase());
			return true;
		}
		return false;
	}

	protected void executeXMLCommand(String command)
	{
		
	}

}
