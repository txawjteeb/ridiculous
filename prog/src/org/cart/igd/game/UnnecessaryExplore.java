

package org.cart.igd.game;

import org.cart.igd.entity.Entity;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.*;
import org.cart.igd.gui.*;
import javax.media.opengl.GL;


public class UnnecessaryExplore extends Entity{
	InGameState igs;
	long timeToUpdate = 0;
	long updateTime = 30;
	long timeToReInZone = 5000;
	long timeCounterToReInZone = 0;
	long timeInZone = 0;
	boolean display = false;
	boolean inZone = false;
	String name;
	
	public UnnecessaryExplore(String name,Vector3f pos, float fD, float bsr, OBJModel model,InGameState igs,boolean display){
		super(pos, fD, bsr, model);
		this.display = display;
		this.igs = igs;
		this.name = name;
	}
	
	public void display(GL gl){
		if(display){
			super.render(gl);		
		}
	
	}
	
	public void update(Vector3f playerPosition,long elapsedTime){
		
		float xDiff = Math.abs(playerPosition.x - this.position.x);
		float zDiff = Math.abs(playerPosition.z - this.position.z);
		if(xDiff < boundingSphereRadius && zDiff<boundingSphereRadius){
			//System.out.println(name+ "  you are in me");
			
			timeInZone += elapsedTime;
			if(!inZone){
				if(name.equals("Water")&&igs.inventory.PSYCH_FOOD_WATER_AFFINITY==0){
					igs.inventory.PSYCH_FOOD_WATER_AFFINITY = 2;
				} else if(name.equals("Food")&&igs.inventory.PSYCH_FOOD_WATER_AFFINITY==0){
					igs.inventory.PSYCH_FOOD_WATER_AFFINITY = 1;
				}
				timeInZone = 0;
				inZone = true;
				if(timeCounterToReInZone>timeToReInZone)igs.inventory.PSYCH_ENTERED_UNIMPORTANT_PLACES_ON_MAP++;
			}
		} else {
			if(inZone){
				timeCounterToReInZone = 0;
				if(name.equals("Nothing"))igs.inventory.PSYCH_TIME_IN_UNIMPORTANT_PLACES_ON_MAP+=timeInZone/1000;
				if(igs.inventory.PSYCH_FIRST_DIRECTION==0&&timeInZone/1000>2){
					if(name.equals("Up")){
						igs.inventory.PSYCH_FIRST_DIRECTION=2;
					}else if(name.equals("Down")){
						igs.inventory.PSYCH_FIRST_DIRECTION=4;
					}else if(name.equals("Left")){
						igs.inventory.PSYCH_FIRST_DIRECTION=3;
					}else if(name.equals("Right")){
						igs.inventory.PSYCH_FIRST_DIRECTION=1;
					}
				}
				
			}
			inZone = false;
			timeCounterToReInZone+=elapsedTime;
			
		}
	}
	
}