package org.cart.igd.states;

import org.cart.igd.states.GameState;
import org.cart.igd.input.*;
import org.cart.igd.core.*;
import org.cart.igd.*;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;


public class MenuState extends GameState
{
	private GameAction selectOption = new GameAction("select", false);
	private GUI gui;
	
	
	
	public MenuState(GL gl)
	{
		GUI gui;
		Kernel.userInput.bindToKey(selectOption, KeyEvent.VK_ENTER);
	}
	
	public void handleInput(long elapsedTime)
	{
		if(selectOption.isPressed()){
			changeState("InGameState");
		}
	}
	
	public void update(long elapsedTime)
	{
		handleInput(elapsedTime);
	}
	
	public void display(GL gl,GLU glu)
	{
		
	}
	
	
}
