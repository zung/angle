package com.alt90.angle2;

/**
 * TileSet for TileMap
 * @author Ivan Pajuelo
 *
 */
public class AngleTileSet extends XMLUnmarshaller
{
	public int fFirstGid;

	AngleTileSet ()
	{
		lXMLTag="tileset";
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processTag(String tag) throws Exception
	{
		// TODO Auto-generated method stub
		
	}
}
