/**
 * @(#)Profiler.java
 *
 *
 * @author Vitaly Maximov
 * @version 1.00 2007/4/2
 */
package org.cart.igd.core;

public class Profiler extends Thread
{
	public long currentTime = System.currentTimeMillis();
	public int sleepTime = 1;
	
	public boolean running = false;
	
    public Profiler()
    {
    	
    }
    
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