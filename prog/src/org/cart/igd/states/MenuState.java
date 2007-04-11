package org.cart.igd.states;

import org.cart.igd.states.GameState;
import org.cart.igd.input.*;
import org.cart.igd.core.Kernel;
import javax.media.opengl.GL;
import org.cart.igd.Display;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import org.cart.igd.gui.*;


public class MenuState extends GameState
{
	private GameAction selectOption = new GameAction("select", false);
	private GUI gui;
	
	
	
	public MenuState(GL gl)
	{
		gui = new MenuGUI(this);
		Kernel.userInput.bindToKey(selectOption, KeyEvent.VK_ENTER);
	}
	
	public void handleInput(long elapsedTime)
	{
		if(selectOption.isPressed()){
			changeGameState("InGameState");
		}
	}
	
	public void update(long elapsedTime)
	{
		handleInput(elapsedTime);
		gui.update(elapsedTime);
	}
	
	public void display(GL gl,GLU glu)
	{
		gui.render( Kernel.display.getRenderer().getGLG() );
	}
	
	
}
