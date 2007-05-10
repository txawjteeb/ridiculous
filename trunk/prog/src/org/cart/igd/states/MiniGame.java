package org.cart.igd.states;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.gui.MiniGamePenguins;

public class MiniGame extends GameState{

	public MiniGame(GL gl){
		
	}
	
	public void init(GL gl, GLU glu){
		gui.add( new MiniGamePenguins( this ) );
	}
	
	@Override
	public void display(GL gl, GLU glu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleInput(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}

}
