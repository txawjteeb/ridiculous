package org.cart.igd;

import org.cart.igd.gl2d.GLGraphics;

public abstract class GUI
{
	
	public GUI()
	{
		
	}
	
	public abstract void render(GLGraphics g);
	
	public abstract void handleInput();
	public abstract void update(long elapsedTime);
	
	public abstract void loadImages() throws java.io.IOException;
}