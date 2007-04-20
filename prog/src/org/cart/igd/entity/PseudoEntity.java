package org.cart.igd.entity;

import org.cart.igd.math.Vector3f;


/** defines entities that are invisible or are not physical objects that occupy 
 * 3d space, such as noise and visual points
 * Purpose: use by ai to determine locations of animals and visa versa
 * */
public class PseudoEntity
{
	public boolean expired = false;
	private boolean temp;
	private long timeLeft = 0L;
	
	Vector3f pos;
	float bsr;
	
	public PseudoEntity(Vector3f pos,float bsr,long duration){
		this.pos = pos;
		this.bsr = bsr;
		if(duration == -1){
			temp = false;
		} else {
			this.timeLeft = duration;
			temp = true;
		}
	}
	
	public void update(long elapsedTime){
		if(temp == true){
			timeLeft -= elapsedTime;
			if(elapsedTime <= 0){
				expired = true;
			}
		}
	}

}
