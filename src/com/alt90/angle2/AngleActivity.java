package com.alt90.angle2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
 
public class AngleActivity extends Activity
{
	private static final int sPointerSize = 10;
	public static AngleActivity uInstance=null; 
	public static boolean[] iKeys = new boolean[KeyEvent.MAX_KEYCODE];
	public static Pointer[] iPointer = new Pointer[sPointerSize];
	/** Called when the activity is first created. */
	protected GLSurfaceView lGLSurfaceView;

	public class Pointer
	{
		public AngleVector mPosition = new AngleVector();
		public boolean isDown;

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		for (int p=0;p<sPointerSize;p++)
			iPointer[p]=new Pointer();
		uInstance=this; 
		lGLSurfaceView = new GLSurfaceView(this);
		lGLSurfaceView.setRenderer(new AngleRenderer(getResources().getDimension(R.dimen.Width), getResources().getDimension(R.dimen.Height)));
		setContentView(lGLSurfaceView);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		lGLSurfaceView.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		lGLSurfaceView.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		iKeys[keyCode] = true;
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		iKeys[keyCode] = false;
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		for (int p = 0; p < event.getPointerCount(); p++)
		{
			int pid = event.getPointerId(p);
			iPointer[pid].mPosition.fX = event.getX(pid);
			iPointer[pid].mPosition.fY = event.getY(pid);
			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				iPointer[pid].isDown = true;
				break;
			case MotionEvent.ACTION_UP:
				iPointer[pid].isDown = true;
				break;
			}
		}
		return true;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		return super.onTrackballEvent(event);
	}

}
