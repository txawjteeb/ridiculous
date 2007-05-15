package org.cart.igd.entity;

import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.MiniGame;
import org.cart.igd.entity.*;
import java.util.*;

public class SlushyBall extends Entity {

	private float gravityPull = .0004f;
	private MiniGame mg;
	/** speed per millisecond */
	private float speed = .08f;
	private float yVec = -.1f;
	int colorId = 0;
	
	/**
	 * SlushyBall.java
	 * note: not to be confused with SlushiBall
	 * 
	 * General Purpose:
	 * used in the penguin MiniGame as a thrown projectile
	 */
	public SlushyBall(Vector3f pos, float fD, float bsr, OBJModel model,
			MiniGame mg, int colorId,float speed) 
	{
		super(pos, fD, bsr, model);
		this.mg = mg;
		this.position.y = .1f;
		this.colorId = colorId;
		this.speed = speed;
	}
	
	public void update(long elapsedTime){
		if(!(position.y< -1f)){
			position.y -= yVec;
				position.x = position.x + ( (float)Math.cos((facingDirection)
						* 0.0174f) )* speed *(float)elapsedTime;
				position.z = position.z + ( (float)Math.sin((facingDirection)
						* 0.0174f) )* speed *(float)elapsedTime;
				yVec += gravityPull*(float)elapsedTime;
				if(new Random().nextInt(4)==0){
					mg.addList.add(new SlushyBallCharge(
							new Vector3f(position.x, position.y, position.z),
							facingDirection, 
							.2f, mg.slushyCharge[colorId] ,mg)
						);
				}
				
				
		} else {
			mg.removeList.add(this);
			mg.addList.add(new SlushyExplosion(
				new Vector3f(position.x, position.y, position.z),
				mg.player.facingDirection, 
				.2f,mg.slushyFragment[colorId] , mg)
			);
			 
		}
	}
}
