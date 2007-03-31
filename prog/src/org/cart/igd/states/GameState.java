package org.cart.igd.states;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public abstract class GameState
{
	public boolean waitToInit = false;
	public boolean initialized = false;

	public GameState()
	{
	}
	
	public GameState(GL gl)
	{
	}
	
	public abstract void display(GL gl, GLU glu);
	
	public abstract void update(long elapsedTime);
	
	public void init(GL gl, GLU glu)
	{
		initialized = true;
	}
	
	public abstract void handleInput(long elapsedTime);
}