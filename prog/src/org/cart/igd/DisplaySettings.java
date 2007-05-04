/**
 * @(#)DisplaySettings.java
 *
 *
 * @author Vitaly Maximov
 * @version 1.00 2007/4/5
 */
 package org.cart.igd;
 
 import javax.swing.*;
 import java.awt.event.*;
 import java.awt.*;
 import org.cart.igd.core.*;
 
public class DisplaySettings
{
	public String title;
	public boolean fullscreen;
	public int w;
	public int h;
	
    public DisplaySettings(int w,int h, boolean fs) {	
    	this.h = h;
    	this.w = w;
    	fullscreen = fs;
    }
}