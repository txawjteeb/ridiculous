package org.cart.igd.states;

import java.util.HashMap;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class GameStateManager
{
	private HashMap<String,GameState> states;
	private String currentStateKey;
	
	public GameStateManager()
	{
		states = new HashMap<String,GameState>();
	}
	
	public GameState getCurrentState()
	{
		return states.get(currentStateKey);
	}
	
	public void setCurrentState(String key)
	{
		currentStateKey = key;
	}
	
	public void addGameState(GameState state, String key)
	{
		states.put(key, state);
	}
	
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