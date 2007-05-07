package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.Renderer;
import org.cart.igd.collision.Collision;
import org.cart.igd.discreet.Model;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;

public class StandingGuard extends Guard
{
	private int guardDirection = -1;
	
	
	/**
	 * Create a guard with 3DSModel 
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param Model model: .3ds format file data
	 * @param InGameState refference the game
	 * @param float speed units per millisecond
	 * @param int guardDirection direction the guad faces when standing
	 **/
	public StandingGuard(Vector3f pos, float fD, float bsr, Model model, 
			InGameState igs, float speed, int guardDirection)
	{
		super(pos,fD,bsr,model,igs,speed);
		target = home;
		this.guardDirection = guardDirection;
	}
	
	/**
	 * Create a guard with OBJModel
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param OBJModel model: .obj format file data
	 * @param InGameState refference the game
	 * @param float speed units per millisecond
	 * @param int guardDirection direction the guad faces when standing
	 **/
	public StandingGuard(Vector3f pos, float fD, float bsr, OBJModel model, 
			InGameState igs,float scale,int guardDirection)
	{
		super(pos,fD,bsr,model,igs,scale);
		target = home;
		this.guardDirection = guardDirection;
	}
	
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
			if(Collision.stsXZ(position,boundingSphereRadius,
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
			if(Collision.stsXZ(position,boundingSphereRadius,
					target.position,target.boundingSphereRadius))
			{
				facingDirection++;
			} else {
				changeDirection();
				walkForward(elapsedTime);
			}
		} 
		
		Renderer.info[5] = "standing guard: "+position.x+"/"+position.z;
	}
}
