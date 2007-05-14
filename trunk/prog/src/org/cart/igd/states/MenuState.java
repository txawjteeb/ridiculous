package org.cart.igd.states;

import org.cart.igd.states.GameState;
import org.cart.igd.input.*;
import org.cart.igd.core.Kernel;
import javax.media.opengl.GL;
import org.cart.igd.Display;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import org.cart.igd.gui.*;

import org.cart.igd.Display;
import org.cart.igd.gl2d.GLGraphics;

/**
 * MenuState.java
 *
 * General Function: Acts as the main menu for the game.
 */
public class MenuState extends GameState
{
	/* GameAction for selecting. */
	private GameAction selectOption = new GameAction("select", false);
	
	/* Variable for GUI state. */
	private int guiState = 0;
	
	/**
	 * Constructor
	 *
	 * General Function: Create an instance of MenuState.
	 *
	 * @param gl The GL instance.
	 */
	public MenuState(GL gl)
	{
		gui.add( new MenuGUI(this) );
		Kernel.userInput.bindToKey(selectOption, KeyEvent.VK_ENTER);
	}
	
	/**
	 * handleInput
	 *
	 * General Function: Handles user input.
	 *
	 * @param elapsedTime The time in milliseconds since last render.
	 */
	public void handleInput(long elapsedTime)
	{
		gui.get(guiState).handleInput(elapsedTime);
		if(selectOption.isPressed())
		{
			changeGameState("InGameState");
		}
	}
	
	/**
	 * update
	 *
	 * General Function: Called to update game logic prior to rendering.
	 *
	 * @param elapsedTime The time in milliseconds since last render.
	 */
	public void update(long elapsedTime)
	{
		handleInput(elapsedTime);
		gui.get(guiState).update(elapsedTime);
	}
	
	/**
	 * display
	 *
	 * General Function: Called as the render method.
	 *
	 * @param gl The GL instance.
	 * @param glu The GLU instance.
	 */
	public void display(GL gl,GLU glu)
	{
		gui.get(guiState).render( Kernel.display.getRenderer().getGLG() );
		GLGraphics glg = Kernel.display.getRenderer().getGLG();
		glg.glgBegin();
		glg.drawImage(GLGraphics.Cursor, Kernel.userInput.mousePos[0], Kernel.userInput.mousePos[1]-32);
		glg.glgEnd();
	}
	
	
}
