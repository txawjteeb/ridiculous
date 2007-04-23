package org.cart.igd.game;

import org.cart.igd.models.obj.OBJModel;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.cart.igd.math.Vector3f;
import org.cart.igd.entity.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;

public class Item extends Entity {
	/*
	 states 
	 0 in map
	 1 in inventory
	 2 used
	 */
	public Texture texture;
	public String name;
	public int id;
	public int state = 0;
	public int amount;
	
	public Item(String name,int id, int amount,float fd, float bsr, OBJModel model, Texture texture, Vector3f location){
		super(location,fd,bsr, model);
		this.name = name;
		this.id = id;
		this.amount = amount;
		this.texture = texture;
	}
	
	public void update(Vector3f playerPosition){
		if(state == 0){
			float xDiff = Math.abs(playerPosition.x - this.position.x);
			float zDiff = Math.abs(playerPosition.z - this.position.z);
			if(xDiff < boundingSphereRadius && zDiff<boundingSphereRadius) state = 1;
		}
		
	}
}

