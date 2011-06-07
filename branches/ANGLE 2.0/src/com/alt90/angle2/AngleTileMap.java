package com.alt90.angle2;

import java.util.Vector;

/**
 * TileMap with layer support
 * @author Ivan Pajuelo
 *
 */
public class AngleTileMap extends XMLUnmarshaller
{
	public int fWidth;
	public int fHeight;
	public int fTileWidth;
	public int fTileHeight;
	private Vector<AngleTileSet> lTileSets;
	private Vector<AngleTileLayer> lTileLayers;
	private XMLProperties properties;
	
	public AngleTileMap()
	{
		lXMLTag="map";
		properties=new XMLProperties();
		lTileSets=new Vector<AngleTileSet>();
		lTileLayers=new Vector<AngleTileLayer>();
		fWidth=0;
		fHeight=0;
		fTileWidth=0;
		fTileHeight=0;
	}


	/**
	 * support for unmarshal TMX files
	 * You can found info about TMX format in http://mapeditor.org/
	 * Property of Thorbjørn Lindeijer   
	 * @throws Exception 
	 */

	@Override
	protected void processTag(String tag) throws Exception
	{
		if (tag.equals("tileset"))
		{
			AngleTileSet ts=new AngleTileSet();
			ts.read(lXMLParser);
			lTileSets.add(ts);
		}
		else if (tag.equals("layer"))
		{
			AngleTileLayer tl=new AngleTileLayer(this);
			tl.read(lXMLParser);
			lTileLayers.add(tl);
		}
		else if (tag.equals("objectgroup"))
		{
			skip(tag);
			//TODO ObjectGroup support?
		}
		else if (tag.equals("properties"))
		{
			properties.read(lXMLParser);
		}
		else
			skip(tag);
	}

	@Override
	protected void processAttribute(String param, String value) throws Exception
	{
		if (param.equals("version"))
		{
			if (Float.parseFloat(value)>1)
				throw new Exception("Unsupported TMX version");
		}
		else if (param.equals("orientation"))
		{
			if (!value.equals("orthogonal"))
				throw new Exception("Unsupported TMX Map orientation");
		}
		else if (param.equals("width"))
			fWidth=Integer.parseInt(value);
		else if (param.equals("height"))
			fHeight=Integer.parseInt(value);
		else if (param.equals("tilewidth"))
			fTileWidth=Integer.parseInt(value);
		else if (param.equals("tileheight"))
			fTileHeight=Integer.parseInt(value);
	}
}
