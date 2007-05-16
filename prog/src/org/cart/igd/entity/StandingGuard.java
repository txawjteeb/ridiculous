package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.Renderer;
import org.cart.igd.collision.Collision;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJAnimation;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;
import org.cart.igd.game.*;

/**
 * A more specific implementaion of the Guard class
 * Stationary, when detects target pursues, when lost target returns to 
 * original location and faces preset direction for the guard to watch
 * */
public class StandingGuard extends Guard
{
	private int guardDirection = -1;
	
	/**
	 * Create a guard with OBJModel
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param OBJModel model: .obj format file data
	 * @param InGameState refference the gameS
	 * @param float speed units per millisecond
	 * @param int guardDirection direction the guad faces when standing
	 **/
	public StandingGuard(Vector3f pos, float fD, float bsr, OBJAnimation walk,
			OBJAnimation idle, InGameState igs,float scale,int guardDirection)
	{
		super(pos,fD,bsr,walk,idle,igs,scale);
		target = home;
		this.guardDirection = guardDirection;
	}
	
	/**
	 * Detect Noise entities in a circular area with radius of hearingDistance
	 */
	public void listenForNoise()
	{
		Renderer.info[7]= "standing guard: no noise";
		for(Entity e: igs.entities){
			if(e instanceof Noise)
			{
				boolean hear = Collision.stsXZ(position,hearingDistance,
						e.position,e.boundingSphereRadius);
				
				if( hear ) {
					Renderer.info[7]= "standing guard: " + hear;
					target = e;
				} else {
					Renderer.info[7]= "standing guard: " + hear;
					target = home;
				}
			}
		}
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
			target = home;
		}
	}
	
	public void update(long elapsedTime)
	{
		super.update(elapsedTime);
		lookForPlayer();
		listenForNoise();
		
		if( target instanceof Player){
			Vector3f s = getNewPointDeg(facingDirection, visionDistance);
			
			if(Collision.stsXZ(s, visionDistance, 
					player.position,player.boundingSphereRadius))
			{
				changeDirection();
				walkForward(elapsedTime);
			} else {
				target = home;
				changeDirection();
				walkForward(elapsedTime);
			}	
		}
			
		/* walk until colides with flag */
		if( target instanceof GuardFlag ){
			if(Collision.stsXZ(position,.5f,
					home.position,home.boundingSphereRadius))
			{
				target = home;
				facingDirection = guardDirection;
			} else {
				target = home;
				changeDirection();
				walkForward(elapsedTime);
			}
		} 
		
		/* walk until colides with flag */
		if( target instanceof Noise ){
			if(Collision.stsXZ(position,.5f,
					target.position,target.boundingSphereRadius))
			{
				facingDirection++;
			} else {
				changeDirection();
				walkForward(elapsedTime);
			}
		} 
		
		Renderer.info[5] = "standing guard: "+position.x+"/"+position.z;
		
		capturePlayer();
	}
}
