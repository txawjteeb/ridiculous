package org.cart.igd.input;

import java.awt.Component;
import java.awt.event.*;
import java.awt.Robot;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.cart.igd.Display;
import org.cart.igd.core.Kernel;
import org.cart.igd.gl2d.UIComponent;
import org.cart.igd.game.Inventory;

/**
 * UserInput.java
 * 
 * General Purpose:
 * In its current state its a hybrid input helper and can be used to check 
 * global input events or local events via GameEvent classes.
 */
public class UserInput implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	/* The flags for every keyboard key. */
	public boolean[] keys     = new boolean[600];
	
	/* The integer array for Mouse Position. */
	public int[] mousePos     = new int[] { 0, 0 };
	
	/* The integer array for Mouse Previous Position. */
	public int[] mousePosPrev = new int[] { 0, 0 };
	
	/* The integer array for Mouse Press. */
	public int[] mousePress   = new int[] { 0, 0 };
	
	/* The integer array for Mouse Release. */
	public int[] mouseRelease = new int[] { 0, 0 };
	
	/* The integer array for Mouse Dragged. */
	public int[] mouseDragged = new int[] { 0, 0 };
	
	/* GameActions for every key. */
	public GameAction keyActions[] = new GameAction[600];
	
	/* GameActions for every mouse action. */
	public GameAction mouseActions[] = new GameAction[20];
	
	/* GameActions for every button action. */
	public GameAction buttonActions[] = new GameAction[50];
	
	/* Java mouse robot. */
	public Robot robot;
	
	/* Mouse Wheel Up index. */
	public static final int MOUSE_WHEEL_UP = 4;
	
	/* Mouse Wheel Down index. */
    public static final int MOUSE_WHEEL_DOWN = 5;
    
    
    /* Mouse wheel movement. */
    public int mouseWheelMoved = 0;

    public ArrayList<UIComponent> guiElements = new ArrayList<UIComponent>();
    
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of UserInput.
	 *
	 * @param comp The Java Component to add the ActionListeners to.
	 */
	public UserInput(Component comp)
	{
		comp.addKeyListener(this);
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
		comp.addMouseWheelListener(this);
		comp.setFocusTraversalKeysEnabled(false);
		
		try
		{
			robot = new Robot();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void addComponentForListening(UIComponent c){
		guiElements.add(c);
	}
	
	/**
	 * isRoundedButtonPressed
	 *
	 * General Function: Test whether a circular button is pressed.
	 *
	 * @param xb The button x position.
	 * @param yb The button y position.
	 * @param br The button radius.
	 * @param xm The mouse x position.
	 * @param ym The mouse y position.
	 */
	public boolean isRoundButtonPressed(int xb, int yb, int br,int xm, int ym)
	{
		if( br > Math.sqrt( ((double)((xm-xb)*(xm-xb))+ ((ym-yb)*(ym-yb)))) )
		{
			return true;
		}
		return false;
	}
	
	/** 
	 * isSquareButtonPressed
	 *
	 * General Function: Test whether a square button is pressed.
	 *
	 * @param c The UIComponent of the button.
	 */
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
	
	/**
	 * isMouseOver
	 *
	 * General Function: Tests whether the mouse is over a UIComponent.
	 *
	 * @param c The UIComponent to test against.
	 */
	public boolean isMouseOver(UIComponent c)
	{
		if( c.getX()<mousePos[0] && (c.getX()+c.getWidth())>mousePos[0] && 
			c.getY()<mousePos[1] && (c.getY()+c.getHeight())>mousePos[1] )
		{
			return true;
		}
		return false;
	}

	/**
	 * isSquareButtonPressed
	 *
	 * General Function: Tests whether a button is being pressed.
	 *
	 * @param bx The button x position.
	 * @param by The button y position.
	 * @param bw The button width.
	 * @param bh The button height.
	 * @param mx The mouse x position.
	 * @param my The mouse y position.
	 */
	public boolean isSquareButtonPressed(int bx, int by,int bw, int bh,int mx, int my)
	{
		if( bx<mx && (bx+bw)>mx && by<my && (by+bh)>my )
		{
			return true;
		}
		return false;
	}
			
	/**
	 * isSquareButtonPressed
	 *
	 * General Function: Test whether a square button is pressed.
	 *
	 * @param c The UIComponent to test against.
	 * @param mx The mouse x position.
	 * @param my The mouse y position.
	 **/
	public boolean isSquareButtonPressed(UIComponent c,int mx, int my)
	{
		if( c.getX()<mx && (c.getX()+c.getWidth())>mx && 
			c.getY()<my && (c.getY()+c.getHeight())>my )
		{
			return true;
		}
		return false;
	}	

	/**
	 * bindToKey
	 *
	 * General Function: Adds an action event to a predifined virtual key code.
	 *
	 * @param action The GameAtion to add.
	 * @param vkey The virtual key to bind to.
	 */
	public void bindToKey(GameAction action, int vkey)
	{
		keyActions[vkey]=action;
	}
	
	/**
	 * bindToButton
	 *
	 * General Function: Adds an action event to a custom button code.
	 *
	 * @param action The GameAction to add.
	 * @param guiCode The gui button code to bind to.
	 */
	public void bindToButton(GameAction action, int guiCode)
	{
		buttonActions[guiCode] = action;
	}
	
	/**
	 * bindToMouse
	 *
	 * General Function: Adds an action event to a predefined mouse button code.
	 *
	 * @param action
	 * @param mouseButton
	 */
	public void bindToMouse(GameAction action, int mouseButton)
	{
		mouseActions[mouseButton] = action;
	}
	
	/**
	 * isKeyPressed
	 *
	 * General Function: Returns a true or false if the given virtual key is pressed.
	 *
	 * @param vk The virtual key index.
	 */
	public boolean isKeyPressed(int vk)
	{
		return keys[vk]; 
	}
	
	/**
	 * keyPressed
	 *
	 * General Function: Detects and handles a key pressed event.
	 *
	 * @param e The KeyEvent that was pressed.
	 */
	public void keyPressed(KeyEvent e)
	{
		/* activate game action if exist */
		if(keyActions[e.getKeyCode()]!= null)
		{
			keyActions[e.getKeyCode()].activate();
		}
		
		/* send a KeyEvent to gui element */
		for(UIComponent c: guiElements){
			if(c.focused){
				c.keyPressed(e);
			}
		}
		
		/* update array of key presses */
		keys[e.getKeyCode()] = true;
		e.consume();
	}
	
	/**
	 * keyReleased
	 *
	 * General Function: Detects and handles a key released event.
	 *
	 * @param e The KeyEvent that was released.
	 */
	public void keyReleased(KeyEvent e)
	{
		if(keyActions[e.getKeyCode()]!= null)
		{
			keyActions[e.getKeyCode()].deactivate();
		}
		keys[e.getKeyCode()] = false;
		e.consume();
	}
	
	/**
	 * keyTyped
	 *
	 * General Function: Detects and handles a key typed event.
	 *
	 * @param e The KeyEvent that was typed.
	 */
	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}
	
	/**
	 * mousePressed
	 *
	 * General Function: Detects and handles a mouse pressed event.
	 *
	 * @param e The MouseEvent that was pressed.
	 */
	public void mousePressed(MouseEvent e)
	{
		Inventory.canPick = true;
		if(mouseActions[e.getButton()]!= null)
		{
			mouseActions[e.getButton()].activate();
		}
		updateMousePress(e.getX(), e.getY());
	}
	
	/**
	 * mouseReleased
	 *
	 * General Function: Detects and handles a mouse released event.
	 *
	 * @param e The MouseEvent that was released.
	 */
	public void mouseReleased(MouseEvent e)
	{
		if(mouseActions[e.getButton()]!= null)
		{
			mouseActions[e.getButton()].deactivate();
		}
		mouseRelease[0] = e.getX();
		mouseRelease[1] = Kernel.display.getScreenHeight() - e.getY();
	}
	
	/**
	 * mouseClicked
	 *
	 * General Function: Detects and handles a mouse clicked event.
	 *
	 * @param e The MouseEvent that was clicked.
	 */
	public void mouseClicked(MouseEvent e)
	{
		updateMousePos(e.getX(), e.getY());
	}
	
	/**
	 * mouseMoved
	 *
	 * General Function: Detects and handles a mouse moved event.
	 *
	 * @param e The MouseEvent that was moved.
	 */
	public void mouseMoved(MouseEvent e)
	{	
		updateMousePos(e.getX(), e.getY());
	}
	
	/**
	 * mouseDragged
	 *
	 * General Function: Detects and handles a mouse dragged event.
	 *
	 * @param e The MouseEvent that was dragged.
	 */
	public void mouseDragged(MouseEvent e)
	{
		mouseDragged[0] = e.getX();
		mouseDragged[1] = Kernel.display.getScreenHeight() - e.getY();
		updateMousePos(e.getX(), e.getY());
	}
	
	/**
	 * mouseEntered
	 *
	 * General Function: Detects and handles a mouse entered event.
	 *
	 * @param e The MouseEvent that was entered.
	 */
	public void mouseEntered(MouseEvent e)
	{
		updateMousePos(e.getX(), e.getY());
	}
	
	/**
	 * mouseExited
	 *
	 * General Function: Detects and handles a mouse exited event.
	 *
	 * @param e The MouseEvent that was exited.
	 */
	public void mouseExited(MouseEvent e)
	{
		updateMousePos(e.getX(), e.getY());
	}
	
	/**
	 * mouseWheelMoved
	 *
	 * General Function: Detects and handles a mouse wheel moved event.
	 *
	 * @param e The MouseWheelEvent that was moved.
	 */
    public void mouseWheelMoved(MouseWheelEvent e)
    {
         mouseActions[MOUSE_WHEEL_DOWN].activate(e.getWheelRotation());
         
         mouseWheelMoved += e.getWheelRotation();
         e.consume();
    }
    
    /**
     * getMouseWheelMovement
     *
     * General Function: Returns the amount the mouse wheel has moved.
     */
    public int getMouseWheelMovement()
    {
    	int mwm = mouseWheelMoved;
    	mouseWheelMoved = 0;
    	return mwm;
    }
	
	/**
	 * updateMousePress
	 *
	 * General Function: Assigns the corrected mouse pressed coordinates to mousePress array.
	 *
	 * @param x The mouse x position.
	 * @param y The mouse y position.
	 */
	private void updateMousePress(int x,int y)
	{
		mousePress[0] = x;
		mousePress[1] = Kernel.display.getScreenHeight()-y;
	}
	
	/**
	 * updateMousePos
	 *
	 * General Function: Assigns the corrected mouse position to an array.
	 *
	 * @param x The mouse x position.
	 * @param y The mouse y position.
	 */
	private void updateMousePos(int x, int y)
	{
		mousePosPrev[0] = mousePos[0];
		mousePosPrev[1] = mousePos[1];
		
		mousePos[0] = x;
		mousePos[1] = Kernel.display.getScreenHeight()-y;
	}
	
	/**
	 * getXDif
	 *
	 * General Function: Get amount of x mouse movement used for mouse camera rotation.
	 */
	public int getXDif()
	{
		int retVal = (mousePosPrev[0] - mousePos[0]);
		mousePosPrev[0] = mousePos[0];
		return retVal;
	}
	
	/**
	 * getYDif
	 *
	 * General Function: Get amount of y mouse movement.
	 */
	public int getYDif()
	{
		int retVal = (mousePosPrev[1] - mousePos[1]);
		mousePosPrev[1] = mousePos[1];
		return retVal;
	}
}