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
		GLGraphics glg = Display.renderer.getGLG();
		glg.glgBegin();
		glg.drawImage(GLGraphics.Cursor, Kernel.userInput.mousePos[0], Kernel.userInput.mousePos[1]);
		glg.glgEnd();
	}
	
	
}
