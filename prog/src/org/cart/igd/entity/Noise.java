package org.cart.igd.entity;

import org.cart.igd.math.Vector3f;
/** 
 * spawned by animals when walking around bsr(bounding sphere radius) 
 * based on how much noise particular animal makes
 **/
public class Noise extends PseudoEntity{
	public Noise(Vector3f pos, float bsr){
		super(pos,bsr,50L);
		
	}

}
