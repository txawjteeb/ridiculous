package org.cart.igd.gui;

import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;

public class MenuGUI extends GUI
{
	UILabel menuSelection[] = new UILabel[3];
	private Texture texStartButton;
	
	/** contains collision detection methods for UIComponents*/
	private UserInput input;
	
	/** pass in a refference from game state that contains this gui class 
	 * to allow for game state change and gui state change*/
	public MenuGUI(GameState gameState){
		super(gameState);//superclass GUI contain ref to GameState
		input = Kernel.userInput;
		menuSelection[0] = new UILabel("Start Game", 200, 300, 200, 16);
		menuSelection[1] = new UILabel("Options", 200, 280, 200, 16);
		menuSelection[2] = new UILabel("Quit", 200, 260, 200, 16);
	}
	
	public void loadImages()
	{
		texStartButton = Kernel.display.getRenderer().loadImage(
			"data/images/buttons/bush_ico_big.png");
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
