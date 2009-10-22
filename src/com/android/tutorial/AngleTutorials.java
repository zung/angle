package com.android.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Set of tutorials to learn to use Angle.
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
		findViewById(R.id.tut02).setOnClickListener(this);
		findViewById(R.id.tut03).setOnClickListener(this);
		findViewById(R.id.tut04).setOnClickListener(this);
		findViewById(R.id.tut05).setOnClickListener(this);
		findViewById(R.id.tut06).setOnClickListener(this);
		findViewById(R.id.tut07).setOnClickListener(this);
		findViewById(R.id.tut08).setOnClickListener(this);
	}

	public void onClick(View v)
	{
		Intent intent;
		switch (v.getId())
		{
			case R.id.tut01:
				intent = new Intent(this, Tutorial01.class);
				startActivity(intent);
				break;
			case R.id.tut02:
				intent = new Intent(this, Tutorial02.class);
				startActivity(intent);
				break;
			case R.id.tut03:
				intent = new Intent(this, Tutorial03.class);
				startActivity(intent);
				break;
			case R.id.tut04:
				intent = new Intent(this, Tutorial04.class);
				startActivity(intent);
				break;
			case R.id.tut05:
				intent = new Intent(this, Tutorial05.class);
				startActivity(intent);
				break;
			case R.id.tut06:
				intent = new Intent(this, Tutorial06.class);
				startActivity(intent);
				break;
			case R.id.tut07:
				intent = new Intent(this, Tutorial07.class);
				startActivity(intent);
				break;
			case R.id.tut08:
				intent = new Intent(this, Tutorial08.class);
				startActivity(intent);
				break;
		}
	}
}