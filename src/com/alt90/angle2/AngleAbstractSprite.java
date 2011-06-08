package com.alt90.angle2;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sprite base class
 * @author Ivan Pajuelo
 *
 */
public abstract class AngleAbstractSprite extends AngleObject
{
	protected AngleSpriteLayout lLayout; //Sprite Layout with information about how to draw the sprite
	protected int lFrame; //Frame number. (ReadOnly)
	public AngleVectorF fPosition_uu; //Set to change the position of the sprite
	public AngleVectorF fScale; //Set to change the scale of the sprite
	public boolean fFlipHorizontal; //Set to flip sprite horizontally
	public boolean fFlipVertical; //Set to flip sprite vertically
	public AngleColor fColor; //Set to change sprite tint color and alpha

	/**
	 * Every sprite needs an AngleSpriteLayout to know how to draw itself
	 * @param layout the AngleSpriteLayout
	 */
	public AngleAbstractSprite(AngleSpriteLayout layout)
	{
		fScale = new AngleVectorF(1, 1);
		fPosition_uu = new AngleVectorF(0, 0);
		lLayout=layout;
		fColor=AngleColor.cWhite;
	}

	/**
	 * Set current frame number
	 * @param frame frame number
	 */
	public abstract void setFrame(int frame);

	/**
	 * Draw all children in block
	 * @param gl
	 */
	public abstract void blockDraw(GL10 gl);

	/**
	 * Set if sprite is flipped horizontally or vertically  
	 * @param flipHorizontal
	 * @param flipVertical
	 */
	public void setFlip(boolean flipHorizontal, boolean flipVertical)
	{
		fFlipHorizontal=flipHorizontal;
		fFlipVertical=flipVertical;
		setFrame(lFrame);
	}
	
	/**
	 * Change the AngleSpriteLayout
	 * @param layout
	 */
	public void setLayout(AngleSpriteLayout layout)
	{
		lLayout=layout;
	}

	public void draw(GL10 gl)
	{
		if (lLayout != null)
			if (lLayout.bindTexture(gl))
				blockDraw (gl);
	}
}
