package org.cart.igd.game;

import org.cart.igd.models.obj.OBJAnimation;
import org.cart.igd.models.obj.OBJModel;
import javax.media.opengl.GL;
import org.cart.igd.math.Vector3f;
import org.cart.igd.entity.*;
import org.cart.igd.states.*;


/**
 * Animal.java
 * 
 * General Purpose: 
 * represent animals as entities and render them at locations
 * based on the progression of the game
 */
public class Animal extends Entity
{
 	public static final int FLAMINGO = 0;
	public static final int TURTLES = 1; 
	public static final int PANDA = 2; 
	public static final int KANGAROO = 3; 
	public static final int GIRAFFE = 4; 
	public static final int TIGER = 5; 
	public static final int PENGUIN = 6; 
	public static final int MEERKAT = 7; 
	public static final int WOODPECKER = 8;
	public static final int ELEPHANT = 9;  

	
	/* states
	0 = incage not talked to
	1 = incage waiting for item
	2 = incage ready to be saved
	3 = incage ready to be saved after item given
	*/
	public static final int SAVED_BUSH = 4;
	public static final int SAVED_PARTY = 5;
	
	public int animalId;

	public int itemWanted;
	
	public String name;
	public int state = 0;
	private boolean Collide = false;
	private InGameState igs;
	private Vector3f bushLocation;
	
	public Animal(String name,int animalId,float fd, float bsr, OBJModel model, 
			Vector3f location,InGameState igs, int itemWanted, Vector3f bushLocation){
		super(location,fd,bsr, model);
		this.name = name;
		this.animalId = animalId;
		this.igs = igs;	
		this.itemWanted = itemWanted;
		this.bushLocation = bushLocation;
	}
	
	/**
	 * with OBJAnimation
	 **/
	public Animal(String name,int animalId,float fd, float bsr, 
			OBJAnimation model, Vector3f location,InGameState igs, int itemWanted, Vector3f bushLocation){
		super(location,fd,bsr, model);
		this.name = name;
		this.animalId = animalId;
		this.igs = igs;	
		this.itemWanted = itemWanted;
		this.bushLocation = bushLocation;
	}
		
	public boolean talkable(){
		if(state==SAVED_BUSH || state == SAVED_PARTY) return false;
		return true;
	}
	
	public void relocateToBush(){
		position = bushLocation;
		super.boundingSphereRadius =30f;
	}
	public void update(Vector3f playerPosition){
		//System.out.println("Im " + animalId + "  My State is " + state);
		/*if(state < 4){
			float xDiff = Math.abs(playerPosition.x - this.position.x);
			float zDiff = Math.abs(playerPosition.z - this.position.z);
			if(xDiff < boundingSphereRadius && zDiff<boundingSphereRadius){
				if(!Collide&&igs.currentGuiState!=1){
					//if(igs.engageTalk){
						((Dialogue)igs.gui.get(1)).createDialogue(this,igs);
						igs.changeGuiState(1);
					//}
				}
				Collide = true;
			} else{
				Collide = false;
			}
		}*/
	}
	
	public void update(long elapsedTime){
		super.update(elapsedTime);
	}
	
	public int getState(){
		return state;
	}
	
	public void display(GL gl){
		if(animalId==1){
			if(state!=SAVED_PARTY)super.render(gl);
			if(state!=SAVED_PARTY)super.renderLocation(gl,new Vector3f(position.x+3f,position.y,position.z+3f),140f);
			if(state!=SAVED_PARTY)super.renderLocation(gl,new Vector3f(position.x+3f,position.y,position.z-3f),45f);
		}else{
			if(state!=SAVED_PARTY)super.render(gl);
		}
	}
}