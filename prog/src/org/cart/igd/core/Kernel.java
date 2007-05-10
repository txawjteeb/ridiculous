package org.cart.igd.core;

import org.cart.igd.input.GameEventList;
import org.cart.igd.input.*;
import org.cart.igd.*;

/**
 * Kernel.java
 * 
 * General Function: To keep track of all game's operations.
 */
public class Kernel
{
	public static Display display;
	public static UserInput userInput;
	public DisplaySettings displaySettings;
	private InputHandler inputHandler;
	/**
	 * keep track of time and performance statistics
	 **/
	public static Profiler profiler;
	
	public static volatile boolean displayRunning = false;
	
	public GameLauncher gameLauncher;
	
	/** 
	 * collection of all the game event 
	 * note: refer to this for game logic 
	 **/
	public static GameEventList gameEventList;
	
	public Kernel()
	{
		profiler = new Profiler();
		profiler.start();
		
		inputHandler = new InputHandler(profiler);
		inputHandler.start();
		
		gameLauncher = new GameLauncher(this);
	}
	
	public void init(DisplaySettings ds)
	{
		gameLauncher.setVisible(false);
		displaySettings = ds;
		display = new Display(ds);
		display.start();
		
		userInput = new UserInput( display.getGLCanvas() );
		
		gameEventList = new GameEventList();
	}
	
	public void reset(){
		displayRunning = false;
		display.stop();
		init(displaySettings);
		display = null;
		userInput = null;
	}
	
	public static void main(String[] args)
	{
		Kernel kernel = new Kernel();
	}
}