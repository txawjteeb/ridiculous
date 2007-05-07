package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.Renderer;
import org.cart.igd.collision.Collision;
import org.cart.igd.discreet.Model;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;

public class WalkingGuard extends Guard
{	
	/**
	 * Create a guard with OBJModel
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param OBJModel model: .obj format file data
	 * @param InGameState refference the game
	 * @param float scale of the model
	 **/
	public WalkingGuard(Vector3f pos, float fD, float bsr, OBJModel model, 
			InGameState igs,float speed)
	{
		super(pos,fD,bsr,model,igs,speed);

		target = home;
	}
	
	/**
	 * Create a guard with 3DSModel 
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param Model model: .3ds format file data
	 **/
	public WalkingGuard(Vector3f pos, float fD, float bsr, Model model, 
			InGameState igs, float speed )
	{
		super(pos,fD,bsr,model,igs,speed);
	}
	
	public void update(long elapsedTime)
	{
		listenForNoise();
		lookForPlayer();
		
		if( target instanceof GuardFlag ){
			if( Collision.stsXZ(position,.5f,target.position,.5f) ){
				getNextTarget();
			}
		}
		
		changeDirection();
		walkForward(elapsedTime);
		
		Renderer.info[6] = "walking guard: "+position.x+"/"+position.z;
	}
	
	public void listenForNoise()
	{
		Renderer.info[8]= "walking guard: no noise";
		for(Entity e: igs.entities){
			if(e instanceof Noise)
			{
				boolean hear = Collision.stsXZ(position,hearingDistance,
						e.position,e.boundingSphereRadius);
				
				if( hear ) {
					Renderer.info[8]= "walking guard: " + hear;
					target = e;
				} else {
					Renderer.info[8]= "walking guard: " + hear;
					target = path.get(currentTarget);
				}
			}
		}
	}
	
	private void getNextTarget()
	{
		if(currentTarget < path.size()-1)
		{
			currentTarget++;
			target = path.get(currentTarget);
		}else{
			currentTarget=0;
			target = path.get(currentTarget);
		}
	}
}
