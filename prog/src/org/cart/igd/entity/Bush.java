package org.cart.igd.entity;

import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.InGameState;

public class Bush extends Entity
{
	private InGameState igs;
	
	public Bush(Vector3f pos, float bsr, OBJModel model, InGameState igs)//, int id, File meshFile, File skinFile)// throws EntityException
	{
		super(pos,0,bsr,model);
		this.igs = igs;
	}
	public void update(long elapsedTime){
		float xDiff = igs.player.position.x - position.x;
		float zDiff = igs.player.position.z - position.z;
		
		float distanceToBush = (float)Math.sqrt( (xDiff*xDiff)+(zDiff*zDiff) );
		
		if(distanceToBush < boundingSphereRadius){
			igs.nearBush=true;
		} else {
			igs.nearBush=false;
		}
	}
}
