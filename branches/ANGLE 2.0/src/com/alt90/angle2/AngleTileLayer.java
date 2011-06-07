package com.alt90.angle2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import android.util.Base64;

/**
 * TileLayer for TileMap
 * @author Ivan Pajuelo
 *
 */
public class AngleTileLayer extends XMLUnmarshaller
{
	private XMLProperties properties;
	private int lWidth;
	private int lHeight;
	private float lOpacity;
	private int lVisible;
	private AngleTileMap lMap;
	private byte[] fData;

	AngleTileLayer(AngleTileMap map)
	{
		lMap=map;
		lXMLTag="layer";
		properties=new XMLProperties();
		lWidth=lMap.fWidth;
		lHeight=lMap.fHeight;
		lOpacity=1;
		lVisible=1;
	}

	/**
	 * support for unmarshal TMX files
	 * You can found info about TMX format in http://mapeditor.org/
	 * Property of Thorbjørn Lindeijer   
	 * @throws Exception 
	 */
	@Override
	protected void processAttribute(String param, String value) throws Exception
	{
		if (param.equals("width"))
			lWidth=Integer.parseInt(value);
		else if (param.equals("height"))
			lHeight=Integer.parseInt(value);
		else if (param.equals("opacity"))
			lOpacity=Float.parseFloat(value);
		else if (param.equals("visible"))
			lVisible=Integer.parseInt(value);
	}

	@Override
	protected void processTag(String tag) throws Exception
	{
		if (tag.equals("data"))
		{
			readData();
		}
		else if (tag.equals("tile"))
		{
			throw new Exception("Don't use <tile> in <layer>");
		}
		else if (tag.equals("properties"))
		{
			properties.read(lXMLParser);
		}
		else
			skip(tag);
	}

	private void readData() throws Exception
	{
		for (int t = 0; t < lXMLParser.getAttributeCount(); t++)
			processDataAttribute(lXMLParser.getAttributeName(t).toLowerCase(), lXMLParser.getAttributeValue(t).toLowerCase());
		
      byte[] dec = Base64.decode(lXMLParser.getText().trim(),Base64.DEFAULT);
      InputStream is=new GZIPInputStream(new ByteArrayInputStream(dec));
      is.read(fData);
      is.close();
   }

	private void processDataAttribute(String param, String value) throws Exception
	{
		if (param.equals("encoding"))
		{
			if (!value.equals("base64"))
				throw new Exception ("Only Base64 encoding supported.");
		}
		else if (param.equals("compression"))
		{
			if (!value.equals("gzip"))
				throw new Exception ("Only GZip compression supported.");
		}
	}
}
