package org.cart.igd.game;

import org.cart.igd.models.obj.OBJModel;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.cart.igd.math.Vector3f;
import org.cart.igd.entity.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.input.*;

public class Tree extends Entity {

	
	public Tree(float fd, float bsr, OBJModel model, Vector3f location){
		super(location,fd,bsr, model);
	}
	

	
	public void update(Vector3f playerPosition){
	
	}
	
	public void display(GL gl){
		render(gl);
	}

}