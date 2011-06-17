package com.android.ace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class ACEMainView extends ACEViewGroup
{
	public static int mWidth=0;
	public static int mHeight=0;
	public static Paint mFastPaint=null;
	public static Paint mComplexPaint=null;
	private MessageHandler mMessageHandler;
	public boolean isRunning;
	private ACEGameEngine mGameEngine;
	
	class MessageHandler extends Handler
	{
		private long lCTM;

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what==0)
			{
				float secondsElapsed=0.0f;
				long CTM = System.currentTimeMillis();
				if (lCTM > 0)
					secondsElapsed = (CTM - lCTM) / 1000.f;
				lCTM = CTM;
	
				if (mGameEngine != null)
					mGameEngine.step(secondsElapsed);

				if (isRunning)
					sendEmptyMessageDelayed(0,16);
			}
		}
		
	}
	public ACEMainView(Context context)
	{
		super(context);
		mMessageHandler=new MessageHandler();
		mFastPaint=new Paint();
		mComplexPaint=new Paint();
		mComplexPaint.setAntiAlias(true);
		mComplexPaint.setFilterBitmap(true);

		mGameEngine=null;
	}

	public void setAwake(boolean awake)
	{
		setKeepScreenOn(awake);
	}
	@Override

	public boolean onTouchEvent(MotionEvent event)
	{
		if (mGameEngine != null)
			return mGameEngine.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (mGameEngine != null)
			return mGameEngine.onKeyDown(keyCode, event);
		return false;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		if (mGameEngine != null)
			return mGameEngine.onTrackballEvent(event);
		return super.onTrackballEvent(event);
	}

	public void setGameEngine(ACEGameEngine gameEngine)
	{
		mGameEngine=gameEngine;
		removeAllViews();
		mGameEngine.linkToView(this);
	}
	
	public void resume()
	{
		isRunning=true;
		mMessageHandler.sendEmptyMessage(0);
		setAwake(true);
	}

	public void pause()
	{
		isRunning=false;
		setAwake(false);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		mWidth=w;
		mHeight=h;
		super.onSizeChanged(w, h, oldw, oldh);
      resume();
	}
}
