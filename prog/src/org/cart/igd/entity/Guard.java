package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.discreet.Model;
import org.cart.igd.math.Vector3f;

public class Guard extends Entity
{
	private ArrayList<Vector3f> path = new ArrayList<Vector3f>();
	private int currentTarget = 0;
	private int posRange = 3;//margin of error
	private Vector3f target = new Vector3f(0,0,0);
	private boolean moving = true;
	
	public Guard(Vector3f pos, float fD, float bsr, Model model)//, int id, File meshFile, File skinFile)// throws EntityException
	{
		super(pos,fD,bsr,model);
		
		/* test path for guards to follow */
		path.add(new Vector3f(50,0,20));
		path.add(new Vector3f(-90,0,-20));
		path.add(new Vector3f(-90,0,90));
		path.add(new Vector3f(20,0,-30));
		
		if(path.size() > 0){
			target=path.get(0);
		} else {
			moving = false; //wait for target to get in range
		}
	}
	
	/**
	 * Test whether the player's animal is withing range 
	 **/
	public void lookForTarget(){
		
		
	}
	
	public void update(long elapsedTime){
		lookForTarget();
		if(moving == true)
		{
			float xDiff = Math.abs(position.x - target.x);
			float zDiff = Math.abs(position.z - target.z);
			
			double refAngleRad = Math.atan(zDiff/xDiff);
			
			System.out.println(""+currentTarget);
			
			/* quadrant 1*/
			if( position.x < target.x && 
				position.z < target.z )
			{
				facingDirection = (float)Math.toDegrees(refAngleRad);
			}
			
			/* quadrant 2*/
			if( position.x > target.x && 
				position.z < target.z )
			{
				facingDirection = 90f+(float)Math.toDegrees(refAngleRad);
			}
			
			/* quadrant 3*/
			if( position.x > target.x && 
				position.z > target.z )
			{
				facingDirection = 180f+(float)Math.toDegrees(refAngleRad);
			}
			
			/* quadrant 4*/
			if( position.x < target.x && 
				position.z > target.z )
			{
				facingDirection = 270f+(float)Math.toDegrees(refAngleRad);
			}
			
			walkForward(elapsedTime);
			
			/* change course when target reached*/
			if( xDiff < posRange && zDiff < posRange)
			{
				getNextTarget();
			}
		}
		
	}
	
	private void getNextTarget(){
		if(currentTarget < path.size()-1){
			currentTarget++;
			target = path.get(currentTarget);
		}else{
			currentTarget=0;
			target = path.get(currentTarget);
		}
	}
	
}
