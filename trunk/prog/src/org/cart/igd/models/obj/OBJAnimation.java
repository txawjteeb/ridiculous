package org.cart.igd.models.obj;

import javax.media.opengl.GL;

import org.cart.igd.math.Vector3f;

public class OBJAnimation {
	long frameDelay;
	long timeLeft;
	
	int modelIndex = 0;
	
	OBJModel model[];
	
	public OBJAnimation( GL gl, int numFrames, String filePath, long delay )
	{
		frameDelay = delay;
		timeLeft = delay;
		model = new OBJModel[numFrames];
		for(int i = 0; i<model.length; i++){
			model[i] = new OBJModel(gl,filePath+i);
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
	
	public void render(GL gl, Vector3f pos, float fd){
		gl.glPushMatrix();
		gl.glTranslatef(pos.x, pos.y -2f, pos.z);
		gl.glRotatef( fd , 0f, -1f, 0f);
		//gl.glScalef(scale.x,scale.y,scale.z);
		model[modelIndex].draw(gl);
		gl.glPopMatrix();
	}

}
