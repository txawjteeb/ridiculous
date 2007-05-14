package org.cart.igd.input;

/**
 * GameAction.java
 *
 * General Function: Works with UserInput for input actions.
 */
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
	
	private int ammount = 0;
	
	private boolean pressed;
	private boolean released;
	
	private boolean continuous = false;

	private int id = 0;
	
    public GameAction(String info)
    {
    	this.info = info;
    	active = false;
    	continuous = true;
    }
    
    public GameAction(String info,boolean cont)
    {
    	this.info = info;
    	active = false;
    	continuous = cont;
    }
    
    public GameAction(String info,boolean cont,int id)
    {
    	this.info = info;
    	active = false;
    	continuous = cont;
    	this.id = id;
    }
    
    /** 
     * use for constant actions such as movement.   
     * Deactivates the action when called 
     **/
    public boolean isActive()
    {   	
    	if(continuous)
    	{
    		return active;
    	}
    	else
    	{
    		if(pressed && released)
    		{
    			active = false;
    			pressed = false;
    			released = false;
    			return true;
    		}
    		return false;
    	}
    }
    
    public void activate()
    {
    	active = true;
    	pressed = true;
    }
    
    public void activate(int amt)
    {
    	active = true;
    	pressed = true;
    	ammount = amt;
    }
    
    public int getAmount()
    {
    	return ammount;
    }
    
    /** 
     * Called by UserInput when mouse or key is released.
     **/
    public void deactivate()
    {
    	active =false;
    	released = true;
    	if(!continuous && pressed )
    	{
    		active = true;
    	}
    }
    
    public boolean isReleased()
    {
    	return released;
    }
    
    public boolean isPressed()
    {
    	return pressed;
    }
    
    public String getInfo()
    {
    	return info;
    }
    
    public int getId()
    {
    	return id;
    }
}