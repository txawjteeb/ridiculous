package org.cart.igd.game;

import java.util.ArrayList;

import javax.media.opengl.GL;

import org.cart.igd.entity.Entity;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.util.ColorRGBA;
import org.cart.igd.util.SkyDome;
/* class for containment all non interactive objecets */
public class Terrain
{
	public boolean loaded = false;
	
	private SkyDome skyDome;
	private OBJModel worldMap;
	
	ArrayList <Tree> trees = new ArrayList<Tree>();
	
	
	public void load(GL gl){
		skyDome	 = new SkyDome( 0, 90, 300f, new ColorRGBA( 0, 51, 51 ), gl);
		worldMap = new OBJModel( gl, "zoo_map_vm", 500, false);	
		
		OBJModel tree0 = new OBJModel(gl, "tree0",4f,false);
		OBJModel tree1 = new OBJModel(gl, "tree1",4f,false);
		OBJModel tree2 = new OBJModel(gl, "tree2",4f, false);
		OBJModel tree3 = new OBJModel(gl, "tree3",4f,false);	
		
		trees.add( new Tree(0f,3f,tree0, new Vector3f(0f,0f,20f)));		
		trees.add( new Tree(0f,3f,tree2, new Vector3f(1f,0f,-35f)));
		trees.add( new Tree(0f,3f,tree3, new Vector3f(4f,0f,0f)));
		trees.add( new Tree(0f,3f,tree0, new Vector3f(6f,0f,10f)));
		trees.add( new Tree(0f,3f,tree1, new Vector3f(16f,0f,20f)));
		trees.add( new Tree(0f,3f,tree2, new Vector3f(20f,0f,-20f)));
		trees.add( new Tree(0f,3f,tree0, new Vector3f(12f,0f,22f)));
		trees.add( new Tree(0f,3f,tree0, new Vector3f(25f,0f,0f)));
		trees.add( new Tree(0f,3f,tree1, new Vector3f(40f,0f,10f)));		
		trees.add( new Tree(0f,3f,tree3, new Vector3f(30f,0f,20f)));
		trees.add( new Tree(0f,3f,tree2, new Vector3f(35f,0f,45f)));
		trees.add( new Tree(0f,3f,tree0, new Vector3f(42f,0f,-35f)));
		trees.add( new Tree(0f,3f,tree0, new Vector3f(63f,0f,0f)));
		trees.add( new Tree(0f,3f,tree2, new Vector3f(-10f,0f,20f)));
		trees.add( new Tree(0f,3f,tree3, new Vector3f(-51f,0f,10f)));
		trees.add( new Tree(0f,3f,tree0, new Vector3f(-15f,0f,7f)));
		trees.add( new Tree(0f,3f,tree0, new Vector3f(-120f,0f,-41f)));		
		trees.add( new Tree(0f,3f,tree2, new Vector3f(-680f,0f,20f)));
		trees.add( new Tree(0f,3f,tree0, new Vector3f(-390f,0f,10f)));
		trees.add( new Tree(0f,3f,tree3, new Vector3f(-6f,0f,32f)));
		trees.add( new Tree(0f,3f,tree0, new Vector3f(-13f,0f,-6f)));
		trees.add( new Tree(0f,3f,tree1, new Vector3f(-190f,0f,20f)));
		trees.add( new Tree(0f,3f,tree2, new Vector3f(-25f,0f,-25f)));
		trees.add( new Tree(0f,3f,tree3, new Vector3f(-50f,0f,10f)));
		
		loaded = true;
	}
	
	/** render all terrain */
	public void render(GL gl, Entity player){
		/* Render Land Map */
		gl.glPushMatrix();
			gl.glTranslatef(0f, 6f, 0f);
			worldMap.draw(gl);
		gl.glPopMatrix();
		
		/* Render trees */
		for(int i = 0;i<trees.size();i++){
			Tree tree = trees.get(i);
			tree.render(gl);
		}
		
		/* Render SkyDome */
		gl.glPushMatrix();
			gl.glDisable(GL.GL_LIGHTING);
			gl.glDisable(GL.GL_LIGHT0);
			gl.glDisable(GL.GL_CULL_FACE);
			gl.glTranslatef(player.position.x, player.position.y-30f, 
					player.position.z);
			skyDome.render(gl);
			gl.glEnable(GL.GL_CULL_FACE);
			gl.glEnable(GL.GL_LIGHTING);
			gl.glEnable(GL.GL_LIGHT0);
		gl.glPopMatrix();
	}
}
