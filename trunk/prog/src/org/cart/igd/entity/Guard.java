package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.discreet.Model;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;

public class Guard extends Entity
{
	public ArrayList<Vector3f> path = new ArrayList<Vector3f>();
	private int currentTarget = 0;
	private int posRange = 3;//margin of error
	private Vector3f target = new Vector3f(0,0,0);
	private boolean moving = true;
	private float hearingRadius = 5f;
	private boolean lostTarget = false;
	public float refAngleRad = 0f;
	final float toDeg = 57.2957f;
	final float toRad = 0.0174f;
	
	private Entity player;
	
	/**
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param OBJModel model: .obj format file data
	 * @param Entity refference to player for detection
	 **/
	public Guard(Vector3f pos, float fD, float bsr, OBJModel model, Entity pl,float scale)
	{
		super(pos,fD,bsr,model,scale);
		this.player = pl;
		path.add(new Vector3f(20,0,20));
		this.speed = 0.004f;
	}
	
	/**
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param Model model: .3ds format file data
	 **/
	public Guard(Vector3f pos, float fD, float bsr, Model model, Entity pl)
	{
		super(pos,fD,bsr,model);
		this.player = pl;
		path.add(new Vector3f(20,0,20));
	}
	
	/**
	 * @purpose detect whether the player's animal is within a
	 **/
	public void lookForTarget(){
		float xDiff = (position.x - player.position.x);
		float zDiff = (position.z - player.position.z);
		
		float playerDistance = (float)Math.sqrt( (xDiff*xDiff)+(zDiff+zDiff) );
		
		if( playerDistance < ( hearingRadius + player.boundingSphereRadius )&&
			testLines(30f)	)
		{
			lostTarget = false;
			target = player.position;
		} else if(!lostTarget){//loose target back to patrol
			target = null;
			lostTarget = true;
		}
	}
	public boolean testLines(float view){
		Vector3f left;
		Vector3f right;
		
		
		return true;
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
		lookForTarget();
		
		if(target == null && path.size()>0){
			moving = true;
			target=path.get(0);
		}
		
		if(moving == true && target != null)
		{		
			float xDiff = (position.x - target.x);
			float zDiff = (position.z - target.z);
			
			refAngleRad = (float)Math.atan(zDiff/xDiff);
			//refAngleRad = Math.abs(refAngleRad);
			
			/* quadrant 1 */
			if( position.x < target.x && 
				position.z < target.z )
			{
				facingDirection = refAngleRad * toDeg;
			}
			
			/* quadrant 2 */
			if( position.x > target.x && 
				position.z < target.z )
			{
				facingDirection = 90f + (refAngleRad * toDeg);
			}
			
			/* quadrant 3 */
			if( position.x > target.x && 
				position.z > target.z )
			{
				facingDirection = 180f + (refAngleRad * toDeg);
			}
			
			/* quadrant 4 */
			if( position.x < target.x && 
				position.z > target.z )
			{
				facingDirection = 270f + (refAngleRad * toDeg);
			}
			
			/* change course when target reached */
			if( xDiff < posRange && zDiff < posRange)
			{
				getNextTarget();
			}
			
			walkForward(elapsedTime);
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
