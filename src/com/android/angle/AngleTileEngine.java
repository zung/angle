package com.android.angle;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

public class AngleTileEngine extends AngleAbstractEngine
{
	public int mResourceID; 	//Resource bitmap
	public int mTextureID;  	//Texture ID on AngleTextureEngine
	protected int[] mTextureCrop = new int[4]; //Cropping coordinates
	public int mTexturePitch; 	//Horizontal tiles in texture
	public int mTileWidth;
	public int mTileHeight;
	public byte[] mMap;
	public int mMapWidth;
	public int mMapHeight;
	public int mLeft;
	public int mTop;
	public int mX;
	public int mY;
	public int mZ;
	private int mViewWidth;
	private int mViewHeight;
	
	AngleTileEngine (int resourceID, int texturePitch, int tileWidth, int tileHeight, int mapWidth, int mapHeight)
	{
		mResourceID=resourceID;
		mTexturePitch=texturePitch;
		mTileWidth=tileWidth;
		mTileHeight=tileHeight;
		mMapWidth=mapWidth;
		mMapHeight=mapHeight;
		mMap=new byte[mMapWidth*mMapHeight];
		mLeft=0;
		mTop=0;
		mX=0;
		mY=0;
		mZ=0;
		mTextureCrop[2] = mTileWidth; // Wcr
		mTextureCrop[3] = -mTileHeight; // Hcr
		mViewWidth=AngleMainEngine.mWidth/mTileWidth;
		mViewHeight=AngleMainEngine.mHeight/mTileHeight;
	}

	@Override
	public void afterLoadTextures(GL10 gl)
	{
	}

	//istream = getResources().openRawResource(R.raw.map);
	public void loadMap (InputStream istream)
	{
		try
		{
			istream.read(mMap);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void drawFrame(GL10 gl)
	{
		if (mTextureID>=0)
		{
			int offsetX=mLeft%mTileWidth;
			int offsetY=mTop%mTileHeight;
			int W=mViewWidth+((offsetX>0)?1:0);
			int H=mViewHeight+((offsetY>0)?1:0);
			AngleTextureEngine.bindTexture(gl, mTextureID);
			for (int y=0;y<H;y++)
			{
				for (int x=0;x<W;x++)
				{
					int mTileIdx=y*mMapWidth+x;
					int mTile=0;
					if ((mTileIdx>=0)&&(mTileIdx<mMapWidth*mMapHeight))
						mTile=mMap[mTileIdx];
					if (mTile>0)
					{
						mTextureCrop[0] = (mTile%mTexturePitch)*mTileWidth; // Ucr
						mTextureCrop[1] = (mTile/mTexturePitch)*mTileHeight + mTileHeight; // Vcr

						((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
							GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureCrop, 0);
	
						((GL11Ext) gl).glDrawTexfOES(mX-offsetX+x*mTileWidth,mY-offsetY+y*mTileHeight, mZ, mTileWidth, mTileHeight);
					}
				}
			}
		}
	}

	@Override
	public void loadTextures(GL10 gl)
	{
		mTextureID = AngleTextureEngine.createHWTextureFromResource(mResourceID);
	}

	@Override
	public void onDestroy(GL10 gl)
	{
	}
}
