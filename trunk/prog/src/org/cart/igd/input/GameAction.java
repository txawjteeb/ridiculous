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
	private int key;
	private int keyCode;
	private String info;
	private boolean active;
	
    public GameAction(String info, int key) {
    	this.info = info;
    	this.key = key;
    	active = false;
    }
    
    /* use for single actions such as cage activation or button clicking*/
    public boolean isPerformed(){
    	if(active){
    		active = false;
    		return true;
    	}	 
    	
    	return false;
    }
    
    /* use for constant actions such as movement */
    public boolean isActive(){
    	return active;
    }
    
    public int getKey(){
    	return key;
    }
    
    public void activate(){
    	active = true;
    }
    public void deactivate(){
    	active =false;
    }
    
    
}