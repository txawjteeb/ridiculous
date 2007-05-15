package org.cart.igd.entity;

import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.MiniGame;

public class SlushyBall extends Entity {

	private float gravityPull = .0002f;
	private MiniGame mg;
	/** speed per millisecond */
	private float speed = .03f;
	private float yVec = -.1f;
	
	/**
	 * SlushyBall.java
	 * note: not to be confused with SlushiBall
	 * 
	 * General Purpose:
	 * used in the penguin MiniGame as a thrown projectile
	 */
	public SlushyBall(Vector3f pos, float fD, float bsr, OBJModel model,
			MiniGame mg) 
	{
		super(pos, fD, bsr, model);
		this.mg = mg;
		this.position.y = .1f;
	}
	
	public void update(long elapsedTime){
		if(!(position.y< -1f)){
			position.y -= yVec;
				position.x = position.x + ( (float)Math.cos((facingDirection)
						* 0.0174f) )* speed *(float)elapsedTime;
				position.z = position.z + ( (float)Math.sin((facingDirection)
						* 0.0174f) )* speed *(float)elapsedTime;
				yVec += gravityPull*(float)elapsedTime;
		} else {
			mg.removeList.add(this);
		}
	}
}
