 package org.cart.igd;
 
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import org.cart.igd.core.*;

/**
 * DisplaySettings.java
 *
 * General Function: Holds the settings for the engine's Display.
 */
public class DisplaySettings
{
	/* Display frame's title. */
	public String title;
	
	/* Fullscreen flag. */
	public boolean fullscreen;
	
	/* Display's width. */
	public int w;
	
	/* Display's height. */
	public int h;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of DisplaySettings.
	 *
	 * @param w The DisplaySetting width.
	 * @param h The DisplaySetting height.
	 * @param fs The DisplaySetting fullscreen flag.
	 */
    public DisplaySettings(int w,int h, boolean fs)
    {	
    	this.h = h;
    	this.w = w;
    	fullscreen = fs;
    }
}