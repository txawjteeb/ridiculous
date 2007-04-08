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
 
public class DisplaySettings extends JFrame{

	String title = "project ridiculous";
	private boolean okPressed = false;
	public boolean fullscreen;
	public int w;
	public int h;
	
    public DisplaySettings() {
    	setLayout(new FlowLayout());
    	//setSize(new Dimension(400,300));
    	setTitle("Project Ridiculous: Display Settings");
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
    	
    	
    	JComboBox jcbResolution = new JComboBox(p);
		jcbResolution.setMaximumRowCount(6);
    	add(jcbResolution);
    	
    	//causes problme if jcbResolution is not validated after change
    	jcbResolution.setSelectedIndex(2);//default 1024 by 768
    	
    	//off by default
    	JCheckBox jcbFullscreen = new JCheckBox("Full Screen Mode",false);
    	add(jcbFullscreen);
    		
    		
    	JButton jbOk = new JButton("Accept");
    	add(jbOk);
    	
    	jbOk.addActionListener( new ActionListener() 
    		{
    			public void actionPerformed(ActionEvent e){
    				okPressed = true;
    			}
    		}	
    	);
    	setVisible(true);
    	
    	//this fixed for full screen selection problem when seting a default
    	validate();
    	jcbResolution.validate();
    	jcbFullscreen.validate();
    	
    	waitForOk();
    	
    	
    	
    	
		//String s=jcbResolution.getSelectedItem().toString();
		
		//this did not fix the full screen resolution problem	
    	String s=jcbResolution.getItemAt(jcbResolution.getSelectedIndex()).toString();
			
    	w = Integer.parseInt(s.substring(0, s.indexOf(" ")));
    	System.out.println("w"+w);
		h = Integer.parseInt(s.substring(s.indexOf(" ")+4));
		System.out.println("h"+h);    	
    	fullscreen = jcbFullscreen.isSelected();
    }
    
    //crude way to stall the game while wating for selection
    //display creation probably should be done within actionPerformed method of click
    public void waitForOk(){
    	while(!okPressed){
    		
    	}
    }
    //not implemented yet
    public void makeDisplay(){
    	Kernel.display = new Display(this);	
    	Kernel.display.createDisplay( title, w,h, fullscreen );
    }
}