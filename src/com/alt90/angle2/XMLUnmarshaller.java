package com.alt90.angle2;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

public abstract class XMLUnmarshaller
{
	protected XmlPullParser lXMLParser;
	protected String lXMLTag;
	protected String lPath; 	

	/**
	 * Load XML from resource
	 * @param context
	 * @param resId
	 * @throws Exception
	 */
	public void loadFromAsset (Context context, String filename) throws Exception
	{
		int pos=filename.lastIndexOf('/');
		if (pos>0)
			lPath=filename.substring(0, pos);
		else
			lPath="";
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setValidating(false);
      lXMLParser = factory.newPullParser();
      InputStream raw = context.getAssets().open(filename);
		lXMLParser.setInput(raw, null);		
		Log.d("XMLUnmarshaller", "open " + filename);
		findTag();
		read(lXMLParser);
	}

	/**
	 * Load XML from resource
	 * @param context
	 * @param resId
	 * @throws Exception
	 */
	public void loadFromURL (Context context, String dir) throws Exception
	{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setValidating(false);
      lXMLParser = factory.newPullParser();
		URL url = new URL(dir);
		URLConnection urlConnection = url.openConnection();
		BufferedInputStream raw = new BufferedInputStream(urlConnection.getInputStream());
      lXMLParser.setInput(raw, null);		
		Log.d("XMLUnmarshaller", "open " + dir);
		findTag();
		read(lXMLParser);
	}

	/**
	 * Find <lXMLTag>
	 * @throws Exception
	 */
	private void findTag() throws Exception
	{
		do
		{
			lXMLParser.next();
			if (lXMLParser.getEventType() == XmlPullParser.START_TAG)
				if (lXMLParser.getName().equals(lXMLTag))
					return;
		}while (lXMLParser.getEventType() != XmlPullParser.END_DOCUMENT);
		
		throw new Exception ("Can't find tag "+lXMLTag);
	}

	/**
	 * Process tags into <lXMLTag>
	 * @return
	 * @throws Exception
	 */
	protected boolean nextTag() throws Exception
	{
		do
		{
			lXMLParser.next();
			if (lXMLParser.getEventType() == XmlPullParser.END_TAG)
			{
				if (lXMLParser.getName().equals(lXMLTag))
					break;
			}
			else if (lXMLParser.getEventType() == XmlPullParser.START_TAG)
			{
				if (lXMLParser.getName()!=null)
				{
					Log.d("XMLUnmarshaller", "processing "+lXMLParser.getName().toLowerCase()+" Type="+lXMLParser.getEventType());
					processTag(lXMLParser.getName().toLowerCase());
					return true;
				}
			}
		}while (lXMLParser.getEventType() != XmlPullParser.END_DOCUMENT);
		return false;
	}

	/**
	 * Skip until /tag
	 * @param tag
	 * @throws Exception
	 */
	protected void skip(String tag) throws Exception
	{
		do
		{
			lXMLParser.next();
			if (lXMLParser.getEventType() == XmlPullParser.END_TAG)
			{
				if (lXMLParser.getName().equals(tag))
					break;
			}
		}while (lXMLParser.getEventType() != XmlPullParser.END_DOCUMENT);
	}

	/**
	 * Reads lXMLTag from xmlParser
	 * @param xmlParser
	 * @throws Exception
	 */
	protected void read(XmlPullParser xmlParser) throws Exception
	{
		lXMLParser=xmlParser;
		readAttributes();
		while (nextTag());
	}

	/**
	 * Read the attributes of a tag
	 * @throws Exception
	 */
	protected void readAttributes() throws Exception
	{
		for (int t = 0; t < lXMLParser.getAttributeCount(); t++)
			processAttribute(lXMLParser.getAttributeName(t).toLowerCase(), lXMLParser.getAttributeValue(t).toLowerCase());
	}

	protected abstract void processAttribute(String param, String value) throws Exception;
	protected abstract void processTag(String tag) throws Exception;
}
