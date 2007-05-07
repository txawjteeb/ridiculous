package org.cart.igd.models.obj;

import javax.media.opengl.GL;

import org.cart.igd.math.Vector3f;

public class OBJAnimation {
	long frameDelay = 90;
	long timeLeft = 90;
	
	int modelIndex = 0;
	
	OBJModel model[];
	
	int fd = 0;
	
	public Vector3f position = new Vector3f( 0f, 2f, 0f );
	
	public OBJAnimation(GL gl){
		
		model = new OBJModel[10];
		for(int i = 0; i<10; i++){
			model[i] = new OBJModel(gl,"data/models/flamingo/flamingo_0"+i);
		}
		
		
	}
	
	public void update(long elapsedTime){
		timeLeft -=elapsedTime;
		if(timeLeft < 0){
			if(modelIndex < model.length-1){
				modelIndex ++;
			} else {
				modelIndex = 0;
			}
			
			timeLeft = frameDelay;
		}
		
	}
	
	public void render(GL gl){
		gl.glPushMatrix();
		gl.glTranslatef(position.x, position.y, position.z);
		gl.glRotatef( fd , 0f, -1f, 0f);
		//gl.glScalef(scale.x,scale.y,scale.z);
		model[modelIndex].draw(gl);
		gl.glPopMatrix();
	}

}
