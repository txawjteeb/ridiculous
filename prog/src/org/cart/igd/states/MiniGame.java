package org.cart.igd.states;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.*;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.Camera;
import org.cart.igd.Renderer;
import org.cart.igd.core.Kernel;
import org.cart.igd.entity.Entity;
import org.cart.igd.entity.Player;
import org.cart.igd.entity.PowerBox;
import org.cart.igd.entity.SlushyBall;
import org.cart.igd.gui.MiniGamePenguins;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJAnimation;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.collision.Collision;
import org.cart.igd.entity.*;

/**
 * MiniGame.java
 *
 * General Purpose: minigame where player has to shoot the 
 * powerbox to free the elephant
 */
public class MiniGame extends GameState
{
	/** conversion decimal*/
	final float toRad = 0.01745f;
	
	/** entity that participates in the game */
	public Entity player;
	
	/** object helps set up camera for gl */
	public  Camera camera;
	
	/** OBJModel for the cage */
	private OBJModel map;
	
	/** OBJModel of the terrain */
	private OBJModel ground;
	
	/** Energy of throw */
	private float energy = 0f;
	

/*	public  List <Entity> entities = 
		Collections.synchronizedList(new ArrayList<Entity>());
	

	public  List <Entity> removeList = 
		Collections.synchronizedList(new ArrayList<Entity>());

	public  List <Entity> addList = 
		Collections.synchronizedList(new ArrayList<Entity>());
	*/
	
	public  ArrayList <Entity> entities = new ArrayList<Entity>();
	
	public  ArrayList <Entity> removeList = new ArrayList<Entity>();

	public  ArrayList <Entity> addList = new ArrayList<Entity>();
	
	OBJModel slushyBall[] = new OBJModel[2];
	public OBJModel slushyFragment[] = new OBJModel[2];
	public OBJModel slushyCharge[] = new OBJModel[2];
	OBJModel mPowerBox;
	
	Entity powerBox;
	
	OBJAnimation penguinThrow;
	OBJAnimation penguinIdle;

	public int colorId;


	private boolean readyToThrow = true;
	
	public MiniGame(GL gl){
		
	}
	
	public void init(GL gl, GLU glu){
		gui.add( new MiniGamePenguins( this ) );
		
		penguinThrow = 
			new OBJAnimation(gl,7,"penguin_toss_",200,2f,false);
		
		penguinIdle = 
			new OBJAnimation(gl,10,"penguin_idle_",250);
		
		map = new OBJModel( gl, "cage_elephant", 50,false );
		ground = new OBJModel( gl, "ground_cc", 100, false);	

		slushyBall[0] = new OBJModel(gl,"slushy_ball0_cc",.7f,false);
		slushyBall[1] = new OBJModel(gl,"slushy_ball1_cc",.7f,false);
		slushyFragment[0] = new OBJModel(gl,"slushy_ball0_fragment_cc",.2f,false);
		slushyFragment[1] = new OBJModel(gl,"slushy_ball1_fragment_cc",.2f,false);
		slushyCharge[0] = new OBJModel(gl,"slushy_ball0_fragment_cc",.05f,false);
		slushyCharge[1] = new OBJModel(gl,"slushy_ball1_fragment_cc",.05f,false);
		
		mPowerBox = new OBJModel(gl,"party_snapper");
		
		powerBox = new PowerBox(new Vector3f(-14f,0f,-22f), 0,2f, mPowerBox );
		
		player = new Player(new Vector3f(3.5f,0f,-50f), 0f, .2f, penguinIdle, penguinIdle);
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
		
		gl.glPushMatrix();
			gl.glTranslatef( 0, -6f, 0 );
			ground.draw(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			gl.glTranslatef( 0, 0, 0 );
			gl.glRotatef(90, 0f, -2f, 0f);
			map.draw(gl);
		gl.glPopMatrix();
		
		for(Entity e: entities){
			e.render(gl);
		}
		
		powerBox.render(gl);
		
		/* Render Player Model */
		player.render(gl);
		
	}
	
	public synchronized void update(long elapsedTime)
	{
		player.update(elapsedTime);
		if(player.objAnimation.finished && player.objAnimation == penguinThrow){
			player.objAnimation=penguinIdle;
		}
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
		
		if(Kernel.userInput.keys[KeyEvent.VK_SPACE] && readyToThrow ){
			colorId = new Random().nextInt(2);
			player.objAnimation = penguinThrow;
			penguinThrow.finished = false;
			penguinThrow.start();
			readyToThrow = false;
		}
		
		if(player.objAnimation.equals(penguinThrow)&&Kernel.userInput.keys[KeyEvent.VK_SPACE]){
			if(player.objAnimation.modelIndex==3){
				if(new Random().nextInt(5)==0){
					Vector3f sp = getNewPointDeg(player.position,
					player.facingDirection-30,1.5f);	
									
					entities.add(new SlushyBallCharge(
							new Vector3f(sp.x, sp.y, sp.z),
							player.facingDirection, 
							.2f, slushyCharge[colorId] ,this)
						);	
				}
				
					
				player.objAnimation.pause = true;
			}
			if(energy<.5f)energy+=.00015f*(float)elapsedTime;
		} else{
			player.objAnimation.pause = false;
		}
		
		if(!readyToThrow && penguinThrow.finished){
			throwSlushyBall();
			readyToThrow = true;
		}
		
		Renderer.info[4] = "player x: "+player.position.x;
		Renderer.info[5] = "player z: "+player.position.z;
			
		entities.removeAll(removeList);
		removeList.clear();
		entities.addAll(addList);
		addList.clear();
	}

	public void handleInput(long elapsedTime) 
	{
		if(Kernel.userInput.keys[KeyEvent.VK_K]){
			//((MoviePlayer)gui.get(3)).playMovie(0);
			this.changeGameState("InGameState");
		}
		
		
		/* Check for Escape key to end program */
		if(Kernel.userInput.keys[KeyEvent.VK_ESCAPE]) Kernel.display.stop();
		/* remove entities safely*/
	}
	
	public Vector3f getNewPointDeg(Vector3f pos,float deg, float distance){
		float newPosX = pos.x + ( distance*(float)Math.cos(deg * toRad) );
		float newPosZ = pos.z + ( distance*(float)Math.sin(deg * toRad) );
		
		return new Vector3f(newPosX,pos.y,newPosZ);
	}
	
	
	public synchronized void throwSlushyBall(){
		/** projectile release point */
		Vector3f sp = getNewPointDeg(player.position,
				player.facingDirection-30,1.5f);
		
		
		entities.add(new SlushyBall(
			new Vector3f(sp.x, sp.y, sp.z),
			player.facingDirection, 
			.2f, slushyBall[colorId] , this,colorId,energy)
		);
		
		
		energy = 0.002f;
	}

}
