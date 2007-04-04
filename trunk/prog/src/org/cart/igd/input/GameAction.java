/**
 * @(#)GameAction.java
 *
 *
 * @author 
 * @version 1.00 2007/3/29
 */
package org.cart.igd.input;

/** use side by side with UserInput */
public class GameAction
{
	public static final int NONE = 0;
	public static final int KEY = 1;
	public static final int MOUSE = 2;
	public static final int GUI = 3;
	
	private String info;
	private boolean active;
	
	private boolean pressed;
	private boolean released;
	
	private boolean continuous = false;

	private int type = 0;
	
    public GameAction(String info) {
    	this.info = info;
    	active = false;
    	continuous = true;
    }
    public GameAction(String info,boolean cont) {
    	this.info = info;
    	active = false;
    	continuous = cont;
    }
    
    /** use for constant actions such as movement */
    public boolean isActive(){
    	if(continuous){
    		return active;
    	} else {
    		if(pressed && released){
    			active = false;
    			pressed = false;
    			released = false;
    			return true;
    		}
    		return false;
    	}
    }
    
    public void activate(){
    	active = true;
    	pressed = true;
    }
    
    /** caled by UserInput when mouse or key is released*/
    public void deactivate(){
    	active =false;
    	released = true;
    	if(!continuous && pressed ){
    		active = true;
    	}
    }
    
    public String getInfo(){
    	return info;
    }
}