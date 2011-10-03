package com.alt90.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Set of tutorials to learn how to use ANGLE.
 * >Serie de tutoriales para aprender a usar ANGLE
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleTutorials extends Activity implements OnClickListener
{
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.main);
		findViewById(R.id.tut01).setOnClickListener(this);
	}

	public void onClick(View v)
	{
		Intent intent;
		switch (v.getId())
		{
			case R.id.tut01:
				intent = new Intent(this, TestAngle2.class);
				startActivity(intent);
				break;
		}
	}
}