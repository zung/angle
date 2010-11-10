package com.alt90.angle2;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Fastest sprite with no rotation support
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSprite extends AngleAbstractSprite
{
	protected int[] lTextureIV; // Texture coordinates

	/**
	 * 
	 * @param layout AngleSpriteLayout
	 */
	public AngleSprite(AngleSpriteLayout layout)
	{
		super(layout);
		doInit(0, 0, 1);
	}

	/**
	 * 
	 * @param x Position
	 * @param y Position
	 * @param layout AngleSpriteLayout
	 */
	public AngleSprite(int x, int y, AngleSpriteLayout layout)
	{
		super(layout);
		doInit(x, y, 1);
	}
	
	/**
	 * 
	 * @param x Position
	 * @param y Position
	 * @param alpha Normalized alpha channel value
	 * @param layout AngleSpriteLayout
	 */
	public AngleSprite(int x, int y, float alpha, AngleSpriteLayout layout)
	{
		super(layout);
		doInit(x, y, alpha);
	}

	private void doInit(int x, int y, float alpha)
	{
		lTextureIV = new int[4];
		setLayout(lLayout);
		fPosition.set(x,y);
		fAlpha=alpha;
	}

	@Override
	public void setLayout(AngleSpriteLayout layout)
	{
		super.setLayout(layout);
		if (lLayout != null)
		{
			setFrame(0);
		}
	}

	@Override
	public void setFrame(int frame)
	{
		if (lLayout.fillTextureValues(frame, lTextureIV,fFlipHorizontal,fFlipVertical))
			lFrame = frame;
	}

	@Override
	public void draw(GL10 gl)
	{
		if (lLayout != null)
		{
			if (lLayout.bindTexture(gl))
			{
				gl.glColor4f(fRed, fGreen, fBlue, fAlpha);

				((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, lTextureIV, 0);
				
				AngleVector pos=lLayout.getPivot(lFrame);
				pos.mul(fScale);
				pos.subAt(AngleRenderer.coordsUserToScreen(fPosition));
				AngleVector size=lLayout.lDimensions;
				size.mul(fScale);

				((GL11Ext) gl).glDrawTexfOES(pos.fX, AngleRenderer.vViewportHeight - pos.fY, 0, size.fX, size.fY);

			}
		}
	}
}
