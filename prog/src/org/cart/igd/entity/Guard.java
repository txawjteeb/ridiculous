package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.collision.Collision;
import org.cart.igd.game.Animal;
import org.cart.igd.game.Inventory;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;


/**
 * Represents guard's basic functionality 
 */
public class Guard extends Entity
{
	final float toDeg =57.29578f;
	final float toRad = 0.01745f;
	final float    pi = 3.14159f;
	
	public ArrayList<Entity> path = new ArrayList<Entity>();
	
	protected InGameState igs;
	
	/** alaways know loaction on player*/
	protected Entity player;
	
	/** object to face toward when calling changeDirection()*/
	protected Entity target;
	
	/** used by stationary guards to return to post */
	protected GuardFlag home;
	
	protected int currentTarget = 0;
	
	protected float posRange = .4f;//margin of error
	
	protected float visionDistance = 7f;
	
	protected float hearingDistance = 30f;
	
	public float refAngleRad = 0f;
	
	public boolean hearNoise = false;
	
	public boolean investigating = false;
	
	/**
	 * Create a guard with OBJModel
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param OBJModel model: .obj format file data
	 * @param InGameState refference the game
	 * @param float scale of the model
	 */
	public Guard(Vector3f pos, float fD, float bsr, OBJModel model, 
			InGameState igs,float speed)
	{
		super(pos,fD,bsr,model);
		this.igs = igs;
		home = new GuardFlag( new Vector3f( pos.x, pos.y, pos.z ), 1f, 1f );
		this.player = igs.player;
		this.speed = speed;
		target = home;
	}
	
	/**
	 * @return new Vector3f relative to previos position based on 
	 * degree direction and distance
	 */
	public Vector3f getNewPointDeg(float deg, float distance){
		float newPosX = position.x + ( distance*(float)Math.cos(deg * toRad) );
		float newPosZ = position.z + ( distance*(float)Math.sin(deg * toRad) );
		
		return new Vector3f(newPosX,position.y,newPosZ);
	}
	
	/**
	 * @return new Vector3f relative to previos position based on 
	 * radian direction and distance
	 */
	public Vector3f getNewPointRad(float rad, float distance){
		float newPosX = position.x + ( distance * (float)Math.cos(rad) );
		float newPosZ = position.z + ( distance * (float)Math.sin(rad) );
		
		return new Vector3f(newPosX,position.y,newPosZ);
	}
	
	public void update(long elapsedTime){
		super.update(elapsedTime);
	}
	
	/**
	 * check if the guard is close enough to catch the player to be 
	 * returned to the cage
	 **/
	public void capturePlayer(){
		if(Collision.stsXZ(position,1f,igs.player.position,
				igs.player.boundingSphereRadius)){
			 igs.guardSquad.reset();
			 igs.player.position = new Vector3f(-20f,0f,-20f);
			 igs.removePartyAnimals();
			 Animal a = igs.getAnimal("Turtles");
			 if(a!=null){
			 	if( a.state!=Inventory.SAVED_IN_PARTY
			 		&& a.state!=Inventory.SAVED_IN_BUSH){
			 		igs.inventory.PSYCH_CAUGHT_BEFORE_FREEING_TURTLES = 1;
			 	}
			 }
		}
	}
	
	/** adjust yrotation to position of the target */
	public void changeDirection()
	{
		float xDiff = (position.x - target.position.x);
		float zDiff = (position.z - target.position.z);
		
		refAngleRad = Math.abs((float)Math.atan(zDiff/xDiff));
		//refAngleRad = Math.abs(refAngleRad);
		
		/* quadrant 1 */
		if( position.x < target.position.x && position.z < target.position.z )
		{
			facingDirection = refAngleRad * toDeg;
		}
		
		/* quadrant 2 */
		if( position.x > target.position.x && position.z < target.position.z )
		{
			facingDirection = 180f - (refAngleRad * toDeg);
		}
		
		/* quadrant 3 */
		if( position.x > target.position.x && position.z > target.position.z )
		{
			facingDirection = 180f + (refAngleRad * toDeg);
		}
		
		/* quadrant 4 */
		if( position.x < target.position.x && position.z > target.position.z )
		{
			facingDirection = 360f - (refAngleRad * toDeg);
		}
	}
}
