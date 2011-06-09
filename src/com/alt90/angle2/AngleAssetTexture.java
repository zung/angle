package com.alt90.angle2;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class AngleAssetTexture extends AngleTexture
{
	public String fFileName;

	public AngleAssetTexture(String filename, int type)
	{
		super(type);
		fFileName=filename;
	}

	@Override
	public Bitmap create()
	{
		InputStream is;
		Bitmap bitmap = null;
		try
		{
			is = AngleActivity.uInstance.getAssets().open(fFileName);
			Bitmap image = BitmapFactory.decodeStream(is);
			is.close();
			
			bitmap = Bitmap.createBitmap(AngleRenderer.fitPow2(image.getWidth()), AngleRenderer.fitPow2(image.getHeight()), Bitmap.Config.ARGB_8888); 

			Canvas canvas = new Canvas(bitmap);

			canvas.drawBitmap(image, 0, 0, null);			
		}
		catch (IOException e)
		{
			Log.e("AngleTextureEngine", "loadTexture::InputStream error: " + e.getMessage());
		}
		return bitmap;
	}

}
