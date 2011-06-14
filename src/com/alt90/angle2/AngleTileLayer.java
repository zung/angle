package com.alt90.angle2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

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
   protected static final byte sVerticalFlip = 0x01;
   protected static final byte sHorizontalFlip = 0x02;
	private static final int COMPRESSION_GZIP = 0;
	private static final int COMPRESSION_ZLIB = 1;
   private static AngleVectorI cCurrent_uu=new AngleVectorI();
   private static AngleVectorI cTileSize_uu=new AngleVectorI();
   private static AngleVectorF cTileSizeScaled_uu=new AngleVectorF();
   private static AngleVectorI cMod_uu=new AngleVectorI();
   private static AngleVectorI cDiv_uu=new AngleVectorI();
   private static AngleVectorI cUVDelta_tx=new AngleVectorI();

	protected static int[] lTextureIV_tx=new int[4]; // Texture coordinates
	protected static float[] lTextureFV_tx=new float[4]; // Texture coordinates
	private XMLProperties properties;
	private AngleTileMap lMap;
	private AngleTileSet lTileSet;
	private byte[] fByteData;
	private short[] fData;
	private byte[] fFlags;
	protected int fMinGid;
	protected int fMaxGid;
	public int fVisible;
	public AngleColor fColor; //Set to change layer tint color and alpha
	public AngleVectorF fTopLeft_uu; //Set to change the position of the layer into the map
	private int lCompression;

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

	protected void beginCheck () throws Exception
	{
		lTileSet=null;
		fMinGid=Integer.MAX_VALUE;
		fMaxGid=Integer.MIN_VALUE;
		fData=new short[lMap.fWidth*lMap.fHeight];
		fFlags=new byte[lMap.fWidth*lMap.fHeight];
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
				fFlags[y*lMap.fWidth+x]=(byte) (gid>>30);
				fFlags[y*lMap.fWidth+x]|=AngleTileLayer.sHorizontalFlip;
				//fFlags[y*lMap.fWidth+x]|=AngleTileLayer.sVerticalFlip;
				if ((gid&0x3FFFFFFF)>0x7FFF)
					throw new Exception("Don't use GIDs greater than 7FFFh");
				gid&=0x7FFF;
				if ((fMinGid>gid)&&(gid>0))
					fMinGid=gid;
				if (fMaxGid<gid)
					fMaxGid=gid;
				fData[y*lMap.fWidth+x]=(short) gid;
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
		   
		   //Solve negative modules
		   if (cMod_uu.fX<0)
		   	cMod_uu.fX+=lMap.fTileWidth;
		   if (cMod_uu.fY<0)
		   	cMod_uu.fY+=lMap.fTileHeight;
			
	      cUVDelta_tx.fY=cMod_uu.fY;
	     	cTileSize_uu.fY=lMap.fTileHeight-cMod_uu.fY;
         cTileSizeScaled_uu.fY=cTileSize_uu.fY*lMap.fScale;
	      int row=cDiv_uu.fY;
	      cCurrent_uu.fY=0;
		   while (cTileSize_uu.fY>0)
		   {
		      //drawRow
	         cUVDelta_tx.fX=cMod_uu.fX;
	         cTileSize_uu.fX=lMap.fTileWidth-cMod_uu.fX;
            cTileSizeScaled_uu.fX=cTileSize_uu.fX*lMap.fScale;
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
							lTileSet.fillTextureValues(lTextureIV_tx,tile-1,cUVDelta_tx,cTileSize_uu,fFlags[row*lMap.fWidth+col]);
							lTileSet.fillTextureValues(lTextureFV_tx,tile-1,cUVDelta_tx,cTileSize_uu,fFlags[row*lMap.fWidth+col]);
	                  ((GL11) gl).glTexParameterfv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, lTextureFV_tx, 0);
	
	                  ((GL11Ext) gl).glDrawTexfOES(
	                  		(cCurrent_uu.fX*lMap.fScale+lMap.fClipRect_uu.fPosition.fX)*AngleRenderer.vHorizontalFactor_px, 
	                  		AngleRenderer.vViewportHeight_px - (cCurrent_uu.fY*lMap.fScale+lMap.fClipRect_uu.fPosition.fY+cTileSizeScaled_uu.fY)*AngleRenderer.vVerticalFactor_px,
	                  		0, 
	                  		cTileSizeScaled_uu.fX*AngleRenderer.vHorizontalFactor_px, 
	                  		cTileSizeScaled_uu.fY*AngleRenderer.vVerticalFactor_px);
	               }
	      		}
	            //------
	            col++;
	            cCurrent_uu.fX+=cTileSize_uu.fX;
	            cTileSize_uu.fX=lMap.fTileWidth;
	            cTileSizeScaled_uu.fX=cTileSize_uu.fX*lMap.fScale;
	            if (cTileSizeScaled_uu.fX>(lMap.fClipRect_uu.fSize.fX-cCurrent_uu.fX*lMap.fScale))
	            {
	               cTileSizeScaled_uu.fX=((lMap.fClipRect_uu.fSize.fX-cCurrent_uu.fX*lMap.fScale));
	               cTileSize_uu.fX=(int) (cTileSizeScaled_uu.fX/lMap.fScale);
	            }
	            cUVDelta_tx.fX=0;
	         }
	         //------
	         row++;
		      cCurrent_uu.fY+=cTileSize_uu.fY;
	     		cTileSize_uu.fY=lMap.fTileHeight;
            cTileSizeScaled_uu.fY=cTileSize_uu.fY*lMap.fScale;
	         if (cTileSizeScaled_uu.fY>(lMap.fClipRect_uu.fSize.fY-cCurrent_uu.fY*lMap.fScale))
	         {
	         	cTileSizeScaled_uu.fY=((lMap.fClipRect_uu.fSize.fY-cCurrent_uu.fY*lMap.fScale));
               cTileSize_uu.fY=(int) (cTileSizeScaled_uu.fY/lMap.fScale);
	         }
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
		{
			if (lMap.fWidth!=Integer.parseInt(value))
				throw new Exception("Map and layer width must be the same");
		}
		else if (param.equals("height"))
		{
			if (lMap.fHeight!=Integer.parseInt(value))
				throw new Exception("Map and layer height must be the same");
		}
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
		
      fByteData=new byte[lMap.fWidth*lMap.fHeight*4];
      byte[] dec = Base64.decode(lXMLParser.getText().trim(),Base64.DEFAULT);
      InputStream is=null;
      if (lCompression==COMPRESSION_GZIP)
      	is=new GZIPInputStream(new ByteArrayInputStream(dec));
      else if (lCompression==COMPRESSION_ZLIB)
      	is=new InflaterInputStream(new ByteArrayInputStream(dec));
      if (is!=null)
      {
      	is.read(fByteData);
      	is.close();
      }
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
			if (value.equals("gzip"))
				lCompression=COMPRESSION_GZIP;
			else if (value.equals("zlib"))
				lCompression=COMPRESSION_ZLIB;
			else
				throw new Exception ("Only GZip and ZLib compression supported.");
		}
	}

}


