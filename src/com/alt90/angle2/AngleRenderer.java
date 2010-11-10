package com.alt90.angle2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class AngleRenderer implements Renderer
{
	public static float vViewportHeight=0;
	private static AngleRect lViewport=null; //TODO Intentar que sea privado
	private static AngleVector lUserExtent=null;
	private static AngleObject lRenderTree=null;
	private static long lCTM;

	public AngleRenderer(float width, float height)
	{
		lUserExtent=new AngleVector(width, height);
	}
	
	public static void setRenderTree(AngleObject newRenderTree)
	{
		lRenderTree=newRenderTree;
	}
	
	public static boolean isRendering(AngleObject object)
	{
		return (object.getRoot()==lRenderTree);
	}

	public static AngleVector coordsUserToScreen(AngleVector coords)
	{
		AngleVector result=new AngleVector(coords);
		result.div(lUserExtent);
		result.mul(lViewport.fSize);
		result.add(lViewport.fPosition);

		return result;
	}

	public static AngleVector coordsScreenToUser(AngleVector coords)
	{
		AngleVector result=new AngleVector(coords);
		result.sub(lViewport.fPosition);
		result.div(lViewport.fSize);
		result.mul(lUserExtent);

		return result;
	}

	private static float secondsElapsed()
	{
		float secondsElapsed = 0;
		long CTM = android.os.SystemClock.uptimeMillis();
		if (lCTM > 0)
			secondsElapsed = (CTM - lCTM) / 1000.f;
		lCTM = CTM;
		return secondsElapsed;
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		vViewportHeight=lViewport.fPosition.fY;
      gl.glMatrixMode(GL10.GL_MODELVIEW);
      gl.glLoadIdentity();
		if (lRenderTree!=null)
		{
			AngleObject.processInQueue();
			lRenderTree.step(secondsElapsed());
			lRenderTree.draw(gl);
			AngleObject.processOutQueue();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int surfaceWidth, int surfaceHeight)
	{
		lViewport=new AngleRect(0,0,surfaceWidth,surfaceHeight);

		if (!lUserExtent.isZero())
		{
			if ((lUserExtent.fX / lUserExtent.fY) > (surfaceWidth / surfaceHeight))
				lViewport.fSize.fY = (int) (lViewport.fSize.fX * lUserExtent.fY / lUserExtent.fX);
			else
				lViewport.fSize.fX = (int) (lViewport.fSize.fY * lUserExtent.fX / lUserExtent.fY);

			lViewport.fPosition.fX = surfaceWidth/2 - lViewport.fSize.fX/2;
			lViewport.fPosition.fY = surfaceHeight/2 - lViewport.fSize.fY/2;
		}
		gl.glViewport((int)lViewport.fPosition.fX, (int)lViewport.fPosition.fY, (int)lViewport.fSize.fX, (int)lViewport.fSize.fY);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, lViewport.fSize.fX, lViewport.fSize.fY, 0.0f, 0.0f, 1.0f);
		
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
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		AngleTextureEngine.loadTextures(gl);
	}

}
