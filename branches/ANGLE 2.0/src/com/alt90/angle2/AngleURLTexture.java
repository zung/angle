package com.alt90.angle2;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class AngleURLTexture extends AngleTexture
{
	public String fURL;

	AngleURLTexture(String url, int type)
	{
		super(type);
		fURL=url;
	}

	@Override
	public Bitmap create()
	{
		URL url;
		BufferedInputStream is;
		Bitmap bitmap = null;
		try
		{
			url = new URL(fURL);
			URLConnection urlConnection = url.openConnection();
			is = new BufferedInputStream(urlConnection.getInputStream());
			Bitmap image = BitmapFactory.decodeStream(is);
			is.close();
			
			bitmap = Bitmap.createBitmap(AngleRenderer.fitPow2(image.getWidth()), AngleRenderer.fitPow2(image.getHeight()), Bitmap.Config.ARGB_8888); 

			Canvas canvas = new Canvas(bitmap);

			canvas.drawBitmap(image, 0, 0, null);			
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return bitmap;
	}

}
