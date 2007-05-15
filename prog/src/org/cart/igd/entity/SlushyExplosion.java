package org.cart.igd.entity;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.MiniGame;
import java.util.*;

public class SlushyExplosion extends Entity {

	private float gravityPull = -.2f;
	private MiniGame mg;
	/** speed per millisecond */
	private float speed = .08f;
	private float yVec = -.1f;
	private Vector3f[] pieces;
	
	
	/**
	 * SlushyExplosion.java
	 * 
	 * 
	 * General Purpose:
	 * effect when slushi ball hits something
	 */
	public SlushyExplosion(Vector3f pos, float fD, float bsr, OBJModel model,
			MiniGame mg) 
	{
		super(pos, fD, bsr, model);
		this.mg = mg;
		pieces =   new Vector3f[new Random().nextInt(30)+1];
		for(int i = 0;i<pieces.length;i++){
			pieces[i] = new Vector3f(pos.x+new Random().nextFloat()*4-2f,pos.y+new Random().nextFloat()*4-2f,pos.z+new Random().nextFloat()*4-2f);
		}
	}
	
	public void update(long elapsedTime){
		
		float degreeAdd = 360f/pieces.length;
		float degreeAt = 0f;
		for(int i = 0;i<pieces.length;i++){
			pieces[i].y-=gravityPull;
			pieces[i].x = pieces[i].x + ( (float)Math.cos((degreeAt)
						* 0.0174f) )* speed/4 *(float)elapsedTime;
			pieces[i].z = pieces[i].z + ( (float)Math.sin((degreeAt)
						* 0.0174f) )* speed/4 *(float)elapsedTime;
						degreeAt+=degreeAdd;
		}
		gravityPull+=.01f;
		
		
		

		//if(!(position.y< -1f)){
		//	position.y -= yVec;
			///	position.x = position.x + ( (float)Math.cos((facingDirection)
				//		* 0.0174f) )* speed *(float)elapsedTime;
				//position.z = position.z + ( (float)Math.sin((facingDirection)
		//				* 0.0174f) )* speed *(float)elapsedTime;
		//		yVec += gravityPull*(float)elapsedTime;
		if(pieces[0].y<-4f){
				mg.removeList.add(this);
		}
		
		
	}
	public void render(GL gl){
		for(int i = 0;i<pieces.length;i++){
			super.renderLocation(gl,pieces[i]);
		}
	}
	
}
