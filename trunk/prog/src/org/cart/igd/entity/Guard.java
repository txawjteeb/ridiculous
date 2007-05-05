package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.collision.Collision;
import org.cart.igd.discreet.Model;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;

public class Guard extends Entity
{
	public static final int PATROLING  = 0;
	public static final int STATIONARY = 1;
	
	public int type = 1;
	
	private InGameState igs;
	public ArrayList<Entity> path = new ArrayList<Entity>();
	private Entity player;
	private Entity target;
	
	private int currentTarget = 0;
	private float posRange = .4f;//margin of error
	
	private boolean moving = true;
	private float hearingRadius = 5f;
	private boolean lostTarget = false;
	public float refAngleRad = 0f;
	public float visionDistance = 7f;
	
	final float toDeg =57.29578f;
	final float toRad = 0.01745f;
	final float    pi = 3.14159f;
	
	/** used by stationary guards to return to post */
	private GuardFlag home;
	

	/**
	 * Create a guard with OBJModel
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param OBJModel model: .obj format file data
	 * @param InGameState refference the game
	 * @param float scale of the model
	 **/
	public Guard(Vector3f pos, float fD, float bsr, OBJModel model, 
			InGameState igs,float scale)
	{
		super(pos,fD,bsr,model,scale);
		this.igs = igs;
		home = new GuardFlag(
				new Vector3f( pos.x, pos.y, pos.z ), 1f, 1f );
		this.player = igs.player;
		this.speed = 0.004f;
	}
	
	/**
	 * Create a guard with 3DSModel 
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param Model model: .3ds format file data
	 **/
	public Guard(Vector3f pos, float fD, float bsr, Model model, InGameState igs)
	{
		super(pos,fD,bsr,model);
		this.player = igs.player;
		this.speed = 0.004f;
	}
	
	/**
	 * @purpose detect whether the player's animal is within range
	 **/
	public void lookForTarget(){
		float xDiff = (position.x - player.position.x);
		float zDiff = (position.z - player.position.z);
		
		float playerDistance = (float)Math.sqrt( (xDiff*xDiff)+(zDiff+zDiff) );
		
		if( playerDistance < ( hearingRadius + player.boundingSphereRadius ) )
		{
			lostTarget = false;
			target = player;
		} else if(!lostTarget){//loose target back to patrol
			target = null;
			lostTarget = true;
		}
	}
	
	public boolean lookForPlayer(){
		Vector3f s = getNewPointDeg(facingDirection, visionDistance);
		
		boolean see = Collision.stsXZ(s,visionDistance,igs.player.position,1f);
		
		return see;
	}
	
	public Vector3f getNewPointDeg(float deg,float distance){
		float newPosX = position.x + ( distance*(float)Math.cos(deg * toRad) );
		float newPosZ = position.z + ( distance*(float)Math.sin(deg * toRad) );
		
		return new Vector3f(newPosX,position.y,newPosZ);
	}
	
	public Vector3f getNewPointRad(float rad,float distance){
		float newPosX = position.x + ( distance * (float)Math.cos(rad) );
		float newPosZ = position.z + ( distance * (float)Math.sin(rad) );
		
		return new Vector3f(newPosX,position.y,newPosZ);
	}
	
	public void update(long elapsedTime){
		//lookForTarget();
		
		//if(target == null && (path.size()>0 || home!=null)  ){
		///	moving = true;
		///	target=path.get(0);
		//}
		
		

		if( lookForPlayer() ){
				
		}
			
		if(type == PATROLING){
				
			changeDirection();
				
			if( !(target instanceof Player) ){
				if( Collision.stsXZ(position,1f,target.position,1f) ){
					getNextTarget();
				}
			}

			walkForward(elapsedTime);
		}
			
		if(type == STATIONARY)
		{
			if(lookForPlayer()){
				target = player;
				lostTarget = false;
			} else if(!lostTarget){//loose target back to patrol
				target = home;
				lostTarget = true;
			}
			
			if( target instanceof Player){
				if(Collision.stsXZ(position, visionDistance, player.position,1f))
				{
					changeDirection();
					walkForward(elapsedTime);
				} else {
					target = home;
				}
				
				
				
			}
			
			if( target instanceof GuardFlag ){
				if(Collision.stsXZ(position,1f,target.position,1f))
				{
					target = home;
					facingDirection = 0;//TODO give a var direction to guard
				} else {
					target = home;
					changeDirection();
					walkForward(elapsedTime);
				}
			} 
			
			lostTarget = true;
		}
		
	}
	
	/** adjust yrotation to position of the target */
	private void changeDirection()
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
	
	private void getNextTarget()
	{
		if(type == PATROLING){
			if(currentTarget < path.size()-1)
			{
				currentTarget++;
				target = path.get(currentTarget);
			}else{
				currentTarget=0;
				target = path.get(currentTarget);
			}
		}
		
		if(type == STATIONARY){
			
			
			float xDiff = (position.x - player.position.x);
			float zDiff = (position.z - player.position.z);
		}
		
	}
}
