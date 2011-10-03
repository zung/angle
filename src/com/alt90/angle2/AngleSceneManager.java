package com.alt90.angle2;

import javax.microedition.khronos.opengles.GL10;

public class AngleSceneManager extends AngleObject
{
	private AngleScene lCurrentScene;
	private AngleScene lOldScene;

	public AngleSceneManager(int maxScenes)
	{
		super (maxScenes);
		lCurrentScene=null;
		lOldScene=null;
	}
	
	private AngleScene findScene (int id)
	{
		for (int s=0;s<lChildrenCount;s++)
		{
			if (((AngleScene)lChildren[s]).fId==id)
				return (AngleScene)lChildren[s];
		}
		return null;
	}
	
	public void setScene (int id)
	{
		AngleScene scene=findScene(id);
		if (scene!=null)
		{
			if (lCurrentScene!=null)
			{
				lOldScene=lCurrentScene;
				lCurrentScene.quit();
			}
			lCurrentScene=scene;
			lCurrentScene.put();
		}
	}

	public void addScene (AngleScene scene)
	{
		scene.lManager=this;
		addObject(scene);
	}

	@Override
	public void draw(GL10 gl)
	{
		if (lCurrentScene!=null)
			lCurrentScene.draw(gl);
		if (lOldScene!=null)
			lOldScene.draw(gl);
	}

	@Override
	protected void step(float secondsElapsed)
	{
		if (lCurrentScene!=null)
			lCurrentScene.step(secondsElapsed);
		if (lOldScene!=null)
		{
			if (lOldScene.fVisible<=0)
				lOldScene=null;
			else
				lOldScene.step(secondsElapsed);
		}
	}
	
}
