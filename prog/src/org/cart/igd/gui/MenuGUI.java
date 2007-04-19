package org.cart.igd.gui;

import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;

public class MenuGUI extends GUI
{
	UILabel menuSelection[] = new UILabel[3];
	
	/** contains collision detection methods for UIComponents*/
	private UserInput input;
	
	/** pass in a refference from game state that contains this gui class 
	 * to allow for game state change and gui state change*/
	public MenuGUI(GameState gameState){
		super(gameState);//superclass GUI contain ref to GameState
		input = Kernel.userInput;
		loadImages();
	}
	
	public void loadImages()
	{
		menuSelection[0] = new UILabel("Start Game", 200, 400, 200, 16);
		menuSelection[0].scale(4);
		menuSelection[1] = new UILabel("Options", 200, 300, 200, 16);
		menuSelection[1].scale(4);
		menuSelection[2] = new UILabel("Quit", 200, 200, 200, 16);
		menuSelection[2].scale(4);
	}
	
	public void update(long elapsedTime)
	{
		
	}
	
	public void handleInput(long elapsedTime)
	{
		if(input.isSquareButtonPressed( menuSelection[0] ) )
		{
			System.out.println();
			gameState.changeGameState("InGameState");
		}
	}
	
	public void render(GLGraphics g)
	{
		g.glgBegin();
		menuSelection[0].draw(g,0,0,1f);
		menuSelection[1].draw(g,0,0,.5f);
		menuSelection[2].draw(g,0,0,.5f);
		g.glgEnd();
	}
		
}
