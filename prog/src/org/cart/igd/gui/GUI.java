package org.cart.igd.gui;

import org.cart.igd.gl2d.GLGraphics;
import org.cart.igd.states.GameState;

public abstract class GUI
{
	/** refference to the container GameState*/
	public GameState gameState;
	
	public boolean picked = false;
	public int pickedId = 0;
	
	public boolean pickingEnabled = true;
	
	public GUI(GameState gs)
	{
		gameState = gs;
	}
	
	public abstract void update(long elapsedTime);
	
	public abstract void render(GLGraphics g);
	
	public abstract void handleInput(long elapsedTime);
	
	public abstract void loadImages() throws java.io.IOException;
}