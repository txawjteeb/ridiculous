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
import org.cart.igd.math.*;

public class MiniGamePenguins extends GUI{
	
	public InGameState igs = null;
	private UserInput input;
	private GameAction panLeft = new GameAction("",false);
	private GameAction panRight = new GameAction("",false);
	private GameAction throwSlushiball = new GameAction("",false);

	long timeToUpdate = 0;
	long updateTime = 100;
	
	long timeToGetSnowball = 2000;
	long snowballTime= 0;
	
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
		input.bindToKey(throwSlushiball, KeyEvent.VK_CONTROL);
	}
	
	public void startGame(){
	//	if(igs.getAnimal("Penguin").state !=Inventory.SAVED_IN_PARTY){
	//		return;
	//	} 
		((InGameState)gameState).changeGuiState(4);
		
	}
	
	public void update(long elapsedTime){
		if(hasSnowball){
			snowballTime = 0;
		} else{
			snowballTime += elapsedTime;
			if(snowballTime>timeToGetSnowball){
				hasSnowball = true;
			}
		}
		timeToUpdate -= elapsedTime;

		if(timeToUpdate<=0){
			update();
			timeToUpdate=updateTime;
		}
	}
	
	public void loadImages(){			
		
	}
	
	
	public void loadSounds(){
		try{
		} catch(Exception e){	
		}
		
	}	
	public void handleInput(long elapsedTime){
		//if(exitFromMovie.isActive()){
		//}
	}

	public void render(GL gl,Animal a){
		a.renderLocation(gl,((InGameState)igs).player.position);
	//	Animal a = igs.getAnimal("Penguin");
	//	if(a!=null){
	//		System.out.println("fine");
	//		a.render(gl);
	//	}
		
	}
	
	public void render(GLGraphics g){
		
	}
	
	public void update(){
		
	}
	
}