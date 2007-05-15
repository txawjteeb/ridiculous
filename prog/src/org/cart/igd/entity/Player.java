package org.cart.igd.entity;

import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJAnimation;
import org.cart.igd.models.obj.OBJModel;

public class Player extends Entity
{
	float walkNoiseRadius;
	float runNoiseRadius;
	float viewDistance;
	float hearDistance;//show pings of movement on "radar"
	private OBJAnimation walk;
	private OBJAnimation idle;
	
	public Player(Vector3f pos, float fD, float bsr,OBJAnimation walk,OBJAnimation idle){
		super(pos,fD,bsr,idle);
		this.walk = walk;
		this.idle = idle;
	}
	
	public void update(long elapsedTime){	
		this.objAnimation.update(elapsedTime);
		
		if(objAnimation.isLooping() || objAnimation.finished ){
			if(walking){
				
				this.objAnimation = walk;
			} else {
				this.objAnimation = idle;
			}
		}
		
		super.update(elapsedTime);
	}
}
