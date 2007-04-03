/**
 * @(#)GameAction.java
 *
 *
 * @author 
 * @version 1.00 2007/3/29
 */
package org.cart.igd.input;

public class GameAction
{
	public static final int NONE = 0;
	public static final int KEY = 1;
	public static final int MOUSE = 2;
	public static final int GUI = 3;
	
	private String info;
	private boolean active;
	
    public GameAction(String info) {
    	this.info = info;
    	active = false;
    }
    
    /** use for single actions such as cage activation or button clicking */
    public boolean isPerformed(){
    	if(active){
    		active = false;
    		return true;
    	}	 
    	
    	return false;
    }
    
    /** use for constant actions such as movement */
    public boolean isActive(){
    	return active;
    }
    
    public void activate(){
    	active = true;
    }
    public void deactivate(){
    	active =false;
    }
    
    
}