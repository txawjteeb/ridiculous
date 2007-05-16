package org.cart.igd.core;

/**
 * Profile.java
 * 
 * General Function: 
 * Access timer to get updated time for use by other time sensitive game engine
 * components.  
 * Keeping track of some statistical data.
 */
public class Profiler extends Thread
{
	public long currentTime = System.currentTimeMillis();
	public int sleepTime = 1;
	
	/* keep track of statistics */
	public int ihTimedHits = 0;
	public int ihAllHits = 0;
	
	public boolean running = false;
	
    public Profiler()
    {
    	running = true;
    }
    
    /** starts the thread */
    public void run()
    {
    	while(running)
    	{
    		currentTime = System.currentTimeMillis();
    		
    		try {
    			Thread.sleep(sleepTime);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}	
    }
    
    
}