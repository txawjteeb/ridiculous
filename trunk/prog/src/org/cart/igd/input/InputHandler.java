package org.cart.igd.input;

import org.cart.igd.core.Kernel;
import org.cart.igd.core.Profiler;
import org.cart.igd.states.GameState;

/**
 * InputHandler.java
 *
 * General Function: Separate thread that monitors input.
 */
public class InputHandler extends Thread
{
	private Profiler profiler;
	public int sleepTime = 1;
	private long lastTime;
	public long currentTime;
	
	public boolean running = false;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of InputHandler.
	 */
	public InputHandler(Profiler profiler)
	{
		this.profiler = profiler;
		lastTime = profiler.currentTime;
		currentTime = profiler.currentTime;
		running = true;
	}
	
	/**
	 * run
	 *
	 * General Function: Thread start method.
	 */
	public void run()
	{
		while(running)
		{
			if(Kernel.displayRunning)
	    	{
	    		currentTime = profiler.currentTime;
	    		long elapsedTime = currentTime - lastTime;
	    		
	    		/* prevent unnecesary updates */
	    		if(elapsedTime > 0)
	    		{
	    			try
	    			{
	    				if(Kernel.display.getRenderer().getStateManager().getCurrentState()!= null)
	   		 			{
	    					Kernel.display.getRenderer().getStateManager().getCurrentState().handleInput(elapsedTime);
	   		 				profiler.ihTimedHits++;
	   		 			}
	    			}
	    			catch(Exception e)
	    			{
	    				e.printStackTrace();
	    			}
	    		}
	    		
	    		profiler.ihAllHits++;
	    		
	    		lastTime = profiler.currentTime;
	    	}
			
			try
			{
    			Thread.sleep(sleepTime);
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
		}
	}
}
