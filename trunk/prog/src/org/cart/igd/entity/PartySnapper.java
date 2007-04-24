package org.cart.igd.entity;

import java.io.File;

import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.util.ColorRGBA;
import org.cart.igd.math.Vector3f;
import org.cart.igd.core.Kernel;
import org.cart.igd.entity.*;
import org.cart.igd.models.obj.OBJModel;

public class PartySnapper extends Entity{
		float gravitypull = -.2f;
		
		public PartySnapper (Vector3f pos, float fD, float bsr, OBJModel model){
			super(pos,fD,bsr, model);
			//position.y+= new Random().nextFloat()*20;
		}
	
		public void draw(GL gl){
			gl.glPushMatrix();
				gl.glTranslatef(position.x, position.y, position.z);
				gl.glRotatef(facingDirection, 0f, -1f, 0f);
				modelObj.draw(gl);
			gl.glPopMatrix();
		}
		
		public void update(long elapsedTime){
			if(!(position.y<-10f)){
				position.y-=gravitypull;
					position.x = position.x + ( (float)Math.cos((facingDirection) * 0.0174f) )*2;
					position.z = position.z + ( (float)Math.sin((facingDirection) * 0.0174f) )*2;
					gravitypull+=.02f;
		
				//position.x-=;
				//position.z-=;
			}
		}
		
		
		
}
