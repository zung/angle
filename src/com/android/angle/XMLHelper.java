package com.android.angle;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XMLHelper
{
	public static boolean nextXMLTag(XmlPullParser parser, int depth) 
	{
		try
		{
			int type;
			do
			{
				type = parser.next();
				if (type == XmlPullParser.START_TAG)
				{
					if (parser.getDepth()==depth)
						return true;
				}
			}while (type != XmlPullParser.END_DOCUMENT);
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	public static boolean nextXMLTag(XmlPullParser parser, String endTag) 
	{
		try
		{
			int type;
			do
			{
				type = parser.next();
				if (type == XmlPullParser.START_TAG)
					return true;
				else if (type == XmlPullParser.END_TAG)
				{
					if (parser.getName().equals(endTag))
						return false;
				}
			}while (type != XmlPullParser.END_DOCUMENT);
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	public static String getXMLText(XmlPullParser parser)
	{
		try
		{
			parser.nextText();
			return parser.getText();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
