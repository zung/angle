package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Engine to draw strings of text
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleTextEngine extends AngleAbstractEngine
{
	private int mMaxFonts;
	private AngleFont[] mFonts;
	private int mFontsCount;
	private int mMaxStrings;
	private AngleString[] mStrings;
	private int mStringsCount;

	/**
	 * 
	 * @param maxFonts
	 *           Max Fonts available in engine
	 * @param maxStrings
	 *           Max Strings available in engine.
	 */
	public AngleTextEngine(int maxFonts, int maxStrings)
	{
		mMaxFonts = maxFonts;
		mMaxStrings = maxStrings;
		mFontsCount = 0;
		mStringsCount = 0;
		mFonts = new AngleFont[mMaxFonts];
		mStrings = new AngleString[mMaxStrings];
	}

	/**
	 * Adds a font to use with strings
	 * 
	 * @param font
	 *           Font to be added
	 */
	public synchronized void addFont(AngleFont font)
	{
		if (mFontsCount < mMaxFonts)
		{
			for (int s = 0; s < mFontsCount; s++)
				if (mFonts[mFontsCount] == font)
					return;
			mFonts[mFontsCount++] = font;
		}
	}

	/**
	 * Removes a font
	 * 
	 * @param font
	 *           Font to be removed
	 */
	public synchronized void removeFont(AngleFont font)
	{
		int r;

		for (r = 0; r < mFontsCount; r++)
			if (mFonts[r] == font)
				break;

		if (r < mFontsCount)
		{
			mFontsCount--;
			for (int d = r; d < mFontsCount; d++)
				mFonts[d] = mFonts[d + 1];
			mFonts[mFontsCount] = null;
		}
	}

	/**
	 * Adds a string
	 * 
	 * @param string
	 *           String to be added
	 */
	public synchronized void addString(AngleString string)
	{
		if (mStringsCount < mMaxStrings)
		{
			for (int s = 0; s < mStringsCount; s++)
				if (mStrings[mStringsCount] == string)
					return;
			mStrings[mStringsCount++] = string;
		}
	}

	/**
	 * Removes a string
	 * 
	 * @param string
	 *           String to be removed
	 */
	public synchronized void removeString(AngleString string)
	{
		int r;

		for (r = 0; r < mStringsCount; r++)
			if (mStrings[r] == string)
				break;

		if (r < mStringsCount)
		{
			mStringsCount--;
			for (int d = r; d < mStringsCount; d++)
				mStrings[d] = mStrings[d + 1];
			mStrings[mStringsCount] = null;
		}
	}

	public void drawFrame(GL10 gl)
	{
		if (!AngleTextureEngine.hasChanges)
		{
			for (int s = 0; s < mStringsCount; s++)
				mStrings[s].draw(gl);
		}
		super.drawFrame(gl);
	}

	public void loadTextures(GL10 gl)
	{
		for (int s = 0; s < mFontsCount; s++)
			mFonts[s].loadTexture(gl);
		super.loadTextures(gl);
	}

	public void onDestroy(GL10 gl)
	{
		for (int s = 0; s < mFontsCount; s++)
			mFonts[s] = null;
		mFontsCount = 0;
		for (int s = 0; s < mStringsCount; s++)
			mStrings[s] = null;
		mStringsCount = 0;
		super.onDestroy(gl);
	}
}
