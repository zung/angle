package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sprite reference for dynamic add an remove from sprite engine
 * AngleSpritesEngine.useReferences must be set to true
 * 
 * @author Ivan Pajuelo
 *
 */
public class AngleSpriteReference extends AngleVisualObject
{
	private AngleSprite mSprite;

	public float mRotation; // Rotation
	
	/**
	 * 
	 * @param sprite Sprite referenced
	 */
	public AngleSpriteReference (AngleSprite sprite)
	{
		mSprite=sprite;
	}

	@Override
	public void draw(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glTranslatef(mCenter.mX, mCenter.mY, mZ);
		gl.glRotatef(-mRotation, 0, 0, 1);

		AngleTextureEngine.bindTexture(gl, mSprite.mTextureID);
		
		//Estas 3 alocatan memoria
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mSprite.mVertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mSprite.mTexCoordBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, AngleSprite.sIndexValues.length, GL10.GL_UNSIGNED_SHORT, mSprite.mIndexBuffer);
		//------------------------
		
		gl.glPopMatrix();
	}

}
