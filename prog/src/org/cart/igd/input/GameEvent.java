package org.cart.igd.input;

/**
 * GameEvent.java
 *
 * General Function: Not directly connected to game input use for general game logic.
 */
public class GameEvent {
	/** discriptive name of what the game event does */
	private String name="";
	/** 
	 * determines this event happened once as well as how many times it happened increment
	 * ex: pressing a button to activate something once
	 **/
	private int type = 0;
	/** 
	 * determines if the event is happening 
	 * ex: continuous event like walking forward 
	 **/
	private boolean active = false;
	
    public GameEvent(String name) {
    	this.name=name;
    }
    
    public GameEvent(String name, int type) {
    	this.name=name;
    	this.type = type;
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
    
    public String getText(){
    	return name;
    }
    
    public String toString(){
    	return name;
    }
}