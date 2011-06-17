package com.android.ace;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class ACESpriteLayout
{
	private Context mContext;
	public Bitmap mBitmap;
	private Rect mSrc;
	private int mCropLeft;
	private int mCropTop;
	private int mCropWidth;
	private int mCropHeight;
	protected int mFrameCount;
	private int mFrameColumns;
	
	public ACESpriteLayout (Context context, int ResourceID, int cropLeft, int cropTop, int cropWidth, int cropHeight, int frameCount, int frameColumns)
	{
		doInit(context, ResourceID, cropLeft, cropTop, cropWidth, cropHeight, frameCount, frameColumns);
	}
	
	public ACESpriteLayout (Context context, int ResourceID, int cropLeft, int cropTop, int cropWidth, int cropHeight)
	{
		doInit(context, ResourceID, cropLeft, cropTop, cropWidth, cropHeight, 1, 1);
	}

	public ACESpriteLayout (Context context, int ResourceID)
	{
		doInit(context, ResourceID, 0, 0, 0, 0, 1, 1);
	}
	
	private void doInit (Context context, int ResourceID, int cropLeft, int cropTop, int cropWidth, int cropHeight, int frameCount, int frameColumns)
	{
		mContext=context;
		mBitmap=null;
		mSrc=new Rect();
		loadResource(ResourceID, cropLeft, cropTop, cropWidth, cropHeight, frameCount, frameColumns);
	}
	
	public void loadResource(int ResourceID, int cropLeft, int cropTop, int cropWidth, int cropHeight, int frameCount, int frameColumns)
	{
		if (mBitmap!=null)
			mBitmap.recycle();
		mBitmap=BitmapFactory.decodeResource(mContext.getResources(), ResourceID);
		mCropLeft = cropLeft;
		mCropWidth = (cropWidth>0)?cropWidth:mBitmap.getWidth();
		mCropTop = cropTop;
		mCropHeight = (cropHeight>0)?cropHeight:mBitmap.getHeight();
		mFrameCount = frameCount;
		mFrameColumns = frameColumns;

		mSrc.right=mCropWidth;
		mSrc.bottom=mCropHeight;
	}
	
	public Rect getSrcRect(int frame)
	{
		mSrc.offsetTo (mCropLeft + ((frame % mFrameColumns) * mCropWidth),
							mCropTop + ((frame / mFrameColumns) * mCropHeight));
		return mSrc;
	}
	
	public int getWidth()
	{
		return mSrc.width();
	}

	public int getHeight()
	{
		return mSrc.height();
	}

	public void getFrame(int frame, Bitmap dst, int[] pixels)
	{
		if (mBitmap!=null)
		{
			getSrcRect(frame);
			int W=dst.getWidth();
			int H=dst.getHeight();
			mBitmap.getPixels(pixels, 0, W, mSrc.left, mSrc.top, W, H);
			dst.setPixels(pixels, 0, W, 0, 0, W, H);
		}
	}

	public void copyRect(int sX, int sY, int dX, int dY, int W, int H, Bitmap dst, int[] pixels)
	{
		if (mBitmap!=null)
		{
			mBitmap.getPixels(pixels, 0, W, sX, sY, W, H);
			dst.setPixels(pixels, 0, W, dX, dY, W, H);
		}
	}

}
