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
	static ArrayList <Leaf> renderLeaves = new ArrayList <Leaf>();
//	private UserInput input;s
	private UserInput input;
	private Texture[] animalIcons = new Texture[10];
	private Texture dialogueBackground;
	private Texture border;
	private Texture leaf;
	private Texture leaves1024,background1024;
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
		
		leaves1024 = Kernel.display.getRenderer().loadImage("data/images/dialogue/leaves1024_cw.png");
		background1024 = Kernel.display.getRenderer().loadImage("data/images/dialogue/dialogue_background1024_cw.png");
		leaf = Kernel.display.getRenderer().loadImage("data/images/dialogue/leaf.png");
		
	}
	
	boolean t = true;
	public void update(long elapsedTime){
		if(t)new ActiveDialogue().start();
		t = false;
	}
	
	public void handleInput(long elapsedTime){
		if(testChangeGui.isActive()){
			((InGameState)gameState).changeGuiState(0);
		}
	}

	public void render(GLGraphics g){
		g.glgBegin();
			
			g.drawImageAlpha(background1024,0,0,1f);
			g.drawImageHue(leaves1024,0,0,new float[]{1f,1f,1f,.94f+new Random().nextFloat()/20});

			for(int i = 0;i<renderDialogue.size();i++){
				DialogueInfo di = renderDialogue.get(i);
				di.update();
				di.draw(g);
			}
			for(int i = 0;i<renderLeaves.size();i++){
				Leaf leaf = renderLeaves.get(i);
				leaf.update();
				leaf.draw(g);
			}
		g.glgEnd();
	}
	
	public static void clearDialogue(){
		renderDialogue.clear();
	}
	/*
	public static void clearFallingDialogue(){
		Iterator iterator = renderDialogue.iterator();
		while(iterator.hasNext()){
			DialogueInfo di = (DialogueInfo)iterator.next();
			if(di.falling){
				renderDialogue.remove(di);
			}
		}
	}
	*/
	private class Leaf{
		int x,y;
		float degree = 0f;
		boolean up = false;
		public Leaf(int x,int y){
			this.x = x;
			this.y = y;
		}
		
		public void update(){
			if(up){
				x++;
				degree += 2f;
			} else{
				x--;
				degree -= 2f;
			}
			if(degree>6f){
				up = false;
			}
			if(degree<-6f){
				up = true;
			}
		}
		
		
		public void draw(GLGraphics g){
			g.drawImageRotateFloat(leaf,x,y,degree);
		}	
	}
	
	private class DialogueInfo{
		int id,animal,type,x,y;
		String words;
		String brokenWords[];
		
		//int xspeed = 0;
		//int yspeed = -20;
		int risespeed= 12;
		int fallspeed = 1;
		boolean rising = true;
		boolean falling = false;
		float alpha = 0f;
	//	int originalx;
		int originaly;
		
		int mouseOverTime = 0;
		boolean mouseOver = false;
		int degree = 0; // positive turns it counterclockwise
		int mathDegrees[] = new int[]{20,10,8,5,2,1};
		int velDegrees[] = new int[]{12,10,8,6,4,2};
		int counter = 0;
		float alphaSwing = 1f;
		boolean left = false;
		boolean swinging;

		public DialogueInfo(int id, int animal, int type, String words, int place){
			if(words.length()<40){
				brokenWords = new String[] {words};
			} else {
				String wordsArray[] = new String[10];
				char c;
				int amountOfLines = 1;
				int lastSpace = 0;
				int lastAmountCopied = 0;
				for(int i = 0;i<words.length();i++){
					c = words.charAt(i);
					if(c ==' ')lastSpace = i;
					if(i<amountOfLines*40){
						
					}  else {
						wordsArray[amountOfLines-1] = words.substring(lastAmountCopied, lastSpace);
						lastAmountCopied = lastSpace+1;
						amountOfLines++;
					}
					if (i==words.length()-1){
						wordsArray[amountOfLines-1] = words.substring(lastAmountCopied, words.length());
						amountOfLines++;
					}
				}
				brokenWords = new String[amountOfLines-1];
				for(int i = 0;i<amountOfLines-1;i++){
					brokenWords[i] = wordsArray[i];
				}
				

			}
			this.id = id;
			this.animal = animal;
			this.type = type;
			this.words = words;
			switch(place){
				case 0:
					x = 200;
					y = 500;
				break;
				case 1:
					x = 270;
					y = 420;
				break;
				case 2:
					x = 270;
					y = 340;
				break;
				case 3:
					x = 270;
					y = 260;
				break;
				case 4:
					x = 270;
					y = 180;
				break;
			}
			
		//	originalx=x;
			originaly=y;
		//	x-=40;
			y-=80;
		}
		
		public void update(){
			if(rising){				
				y+=risespeed;
				if(risespeed>1)risespeed--;
				alpha+=.06f;
				if(y>originaly){;
					rising = false;
					alpha = 1f;
					y = originaly;
				}
			} else if(falling){
				y-=risespeed;
				risespeed++;
				alpha-=.06f;
			} else {
				
				mouseOver = false;
				if(input.mousePos[0]>x &&input.mousePos[0]<x+64&&input.mousePos[1]>y&&input.mousePos[1]<y+64){
					mouseOver = true;
					mouseOverTime++;
					if(!swinging){
						degree = 0;
						left = true;
						swinging = true;	
					} 
						counter = 0;
						alphaSwing = .6f;
				} else{
					mouseOverTime = 0;
				}	
			}
			
		}
		
		public void draw(GLGraphics g){
			switch(type){
				case 0:
					if(swinging){
						if(left){
							degree+=velDegrees[counter];
							if(degree>mathDegrees[counter]){
								counter++;
								left = false;
							}
						}else{
							degree-=velDegrees[counter];
							if(degree<-mathDegrees[counter]){
								counter++;
								left = true;
							}
						}
						if(counter>mathDegrees.length-1){
							swinging = false;
							degree = 0;
						}
					}
					
						
					if(mouseOver){
							g.drawImageRotateHue(animalIcons[animal],x,y,degree, new float[]{alphaSwing,1f,alphaSwing,1f});	

						if(mouseOverTime > 5){
								g.drawImageRotateHue(border,x,y,degree,new float[]{.2f,.2f,1f,1f});
						} else {
								g.drawImageRotateHue(border,x,y,degree+new Random().nextInt(30)-15,new float[]{.2f,.2f,1f,1f});
						}
					
					} else{
						alphaSwing +=.05f;
							
							g.drawImageRotateHue(animalIcons[animal],x,y,degree, new float[]{alphaSwing,1f,alphaSwing,1f});	
				
						
						g.drawImageRotate(border,x,y,degree);
					}
					
			
					for(int i = 0;i<brokenWords.length;i++){
						g.drawBitmapStringStroke(brokenWords[i],x+76,y+50-i*20,1,new float[]{.6f,1f,.6f,1f},new float[]{0f,0f,0f,1f});
					}
				
					
				break;
				case 1:
					if(swinging){
						if(left){
							degree+=velDegrees[counter];
							if(degree>mathDegrees[counter]){
								counter++;
								left = false;
							}
						}else{
							degree-=velDegrees[counter];
							if(degree<-mathDegrees[counter]){
								counter++;
								left = true;
							}
						}
						if(counter>mathDegrees.length-1){
							swinging = false;
							degree = 0;
						}
					}
					
						
					if(mouseOver){
			
							g.drawImageRotateHue(animalIcons[animal],x,y,degree, new float[]{1f,alphaSwing,alphaSwing,alpha});

						if(mouseOverTime > 5){
								g.drawImageRotateHue(border,x,y,degree,new float[]{.2f,.2f,1f,1f});
						} else {
								g.drawImageRotateHue(border,x,y,degree+new Random().nextInt(30)-15,new float[]{.2f,.2f,1f,alpha});
						}
						for(int i = 0;i<brokenWords.length;i++){
							g.drawBitmapStringStroke(brokenWords[i],x+76,y+50-i*20,1,new float[]{1f,1f,1f,1f},new float[]{0f,0f,0f,alpha});
						}
					//	g.drawBitmapStringStroke(words,x+76,y+20,1,new float[]{1f,1f,1f,1f},new float[]{0f,0f,0f,1f});
					} else {
						
						alphaSwing +=.05f;

							g.drawImageRotateHue(animalIcons[animal],x,y,degree,new float[]{1f,alphaSwing,alphaSwing,alpha});
					
						
						g.drawImageRotate(border,x,y,degree);
						for(int i = 0;i<brokenWords.length;i++){
							g.drawBitmapStringStroke(brokenWords[i],x+76,y+50-i*20,1,new float[]{1f,.6f,.6f,1f},new float[]{0f,0f,0f,alpha});
						}
					//	g.drawBitmapStringStroke(words,x+76,y+20,1,new float[]{1f,.6f,.6f,1f},new float[]{0f,0f,0f,1f});
					}
					
				break;
				case 2:
				break;
			}
		}
	}

	private class ActiveDialogue extends Thread{
		int lastMousePress[] = new int[]{0,0};

		public void run(){
					Dialogue.renderLeaves.add(new Leaf(-20,500));
					Dialogue.renderLeaves.add(new Leaf(-20,200));
					Dialogue.renderLeaves.add(new Leaf(-20,800));
					Dialogue.renderLeaves.add(new Leaf(-20,100));
					Dialogue.renderDialogue.add(new DialogueInfo(0,1,0,"Oh hey flamingo... uhgg hello go sublime sublime hello go go go sublime",0));
					Dialogue.renderDialogue.add(new DialogueInfo(1,0,1,"I escaped, and you need to too",1));
					Dialogue.renderDialogue.add(new DialogueInfo(2,0,1,"Hey Giraffe, ill be right back",2));
					Dialogue.renderDialogue.add(new DialogueInfo(3,0,1,"Is something wrong?",3));
					switch(getSelection()){
						case 1:
							pause(2000);
							Dialogue.clearDialogue();
							Dialogue.renderDialogue.add(new DialogueInfo(4,1,0,"Alright, these trees are to low for me to eat off of. It makes my neck hurt, I could use a vacation.",0));
							
							break;
						case 2:
							//JUST THERE FOR SHOW
							pause(500);
							Dialogue.clearDialogue();
							Dialogue.renderDialogue.add(new DialogueInfo(5,1,0,"Its just my neck. What are you doing out of your cage?",0));
							Dialogue.renderDialogue.add(new DialogueInfo(6,0,1,"Oh yea, it doesn't look so good. You need to get yourself out of this cage",1));
							Dialogue.renderDialogue.add(new DialogueInfo(7,0,1,"I can tell you are in pain, but no time for that, you have to escape and save your life",2));
							Dialogue.clearDialogue();
							break;
						case 3:
							pause(500);
							Dialogue.clearDialogue();
							Dialogue.renderDialogue.add(new DialogueInfo(5,1,0,"Its just my neck. What are you doing out of your cage?",0));
							Dialogue.renderDialogue.add(new DialogueInfo(6,0,1,"Oh yea, it doesn't look so good. You need to get yourself out of this cage",1));
							Dialogue.renderDialogue.add(new DialogueInfo(7,0,1,"I can tell you are in pain, but no time for that, you have to escape and save your life",2));
							switch(getSelection()){
								case 6:
									pause(300);
									Dialogue.clearDialogue();
									Dialogue.renderDialogue.add(new DialogueInfo(8,1,0,"You don't look so good yourself. Well until my neck feels better, I'm not leaving.",0));
									
								break;
								case 7:
									pause(300);
									Dialogue.clearDialogue();
									Dialogue.renderDialogue.add(new DialogueInfo(9,1,0,"Yea, of course there is no time for my pain. You are just like the zookeepers.",0));
									
								break;
							}
							break;
					}			
			}

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
			for(int i = 0;i < Dialogue.renderDialogue.size();i++){
				DialogueInfo di = Dialogue.renderDialogue.get(i);
				di.falling = true;
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
				if(di.type==0) continue; // supposed to be not npc
				if(lastMousePress[0]>di.x&&
					lastMousePress[0]<di.x+64&&
					lastMousePress[1]>di.y&&
					lastMousePress[1]<di.y+64){
						return di.id;
					}
			}
			return -1;
		}
	
	}	
}
