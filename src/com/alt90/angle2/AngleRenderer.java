package com.alt90.angle2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

/**
 * Main renderer
 * @author Ivan Pajuelo
 *
 */
public class AngleRenderer implements Renderer
{
	public static final boolean sUseHWBuffers = true; //UBW Determines if hardware buffers are used
	private static long lCTM;
	private static AngleRect lViewport_px=null; 
	private static AngleVectorF lUserExtent_uu=new AngleVectorF(0,0);
	private static AngleObject lRenderTree=null;

	//Only for reading. DO NOT change this filed. It's never updated
	public static AngleVectorF rViewportExtent_uu=new AngleVectorF(0,0); //Dimensions of viewport in user units 
	//Virtual fields for scaled rendering. Updated on surface changed
	public static float vViewportHeight_px=0;		//Viewport height in pixels
	public static float vHorizontalFactor_uu=0;  //Relation factor between viewport horizontal uu and px (uu/px)
	public static float vVerticalFactor_uu=0;    //Relation factor between viewport vertical uu and px (uu/px)
	public static float vHorizontalFactor_px=0;  //Inverted relation factors (px/uu)
	public static float vVerticalFactor_px=0;    //      "           "

	/**
	 * Create new renderer 
	 * @param width_uu Width in user units
	 * @param height_uu Height in user units
	 */
	public AngleRenderer(float width_uu, float height_uu)
	{
		lUserExtent_uu.set(width_uu, height_uu);
		rViewportExtent_uu.set(lUserExtent_uu);
	}
	
	/**
	 * Set the current tree for render
	 * @param newRenderTree Root object of the current render tree
	 */
	public static void setRenderTree(AngleObject newRenderTree)
	{
		lRenderTree=newRenderTree;
	}
	
	/**
	 * Check if object in in the current render tree
	 * @param object Object to check
	 * @return True if the object is rendering in current tree
	 */
	public static boolean isRendering(AngleObject object)
	{
		return (object.getRoot()==lRenderTree);
	}

	/**
	 * Converts user units to pixels within viewport
	 * @param fPosition coordinates in user units
	 * @return coordinates in pixels (viewport pixels)
	 */
	public static AngleVectorF coordsUserToViewport(AngleVectorI fPosition)
	{
		AngleVectorF result=new AngleVectorF(fPosition);
		result.div(lUserExtent_uu);
		result.mul(lViewport_px.fSize);

		return result;
	}
	public static AngleVectorF coordsUserToViewport(AngleVectorF fPosition)
	{
		AngleVectorF result=new AngleVectorF(fPosition);
		result.div(lUserExtent_uu);
		result.mul(lViewport_px.fSize);

		return result;
	}

	/**
	 * Converts pixels to user units within viewport
	 * @param coords_uu coordinates in pixels (viewport pixels)
	 * @return coordinates in user units
	 */
	public static AngleVectorF coordsViewportToUser(AngleVectorF coords_px)
	{
		AngleVectorF result=new AngleVectorF(coords_px);
		result.div(lViewport_px.fSize);
		result.mul(lUserExtent_uu);

		return result;
	}

	/**
	 * Converts global (screen) pixels to user units
	 * @param coords_uu coordinates in pixels (screen pixels)
	 * @return coordinates in user units
	 */
	public static AngleVectorF coordsScreenToUser(AngleVectorF coords_px)
	{
		AngleVectorF result=new AngleVectorF(coords_px);
		result.sub(lViewport_px.fPosition);
		result.div(lViewport_px.fSize);
		result.mul(lUserExtent_uu);

		return result;
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
      gl.glMatrixMode(GL10.GL_MODELVIEW);
      gl.glLoadIdentity();
		if (lRenderTree!=null)
		{
			float secondsElapsed = 0; //Seconds elapsed since last frame
			long CTM = android.os.SystemClock.uptimeMillis();
			if (lCTM > 0)
				secondsElapsed = (CTM - lCTM) / 1000.f;
			lCTM = CTM;

			AngleObject.processInQueue();
			lRenderTree.step(secondsElapsed);
			lRenderTree.draw(gl);
			AngleObject.processOutQueue();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int surfaceWidth, int surfaceHeight)
	{
		lViewport_px=new AngleRect(0,0,surfaceWidth,surfaceHeight);

		if (!lUserExtent_uu.isZero())
		{
			if ((lUserExtent_uu.fX / lUserExtent_uu.fY) > (surfaceWidth / surfaceHeight))
				lViewport_px.fSize.fY = (int) (lViewport_px.fSize.fX * lUserExtent_uu.fY / lUserExtent_uu.fX);
			else
				lViewport_px.fSize.fX = (int) (lViewport_px.fSize.fY * lUserExtent_uu.fX / lUserExtent_uu.fY);

			lViewport_px.fPosition.fX = surfaceWidth/2 - lViewport_px.fSize.fX/2;
			lViewport_px.fPosition.fY = surfaceHeight/2 - lViewport_px.fSize.fY/2;
		}
		gl.glViewport((int)lViewport_px.fPosition.fX, (int)lViewport_px.fPosition.fY, (int)lViewport_px.fSize.fX, (int)lViewport_px.fSize.fY);
		
		vViewportHeight_px=lViewport_px.fSize.fY;
		vHorizontalFactor_uu=lUserExtent_uu.fX/lViewport_px.fSize.fX;
		vVerticalFactor_uu=lUserExtent_uu.fY/lViewport_px.fSize.fY;
		vHorizontalFactor_px=lViewport_px.fSize.fX/lUserExtent_uu.fX;
		vVerticalFactor_px=lViewport_px.fSize.fY/lUserExtent_uu.fY;

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, lViewport_px.fSize.fX, lViewport_px.fSize.fY, 0.0f, 0.0f, 1.0f);
		
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glDisable(GL10.GL_DITHER);
		gl.glDisable(GL10.GL_MULTISAMPLE);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

      gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1, 1, 1, 1);
		gl.glClearColor(0, 0, 0, 1);
		
		if (lRenderTree!=null)
			lRenderTree.invalidateHardwareBuffers(gl);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		AngleTextureEngine.loadTextures(gl);
	}

	public static int fitPow2(int ammount)
	{
		for (int p=0;p<32;p++)
		{
			if (ammount<(1<<p))
				return (1<<p);
		}
		return 0;
	}

}
