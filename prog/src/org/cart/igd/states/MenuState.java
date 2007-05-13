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

public class MenuState extends GameState
{
	private GameAction selectOption = new GameAction("select", false);
	private int guiState = 0;
	public MenuState(GL gl)
	{
		gui.add( new MenuGUI(this) );
		Kernel.userInput.bindToKey(selectOption, KeyEvent.VK_ENTER);
	}
	
	public void handleInput(long elapsedTime)
	{
		gui.get(guiState).handleInput(elapsedTime);
		if(selectOption.isPressed()){
			changeGameState("InGameState");
		}
	}
	
	public void update(long elapsedTime)
	{
		handleInput(elapsedTime);
		gui.get(guiState).update(elapsedTime);
	}
	
	public void display(GL gl,GLU glu)
	{
		gui.get(guiState).render( Kernel.display.getRenderer().getGLG() );
		GLGraphics glg = Kernel.display.getRenderer().getGLG();
		glg.glgBegin();
		glg.drawImage(GLGraphics.Cursor, Kernel.userInput.mousePos[0], Kernel.userInput.mousePos[1]-32);
		glg.glgEnd();
	}
	
	
}
