package org.cart.igd.game;

import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.entity.Entity;
import org.cart.igd.entity.Guard;
import org.cart.igd.entity.GuardFlag;
import org.cart.igd.entity.Noise;
import org.cart.igd.entity.PartySnapper;
import org.cart.igd.entity.StandingGuard;
import org.cart.igd.entity.WalkingGuard;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJAnimation;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;


/**
 * use: to organize guards as a unit, when one guard 
 * sight an animal the whole guard team is alerted
 **/
public class GuardSquad {
	public boolean reset = false;
	private InGameState igs;
	public GuardSquad(InGameState igs){
		this.igs = igs;
	}
	
	public void init(GL gl, GLU glu)
	{
		/* create an OBJModel used for guard */
		OBJAnimation guardIdle = new OBJAnimation(gl,10, 
				"guard_idle_",150,1.5f,true);
		OBJAnimation guardWalk = new OBJAnimation(gl,10, 
				"guard_notice_",150,1.5f, true);
				
				
				/*
				 	  /// orange = 1 blue = 2 red = 3 yellow = 4
	  public int PSYCH_CAUGHT_ORANGE = 0;
	  public int PSYCH_CAUGHT_BLUE = 0;
	  public int PSYCH_CAUGHT_RED = 0;
	  public int PSYCH_CAUGHT_YELLOW = 0;
				 */
		OBJAnimation orangeGuard = new OBJAnimation(gl,1, 
				"orange_guard",150,1.5f, true);
		OBJAnimation blueGuard = new OBJAnimation(gl,1, 
				"blue_guard",150,1.5f, true);
		OBJAnimation redGuard = new OBJAnimation(gl,1, 
				"red_guard",150,1.5f, true);
		OBJAnimation yellowGuard = new OBJAnimation(gl,1, 
				"yellow_guard",150,1.5f, true);
		
		/* create and add test guard */
		WalkingGuard g1 = new WalkingGuard(
				new Vector3f(0f,0f,0f),0f,.5f,guardIdle,guardWalk,igs,.004f,1); 
		WalkingGuard g2 = new WalkingGuard(
				new Vector3f(0f,0f,0f),0f,.5f,guardIdle,guardWalk,igs,.004f,1);
		
		igs.entities.add(g1);
		createPath(g1, new Vector3f(10f,0f,10f),17f);
		
		igs.entities.add(g2);
		createPath(g2, new Vector3f(-10f,0f,-10f),8f);
		
		igs.entities.add(new StandingGuard(new Vector3f(0f,0f,0f),0f,.5f,blueGuard,blueGuard,igs,.004f,90,2));
	}
	
	/**
	 * @param g walking guard path asigned
	 *@param p center point of the patrol area
	 *@param s size of the patrol perimiter 
	 */
	public void createPath(Guard g, Vector3f p, float s){
		/* create paths for the guard to follow*/
		g.path.add(new GuardFlag(new Vector3f(p.x+s,0f,p.y+s),1f,1f));
		g.path.add(new GuardFlag(new Vector3f(p.x-s,0f,p.y+s),1f,1f));
		g.path.add(new GuardFlag(new Vector3f(p.x-s,0f,p.y-s),1f,1f));
		g.path.add(new GuardFlag(new Vector3f(p.x+s,0f,p.y-s),1f,1f));
	}
	
	public void reset(){
		if(reset == true){
			
		
		synchronized(igs.entities){
			Iterator itr = igs.entities.iterator();
			while(itr.hasNext()){
			
				Entity e = (Entity)itr.next();
				if( e instanceof Noise || e instanceof PartySnapper){
					igs.removeList.add(e); 
				}
			}
		}
		reset = false;
		}
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
