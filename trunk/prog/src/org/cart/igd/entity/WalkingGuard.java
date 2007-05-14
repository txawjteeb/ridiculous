package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.Renderer;
import org.cart.igd.collision.Collision;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;
import org.cart.igd.game.*;

/**
 * A more specific implementation of guard class,
 * Patrols a given set of pints in a continuous cycle,
 * When sighting target pursu untill capture or loss of sight
 * Resumes patrol cycle when no targets in sight.
 */
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
	 */
	public WalkingGuard(Vector3f pos, float fD, float bsr, OBJModel model, 
			InGameState igs,float speed)
	{
		super(pos,fD,bsr,model,igs,speed);

		target = home;
	}
	
	/**
	 * cycle through guards AI cycle checking for specific targets
	 * @param long elapsedTime for fps/time sensitive functions
	 */
	public void update(long elapsedTime)
	{
		super.update(elapsedTime);
		lookForPlayer();
		listenForNoise();
		
		
		if( target instanceof GuardFlag ){
			if( Collision.stsXZ(position,.5f,target.position,.5f) ){
				followPath();
			}
		}
		
		if( target instanceof Noise ){
			if( Collision.stsXZ(position,.5f,target.position,.5f) ){
				changeDirection();
				walkForward(elapsedTime);
			}
		}
		
		changeDirection();
		walkForward(elapsedTime);
		
		Renderer.info[6] = "walking guard: "+position.x+"/"+position.z;
		
		capturePlayer();
	}
	
	/**
	 * detect Player entity in a circlular area offset forward by the radius
	 * of the circle in front of the guard 
	 */
	public void lookForPlayer()
	{
		Vector3f s = getNewPointDeg(facingDirection, visionDistance);
		
		boolean see = Collision.stsXZ(s,visionDistance,
				igs.player.position,igs.player.boundingSphereRadius);
		
		if(see){
			target = player;
		} else {
			target = path.get(currentTarget);
		}
	}
	
	/**
	 * Detect Noise entities in a circular area with radius of hearingDistance
	 */
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
	
	/**
	 * Cycle through a path when failed tetecting either Player or Noise  
	 */
	private void followPath()
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
