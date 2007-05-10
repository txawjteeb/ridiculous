package org.cart.igd.states;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.gui.GUI;

public abstract class GameState
{
	public String nextState;
	public boolean changeState = false; 
	public boolean waitToInit = false;
	public boolean initialized = false;
	protected ArrayList<GUI> gui = new ArrayList<GUI>();
	
	public int currentGuiState = 0;
	
	public GameState()
	{
		
	}
	
	public GameState(GL gl)
	{
		
	}
	
	public void init(GL gl, GLU glu)
	{
		initialized = true;
	}
	
	public void changeGameState(String nextState)
	{
		this.changeState = true;
		this.nextState = nextState;
	}
	
	public abstract void display(GL gl, GLU glu);
	
	public abstract void update(long elapsedTime);
	
	public abstract void handleInput(long elapsedTime);
	
	

	
	
}