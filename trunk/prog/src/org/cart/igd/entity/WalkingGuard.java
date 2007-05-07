package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.Renderer;
import org.cart.igd.collision.Collision;
import org.cart.igd.discreet.Model;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;
import org.cart.igd.game.*;

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
		if(Collision.stsXZ(position,1f,igs.player.position,igs.player.boundingSphereRadius)){
			 igs.guardSquad.reset();
			 igs.player.position = new Vector3f(-20f,0f,-20f);
			 igs.removePartyAnimals();
			 Animal a = igs.getAnimal("Turtles");
			 if(a!=null){
			 	if(a.state!=Inventory.SAVED_IN_PARTY&&a.state!=Inventory.SAVED_IN_BUSH){
			 		igs.inventory.PSYCH_CAUGHT_BEFORE_FREEING_TURTLES = 1;
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
			target = path.get(currentTarget);
		}
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
