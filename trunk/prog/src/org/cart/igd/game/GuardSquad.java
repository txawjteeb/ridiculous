package org.cart.igd.game;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.entity.Entity;
import org.cart.igd.entity.Guard;
import org.cart.igd.entity.GuardFlag;
import org.cart.igd.entity.StandingGuard;
import org.cart.igd.entity.WalkingGuard;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;


/**
 * use: to organize guards as a unit, when one guard 
 * sight an animal the whole guard team is alerted
 **/
public class GuardSquad {

	private InGameState igs;
	public GuardSquad(InGameState igs){
		this.igs = igs;
	}
	
	public void init(GL gl, GLU glu)
	{
		/* create an OBJModel used for guard */
		OBJModel guard = new OBJModel(gl, "data/models/guard_vm",1.5f,false);
		
		
		/* create and add test guard */
		igs.entities.add(new WalkingGuard(new Vector3f(0f,0f,0f),0f,.5f,guard,igs,.004f));
		
		/* create paths for the guard to follow*/
		((Guard)igs.entities.get(0)).path.add(new GuardFlag(new Vector3f(10f,0f,10f),1f,1f));
		((Guard)igs.entities.get(0)).path.add(new GuardFlag(new Vector3f(-10f,0f,10f),1f,1f));
		((Guard)igs.entities.get(0)).path.add(new GuardFlag(new Vector3f(-10,0f,-10f),1f,1f));
		((Guard)igs.entities.get(0)).path.add(new GuardFlag(new Vector3f(10f,0f,-10f),1f,1f));
		
		igs.entities.add(new StandingGuard(new Vector3f(0f,0f,0f),0f,.5f,guard,igs,.004f,90));
	}
	
	public void raiseAlarm(){
		for( Entity e : igs.entities){
			if(e instanceof Guard){
				e.speed = .008f;
			}
		}
	}
	
	public void stopAlarm(){
		for( Entity e : igs.entities){
			if(e instanceof Guard){
				e.speed = .004f;
			}
		}
	}
}
