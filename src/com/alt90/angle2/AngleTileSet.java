package com.alt90.angle2;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * TileSet for TileMap
 * @author Ivan Pajuelo
 *
 */
public class AngleTileSet extends XMLUnmarshaller
{
	protected AngleTexture lTexture;
	private AngleTileMap lMap;
	protected int fFirstGid;
	protected int fTileWidth;
	protected int fTileHeight;
	private int fSpacing;
	private int fMargin;
	protected int fWidth;
	protected int fHeight;

	AngleTileSet (AngleTileMap map)
	{
		lMap=map;
		lXMLTag="tileset";
		fFirstGid=0;
		fTileWidth=0;
		fTileHeight=0;
		fWidth=0;
		fHeight=0;
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
		if (param.equals("firstgid"))
			fFirstGid=Integer.parseInt(value);
		else if (param.equals("source"))
			throw new Exception("TSX files not supported");
		else if (param.equals("tilewidth"))
			fTileWidth=Integer.parseInt(value);
		else if (param.equals("tileheight"))
			fTileHeight=Integer.parseInt(value);
		else if (param.equals("spacing"))
			fSpacing=Integer.parseInt(value);
		else if (param.equals("margin"))
			fMargin=Integer.parseInt(value);
	}

	@Override
	protected void processTag(String tag) throws Exception
	{
		if (tag.equals("image"))
		{
			readImage();
		}
		else
			skip(tag);
	}
	private void readImage() throws Exception
	{
		for (int t = 0; t < lXMLParser.getAttributeCount(); t++)
			processImageAttribute(lXMLParser.getAttributeName(t).toLowerCase(), lXMLParser.getAttributeValue(t).toLowerCase());
		
   }

	private void processImageAttribute(String param, String value) throws Exception
	{
		if (param.equals("source"))
		{
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
			InputStream is;
			Bitmap bitmap = null;
			try
			{
				is = AngleActivity.uInstance.getAssets().open(lMap.lPath+value);
				bitmap = BitmapFactory.decodeStream(is, null, bitmapOptions);
				is.close();
			}
			catch (IOException e)
			{
				throw new Exception ("Image "+lMap.lPath+value+" not found.");
			}
			fWidth=(bitmap.getWidth()-fMargin)/(fTileWidth+fSpacing);
			fHeight=(bitmap.getHeight()-fMargin)/(fTileHeight+fSpacing);
			bitmap.recycle();
			lTexture = AngleTextureEngine.createTextureFromAsset(lMap.lPath+value);
		}
		else if (param.equals("trans"))
			throw new Exception ("Transparent color not supported. Use PNG32 instead.");
	}
}
