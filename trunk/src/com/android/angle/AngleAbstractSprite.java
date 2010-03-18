package com.android.angle;

/**
 * Sprite base class
 * @author Ivan
 *
 */
public abstract class AngleAbstractSprite extends AngleObject
{
	public AngleSpriteLayout roLayout; //Sprite Layout with information about how to draw the sprite
	public int roFrame; //Frame number. (ReadOnly)
	public AngleVector mPosition; //Set to change the position of the sprite
	public float mZ; // Z position (0=Near, 1=Far)
	public AngleVector mScale; //Set to change the scale of the sprite
	public float mRed;   //Red tint (0 - 1)
	public float mGreen;	//Green tint (0 - 1)
	public float mBlue;	//Blue tint (0 - 1)
	public float mAlpha;	//Alpha channel (0 - 1)

	public AngleAbstractSprite(AngleSpriteLayout layout)
	{
		mScale = new AngleVector(1, 1);
		mPosition = new AngleVector(0, 0);
		mZ = 0;
		roLayout=layout;
		mRed=1;
		mGreen=1;
		mBlue=1;
		mAlpha=1;
	}

	/**
	 * Set current frame number
	 * @param frame frame number
	 */
	public abstract void setFrame(int frame);
	public abstract void setFlip(boolean flipHorizontal, boolean flipVertical);

	public void setLayout(AngleSpriteLayout layout)
	{
		roLayout=layout;
	}
	
}
