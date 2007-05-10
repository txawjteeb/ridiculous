package org.cart.igd.gui;

import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;

public class MenuGUI extends GUI
{
	Texture bg;
	Texture texMenuButtons[] = new Texture[8];
	GLRolloverButton btMenu[] = new GLRolloverButton[4];
	
	/** contains collision detection methods for UIComponents*/
	private UserInput input;
	
	/** 
	 * Constructor
	 * @param GameState gameState refference to allow state change 
	 */
	public MenuGUI(GameState gameState){
		super(gameState);//superclass GUI contain ref to GameState
		input = Kernel.userInput;
		loadImages();
	}
	
	public void loadImages()
	{
		bg = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/MainMenu_sa.png");
		
		texMenuButtons[0] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/start_button_jh.png");
		texMenuButtons[1] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/play_02.png");
		texMenuButtons[2] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/options_button_jh.png");
		texMenuButtons[3] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/settings_02.png");
		texMenuButtons[4] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/credits_button_jh.png");
		texMenuButtons[5] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/credits_02.png");
		texMenuButtons[6] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/exit_button_jh.png");
		texMenuButtons[7] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/exit_02.png");
		
		btMenu[0] = new GLRolloverButton(texMenuButtons[0],texMenuButtons[1],
				200,536,128,64);
		
		btMenu[1] = new GLRolloverButton(texMenuButtons[2],texMenuButtons[3],
				200,426,128,64);
		
		btMenu[2] = new GLRolloverButton(texMenuButtons[4],texMenuButtons[5],
				200,316,128,64);
		
		btMenu[3] = new GLRolloverButton(texMenuButtons[6],texMenuButtons[7],
				200,206,128,64);
		
	}
	
	public void update(long elapsedTime)
	{
		for(GLRolloverButton b: btMenu){
			b.update(input, elapsedTime);
		}
	}
	
	public void handleInput(long elapsedTime)
	{
		if(input.isSquareButtonPressed( btMenu[0] ) )
		{
			System.out.println();
			gameState.changeGameState("InGameState");
		}
	}
	
	public void render(GLGraphics g)
	{
		g.glgBegin();
		g.drawImage(bg, 0, 0);
		for( GLRolloverButton b: btMenu){
			b.draw(g);
		}
		g.glgEnd();
	}
		
}
