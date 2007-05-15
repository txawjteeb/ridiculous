package org.cart.igd.entity;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.MiniGame;
import java.util.*;

public class SlushyBallCharge extends Entity {

	private float gravityPull = -.2f;
	private MiniGame mg;
	/** speed per millisecond */
	private float speed = .08f;
	private float yVec = -.1f;
	
	
	/**
	 * SlushyExplosion.java
	 * 
	 * 
	 * General Purpose:
	 * effect when slushi ball hits something
	 */
	public SlushyBallCharge(Vector3f pos, float fD, float bsr, OBJModel model,
			MiniGame mg) 
	{
		super(pos, fD, bsr, model);
		this.mg = mg;
		facingDirection = new Random().nextFloat()*360;
	}
	
	public void update(long elapsedTime){
		

			position.y-=gravityPull;
			position.x = position.x + ( (float)Math.cos((facingDirection)
						* 0.0174f) )* speed/24 *(float)elapsedTime;
			position.z = position.z + ( (float)Math.sin((facingDirection)
						* 0.0174f) )* speed/24 *(float)elapsedTime;
		gravityPull+=.01f;
		
		if(position.y<0f){
				mg.removeList.add(this);
		}
		
		
	}
	
}
