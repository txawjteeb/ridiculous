package org.cart.igd.states;

import java.util.HashMap;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;


/**
 * GameStateManager.java
 *
 * General Function: Holds a collection of GameStates and decides which to use.
 */
public class GameStateManager
{
	/* Collection of all states. */
	private HashMap<String,GameState> states;
	
	/* Current state identifier. */
	private String currentStateKey;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of GameStateManager.
	 */
	public GameStateManager()
	{
		states = new HashMap<String,GameState>();
	}
	
	/**
	 * getCurrentState
	 *
	 * General Function: Returns the current GameState that is active.
	 */
	public GameState getCurrentState()
	{
		return states.get(currentStateKey);
	}
	
	/**
	 * setCurrentState
	 *
	 * General Function: Sets the current state identifier to a new value.
	 *
	 * @param key The state identifier to set as the current state identifier.
	 */
	public void setCurrentState(String key)
	{
		currentStateKey = key;
	}
	
	/**
	 * addGameState
	 *
	 * General Function: Adds a GameState to the collection of GameStates.
	 *
	 * @param state The GameState to add to the collection.
	 * @param key The GameState's identifier.
	 */
	public void addGameState(GameState state, String key)
	{
		states.put(key, state);
	}
	
	/**
	 * initStates
	 *
	 * General Function: Initializes all GameStates that aren't flagged to wait for initialization.
	 *
	 * @param gl The GL instance.
	 * @param glu The GLU instance.
	 */
	public void initStates(GL gl, GLU glu)
	{
		Iterator<GameState> i = states.values().iterator();
		while(i.hasNext())
		{
			GameState state = i.next();
			if(!state.waitToInit)
			{
				state.init(gl, glu);
			}
		}
	}
}