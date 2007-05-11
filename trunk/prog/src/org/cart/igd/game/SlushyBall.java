package org.cart.igd.game;

import java.util.Random;

import org.cart.igd.entity.Entity;
import org.cart.igd.entity.Noise;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.MiniGame;

public class SlushyBall extends Entity {

	private float gravitypull = -.6f;
	private MiniGame mg;
	
	public SlushyBall(Vector3f pos, float fD, float bsr, OBJModel model,MiniGame mg) 
	{
		super(pos, fD, bsr, model);
		this.mg = mg;
	}
	
	public void update(long elapsedTime){
		if(!(position.y< 0f)){
			position.y-=gravitypull;
				position.x = position.x + ( (float)Math.cos((facingDirection) * 0.0174f) )*.02f*(float)elapsedTime;
				position.z = position.z + ( (float)Math.sin((facingDirection) * 0.0174f) )*.02f*(float)elapsedTime;
				gravitypull+=.001f*(float)elapsedTime;
		} else {
			mg.removeList.add(this);
		}
	}
}
