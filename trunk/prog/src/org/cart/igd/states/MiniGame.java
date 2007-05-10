package org.cart.igd.states;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.Camera;
import org.cart.igd.core.Kernel;
import org.cart.igd.entity.Entity;
import org.cart.igd.entity.Player;
import org.cart.igd.gui.MiniGamePenguins;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJAnimation;
import org.cart.igd.models.obj.OBJModel;

public class MiniGame extends GameState
{
	public Entity player;
	
	
	
	public  Camera camera;
	private OBJModel map;
	
	private float shoootAngleIncrement = 1f;
	
	public MiniGame(GL gl){
		
	}
	
	public void init(GL gl, GLU glu){
		gui.add( new MiniGamePenguins( this ) );
		
		OBJAnimation penguinIdle = 
			new OBJAnimation(gl,1,"data/models/flamingo",90);
		
		map = new OBJModel( gl, "data/models/cage_elephant" );
		
		player = new Player(new Vector3f(-20f,0f,-20f), 0f, .2f, penguinIdle,penguinIdle);
		camera = new Camera(player, 10f, 4f);
		
	}
	
	@Override
	public void display(GL gl, GLU glu) {

		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		
		/* Setup Camera */
		camera.lookAt(glu, player);
		
		/* render the bush */
		
		gl.glPushMatrix();
			gl.glTranslatef( 0, 0, 0 );
			gl.glRotatef(90, 0f, -1f, 0f);
			gl.glScalef( 50, 50, 50 );
			map.draw(gl);
		gl.glPopMatrix();

		//objAnimation.render(gl);
		

		/* Render Player Model */
		player.render(gl);
		
	}

	public void handleInput(long elapsedTime) 
	{
		if(Kernel.userInput.keys[KeyEvent.VK_K]){
			//((MoviePlayer)gui.get(3)).playMovie(0);
			this.changeGameState("InGameState");
		}
		
		
	}

	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		/* D/A - Rotate the player on y axis */
		if(Kernel.userInput.keys[KeyEvent.VK_D])
			player.turnRight(elapsedTime);
		else if(Kernel.userInput.keys[KeyEvent.VK_A])
			player.turnLeft(elapsedTime);
	}

}
