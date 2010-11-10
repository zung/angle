package com.alt90.angle2;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteLayout
{
	protected AngleTexture lTexture;
	protected AngleRect lCrop;	//Texels
	protected int lFrameCount;		
	protected int lFrameColumns;
	protected int lFrame;

	protected AngleVector lDimensions; 	//User units
	protected AngleVector[] lPivot;		//User units

	/**
	 * 
	 * @param width
	 *           Width in pixels
	 * @param height
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 * @param cropLeft
	 *           Most left pixel in texture
	 * @param cropTop
	 *           Most top pixel in texture
	 * @param cropWidth
	 *           Width of the cropping rectangle in texture
	 * @param cropHeight
	 *           Height of the cropping rectangle in texture
	 */
	public AngleSpriteLayout(int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight)
	{
		doInit(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, 1, 1);
	}

	/**
	 * 
	 * @param width
	 *           Width in pixels
	 * @param height
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 * @param cropLeft
	 *           Most left pixel in texture
	 * @param cropTop
	 *           Most top pixel in texture
	 * @param cropWidth
	 *           Width of the cropping rectangle in texture
	 * @param cropHeight
	 *           Height of the cropping rectangle in texture
	 * @param frameCount
	 *           Number of frames in animation
	 * @param frameColumns
	 *           Number of frames horizontally in texture
	 */
	public AngleSpriteLayout(int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight, int frameCount, int frameColumns)
	{
		doInit(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, frameCount, frameColumns);
	}

	public AngleSpriteLayout(int width, int height, int resourceId)
	{
		doInit(width, height, resourceId, 0, 0, 0, 0, 1, 1);
	}

	private void doInit(float width, float height, int resourceId, int cropLeft, int cropTop, int cropWidth, int cropHeight, int frameCount,
			int frameColumns)
	{
		lTexture = AngleTextureEngine.createTextureFromResourceId(resourceId);
		lCrop=new AngleRect(cropLeft,cropTop,cropWidth,cropHeight);
		lDimensions=new AngleVector(width,height);
		if ((lCrop.fSize.fX==0)||(lCrop.fSize.fY==0))
		{
			InputStream is = AngleActivity.uInstance.getResources().openRawResource(resourceId);			
			try
			{
				Bitmap bitmap = BitmapFactory.decodeStream(is, null, new BitmapFactory.Options());
				lCrop.fSize.fX = bitmap.getWidth();
				lCrop.fSize.fY = bitmap.getHeight();
				bitmap.recycle();
			}
			finally
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					Log.e("AngleTextureEngine", "loadTexture::InputStream.close error: " + e.getMessage());
				}
			}
		}
		if (lDimensions.fX==0)
			lDimensions.fX = lCrop.fSize.fX;
		if (lDimensions.fY==0)
			lDimensions.fY = lCrop.fSize.fY;
		lFrameCount = frameCount;
		lFrameColumns = frameColumns;

		lPivot=new AngleVector[lFrameCount];
		//Set all frame pivots at center point by default
		for (int f=0;f<lFrameCount;f++)
			lPivot[f]=new AngleVector(lDimensions.fX / 2, lDimensions.fY / 2);

	}

	/**
	 * Set pivot point of one frame
	 * @param frame
	 * @param x
	 * @param y
	 */
	public void setPivot(int frame, float x, float y)
	{
		if (frame<lFrameCount)
			lPivot[frame].set(x,y);
	}

	/**
	 * Set pivot point of all frames
	 * @param x
	 * @param y
	 */
	public void setPivot(float x, float y) //Texels
	{
		for (int f=0;f<lFrameCount;f++)
			lPivot[f].set(x,y);
	}

	/**
	 * get pivot point of one frame
	 * 
	 * @param frame
	 * @return pivot point 
	 */
	public AngleVector getPivot(int frame) //User units
	{
		if (frame<lFrameCount)
			return AngleRenderer.coordsUserToScreen(lPivot[frame]);
		return null;
	}

	public boolean fillTextureValues(int frame, int[] lTextureIV, boolean flipHorizontal, boolean flipVertical)
	{
		if (frame<lFrameCount)
		{
			if (flipHorizontal)
			{
				lTextureIV[0] = (int) ((lCrop.fPosition.fX + lCrop.fSize.fX) + ((lFrame % lFrameColumns) * lCrop.fSize.fX));// Ucr
				lTextureIV[2] = (int) -lCrop.fSize.fX; // Wcr
			}
			else
			{
				lTextureIV[0] = (int) (lCrop.fPosition.fX + ((lFrame % lFrameColumns) * lCrop.fSize.fX));// Ucr
				lTextureIV[2] = (int) lCrop.fSize.fX; // Wcr
			}

			if (flipVertical)
			{
				lTextureIV[1] = (int) (lCrop.fPosition.fY + ((lFrame / lFrameColumns) * lCrop.fSize.fY));// Vcr
				lTextureIV[3] = (int) lCrop.fSize.fY; // Hcr
			}
			else
			{
				lTextureIV[1] = (int) ((lCrop.fPosition.fY + lCrop.fSize.fY) + ((lFrame / lFrameColumns) * lCrop.fSize.fY));// Vcr
				lTextureIV[3] = (int) -lCrop.fSize.fY; // Hcr
			}
			return true;
		}
		return false;
	}
	public boolean fillVertexValues(int frame, float[] vertexValues)
	{
		if (frame<lFrameCount)
		{
			AngleVector size=AngleRenderer.coordsUserToScreen(lDimensions);
			AngleVector pivot=AngleRenderer.coordsUserToScreen(lPivot[frame]);
			vertexValues[0] = -pivot.fX;
			vertexValues[1] = size.fY - pivot.fY;
			vertexValues[2] = size.fX - pivot.fX;
			vertexValues[3] = size.fY - pivot.fY;
			vertexValues[4] = -pivot.fX;
			vertexValues[5] = -pivot.fY;
			vertexValues[6] = size.fX - pivot.fX;
			vertexValues[7] = -pivot.fY;
			return true;
		}
		return false;
	}
	
	public void onDestroy(GL10 gl)
	{
		AngleTextureEngine.deleteTexture(lTexture);
	}
	
	/**
	 * Change the content of the texture
	 * @param resourceId Durable
	 */
	public void changeTexture (int resourceId)
	{
		AngleTextureEngine.deleteTexture(lTexture);
		lTexture = AngleTextureEngine.createTextureFromResourceId(resourceId);
	}

	/**
	 * Change the entire layout
	 * @param width in viewport units
	 * @param height in viewport units
	 * @param resourceId
	 */
	public void changeLayout(float width, float height, int resourceId)
	{
		changeLayout(width, height, resourceId, 0, 0, (int)width, (int)height, 1, 1);
	}
	
	/**
	 * Change the entire layout
	 * @param width in viewport units
	 * @param height in viewport units
	 * @param resourceId
	 * @param cropLeft in texels
	 * @param cropTop in texels
	 * @param cropWidth in texels
	 * @param cropHeight in texels
	 */
	public void changeLayout(float width, float height, int resourceId, int cropLeft, int cropTop, int cropWidth, int cropHeight)
	{
		changeLayout(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, 1, 1);
	}
	
	/**
	 * Change the entire layout
	 * @param width in viewport units
	 * @param height in viewport units
	 * @param resourceId
	 * @param cropLeft in texels
	 * @param cropTop in texels
	 * @param cropWidth in texels
	 * @param cropHeight in texels
	 * @param frameCount
	 * @param frameColumns
	 */
	public void changeLayout(float width, float height, int resourceId, int cropLeft, int cropTop, int cropWidth, int cropHeight, int frameCount, int frameColumns)
	{
		lDimensions.fX = width;
		lDimensions.fY = height;
		lCrop.fPosition.fX = cropLeft;
		lCrop.fPosition.fY = cropTop;
		lCrop.fSize.fX = cropWidth;
		lCrop.fSize.fY = cropHeight;
		lFrameCount = frameCount;
		lFrameColumns = frameColumns;
		changeTexture (resourceId);
	}

	public boolean bindTexture(GL10 gl)
	{
		if (lTexture != null)
			return lTexture.bind(gl);
		return false;
	}
}
