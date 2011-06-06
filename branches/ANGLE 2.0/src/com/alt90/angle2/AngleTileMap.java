package com.alt90.angle2;

public class AngleTileMap extends XMLUnmarshaller
{
   /** Orthogonal. */
   public static final int MDO_ORTHO   = 1;
   /** Isometric. */
   public static final int MDO_ISO     = 2;
   /** Hexagonal. */
   public static final int MDO_HEX     = 4;
   /** Shifted (used for iso and hex). */
   public static final int MDO_SHIFTED = 5;
	
   private int lOrientation=MDO_ORTHO;
	public int fTileWidth;
	public int fTileHeight;
	
	
	public AngleTileMap(int mapWidth, int mapHeight)
	{
		// TODO Auto-generated constructor stub
	}


	public void setOrientation(int orientation)
	{
		lOrientation=orientation;
	}

	@Override
	protected void processTag(String tag)
	{
		if (tag.equals("map"))
		{
			
		}
	} 
}
