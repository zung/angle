package com.android.ace;

public interface ACEObject
{
	void addToView(ACEViewGroup view);
	void removeFromView();
	void step(float secondsElapsed);
}
