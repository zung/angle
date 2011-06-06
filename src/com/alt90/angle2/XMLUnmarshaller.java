package com.alt90.angle2;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;

public abstract class XMLUnmarshaller
{
	public XmlPullParser xmlParser;

	void open (Context context, int resId)
	{
		xmlParser = context.getResources().getXml(resId);
		Log.d("XMLUnmarshaller", "open " + resId);
		while (nextTag());
	}

	private boolean nextTag()
	{
		try
		{
			xmlParser.next();
			while (!((xmlParser.getEventType() == XmlPullParser.START_TAG) || (xmlParser.getEventType() == XmlPullParser.END_DOCUMENT)))
			{
				if (xmlParser.getName()!=null)
					Log.d("XMLUnmarshaller", "skiping "+xmlParser.getName().toLowerCase());
				xmlParser.next();// skip comments
			}
			if (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT)
			{
				if (xmlParser.getName()!=null)
				{
					Log.d("XMLUnmarshaller", "processing "+xmlParser.getName().toLowerCase()+" Type="+xmlParser.getEventType());
					processTag(xmlParser.getName().toLowerCase());
				}
			}
			else
				return false;
		}
		catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	protected abstract void processTag(String tag);
}
