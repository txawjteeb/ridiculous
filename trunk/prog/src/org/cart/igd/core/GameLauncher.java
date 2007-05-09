package org.cart.igd.core;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import org.cart.igd.Display;
import org.cart.igd.DisplaySettings;

/**
 * GamerLauncer.java
 *
 * General Function:
 * A JFrame that allows for custom settings to send to DisplaySettings.
 */
public class GameLauncher extends JFrame
{
	/* The JFrame's title. */
	String title = "project ridiculous";
	
	/* Fullscreen flag. */
	public boolean fullscreen;
	
	/* DisplaySetting width. */
	public int w;
	
	/* DisplaySetting height. */
	public int h;
	
	/* Instance of Kernel class. */
	private Kernel kernel;
	
	/* Java ComboBox */
	JComboBox jcbResolution;
	
	/* Java CheckBox */
	JCheckBox jcbFullscreen;
	
	/* Java Button */
	JButton jbOk;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of GameLauncher.
	 */
    public GameLauncher(Kernel sanders)
    {
    	/* INCOMING PUN, NUB! */
    	kernel = sanders;
    	
    	setLayout(new FlowLayout());
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setTitle("Game Launcher: Zoo Escape");
    	setSize(400,300);
    	setLocation( 
    		(Toolkit.getDefaultToolkit().getScreenSize().width/2)-200,
    		(Toolkit.getDefaultToolkit().getScreenSize().height/2)-150
    	);
    	
		Object[] p = new Object[] { 
			"640 by 480", 
			"800 by 600", 
			"1024 by 768", 
			"1280 by 960", 
			"1280 by 1024", 
			"1280 by 800",   
		};
		
    	fullscreen = false;
    	jcbResolution = new JComboBox(p);
		jcbResolution.setMaximumRowCount(6);
    	add(jcbResolution);
    	
    	/* Causes problem if jcbResolution is not validated after change */
    	jcbResolution.setSelectedIndex(2);
    	
    	/* off by default */
    	jcbFullscreen = new JCheckBox("Full Screen Mode",false);
    	add(jcbFullscreen);
    		
    	jbOk = new JButton("Accept");
    	add(jbOk);
       	setVisible(true);
    	
    	//this fixed for full screen selection problem when seting a default
    	validate();
    	jcbResolution.validate();
    	jcbFullscreen.validate();
    	
    	jbOk.addActionListener(
    		new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				updateInfo();
    				kernel.init(new DisplaySettings(w,h,fullscreen));
    			}
    		
    		}
    	);
    	
    	jcbResolution.addKeyListener(
        	new KeyAdapter()
        	{
        		public void keyPressed(KeyEvent e)
        		{
        			if(e.getKeyCode()==KeyEvent.VK_ENTER)
        			{
        				updateInfo();
        				kernel.init(new DisplaySettings(w,h,fullscreen));
        			}
        		}
        	}
        );
    }
    
    /**
     * updateInfo
     *
     * General Function: Updates the DisplaySettings.
     */
    public void updateInfo()
    {
    	String s=jcbResolution.getItemAt(jcbResolution.getSelectedIndex()).toString();
    	w = Integer.parseInt(s.substring(0, s.indexOf(" ")));
		h = Integer.parseInt(s.substring(s.indexOf(" ")+4));
    	fullscreen = jcbFullscreen.isSelected();
    }
}
