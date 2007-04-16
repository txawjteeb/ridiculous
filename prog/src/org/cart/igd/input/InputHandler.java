package org.cart.igd.input;

import org.cart.igd.core.Kernel;
import org.cart.igd.core.Profiler;
import org.cart.igd.states.GameState;

public class InputHandler extends Thread{
	private Profiler profiler;
	public int sleepTime = 1;
	private long lastTime = System.currentTimeMillis();
	public long currentTime = System.currentTimeMillis();
	
	public boolean running = false;
	private GameState gameState;
	
	public InputHandler(){
	
		
	}
	
	public void run(){
		
		while(running)
    	{
    		currentTime = System.currentTimeMillis();
    		long elapsedTime = currentTime - lastTime;
    		
    		Kernel.display.getRenderer().getStateManager().
   		 		getCurrentState().handleInput(elapsedTime);
    		System.out.println(elapsedTime);
    		lastTime = System.currentTimeMillis();
    		
    		try {
    			Thread.sleep(sleepTime);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
		
	}
}
