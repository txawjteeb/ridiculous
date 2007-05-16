

package org.cart.igd.game;

import org.cart.igd.entity.Entity;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.*;
import org.cart.igd.gui.*;
import javax.media.opengl.GL;


public class Cage extends Entity{
	InGameState igs;
	int id;
	int cageId;

	public Cage(int id,Vector3f pos, float fD, float bsr, OBJModel model,InGameState igs){
		super(pos, fD, bsr, model);

		this.igs = igs;
		this.id = id;
		cageId = id;
	}
	
	public void display(GL gl){
			super.render(gl);		
	}
	
	public void update(Vector3f playerPosition,long elapsedTime){
				
	}
	
	public boolean collide(Vector3f playerPosition,boolean forward,long elapsedTime,float fd,float speed){
		if(this.id ==0&&igs.inventory.inCage){
			Vector3f fake = new Vector3f(playerPosition.x,playerPosition.y,playerPosition.z);
			if(forward){
				fake.x += ( ((float)elapsedTime * speed) * (float)Math.cos(fd * 0.0174f) );
				fake.z += ( ((float)elapsedTime * speed) * (float)Math.sin(fd * 0.0174f) );			
			}else{
				fake.x -= ( ((float)elapsedTime * speed) * (float)Math.cos(fd * 0.0174f) );
				fake.z -= ( ((float)elapsedTime * speed) * (float)Math.sin(fd * 0.0174f) );	
			}
	
			
			float xDiff = Math.abs(fake.x - this.position.x);
			float zDiff = Math.abs(fake.z - this.position.z);
			if(xDiff < boundingSphereRadius && zDiff<boundingSphereRadius){
				return false;
			} else{
				igs.inventory.inCage = false;
				return false;
			}
			
		}
		
		
		Vector3f fake = new Vector3f(playerPosition.x,playerPosition.y,playerPosition.z);
		if(forward){
			fake.x += ( ((float)elapsedTime * speed) * (float)Math.cos(fd * 0.0174f) );
			fake.z += ( ((float)elapsedTime * speed) * (float)Math.sin(fd * 0.0174f) );			
		}else{
			fake.x -= ( ((float)elapsedTime * speed) * (float)Math.cos(fd * 0.0174f) );
			fake.z -= ( ((float)elapsedTime * speed) * (float)Math.sin(fd * 0.0174f) );	
		}

		
		float xDiff = Math.abs(fake.x - this.position.x);
		float zDiff = Math.abs(fake.z - this.position.z);
		if(xDiff < boundingSphereRadius && zDiff<boundingSphereRadius){
			return true;
		}
		return false;	
	}
	
	
}