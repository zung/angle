package com.alt90.angle2;

public class AngleScene extends AngleObject
{
	public int fId;
	protected AngleSceneManager lManager;
	public float fVisible;
	
	public AngleScene(int id)
	{
		super ();
		fId=id;
		lManager=null;
		fVisible=0;
	}

	public AngleScene(int id, int maxChildren)
	{
		super (maxChildren);
		fId=id;
		lManager=null;
		fVisible=0;
	}

	public AngleScene(AngleSceneManager manager, int maxChildren)
	{
		super (maxChildren);
		fId=0;
		lManager=manager;
		fVisible=0;
	}

	public AngleScene(AngleSceneManager manager, int id, int maxChildren)
	{
		super (maxChildren);
		fId=id;
		lManager=manager;
		fVisible=0;
	}
	
	public void changeBy(int id)
	{
		if (lManager!=null)
			lManager.setScene(id);
	}

	protected void quit()
	{
		fVisible=0;
	}

	protected void put()
	{
		fVisible=1;
	}

}
