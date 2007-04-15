package org.cart.igd.gui;

import java.util.*;
import java.awt.event.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;

public class Dialogue extends GUI {
	
	static ArrayList <DialogueInfo> renderDialogue = new ArrayList <DialogueInfo>();
//	private UserInput input;
	private UserInput input;
	private Texture[] animalIcons = new Texture[10];
	private Texture dialogueBackground;
	private Texture border;
	
	/**
	 * param1 informative string
	 * param2 continuous action
	 **/
	private GameAction testChangeGui = new GameAction("test swap",false);
	
	/** 
	 * pass in a refference from game state that contains this gui class 
	 * to allow for game state change and gui state change
	 * @param GameState refference to container
	 **/
	 
	public Dialogue(GameState gameState){
		super(gameState);//superclass GUI contain ref to GameState
		input = Kernel.userInput;
		initInput();
		loadImages();
	}
	public void initInput()
	{
		input.bindToKey(testChangeGui, KeyEvent.VK_T);
	}
	
	public void loadImages(){		
		for(int i = 0;i<animalIcons.length;i++){
			animalIcons[i] = Kernel.display.getRenderer().loadImage("data/images/dialogue/" + i + ".png");
		}
		dialogueBackground = Kernel.display.getRenderer().loadImage("data/images/dialogue/background.png");
		border = Kernel.display.getRenderer().loadImage("data/images/dialogue/border.png");
	}
	
	boolean t = true;
	public void update(long elapsedTime){
		if(t)new ActiveDialogue().start();
		handleInput();
		t = false;
	}
	
	public void handleInput(){
		if(testChangeGui.isActive()){
			((InGameState)gameState).changeGuiState(0);
		}
	}

	public void render(GLGraphics g){
		g.glgBegin();
			g.drawImageAlpha(dialogueBackground,0,400,1f);
			for(int i = 0;i<renderDialogue.size();i++){
				DialogueInfo di = renderDialogue.get(i);
				di.update();
				di.draw(g);
			}
		g.glgEnd();
	}
	
	public static void clearDialogue(){
		renderDialogue.clear();
	}
	
	private class DialogueInfo{
		int id,animal,type,x,y;
		String words;
		boolean mouseover = false;
		
		int degreesLeft=0;
		int degreesRight=0;
		int degreesCurrent=0;
	//	int degreeProgress[] = new int[]{50,30,20,15,12,9,7,5,3,2,1,0};
		int degreeProgress[] = new int[]{50,40,30,20,10,5};
		int progress=0;
		boolean swinging = false;
		boolean left = false;
		
	//	public DialogueInfo(int id, int animal, int type, String words, int x, int y){
	//		this.id = id;
	//		this.animal = animal;
	//		this.type = type;
	//		this.words = words;
	//		this.x = x;
	//		this.y = y;
	//	}
		
		public DialogueInfo(int id, int animal, int type, String words, int place){
			this.id = id;
			this.animal = animal;
			this.type = type;
			this.words = words;
			switch(place){
				case 0:
					x = 200;
					y = 600;
				break;
				case 1:
					x = 270;
					y = 520;
				break;
				case 2:
					x = 270;
					y = 440;
				break;
				case 3:
					x = 270;
					y = 360;
				break;
				case 4:
					x = 270;
					y = 280;
				break;
			}
		}
		
		public void update(){
			mouseover = false;
			if(input.mousePos[0]>x &&input.mousePos[0]<x+64&&input.mousePos[1]>y&&input.mousePos[1]<y+64){
				swinging = true;
				left = true;
				progress = 0;
				degreesCurrent = 0;
				degreesLeft = degreeProgress[progress];
			}
			//mouseover = false;
		//	if(input.mousePos[0]>x &&input.mousePos[0]<x+64&&input.mousePos[1]>y&&input.mousePos[1]<y+64) mouseover = true;
		}
		
		
		public void draw(GLGraphics g){
			switch(type){
				case 0:
				break;
				case 1:
					if(swinging){
						
						if(left){
							degreesCurrent+=20;
							g.drawImageRotate(animalIcons[animal],x,y,degreesCurrent);
							g.drawImageRotate(border,x,y,degreesCurrent);
							if(degreesCurrent>=degreesLeft){
								left = false;
								degreesRight = -degreeProgress[progress];
							}
						} else {
							degreesCurrent-=20;
							g.drawImageRotate(animalIcons[animal],x,y,degreesCurrent);
							g.drawImageRotate(border,x,y,degreesCurrent);
							if(degreesCurrent<=degreesRight){
								left = true;
								progress++;
								
								degreesLeft = degreeProgress[progress];
							}
						}
						
						if(progress==degreeProgress.length-1){
							swinging = false;
						}
					} else{
						g.drawImageAlpha(animalIcons[animal],x,y,1f);
						g.drawImageAlpha(border,x,y,1f);
					}
					g.drawBitmapString(words,x+76,y+20);
					/*if(mouseover){
						g.drawImageRotate(animalIcons[animal],x,y,6);
						g.drawImageRotate(border,x,y,6);
					} else{
						g.drawImageAlpha(animalIcons[animal],x,y,1f);
						g.drawImageAlpha(border,x,y,1f);
					}
					g.drawBitmapString(words,x+76,y+20);	
					*/
				break;
				case 2:
				break;
			}
		}
	}

