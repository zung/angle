package com.android.angle;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

public class AngleTileEngine extends AngleAbstractEngine
{
	public int mResourceID; // Resource bitmap
	public int mTextureID; // Texture ID on AngleTextureEngine
	protected int[] mTextureCrop = new int[4]; // Cropping coordinates
	public int mTexturePitch; // Horizontal tiles in texture
	public int mTileWidth;
	public int mTileHeight;
	public byte[] mMap;
	public int mMapWidth;
	public int mMapHeight;
	public float mLeft;
	public float mTop;
	public float mX;
	public float mY;
	public float mZ;
	public int mViewWidth;
	public int mViewHeight;
	public int mTilesCount;

	public AngleTileEngine(int resourceID, int texturePitch, int tilesCount,
			int tileWidth, int tileHeight, int mapWidth, int mapHeight)
	{
		mResourceID = resourceID;
		mTextureID = -1;
		mTexturePitch = texturePitch;
		mTilesCount = tilesCount;
		mTileWidth = tileWidth;
		mTileHeight = tileHeight;
		mMapWidth = mapWidth;
		mMapHeight = mapHeight;
		mMap = new byte[mMapWidth * mMapHeight];
		mLeft = 0;
		mTop = 0;
		mX = 0;
		mY = 0;
		mZ = 0;
		mTextureCrop[2] = mTileWidth; // Wcr
		mTextureCrop[3] = -mTileHeight; // Hcr
	}

	@Override
	public void afterLoadTextures(GL10 gl)
	{
	}

	public void loadMap(InputStream istream)
	{
		try
		{
			istream.read(mMap, 0, mMapWidth * mMapHeight);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void drawFrame(GL10 gl)
	{
		if (mTextureID >= 0)
		{
			if (!AngleTextureEngine.hasChanges)
			{
				int offsetX = (int) mLeft % mTileWidth;
				int offsetY = (int) mTop % mTileHeight;
				int W = mViewWidth + ((offsetX > 0) ? 1 : 0);
				int H = mViewHeight + ((offsetY > 0) ? 1 : 0);
				AngleTextureEngine.bindTexture(gl, mTextureID);
				for (int y = 0; y < H; y++)
				{
					for (int x = 0; x < W; x++)
					{
						int mTileIdx = ((y + ((int) mTop / mTileHeight)) * mMapWidth
								+ x + ((int) mLeft / mTileWidth));
						int mTile = 0;
						if ((mTileIdx >= 0) && (mTileIdx < mMapWidth * mMapHeight))
							mTile = mMap[mTileIdx];
						if (mTile < mTilesCount)
						{
							mTextureCrop[0] = (mTile % mTexturePitch) * mTileWidth; // Ucr
							mTextureCrop[1] = (mTile / mTexturePitch) * mTileHeight
									+ mTileHeight; // Vcr

							((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
									GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureCrop, 0);

							((GL11Ext) gl).glDrawTexfOES(
									mX - offsetX + x * mTileWidth,
									AngleMainEngine.mHeight
											- (mY - offsetY + y * mTileHeight), mZ,
									mTileWidth, mTileHeight);
						}
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
