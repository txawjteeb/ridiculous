package org.cart.igd.gui;

import java.util.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;

public class Dialogue extends GUI {
	
	static ArrayList <DialogueInfo> renderDialogue = new ArrayList <DialogueInfo>();
//	private UserInput input;
	private UserInput input;
	Texture[] animalIcons = new Texture[10];
	Texture dialogueBackground;
	
	/** pass in a refference from game state that contains this gui class 
	 * to allow for game state change and gui state change*/
	public Dialogue(GameState gameState){
		super(gameState);//superclass GUI contain ref to GameState
		input = Kernel.userInput;
		loadImages();
	}
	
	public void loadImages(){		
		for(int i = 0;i<animalIcons.length;i++){
			animalIcons[i] = Kernel.display.getRenderer().loadImage("img/dialogue/" + i + ".png");
		}
		dialogueBackground = Kernel.display.getRenderer().loadImage("img/dialogue/background.png");
	}
	
	boolean t = true;
	public void update(long elapsedTime){
		if(t)new ActiveDialogue().start();
		handleInput();
		t = false;
	}
	
	public void handleInput(){
		
	}

	public void render(GLGraphics g){
		g.glgBegin();
			g.drawImageAlpha(dialogueBackground,0,400,1f);
			for(int i = 0;i<renderDialogue.size();i++){
				DialogueInfo di = renderDialogue.get(i);
				g.drawImageAlpha(animalIcons[di.animal],1,500-i*100,1f);
				g.drawBitmapString(di.words,70,500-i*100);
			}
		g.glgEnd();
	}
	
	public static void clearDialogue(){
		renderDialogue.clear();
	}
	
	private class DialogueInfo{
		int id,animal,type;
		String words;
		public DialogueInfo(int id, int animal, int type, String words){
			this.id = id;
			this.animal = animal;
			this.type = type;
			this.words = words;
		} 
	}
	/*
	private class ActiveDialogue extends Thread{
		int lastMousePress[] = new int[]{0,0};
	}
	*/
//	private class ActiveDialogue {
//		int lastMousePress[] = new int[]{0,0};
//	}

	private class ActiveDialogue extends Thread{
		int lastMousePress[] = new int[]{0,0};
		//Dialogue dialogue;
		/*
		public void run(){
				dialogue.renderDialogue.add(new DialogueInfo(0,0,1,"Oh hey flamingo... uhgg"));
				dialogue.renderDialogue.add(new DialogueInfo(1,1,1,"I escaped, and you need to too"));
				dialogue.renderDialogue.add(new DialogueInfo(2,1,1,"Hey Giraffe, ill be right back"));
				dialogue.renderDialogue.add(new DialogueInfo(3,1,1,"Is something wrong?"));
				switch(getSelection()){
					case 1:
						dialogue.clearDialogue();
						dialogue.renderDialogue.add(new DialogueInfo(4,0,1,"Alright, these trees are to low for me to eat off of. It makes my neck hurt, I could use a vacation."));
						pause(5000);
						break;
					case 2:
						dialogue.clearDialogue();
						break;
					case 3:
						dialogue.clearDialogue();
						dialogue.renderDialogue.add(new DialogueInfo(5,0,1,"Its just my neck. What are you doing out of your cage?"));
						dialogue.renderDialogue.add(new DialogueInfo(6,1,1,"Oh yea, it doesn't look so good. You need to get yourself out of this cage"));
						dialogue.renderDialogue.add(new DialogueInfo(7,1,1,"I can tell you are in pain, but no time for that, you have to escape and save your life"));
						switch(getSelection()){
							case 6:
								dialogue.clearDialogue();
								dialogue.renderDialogue.add(new DialogueInfo(8,0,1,"You don't look so good yourself. Well until my neck feels better, I'm not leaving."));
								pause(5000);
							break;
							case 7:
								dialogue.clearDialogue();
								dialogue.renderDialogue.add(new DialogueInfo(9,0,1,"Yea, of course there is no time for my pain. You are just like the zookeepers."));
								pause(5000);
							break;
						}
						break;
				}
				
						
		}
		*/
	public void run(){
		
				Dialogue.renderDialogue.add(new DialogueInfo(0,0,1,"Oh hey flamingo... uhgg"));
				Dialogue.renderDialogue.add(new DialogueInfo(1,1,1,"I escaped, and you need to too"));
				Dialogue.renderDialogue.add(new DialogueInfo(2,1,1,"Hey Giraffe, ill be right back"));
				Dialogue.renderDialogue.add(new DialogueInfo(3,1,1,"Is something wrong?"));
				switch(getSelection()){
					case 1:
						Dialogue.clearDialogue();
						Dialogue.renderDialogue.add(new DialogueInfo(4,0,1,"Alright, these trees are to low for me to eat off of. It makes my neck hurt, I could use a vacation."));
						pause(5000);
						break;
					case 2:
						Dialogue.clearDialogue();
						break;
					case 3:
						Dialogue.clearDialogue();
						Dialogue.renderDialogue.add(new DialogueInfo(5,0,1,"Its just my neck. What are you doing out of your cage?"));
						Dialogue.renderDialogue.add(new DialogueInfo(6,1,1,"Oh yea, it doesn't look so good. You need to get yourself out of this cage"));
						Dialogue.renderDialogue.add(new DialogueInfo(7,1,1,"I can tell you are in pain, but no time for that, you have to escape and save your life"));
						switch(getSelection()){
							case 6:
								Dialogue.clearDialogue();
								Dialogue.renderDialogue.add(new DialogueInfo(8,0,1,"You don't look so good yourself. Well until my neck feels better, I'm not leaving."));
								pause(5000);
							break;
							case 7:
								Dialogue.clearDialogue();
								Dialogue.renderDialogue.add(new DialogueInfo(9,0,1,"Yea, of course there is no time for my pain. You are just like the zookeepers."));
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
