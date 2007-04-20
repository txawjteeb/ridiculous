/**
 * UserInput
 * in its current state its a hybrid and can be used to check global input events
 * or local events via GameEvent classes
 **/
package org.cart.igd.input;

import java.awt.Component;
import java.awt.event.*;
import java.awt.Robot;
import javax.swing.SwingUtilities;

import org.cart.igd.Display;
import org.cart.igd.core.Kernel;
import org.cart.igd.gl2d.UIComponent;


public class UserInput implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	public boolean[] keys     = new boolean[600];
	public int[] mousePos     = new int[] { 0, 0 };
	public int[] mousePosPrev = new int[] { 0, 0 };
	public int[] mousePress   = new int[] { 0, 0 };
	public int[] mouseRelease = new int[] { 0, 0 };
	public int[] mouseDragged = new int[] { 0, 0 };
	
	public GameAction keyActions[] = new GameAction[512];
	public GameAction mouseActions[] = new GameAction[20];
	public GameAction buttonActions[] = new GameAction[50];
	
	public Robot robot;
	
	public static final int MOUSE_WHEEL_UP = 4;
    public static final int MOUSE_WHEEL_DOWN = 5;
    
    public int mouseWheelMoved = 0;

	public UserInput(Component comp)
	{
		comp.addKeyListener(this);
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
		comp.addMouseWheelListener(this);
		comp.setFocusTraversalKeysEnabled(false);
		
		try {
			robot = new Robot();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * test whether a circular button is pressed 
	 **/
	public boolean isRoundButtonPressed(int xb, int yb, int br,int xm, int ym)
	{
		if( br > Math.sqrt( ((double)((xm-xb)*(xm-xb))+ ((ym-yb)*(ym-yb)))) )
		{
			return true;
		}
		return false;
	}
	
	/** 
	 * test whether a square button is pressed
	 **/
	public boolean isSquareButtonPressed(UIComponent c)
	{
		if( c.getX()<mousePress[0] && (c.getX()+c.getWidth())>mousePress[0] && 
			c.getY()<mousePress[1] && (c.getY()+c.getHeight())>mousePress[1] )
		{
			c.activate();
			return true;
		}
		return false;
	}

	public boolean isSquareButtonPressed(int bx, int by,int bw, int bh,int mx, int my)
	{
		if( bx<mx && (bx+bw)>mx && by<my && (by+bh)>my )
		{
			return true;
		}
		return false;
	}
			
	/** test whether a square button is pressed*/
	public boolean isSquareButtonPressed(UIComponent c,int mx, int my)
	{
		if( c.getX()<mx && (c.getX()+c.getWidth())>mx && 
			c.getY()<my && (c.getY()+c.getHeight())>my )
		{
			return true;
		}
		return false;
	}	

	/** add action event to a predifined virtual key code */
	public void bindToKey(GameAction action, int vkey){
		keyActions[vkey]=action;
	}
	
	/** add action event to a custom button code */
	public void bindToButton(GameAction action, int guiCode){
		buttonActions[guiCode] = action;
	}
	
	/** add action event to a predefined mouse button code */
	public void bindToMouse(GameAction action, int mouseButton){
		mouseActions[mouseButton] = action;
	}
	
	public boolean isKeyPressed(int vk)
	{
		return keys[vk]; 
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(keyActions[e.getKeyCode()]!= null){
			keyActions[e.getKeyCode()].activate();
		}
		keys[e.getKeyCode()] = true;
		e.consume();
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(keyActions[e.getKeyCode()]!= null){
			keyActions[e.getKeyCode()].deactivate();
		}
		keys[e.getKeyCode()] = false;
		e.consume();
	}
	
	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(mouseActions[e.getButton()]!= null){
			mouseActions[e.getButton()].activate();
		}
		updateMousePress(e.getX(), e.getY());
	}
	
	public void mouseReleased(MouseEvent e)
	{
		if(mouseActions[e.getButton()]!= null){
			mouseActions[e.getButton()].deactivate();
		}
		mouseRelease[0] = e.getX();
		mouseRelease[1] = Kernel.display.getScreenHeight() - e.getY();
	}
	
	public void mouseClicked(MouseEvent e)
	{
		updateMousePos(e.getX(), e.getY());
	}
	
	public void mouseMoved(MouseEvent e)
	{	
		updateMousePos(e.getX(), e.getY());
	}
	
	public void mouseDragged(MouseEvent e)
	{
		mouseDragged[0] = e.getX();
		mouseDragged[1] = Kernel.display.getScreenHeight() - e.getY();
		updateMousePos(e.getX(), e.getY());
	}
	
	public void mouseEntered(MouseEvent e)
	{
		updateMousePos(e.getX(), e.getY());
	}
	
	public void mouseExited(MouseEvent e)
	{
		updateMousePos(e.getX(), e.getY());
	}
	
//	 from the MouseWheelListener interface
    public void mouseWheelMoved(MouseWheelEvent e) {
    	 //if (e.getWheelRotation() < 0) {
        //     mouseActions[MOUSE_WHEEL_UP].activate(e.getWheelRotation());
        // }
         //else {
         //    mouseActions[MOUSE_WHEEL_DOWN].activate(e.getWheelRotation());
         //}
         mouseActions[MOUSE_WHEEL_DOWN].activate(e.getWheelRotation());
         
         mouseWheelMoved += e.getWheelRotation();
         e.consume();
    }
    
    public int getMouseWheelMovement(){
    	int mwm = mouseWheelMoved;
    	mouseWheelMoved = 0;
    	return mwm;
    }
	
	private void updateMousePress(int x,int y)
	{
		mousePress[0] = x;
		mousePress[1] = Kernel.display.getScreenHeight()-y;
	}
	
	private void updateMousePos(int x, int y)
	{
		mousePosPrev[0] = mousePos[0];
		mousePosPrev[1] = mousePos[1];
		
		mousePos[0] = x;
		mousePos[1] = Kernel.display.getScreenHeight()-y;
	}
	
	/** get amount of x mouse movement used for mouse camera roation */
	public int getXDif()
	{
		int retVal = (mousePosPrev[0] - mousePos[0]);
		mousePosPrev[0] = mousePos[0];
		return retVal;
	}
	
	/** get amount of y mouse movement*/
	public int getYDif()
	{
		int retVal = (mousePosPrev[1] - mousePos[1]);
		mousePosPrev[1] = mousePos[1];
		return retVal;
	}
}