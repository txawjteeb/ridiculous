package org.cart.igd.entity;

import org.cart.igd.math.Vector3f;
import org.cart.igd.states.InGameState;
/** 
 * spawned by animals when walking around bsr(bounding sphere radius) 
 * based on how much noise particular animal makes
 **/
public class Noise extends Entity
{
	private InGameState igs;
	private long timeLeft;
	
	public Noise(Vector3f pos, float bsr, long timeLeft, InGameState igs){
		super(pos,0,bsr);
		this.igs = igs;
		this.timeLeft = timeLeft;
	}
	
	public void update(long elapsedTime){
		timeLeft -= elapsedTime;
		if(timeLeft < 0){
			igs.entities.remove(this);
		}
	}

}
