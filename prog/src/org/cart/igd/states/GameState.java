package org.cart.igd.states;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.gui.GUI;

/**
 * GameState.java
 *
 * General Function: An abstract class to be inherited in order to manage game control and flow.
 */
public abstract class GameState
{
	/* Holds the identifier for the state following this one. Used by GameStateManager. */
	public String nextState;
	
	/* A flag for GameStateManager to tell whether to continue past this state. */
	public boolean changeState = false;
	
	/* A flag that tells the GameState to wait to initialize itself. */
	public boolean waitToInit = false;
	
	/* A flag to tell whether this GameState has been initialized in the GameStateManager. */
	public boolean initialized = false;
	
	/* Collection of GUI objects used by this GameState. */
	protected ArrayList<GUI> gui = new ArrayList<GUI>();
	
	/* The GUI index that is currently active. */
	public int currentGuiState = 0;
	
	/**
	 * Constructor
	 *
	 * @override
	 */
	public GameState()
	{
		
	}
	
	/**
	 * Constructor
	 *
	 * @override
	 *
	 * @param gl The GL instance.
	 */
	public GameState(GL gl)
	{
		
	}
	
	/**
	 * init
	 *
	 * General Function: Called when the state is first initialized.
	 *
	 * @param gl The GL instance.
	 * @param glu The GLU instance.
	 */
	public void init(GL gl, GLU glu)
	{
		initialized = true;
	}
	
	/**
	 * changeGameState
	 *
	 * General Function: Used by GameStateManager to cycle GameStates
	 *
	 * @param nextState The identifier for the next state to change to.
	 */
	public void changeGameState(String nextState)
	{
		this.changeState = true;
		this.nextState = nextState;
	}
	
	/**
	 * display
	 *
	 * General Function: Called every render cycle.
	 *
	 * @param gl The GL instance.
	 * @param glu The GLU instance.
	 */
	public abstract void display(GL gl, GLU glu);
	
	/**
	 * update
	 *
	 * General Function: Called to update game logic before rendering.
	 *
	 * @param elapsedTime The time in milliseconds since last render.
	 */
	public abstract void update(long elapsedTime);
	
	/**
	 * handleInput
	 *
	 * General Function: Called from update to handle user input.
	 *
	 * @param elapsedTime The time in milliseconds since last render.
	 */
	public abstract void handleInput(long elapsedTime);
}