package com.alt90.angle2;

/**
 * Sprite base class
 * @author Ivan Pajuelo
 *
 */
public abstract class AngleAbstractSprite extends AngleObject
{
	protected AngleSpriteLayout lLayout; //Sprite Layout with information about how to draw the sprite
	protected int lFrame; //Frame number. (ReadOnly)
	public AngleVector fPosition; //Set to change the position of the sprite
	public AngleVector fScale; //Set to change the scale of the sprite
	public boolean fFlipHorizontal;
	public boolean fFlipVertical;
	public float fRed;   //Red tint (0 - 1)
	public float fGreen;	//Green tint (0 - 1)
	public float fBlue;	//Blue tint (0 - 1)
	public float fAlpha;	//Alpha channel (0 - 1)

	/**
	 * Every sprite needs an AngleSpriteLayout to know how to draw itself
	 * @param layout the AngleSpriteLayout
	 */
	public AngleAbstractSprite(AngleSpriteLayout layout)
	{
		fScale = new AngleVector(1, 1);
		fPosition = new AngleVector(0, 0);
		lLayout=layout;
		fRed=1;
		fGreen=1;
		fBlue=1;
		fAlpha=1;
	}

	/**
	 * Set current frame number
	 * @param frame frame number
	 */
	public abstract void setFrame(int frame);
	
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
}
