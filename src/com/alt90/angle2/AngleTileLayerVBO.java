package com.alt90.angle2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.util.Base64;
import android.util.Log;

public class AngleTileLayerVBO extends AngleObject
{
	private static final boolean sLogAngleTileLayerVBO = true;

   protected static final byte sVerticalFlip = 0x01;
   protected static final byte sHorizontalFlip = 0x02;
	private static final int COMPRESSION_GZIP = 0;
	private static final int COMPRESSION_ZLIB = 1;
	
	private int lVertexCount;
	private char[] lIndexValues;
	private int lIndexBufferIndex;
	public float[] fVertexValues;
	public int fVertBufferIndex;
	protected float[] lTexCoordValues;
	protected int lTextureCoordBufferIndex;

	public static AngleVectorF cDelta_uu=new AngleVectorF();
	public static AngleVectorF cTopLeftTileCorner_uu=new AngleVectorF();
	public static AngleVectorF cBottomRightTileCorner_uu=new AngleVectorF();

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
	public AngleVectorF fPosition_uu; //Set to change the position of the layer into the map
	private int lCompression;
	private int lColCount;
	private int lRowCount;

	AngleTileLayerVBO(AngleTileMap map)
	{
		lMap=map;
		lXMLTag="layer";
		properties=new XMLProperties();
		fColor=new AngleColor(AngleColor.cWhite);
		fPosition_uu=new AngleVectorF();
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
	
	public void generateIndexBuffer (GL10 gl)
	{
		if (lIndexBufferIndex<0)
		{
			if (sLogAngleTileLayerVBO)
				Log.d("AngleTileLayerVBO","generateIndexBuffer");
			int[] hwBuffers=new int[1];
			((GL11)gl).glGenBuffers(1, hwBuffers, 0);
	
			// Allocate and fill the index buffer.
			lIndexBufferIndex = hwBuffers[0];
			((GL11)gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, lIndexBufferIndex);
			((GL11)gl).glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, lVertexCount * 2, CharBuffer.wrap(lIndexValues), GL11.GL_STATIC_DRAW);
			((GL11)gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
	}

	public void releaseIndexBuffer (GL10 gl)
	{
		if (lIndexBufferIndex>=0)
		{
			if (sLogAngleTileLayerVBO)
				Log.d("AngleTileLayerVBO","releaseIndexBuffer");
			int[] hwBuffers = new int[1];
			hwBuffers[0]=lIndexBufferIndex;
			if (gl!=null)
				((GL11) gl).glDeleteBuffers(1, hwBuffers, 0);
			lIndexBufferIndex=-1;
		}
	}

	private void fillBuffers()
	{
		cDelta_uu.fX=fPosition_uu.fX%lMap.fTileWidth;
		cDelta_uu.fY=fPosition_uu.fY%lMap.fTileHeight;
		
		int cL = (int) Math.ceil((lMap.fClipRect_uu.fPosition.fX-cTopLeftTileCorner_uu.fX)/(lMap.fTileWidth*lMap.fScale));
		int rU = (int) Math.ceil((lMap.fClipRect_uu.fPosition.fY-cTopLeftTileCorner_uu.fY)/(lMap.fTileHeight*lMap.fScale));
		int cR = (int) Math.ceil((lMap.fClipRect_uu.fPosition.fX+lMap.fClipRect_uu.fSize.fX-cBottomRightTileCorner_uu.fX)/(lMap.fTileWidth*lMap.fScale));
		int rD = (int) Math.ceil((lMap.fClipRect_uu.fPosition.fY+lMap.fClipRect_uu.fSize.fY-cBottomRightTileCorner_uu.fY)/(lMap.fTileHeight*lMap.fScale));
		lColCount=cR-cL;
		lRowCount=rD-rU;
		lVertexCount=(lRowCount+1)*(lColCount+1);
		lIndexValues=new char[lVertexCount];
		fVertexValues=new float[lVertexCount];
		lTexCoordValues=new float[lVertexCount];
		for (int r=rU,vn=0;r<=rD;r++)
		{
		   for (int c=cL;c<=cR;c++)
		   {
		   	fVertexValues[vn++]=c*lMap.fTileWidth-cDelta_uu.fX;
		   	fVertexValues[vn++]=r*lMap.fTileHeight-cDelta_uu.fY;
		   }
		}
		//TODO rellenar lIndexValues y lTexCoordValues
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
