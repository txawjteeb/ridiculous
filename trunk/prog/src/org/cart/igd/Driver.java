package org.cart.igd;

import org.cart.igd.input.GameEventList;
import org.cart.igd.input.*;

/** todo: rename to Kernel and make this a starting point for 
 * different components of the program*/
public class Driver
{
	public static Display display;
	public static UserInput userInput;
	
	/** 
	 * collection of all the game event
	 * note: refer to this for game logic 
	 **/
	public static GameEventList gameEventList;
	
	public static void main(String[] args)
	{
		gameEventList = new GameEventList();
		
		display = Display.createDisplay("Project Ridiculous");
		display.start();
	}
}
