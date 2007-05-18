package org.cart.igd.core;

import org.cart.igd.game.Global;
import org.cart.igd.input.InputHandler;
import org.cart.igd.input.UserInput;
import org.cart.igd.internet.Connect;
import org.cart.igd.sound.SoundSettings;
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
	
	public static SoundSettings soundSettings;
	
	public static volatile boolean displayRunning = false;
	
	public GameLauncher gameLauncher;
	
	/** 
	 * sotore important variables for logic
	 **/
	public static Global global;
	
	/** connection to server */
	public static Connect connect;
	
	public Kernel()
	{
		profiler = new Profiler();
		profiler.start();
		
		inputHandler = new InputHandler(profiler);
		inputHandler.start();
		
		gameLauncher = new GameLauncher(this);
	}
	
	/**
	 * initialized the engine and game
	 */
	public void init(DisplaySettings ds)
	{
		soundSettings = new SoundSettings(.5f,1f,1f,false);
		gameLauncher.setVisible(false);
		displaySettings = ds;
		display = new Display(ds);
		display.start();
		
		userInput = new UserInput( display.getGLCanvas() );
		
		global = new Global();
		connect= new Connect();
	}
	
	/**
	 * restart the game engine 
	 */
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