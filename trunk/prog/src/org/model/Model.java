package org.cart.igd.model;

public interface Model
{
	public void render(float percent);
	
	public int getMaximumNumberOfStates();
	
	public String getName();
	
	public int getNextFrame();
	
	public int getState();
	
	public int getStateEndFrame();
	
	public boolean isAnimationCompleted();
	
	public void setState(int state);
	
	public int getNumberOfPolygons();
}
