package com.alt90.angle2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.util.Base64;

/**
 * TileLayer for TileMap
 * @author Ivan Pajuelo
 *
 */
public class AngleTileLayer extends AngleObject
{
	//Cached variables
   private static AngleVectorI cCurrent_uu=new AngleVectorI();
   private static AngleVectorI cTileSize_uu=new AngleVectorI();
   private static AngleVectorI cMod_uu=new AngleVectorI();
   private static AngleVectorI cDiv_uu=new AngleVectorI();
   private static AngleVectorI cUVDelta_tx=new AngleVectorI();

	protected static int[] lTextureIV_tx=new int[4]; // Texture coordinates
	private XMLProperties properties;
	private AngleTileMap lMap;
	private AngleTileSet lTileSet;
	private byte[] fByteData;
	private int[] fData;
	protected int fMinGid;
	protected int fMaxGid;
	public int fVisible;
	public AngleColor fColor; //Set to change layer tint color and alpha
	public AngleVectorF fTopLeft_uu; //Set to change the position of the layer into the map

	AngleTileLayer(AngleTileMap map)
	{
		lMap=map;
		lXMLTag="layer";
		properties=new XMLProperties();
		fColor=new AngleColor(AngleColor.cWhite);
		fTopLeft_uu=new AngleVectorF();
		fVisible=1;
		lTileSet=null;
	}

	protected void beginCheck ()
	{
		lTileSet=null;
		fMinGid=Integer.MAX_VALUE;
		fMaxGid=Integer.MIN_VALUE;
		fData=new int[lMap.fWidth*lMap.fHeight];
		for (int y=0;y<lMap.fHeight;y++)
		{
			for (int x=0;x<lMap.fWidth;x++)
			{
				int gid=fByteData[(y*lMap.fWidth+x)*4+3];
				gid<<=8;
				gid|=fByteData[(y*lMap.fWidth+x)*4+2];
				gid<<=8;
				gid|=fByteData[(y*lMap.fWidth+x)*4+1];
				gid<<=8;
				gid|=fByteData[(y*lMap.fWidth+x)*4+0];
				if ((fMinGid>gid)&&(gid>0))
					fMinGid=gid;
				if (fMaxGid<gid)
					fMaxGid=gid;
				fData[y*lMap.fWidth+x]=gid;
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
		for (int y=0;y<lMap.fHeight;y++)
		{
			for (int x=0;x<lMap.fWidth;x++)
				fData[y*lMap.fWidth+x]-=(lTileSet.fFirstGid-1);
		}
	}

	@Override
	public void draw(GL10 gl)
	{
		if (lTileSet.bindTexture(gl))
		{
			gl.glColor4f(fColor.fRed, fColor.fGreen, fColor.fBlue, fColor.fAlpha);
			
			cMod_uu.set((int)fTopLeft_uu.fX%lMap.fTileWidth, (int)fTopLeft_uu.fY%lMap.fTileHeight);
		   cDiv_uu.set((int)fTopLeft_uu.fX/lMap.fTileWidth, (int)fTopLeft_uu.fY/lMap.fTileHeight);
			
	      cUVDelta_tx.fY=cMod_uu.fY;
	     	cTileSize_uu.fY=lMap.fTileHeight-cMod_uu.fY;
	      int row=cDiv_uu.fY;
	      cCurrent_uu.fY=0;
		   while (cTileSize_uu.fY>0)
		   {
		      //drawRow
	         cUVDelta_tx.fX=cMod_uu.fX;
	         cTileSize_uu.fX=lMap.fTileWidth-cMod_uu.fX;
		      int col=cDiv_uu.fX;
	         cCurrent_uu.fX=0;
	         while (cTileSize_uu.fX>0)
	         {
	            //drawTile
			      if ((col>=0)&&(row>=0)&&(col<lMap.fWidth)&&(row<lMap.fHeight))
			      {
				      int tile=fData[row*lMap.fWidth+col];
	               if (tile>0)
	               {
							lTileSet.fillTextureValues(lTextureIV_tx,tile-1,cUVDelta_tx,cTileSize_uu);
	                  ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, lTextureIV_tx, 0);
	
	                  ((GL11Ext) gl).glDrawTexfOES(
	                  		(cCurrent_uu.fX*lMap.fScale+lMap.fClipRect_uu.fPosition.fX)*AngleRenderer.vHorizontalFactor_px, 
	                  		AngleRenderer.vViewportHeight_px - (cCurrent_uu.fY*lMap.fScale+lMap.fClipRect_uu.fPosition.fY+cTileSize_uu.fY*lMap.fScale)*AngleRenderer.vVerticalFactor_px,
	                  		0, 
	                  		cTileSize_uu.fX*lMap.fScale*AngleRenderer.vHorizontalFactor_px, 
	                  		cTileSize_uu.fY*lMap.fScale*AngleRenderer.vVerticalFactor_px);
	               }
	      		}
	            //------
	            col++;
	            cCurrent_uu.fX+=cTileSize_uu.fX;
	            cTileSize_uu.fX=lMap.fTileWidth;
	            if (cTileSize_uu.fX>(lMap.fClipRect_uu.fSize.fX-cCurrent_uu.fX*lMap.fScale)/lMap.fScale)
	               cTileSize_uu.fX=(int) ((lMap.fClipRect_uu.fSize.fX-cCurrent_uu.fX*lMap.fScale)/lMap.fScale);
	            cUVDelta_tx.fX=0;
	         }
	         //------
	         row++;
		      cCurrent_uu.fY+=cTileSize_uu.fY;
	     		cTileSize_uu.fY=lMap.fTileHeight;
	         if (cTileSize_uu.fY>(lMap.fClipRect_uu.fSize.fY-cCurrent_uu.fY*lMap.fScale)/lMap.fScale)
	         	cTileSize_uu.fY=(int) ((lMap.fClipRect_uu.fSize.fY-cCurrent_uu.fY*lMap.fScale)/lMap.fScale);
            cUVDelta_tx.fY=0;
		   }
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
			if (lMap.fWidth!=Integer.parseInt(value))
				throw new Exception("Map and layer width must be the same");
		else if (param.equals("height"))
			if (lMap.fHeight!=Integer.parseInt(value))
				throw new Exception("Map and layer height must be the same");
		else if (param.equals("opacity"))
			fColor.fAlpha=Float.parseFloat(value);
		else if (param.equals("visible"))
			fVisible=Integer.parseInt(value);
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
      fByteData=new byte[lMap.fWidth*lMap.fHeight*4];
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
