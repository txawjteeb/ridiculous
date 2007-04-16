package org.cart.igd.core;

import org.cart.igd.input.GameEventList;
import org.cart.igd.input.*;
import org.cart.igd.*;

public class Kernel
{
	public static Display display;
	public static UserInput userInput;
	public static DisplaySettings displaySettings;
	private InputHandler inputHandler;
	/**
	 * keep track of time and performance statistics
	 **/
	public static Profiler profiler;
	
	/** 
	 * collection of all the game event 
	 * note: refer to this for game logic 
	 **/
	public static GameEventList gameEventList;
	
	public Kernel()
	{
		profiler = new Profiler();
		profiler.start();
		
		gameEventList = new GameEventList();
		
		displaySettings = new DisplaySettings();
		
		display = new Display(displaySettings);
		display.start();
		
		userInput = new UserInput( display.getGLCanvas() );
		
		inputHandler = new InputHandler();
		inputHandler.start();
	}
	
	public static void main(String[] args)
	{
		Kernel kernel = new Kernel();
	}
}