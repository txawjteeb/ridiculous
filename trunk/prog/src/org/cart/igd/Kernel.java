package org.cart.igd;

import org.cart.igd.input.GameEventList;
import org.cart.igd.input.*;

public class Kernel
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
		
		userInput = new UserInput( display.getGLCanvas() );
	}
}