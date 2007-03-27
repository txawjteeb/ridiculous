package org.cart.igd.input;

import java.awt.Component;
import java.awt.event.*;
import java.awt.Robot;
import javax.swing.SwingUtilities;

import org.cart.igd.Display;

public class UserInput implements KeyListener, MouseListener, MouseMotionListener
{
	public static boolean[] keys = new boolean[256];
	public static int[] mousePos = new int[] { 0, 0 };
	public static boolean typingEnabled = false;
	
	public static Robot robot;

	public UserInput(Component comp)
	{
		comp.addKeyListener(this);
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
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
	
	public static boolean isKeyPressed(int vk) { return keys[vk]; }
	
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
		e.consume();
	}
	
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
		e.consume();
	}
	
	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}
	
	public void mousePressed(MouseEvent e)
	{
		updateMousePos(e.getX(), e.getY());
	}
	
	public void mouseReleased(MouseEvent e)
	{
		updateMousePos(e.getX(), e.getY());
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
	
	private void updateMousePos(int x, int y)
	{
		mousePos[0] = x;
		mousePos[1] = Display.getScreenHeight()-y;
	}
}