package com.android.angle;

import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL11;

import android.view.SurfaceHolder;

/**
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * EGLHelper class extracted from GLView
 */

public class EGLHelper
{
	public EGLHelper()
	{

	}

	/**
	 * Initialize EGL for a given configuration spec.
	 * 
	 * @param configSpec
	 */
	public void start(int[] configSpec)
	{
		/*
		 * Get an EGL instance
		 */
		mEgl = (EGL10) EGLContext.getEGL();

		/*
		 * Get to the default display.
		 */
		mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

		/*
		 * We can now initialize EGL for that display
		 */
		int[] version = new int[2];
		mEgl.eglInitialize(mEglDisplay, version);

		EGLConfig[] configs = new EGLConfig[1];
		int[] num_config = new int[1];
		mEgl.eglChooseConfig(mEglDisplay, configSpec, configs, 1, num_config);
		mEglConfig = configs[0];

		/*
		 * Create an OpenGL ES context. This must be done only once, an OpenGL
		 * context is a somewhat heavy object.
		 */
		mEglContext = mEgl.eglCreateContext(mEglDisplay, mEglConfig,
				EGL10.EGL_NO_CONTEXT, null);

		mEglSurface = null;
	}

	/*
	 * Create and return an OpenGL surface
	 */
	public GL createSurface(SurfaceHolder holder)
	{
		/*
		 * The window size has changed, so we need to create a new surface.
		 */
		if (mEglSurface != null)
		{

			/*
			 * Unbind and destroy the old EGL surface, if there is one.
			 */
			mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
					EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
			mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
		}

		/*
		 * Create an EGL surface we can render into.
		 */
		mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay, mEglConfig,
				holder, null);

		/*
		 * Before we can issue GL commands, we need to make sure the context is
		 * current and bound to a surface.
		 */
		mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext);
		if (mEgl.eglGetError()==EGL11.EGL_CONTEXT_LOST)
			AngleMainEngine.mDirty=true;

		GL gl = mEglContext.getGL();
		return gl;
	}

	/**
	 * Display the current render surface.
	 * 
	 * @return false if the context has been lost.
	 */
	public boolean swap()
	{
		mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);

		/*
		 * Always check for EGL_CONTEXT_LOST, which means the context and all
		 * associated data were lost (For instance because the device went to
		 * sleep). We need to sleep until we get a new surface.
		 */
		return mEgl.eglGetError() != EGL11.EGL_CONTEXT_LOST;
	}

	public void finish()
	{
		boolean success=true;
		if (mEglSurface != null)
		{
			success&=mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
					EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
			success&=mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
			mEglSurface = null;
		}
		if (mEglContext != null)
		{
			success&=mEgl.eglDestroyContext(mEglDisplay, mEglContext);
			mEglContext = null;
		}
		if (mEglDisplay != null)
		{
			success&=mEgl.eglTerminate(mEglDisplay);
			mEglDisplay = null;
		}
		AngleMainEngine.mDirty=!success;
	}

	EGL10 mEgl;
	EGLDisplay mEglDisplay;
	EGLSurface mEglSurface;
	EGLConfig mEglConfig;
	EGLContext mEglContext;
}
