package com.android.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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
		findViewById(R.id.tut99).setOnClickListener(this);
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
			case R.id.tut99:
				intent = new Intent(this, Tutorial99.class);
				startActivity(intent);
				break;
		}
	}
}