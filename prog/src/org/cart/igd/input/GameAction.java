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
	
	public static final int ON_PRESS_ONLY = 10;
	public static final int ON_RELEASE_ONLY= 11;
	
	private String info;
	private boolean active;
	
	public int ammount = 0;
	
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
    public GameAction(String info,boolean cont,int type) {
    	this.info = info;
    	active = false;
    	continuous = cont;
    	this.type = type;
    }
    
    /** use for constant actions such as movement */
    public boolean isActive(){
    	/*if(type == ON_RELEASE_ONLY ){
    		boolean retVal = released;
    		released = false;
    		return retVal;
    	}
    	if(type == ON_PRESS_ONLY){
    		boolean retVal = pressed;
    		pressed = false;
    		return retVal;
    	}*/
    	
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
    
    public boolean isReleased(){
    	return released;
    }
    
    public boolean isPressed(){
    	return pressed;
    }
    public int getAmount(){
    	int retVal = ammount;
    	return retVal;
    }
    
    public String getInfo(){
    	return info;
    }
}