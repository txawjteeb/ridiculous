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
 
public class DisplaySettings extends JFrame{

	String title = "project ridiculous";
	boolean okPressed = false;
	boolean fullscreen;
	int w;
	int h;
	
    public DisplaySettings() {
    	setLayout(new FlowLayout());
    	//setSize(new Dimension(400,300));
    	setTitle("Project Ridiculous: Display Settings");
    	setSize(400,300);
    	setLocation( 
    		(Toolkit.getDefaultToolkit().getScreenSize().width/2)-200,
    		(Toolkit.getDefaultToolkit().getScreenSize().height/2)-150
    	);
    	
		Object[] p = new Object[] { "640 by 480", "800 by 600", "1024 by 768", "1280 by 960", "1280 by 1024", "1280 by 800" };
		
    	fullscreen = false;
    	
    	
    	JComboBox jcbResolution = new JComboBox(p);
		jcbResolution.setMaximumRowCount(5);
    	add(jcbResolution);
    	jcbResolution.setSelectedIndex(2);//default 1024 by 768
    	
    	JCheckBox jcbFullscreen = new JCheckBox("Full Screen Mode",false);//default off
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
    	
    	
    	waitForOk();
    		
    	String s = jcbResolution.getItemAt(jcbResolution.getSelectedIndex()).toString();
			
    		
    	w = Integer.parseInt(s.substring(0, s.indexOf(" ")));
    	System.out.println("w"+w);
		h = Integer.parseInt(s.substring(s.indexOf(" ")+4));
		System.out.println("h"+h);    	
    	fullscreen = jcbFullscreen.isSelected();
    }
    
    public void waitForOk(){
    	while(!okPressed){
    		
    	}
    }
    
    
    
    
    
    
}