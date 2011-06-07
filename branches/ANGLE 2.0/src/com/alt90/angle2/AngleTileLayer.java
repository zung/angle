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
	private AngleTileSet lTileSet;
	private byte[] fByteData;
	private int[] fData;
	protected int fMinGid;
	protected int fMaxGid;

	AngleTileLayer(AngleTileMap map)
	{
		lMap=map;
		lXMLTag="layer";
		properties=new XMLProperties();
		lWidth=lMap.fWidth;
		lHeight=lMap.fHeight;
		lOpacity=1;
		lVisible=1;
		lTileSet=null;
	}

	protected void beginCheck ()
	{
		lTileSet=null;
		fMinGid=Integer.MAX_VALUE;
		fMaxGid=Integer.MIN_VALUE;
		fData=new int[lWidth*lHeight];
		for (int y=0;y<lHeight;y++)
		{
			for (int x=0;x<lWidth;x++)
			{
				int gid=fByteData[(y*lWidth+x)*4+3];
				gid<<=8;
				gid|=fByteData[(y*lWidth+x)*4+2];
				gid<<=8;
				gid|=fByteData[(y*lWidth+x)*4+1];
				gid<<=8;
				gid|=fByteData[(y*lWidth+x)*4+0];
				if (fMinGid>gid)
					fMinGid=gid;
				if (fMaxGid<gid)
					fMaxGid=gid;
				fData[y*lWidth+x]=gid;
			}
		}
	}
	
	public void endCheck() throws Exception
	{
		if (lTileSet==null)
			throw new Exception("Layer without TileSet");
	}

	protected void setTileSet (AngleTileSet tileset) throws Exception
	{
		if (lTileSet==null)
			lTileSet=tileset;
		else
			throw new Exception("Only one TileSet per layer supported");
		for (int y=0;y<lHeight;y++)
		{
			for (int x=0;x<lWidth;x++)
				fData[y*lWidth+x]-=(lTileSet.fFirstGid-1);
		}
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
		
		lXMLParser.next();
		
      byte[] dec = Base64.decode(lXMLParser.getText().trim(),Base64.DEFAULT);
      InputStream is=new GZIPInputStream(new ByteArrayInputStream(dec));
      fByteData=new byte[lWidth*lHeight*4];
      is.read(fByteData);
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
