package org.cart.igd.game;

import org.cart.igd.models.obj.OBJModel;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.cart.igd.math.Vector3f;
import org.cart.igd.entity.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;



public class Animal extends Entity{
	/*
	 states
	 0 = incage not talked to
	 1 = incage waiting for item
	 2 = incage ready to be saved
	 3 = saved by bush
	 4 = saved in party
	 
	 
	 ids
	 0 flamingo
	 1 turtles
	 2 panda
	 3 kangaroo
	 4 giraffe
	 5 tiger
	 6 penguin
	 7 meerkat
	 8 woodpecker 
	 9 elephant
	 */
	public String name;
	public int id;
	public int state = 0;
	
	
	public Animal(String name,int id,float fd, float bsr, OBJModel model, Vector3f location){
		super(location,fd,bsr, model);
		this.name = name;
		this.id = id;
	}	
		
	public void update(Vector3f playerPosition){
		if(state < 3){
			float xDiff = Math.abs(playerPosition.x - this.position.x);
			float zDiff = Math.abs(playerPosition.z - this.position.z);
			if(xDiff < boundingSphereRadius && zDiff<boundingSphereRadius) state = 1;
		}
	}
	
	public void display(GL gl){
		if(state<4)render(gl);
	}
}