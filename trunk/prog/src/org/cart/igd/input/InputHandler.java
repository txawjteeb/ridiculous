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
	
	public InputHandler(Profiler profiler){
		this.profiler = profiler;
		lastTime = profiler.currentTime;
		currentTime = profiler.currentTime;
		running = true;
	}
	
	public void run(){
		while(running)
    	{
    		currentTime = profiler.currentTime;
    		long elapsedTime = currentTime - lastTime;
    		
    		/* prevent unnecesary updates*/
    		if(elapsedTime > 0 && Kernel.display != null && null != Kernel.display.getRenderer()){
    			try{
    				if(Kernel.display.getRenderer().getStateManager().
   		 					getCurrentState()!= null)
   		 			{
   		 						
    					Kernel.display.getRenderer().getStateManager().
   		 						getCurrentState().handleInput(elapsedTime);
   		 				
   		 				profiler.ihTimedHits ++;
   		 			}
    			} catch (Exception e){
    				e.printStackTrace();
    			}
    		}
    		
    		profiler.ihAllHits++;
    		
    		lastTime = profiler.currentTime;
    		
    		try {
    			Thread.sleep(sleepTime);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
		
	}
}
