package com.alt90.angle2;

import java.util.Iterator;
import java.util.Vector;

import android.content.Context;

/**
 * TileMap with layer support
 * @author Ivan Pajuelo
 *
 */
public class AngleTileMap extends AngleObject 
{
	protected int fWidth;
	protected int fHeight;
	protected int fTileWidth;
	protected int fTileHeight;
	private Vector<AngleTileSet> lTileSets;
	private Vector<AngleTileLayer> lTileLayers;
	public XMLProperties properties;
	public AngleRect fClipRect_uu; //Set to change the position and dimensions of the map in the screen
	public float fScale; //Scale factor of whole map
	private boolean lOpacitySupport;
	
	public AngleTileMap(AngleRect clipRect, boolean opacitySupport)
	{
		lXMLTag="map";
		properties=new XMLProperties();
		lTileSets=new Vector<AngleTileSet>();
		lTileLayers=new Vector<AngleTileLayer>();
		fWidth=0;
		fHeight=0;
		fTileWidth=0;
		fTileHeight=0;
		fScale=1f;
		fClipRect_uu=clipRect;
		lOpacitySupport=opacitySupport;
	}
	public AngleTileLayer getLayer(int idx)
	{
		return (AngleTileLayer) this.lChildren[idx];
	}

	
	/**
	 * support for unmarshal TMX files
	 * You can found info about TMX format in http://mapeditor.org/
	 * Property of Thorbjørn Lindeijer   
	 * @throws Exception 
	 */

	@Override
	void loadFromAsset(Context context, String filename) throws Exception
	{
		super.loadFromAsset(context, filename);
		resize(lTileLayers.size());
		Iterator<AngleTileLayer> tl_it = lTileLayers.iterator();
		int idx=0;
		while (tl_it.hasNext())
	   {
			AngleTileLayer tl=tl_it.next();
			tl.beginCheck();
			Iterator<AngleTileSet> ts_it = lTileSets.iterator();
			while (ts_it.hasNext())
		   {
				AngleTileSet ts=ts_it.next();
			   if ((tl.fMinGid>=ts.fFirstGid)&&(tl.fMaxGid<ts.fFirstGid+(ts.fCols*ts.fRows)))
			   	tl.setTileSet(ts);
		   }
			tl.endCheck();
			addObject(tl);
			idx++;
	   }
	}

	@Override
	protected void processTag(String tag) throws Exception
	{
		if (tag.equals("tileset"))
		{
			AngleTileSet ts=new AngleTileSet(this, lOpacitySupport);
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
