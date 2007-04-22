package org.cart.igd.entity;

import java.util.ArrayList;

import org.cart.igd.discreet.Model;
import org.cart.igd.math.Vector3f;

public class Guard extends Entity {
	private ArrayList<Vector3f> path = new ArrayList<Vector3f>();
	private int currentTarget = 0;
	private int posRange = 3;//margin of error
	public Guard(Vector3f pos, float fD, float bsr, Model model)//, int id, File meshFile, File skinFile)// throws EntityException
	{
		super(pos,fD,bsr,model);
		/* test path for guards to follow */
		path.add(new Vector3f(50,0,20));
		path.add(new Vector3f(-90,0,-20));
		path.add(new Vector3f(-90,0,90));
		path.add(new Vector3f(20,0,-30));
		
	}
	
	public void update(long elapsedTime){
		float xDiff = Math.abs(position.x - path.get(currentTarget).x);
		float zDiff = Math.abs(position.z - path.get(currentTarget).z);
		
		double refAngleRad = Math.atan(zDiff/xDiff);
		
		System.out.println(""+currentTarget);
		
		/* quadrant 1*/
		if( position.x < path.get(currentTarget).x && 
			position.z < path.get(currentTarget).z )
		{
			facingDirection = (float)Math.toDegrees(refAngleRad);
		}
		
		/* quadrant 2*/
		if( position.x > path.get(currentTarget).x && 
			position.z < path.get(currentTarget).z )
		{
			facingDirection = 90f+(float)Math.toDegrees(refAngleRad);
		}
		
		/* quadrant 3*/
		if( position.x > path.get(currentTarget).x && 
			position.z > path.get(currentTarget).z )
		{
			facingDirection = 180f+(float)Math.toDegrees(refAngleRad);
		}
		
		/* quadrant 4*/
		if( position.x < path.get(currentTarget).x && 
			position.z > path.get(currentTarget).z )
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
	
	private void getNextTarget(){
		if(currentTarget < path.size()-1){
			currentTarget++;
		}else{
			currentTarget=0;
		}
	}
	
}
