/**
 * @(#)GameEvent.java
 *
 *
 * @author Vitaly Maximov
 * @version 1.00 2007/3/27
 */
package org.cart.igd.input;

public class GameEvent {
	/* discriptive name of what the game event does */
	private String name="";
	
	/** 
	 * determines this event happened once as well as how many times it happened increment
	 * ex: pressing a button to activate something once
	 **/
	private int selected = 0;
	
	/** 
	 * determines if the event is happening 
	 * ex: continuous event like walking forward 
	 **/
	private boolean active = false;
	
    public GameEvent(String name) {
    	this.name=name;
    }
    
    /** start either by a mouseclick or a button pressed event */
    public void select(){
    	selected++;
    }
    
    public void deselect(){
    	selected = 0;
    }
    
    /** start an event by either button pressed event*/
    public void activate(){
    	active = true;
    }
    /** stop event usualy by a button release event*/
    public void deactivate(){
    	active = false;
    }
    
    public boolean isActive(){
    	return active;
    }
    
    /** once checked will no longer be selected */
    public boolean isSelected(){
    	if(selected>0){
    		selected = 0;
    		return true;
    	}
    	return false;
    }
    
    /** 
     * once checked will no longer be selected 
     * returns int:  
 	 * the number of times the action was pressed before it was checked for selection
 	 **/
    public int isSelectedCount(){
    	if(selected>0){
    		return selected;
    	}
    	return 0;
    }
    
    public String getText(){
    	return name;
    }
}