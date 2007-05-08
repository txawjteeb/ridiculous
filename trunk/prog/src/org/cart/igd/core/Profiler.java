/**
 * @(#)Profiler.java
 *
 *
 * @author Vitaly Maximov
 * @version 1.00 2007/4/2
 */
package org.cart.igd.core;

/**
 * qwfasdfasdf
 **/

public class Profiler extends Thread
{
	public long currentTime = System.currentTimeMillis();
	public int sleepTime = 1;
	
	public int ihTimedHits = 0;
	public int ihAllHits = 0;
	
	public boolean running = false;
	
    public Profiler()
    {
    	running = true;
    }
    
    public void run()
    {
    	while(running)
    	{
    		currentTime = System.currentTimeMillis();
    		
    		
    		//System.out.println("profiler cycle");
    		
    		try {
    			Thread.sleep(sleepTime);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}	
    }
    
    
}