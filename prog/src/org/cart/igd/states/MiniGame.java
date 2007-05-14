package org.cart.igd.states;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.Camera;
import org.cart.igd.Renderer;
import org.cart.igd.core.Kernel;
import org.cart.igd.entity.Entity;
import org.cart.igd.entity.PartySnapper;
import org.cart.igd.entity.Player;
import org.cart.igd.entity.PowerBox;
import org.cart.igd.game.Item;
import org.cart.igd.game.SlushyBall;
import org.cart.igd.gui.MiniGamePenguins;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJAnimation;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.collision.Collision;

public class MiniGame extends GameState
{
	public Entity player;
	
	
	
	public  Camera camera;
	private OBJModel map;
	private OBJModel ground;
	
	private float shoootAngleIncrement = 1f;
	
	public  List <Entity> entities = 
		Collections.synchronizedList(new ArrayList<Entity>());
	public  List <Entity> removeList = 
		Collections.synchronizedList(new ArrayList<Entity>());
	
	
	OBJModel slushyBall;
	OBJModel mPowerBox;
	
	Entity powerBox;
	
	OBJAnimation penguinThrow;
	
	public MiniGame(GL gl){
		
	}
	
	public void init(GL gl, GLU glu){
		gui.add( new MiniGamePenguins( this ) );
		
		penguinThrow = 
			new OBJAnimation(gl,6,"penguin_throw_",250);
		
		OBJAnimation penguinIdle = 
			new OBJAnimation(gl,10,"penguin",250);
		
		map = new OBJModel( gl, "cage_elephant" );
		ground = new OBJModel( gl, "ground_cc", 500, false);	
		
		slushyBall = new OBJModel(gl,"party_snapper");
		mPowerBox = new OBJModel(gl,"party_snapper");
		
		powerBox = new PowerBox(new Vector3f(-14f,0f,-22f), 0,2f, mPowerBox );
		
		player = new Player(new Vector3f(3.5f,0f,-50f), 0f, .2f, penguinThrow,penguinThrow);
		player.facingDirection = 90f;
		camera = new Camera(player, 10f, 4f);
		camera.distance = 5f;
		
	}
	
	public synchronized void display(GL gl, GLU glu) {

		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		
		/* Setup Camera */
		camera.lookAt(glu, player);
		
		for(Entity e: entities){
			e.render(gl);
		}
		
		powerBox.render(gl);
		
		gl.glPushMatrix();
			gl.glTranslatef( 0, 0, 0 );
			gl.glRotatef(90, 0f, -1f, 0f);
			gl.glScalef( 50, 50, 50 );
			map.draw(gl);
		gl.glPopMatrix();

		

		/* Render Player Model */
		player.render(gl);
		
	}
	
	public synchronized void update(long elapsedTime)
	{
		player.update(elapsedTime);
		/* D/A - Rotate the player on y axis */
		if(Kernel.userInput.keys[KeyEvent.VK_D])
			player.turnRight(elapsedTime);
		else if(Kernel.userInput.keys[KeyEvent.VK_A])
			player.turnLeft(elapsedTime);
		
		for(Entity e: entities){
			e.update(elapsedTime);
		}
		
		for(Entity e: entities){
			boolean collide = Collision.stsXZ(e.position, e.boundingSphereRadius, 
					powerBox.position, powerBox.boundingSphereRadius);
			if(collide)System.out.println("collision");
		}
		
		if(Kernel.userInput.keys[KeyEvent.VK_W])
		{
			player.walkForward(elapsedTime);
		}else 
		if(Kernel.userInput.keys[KeyEvent.VK_S]){
			player.walkBackward(elapsedTime);
		}
		
		Renderer.info[4] = "player x: "+player.position.x;
		Renderer.info[5] = "player z: "+player.position.z;
			
		entities.removeAll(removeList);
		removeList.clear();
	}

	public void handleInput(long elapsedTime) 
	{
		if(Kernel.userInput.keys[KeyEvent.VK_K]){
			//((MoviePlayer)gui.get(3)).playMovie(0);
			this.changeGameState("InGameState");
		}
		
		if(Kernel.userInput.keys[KeyEvent.VK_SPACE]){
			
			throwSlushyBall();
			Kernel.userInput.keys[KeyEvent.VK_SPACE]=false;
			player.objAnimation = penguinThrow;
		}
		/* Check for Escape key to end program */
		if(Kernel.userInput.keys[KeyEvent.VK_ESCAPE]) Kernel.display.stop();
		/* remove entities safely*/
	}
	
	
	
	
	public synchronized void throwSlushyBall(){
		entities.add(new SlushyBall(
			new Vector3f(player.position.x, player.position.y, player.position.z),
			player.facingDirection, 
			.2f, slushyBall , this)
		);
		
		
		
	}

}
