package org.cart.igd.core;

import org.cart.igd.input.GameEventList;
import org.cart.igd.input.*;
import org.cart.igd.*;

public class Kernel
{
	public static Display display;
	public static UserInput userInput;
	
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
		
		display = Display.createDisplay("Project Ridiculous");
		display.start();
		
		userInput = new UserInput( display.getGLCanvas() );
	}
	
	
	
	
	public static void main(String[] args)
	{
		Kernel kernel = new Kernel();
	}
}