	private class ActiveDialogue extends Thread{
		int lastMousePress[] = new int[]{0,0};

		public void run(){
			
					Dialogue.renderDialogue.add(new DialogueInfo(0,1,1,"Oh hey flamingo... uhgg",0));
					Dialogue.renderDialogue.add(new DialogueInfo(1,0,1,"I escaped, and you need to too",1));
					Dialogue.renderDialogue.add(new DialogueInfo(2,0,1,"Hey Giraffe, ill be right back",2));
					Dialogue.renderDialogue.add(new DialogueInfo(3,0,1,"Is something wrong?",3));
					switch(getSelection()){
						case 1:
							Dialogue.clearDialogue();
							Dialogue.renderDialogue.add(new DialogueInfo(4,1,1,"Alright, these trees are to low for me to eat off of. It makes my neck hurt, I could use a vacation.",0));
							pause(5000);
							break;
						case 2:
							Dialogue.clearDialogue();
							break;
						case 3:
							Dialogue.clearDialogue();
							Dialogue.renderDialogue.add(new DialogueInfo(5,1,1,"Its just my neck. What are you doing out of your cage?",0));
							Dialogue.renderDialogue.add(new DialogueInfo(6,0,1,"Oh yea, it doesn't look so good. You need to get yourself out of this cage",1));
							Dialogue.renderDialogue.add(new DialogueInfo(7,0,1,"I can tell you are in pain, but no time for that, you have to escape and save your life",2));
							switch(getSelection()){
								case 6:
									Dialogue.clearDialogue();
									Dialogue.renderDialogue.add(new DialogueInfo(8,1,1,"You don't look so good yourself. Well until my neck feels better, I'm not leaving.",0));
									pause(5000);
								break;
								case 7:
									Dialogue.clearDialogue();
									Dialogue.renderDialogue.add(new DialogueInfo(9,1,1,"Yea, of course there is no time for my pain. You are just like the zookeepers.",0));
									pause(5000);
								break;
							}
							break;
					}
					
					
							
			}
	//	public void ActiveDialogue(Dialogue dialogue){
	//		this.dialogue = dialogue;
	//	}
		
		public void pause(int miliseconds){
			try{
				this.sleep(miliseconds);			
			} catch(Exception e){
				
			}
		}
		
		public int getSelection(){
			int id = -1;
			while(true){
				if(newClick()){
					id = checkCollision();
				}
				if(id!=-1) break;
				try{
					this.sleep(5);
				} catch(Exception e){
				}
			}
			return id;
		}
		
		public boolean newClick(){	
			if(Kernel.userInput.mousePress[0]!=lastMousePress[0]||Kernel.userInput.mousePress[1]!=lastMousePress[1]){
				lastMousePress[0]=Kernel.userInput.mousePress[0];
				lastMousePress[1]=Kernel.userInput.mousePress[1];
				return true;
			}
			return false;
		}
			
		public int checkCollision(){	
			for(int i = 0;i < Dialogue.renderDialogue.size();i++){
				DialogueInfo di = Dialogue.renderDialogue.get(i);
				if(di.type==122) continue; // supposed to be not npc
				if(lastMousePress[0]>1&&
					lastMousePress[0]<64&&
					lastMousePress[1]>500-i*100&&
					lastMousePress[1]<500-i*100+64){
						return di.id;
					}
			}
			return -1;
		}
	
	}	
}
