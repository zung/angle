package com.alt90.angle2;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public abstract class XMLUnmarshaller
{
	protected XmlPullParser xmlParser;

	void open (Context context, int resId, String tag) throws Exception
	{
		xmlParser = context.getResources().getXml(resId);
		Log.d("XMLUnmarshaller", "open " + resId);
		findTag(tag);
	}

	private void findTag(String tag) throws Exception
	{
		do
		{
			xmlParser.next();
			if (xmlParser.getEventType() == XmlPullParser.START_TAG)
				if (xmlParser.getName()==tag)
					return;
		}while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT);
		
		throw new Exception ("Can't find tag "+tag);
	}

	protected boolean nextTag(String tag) throws Exception //coge todo los tags que haya dentro de 'tag' hasta que encuentre /tag
	{
		do
		{
			xmlParser.next();
			if (xmlParser.getEventType() == XmlPullParser.END_TAG)
			{
				if (xmlParser.getName()==tag)
					break;
			}
			else if (xmlParser.getEventType() == XmlPullParser.START_TAG)
			{
				if (xmlParser.getName()!=null)
				{
					Log.d("XMLUnmarshaller", "processing "+xmlParser.getName().toLowerCase()+" Type="+xmlParser.getEventType());
					processTag(xmlParser.getName().toLowerCase());
					return true;
				}
			}
		}while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT);
		return false;
	}

	protected void readAttributes() throws Exception
	{
		for (int t = 0; t < xmlParser.getAttributeCount(); t++)
			processAttribute(xmlParser.getAttributeName(t).toLowerCase(), xmlParser.getAttributeValue(t).toLowerCase());
	}

	protected abstract void processAttribute(String param, String value) throws Exception;
	protected abstract void processTag(String tag) throws Exception;
}
