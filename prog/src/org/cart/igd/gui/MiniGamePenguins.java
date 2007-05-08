package org.cart.igd.gui;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;
import org.cart.igd.game.*;
import org.cart.igd.sound.*;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class MiniGamePenguins extends GUI{
	
	public InGameState igs = null;
	private UserInput input;
	private GameAction panLeft = new GameAction("",false);
	private GameAction panRight = new GameAction("",false);
	private GameAction throwSnowball = new GameAction("",false);

	long timeToUpdate = 0;
	long updateTime = 100;
	
	boolean rechargingSnowball = true;
	boolean hasSnowball = false;
			
	public MiniGamePenguins(GameState gameState){
		super(gameState);
		input = Kernel.userInput;
		initInput();
		loadImages();
		loadSounds();
	}
	
	public void initInput()	{
		input.bindToKey(panLeft, KeyEvent.VK_LEFT);
		input.bindToKey(panRight, KeyEvent.VK_RIGHT);
		input.bindToKey(throwSnowball, KeyEvent.VK_CONTROL);
	}
	
	public void loadImages(){			
		
	}
	
	public void loadSounds(){
		try{
		} catch(Exception e){	
		}
		
	}
	
	public void update(long elapsedTime){
		timeToUpdate -= elapsedTime;

		if(timeToUpdate<=0){

			timeToUpdate=updateTime;
		}
	}
	
	public void handleInput(long elapsedTime){
		//if(exitFromMovie.isActive()){
			
		//}
	}

	public void render(GL gl){
		igs.getAnimal("Penguin").renderLocation(gl,igs.player.position);
	}
	
	public void render(GLGraphics g){
		
	}
	
}