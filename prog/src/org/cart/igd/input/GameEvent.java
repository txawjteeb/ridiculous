package org.cart.igd.input;

/**
 * GameEvent.java
 *
 * General Function: Not directly connected to game input use for general game logic.
 */
public class GameEvent
{
	/* Discriptive name of what the game event does. */
	private String name="";
	
	/**
	 * determines this event happened once as well as how many times it happened increment
	 * ex: pressing a button to activate something once
	 */
	private int type = 0;
	
	/** 
	 * determines if the event is happening 
	 * ex: continuous event like walking forward 
	 */
	private boolean active = false;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of GameEvent.
	 */
    public GameEvent(String name)
    {
    	this.name=name;
    }
    
    /**
	 * Constructor
	 *
	 * General Function: Creates an instance of GameEvent.
	 */
    public GameEvent(String name, int type)
    {
    	this.name=name;
    	this.type = type;
    }
    
    /** 
     * activate
     * 
     * General Function: Start an event by either button pressed event.
     */
    public void activate()
    {
    	active = true;
    }
    
    /**
     * deactivate
     *
     * General Function: stop event usualy by a button release event.
     */
    public void deactivate()
    {
    	active = false;
    }
    
    /**
     * isActive
     *
     * General Function: Returns active flag.
     */
    public boolean isActive()
    {
    	return active;
    }
    
    /**
     * getText
     *
     * General Function: Returns the name.
     */
    public String getText()
    {
    	return name;
    }
    
    /**
     * toString
     *
     * General Function: Returns data representing this object.
     */
    public String toString()
    {
    	return name;
    }
}