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
				if (fMinGid>gid)
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
			
		   AngleVectorI current_uu=new AngleVectorI();
			AngleVectorF tileSize_uu=new AngleVectorF();
		   AngleVectorI mod_uu=new AngleVectorI((int)fTopLeft_uu.fX%lTileSet.fTileSize_uu.fX, (int)fTopLeft_uu.fY%lTileSet.fTileSize_uu.fY);
		   AngleVectorI div_uu=new AngleVectorI((int)fTopLeft_uu.fX/lTileSet.fTileSize_uu.fX, (int)fTopLeft_uu.fY/lTileSet.fTileSize_uu.fY);
			AngleVectorI uvDelta_tx=new AngleVectorI();
	
	      uvDelta_tx.fY=mod_uu.fY;
	     	tileSize_uu.fY=(lTileSet.fTileSize_uu.fY-mod_uu.fY)*lMap.fScale;
	      int col=div_uu.fX;
	      current_uu.fY=0;
		   while (current_uu.fY<lMap.fClipRect_uu.fSize.fY)
		   {
		      //drawRow
	         uvDelta_tx.fX=mod_uu.fX;
	         tileSize_uu.fX=(lTileSet.fTileSize_uu.fX-mod_uu.fX)*lMap.fScale;
		      int row=div_uu.fY;
	         current_uu.fX=0;
	         while (current_uu.fX<lMap.fClipRect_uu.fSize.fX)
	         {
	            //drawTile
			      if ((col>=0)&&(row>=0)&&(col<lMap.fWidth)&&(row<lMap.fHeight))
			      {
				      int tile=fData[row*lMap.fWidth+col];
	               if (tile>0)
	               {
							lTileSet.fillTextureValues(lTextureIV_tx,tile,uvDelta_tx,tileSize_uu);
	                  ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, lTextureIV_tx, 0);
	
							AngleVectorF tilePos_px=AngleRenderer.coordsUserToViewport(current_uu);
							AngleVectorF tileSixe_px=AngleRenderer.coordsUserToViewport(tileSize_uu);
	                  ((GL11Ext) gl).glDrawTexfOES(tilePos_px.fX, AngleRenderer.vViewportHeight_px - tilePos_px.fY - tilePos_px.fY, 0, tileSixe_px.fX, tileSixe_px.fY);
	               }
	      		}
	            //------
	            col++;
	            current_uu.fX+=tileSize_uu.fX;
	            tileSize_uu.fX=(lTileSet.fTileSize_uu.fX)*lMap.fScale;
	            if (tileSize_uu.fX>(lMap.fClipRect_uu.fSize.fX-current_uu.fX)*lMap.fScale)
	               tileSize_uu.fX=(lMap.fClipRect_uu.fSize.fX-current_uu.fX)*lMap.fScale;
	         }
	         //------
	         row++;
		      current_uu.fY+=tileSize_uu.fY;
	     		tileSize_uu.fY=(lTileSet.fTileSize_uu.fY)*lMap.fScale;
	         if (tileSize_uu.fY>(lMap.fClipRect_uu.fSize.fY-current_uu.fY)*lMap.fScale)
	         	tileSize_uu.fY=(lMap.fClipRect_uu.fSize.fY-current_uu.fY)*lMap.fScale;
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
