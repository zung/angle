package com.alt90.angle2;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class AngleAssetTexture extends AngleTexture
{
	private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	public String fFileName;

	public AngleAssetTexture(String filename)
	{
		super();
		fFileName=filename;
	}

	@Override
	public Bitmap create()
	{
		sBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		InputStream is;
		Bitmap bitmap = null;
		try
		{
			is = AngleActivity.uInstance.getAssets().open(fFileName);
			bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
			is.close();
		}
		catch (IOException e)
		{
			Log.e("AngleTextureEngine", "loadTexture::InputStream error: " + e.getMessage());
		}
		return bitmap;
	}

}
