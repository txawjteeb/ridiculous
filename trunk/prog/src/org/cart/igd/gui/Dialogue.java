package org.cart.igd.gui;

import java.util.*;
import java.awt.event.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;
import org.cart.igd.game.*;
import org.cart.igd.sound.*;

/**
 * 
 */

//TODO: *make sure all sounds are played through SoundManager
public class Dialogue extends GUI
{
	ArrayList <DialogueInfo> renderDialogue = new ArrayList <DialogueInfo>();
	static ArrayList <Leaf> renderLeaves = new ArrayList <Leaf>();
	private UserInput input;
	private Texture[] animalIcons = new Texture[10];
	private Texture border;
	private Texture leaf;
	private Texture background;
	public float alphaBackground = 0f;
	public boolean down = false;
	public InGameState igs = null;
	private float rgbaCursor[]={1f,1f,1f,1f};
	
	private Sound turtleVoiceOvers[] = new Sound[6];
	private Sound elephantVoiceOvers[] = new Sound[4];  
	private Sound woodpeckerVoiceOvers[] = new Sound[9];  
	private Sound meerkatVoiceOvers[] = new Sound[7];  
	private Sound penguinVoiceOvers[] = new Sound[8]; 
	private Sound tigerVoiceOvers[] = new Sound[8]; 
	private Sound giraffeVoiceOvers[] = new Sound[8]; 
	private Sound kangarooVoiceOvers[] = new Sound[8]; 
	private Sound pandaVoiceOvers[] = new Sound[7]; 
	private Sound questLogUpdated;
	private Sound backgroundMusic[] = new Sound[10];	
	
	/** create a sound manager with current sound settings */
	private SoundManager sm = new SoundManager(Kernel.soundSettings);
	/** 
	 * @param GameState refference to container state
	 */
	public Dialogue(GameState gameState){
		super(gameState);//superclass GUI contain ref to GameState
		input = Kernel.userInput;
		initInput();
		loadImages();
	}
	
	public void initInput()	{

	}
	
	public void createDialogue(Animal animal,InGameState igs){
		down = false;
		this.igs = igs;
		alphaBackground = 0f;
		
		backgroundMusic[0]=igs.backgroundMusic;//assign default music
		loadSounds(animal.animalId);
		
		animal.facingDirection = igs.player.facingDirection + 180f;
		new ActiveDialogue(animal,igs).start();
		
		sm.stopMusic(igs.backgroundMusic);
		
		if(animal.state!=6){
			sm.loopMusic(backgroundMusic[animal.animalId]);
		} else{
			sm.playSound(igs.freeAnimalTune);
		}
			
		igs.camera.distance = 4;
	}

	
	
	public void loadImages(){		
		for(int i = 0;i<animalIcons.length;i++){
			animalIcons[i] = Kernel.display.getRenderer().loadImage("data/images/dialogue/" + i + ".png");
		}
		//dialogueBackground = Kernel.display.getRenderer().loadImage("data/images/dialogue/background.png");
		border = Kernel.display.getRenderer().loadImage("data/images/dialogue/border.png");

		background = Kernel.display.getRenderer().loadImage("data/images/dialogue/background.png");
	}
	
	/** load only the sounds that are needed for current dialogue session */
	public void loadSounds(int aId){
		try{
			switch ( aId ){
				case 1:
					for(int i = 0;i< turtleVoiceOvers.length;i++){
						turtleVoiceOvers[i] = new Sound("data/sounds/voices/turtles/turtles-" + (i+1) + ".ogg");
					}
					backgroundMusic[aId] = new Sound("data/sounds/cages/cage-" + (aId) + ".ogg");
				break;
				
				
				case 2:
					for(int i = 0;i< pandaVoiceOvers.length;i++){
						pandaVoiceOvers[i] = new Sound("data/sounds/voices/panda/panda-" + (i+1) + ".ogg");
					}
					backgroundMusic[aId] = new Sound("data/sounds/cages/cage-" + (aId) + ".ogg");
				break;
				
				
				case 8:
					for(int i = 0;i< woodpeckerVoiceOvers.length;i++){
						woodpeckerVoiceOvers[i] = new Sound("data/sounds/voices/woodpecker/woodpecker-" + (i+1) + ".ogg");
					}
					if(backgroundMusic[aId]==null);
					backgroundMusic[aId] = new Sound("data/sounds/cages/cage-" + (aId) + ".ogg");
				break;
				
				
				case 7:
					for(int i = 0;i< meerkatVoiceOvers.length;i++){
						meerkatVoiceOvers[i] = new Sound("data/sounds/voices/meerkat/meerkat-" + (i+1) + ".ogg");
					}
					backgroundMusic[aId] = new Sound("data/sounds/cages/cage-" + (aId) + ".ogg");
				break;
				
				
				case 6:
					for(int i = 0;i< penguinVoiceOvers.length;i++){
						penguinVoiceOvers[i] = new Sound("data/sounds/voices/penguins/penguins-" + (i+1) + ".ogg");
					} 
					backgroundMusic[aId] = new Sound("data/sounds/cages/cage-" + (aId) + ".ogg");
				break;
				
				
				case 5:
					for(int i = 0;i< tigerVoiceOvers.length;i++){
						tigerVoiceOvers[i] = new Sound("data/sounds/voices/tiger/tiger-" + (i+1) + ".ogg");
					} 
					backgroundMusic[aId] = new Sound("data/sounds/cages/cage-" + (aId) + ".ogg");
				break;
				
				
				case 4:
					for(int i = 0;i< giraffeVoiceOvers.length;i++){
						if(giraffeVoiceOvers[i]==null);
						giraffeVoiceOvers[i] = new Sound("data/sounds/voices/giraffe/giraffe-" + (i+1) + ".ogg");
					}
					if(backgroundMusic[aId] == null);
					backgroundMusic[aId] = new Sound("data/sounds/cages/cage-" + (aId) + ".ogg");
				break;
				
				case 3:
					for(int i = 0;i< kangarooVoiceOvers.length;i++){
						kangarooVoiceOvers[i] = new Sound("data/sounds/voices/kangaroo/kangaroo-" + (i+1) + ".ogg");
					}
					backgroundMusic[aId] = new Sound("data/sounds/cages/cage-" + (aId) + ".ogg");
				break;
				
				case 9:
					for(int i = 0;i< elephantVoiceOvers.length;i++){
						elephantVoiceOvers[i] = new Sound("data/sounds/voices/elephant/elephant-" + (i+1) + ".ogg");
					}
					backgroundMusic[aId] = new Sound("data/sounds/cages/cage-" + (aId) + ".ogg");
				break;	
				
				
				
			}
			} catch (Exception e){
				e.printStackTrace();
			}
		
		try{
			questLogUpdated = new Sound("data/sounds/voices/questlog/questlogupdated.ogg");
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void update(long elapsedTime){
		
	}
	
	public void handleInput(long elapsedTime){
		
	}

	public void render(GLGraphics g){
		g.glgBegin();
			if(down){
				alphaBackground-=.15f;
			} else {
				alphaBackground+=.07f;
			}
			
			g.drawImageAlpha(background,0,0,alphaBackground);
			
			for(int i = 0;i<renderDialogue.size();i++){
				DialogueInfo di = renderDialogue.get(i);
				di.update();
				di.draw(g);
			}
				g.drawImage( GLGraphics.Cursor, 
				input.mousePos[0],input.mousePos[1],
				GLGraphics.Cursor.imageWidth, GLGraphics.Cursor.imageHeight,
				0, rgbaCursor, new float[] {1f,1f} );

		g.glgEnd();
	}
	
	public void clearDialogue(){
		renderDialogue.clear();
	}

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
		
		int risespeed= 12;
		int fallspeed = 1;
		boolean rising = true;
		boolean falling = false;
		float alpha = 0f;
		int originalx;
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
			
			originalx=x;
			originaly=y;
		//	x-=40;
		
		/*
			if(type!=0){
				y-=80;	
			} else {
				x -= 80;
			}
		*/	
			
			if(type!=0){
				x+=80;	
			} else {
				x -= 80;
			}
		}
		
		public void update(){
			if(rising){	
				if(type >0){
					/*
					y+=risespeed;
					if(risespeed>1)risespeed--;
					alpha+=.06f;
					if(y>originaly){
						rising = false;
						alpha = 1f;
						y = originaly;
					}
					*/
					x-=risespeed;
					if(risespeed>1)risespeed--;
					alpha+=.06f;
					if(x<originalx){
						rising = false;
						alpha = 1f;
						x = originalx;
					}
				}else{
					x+=risespeed;
					if(risespeed>1)risespeed--;
					alpha+=.06f;
					if(x>originalx){
					rising = false;
					alpha = 1f;
					x = originalx;
				}
			}			
				
			} else if(falling){
				if(type ==0){
					/*
					x+=risespeed;
					risespeed++;
					alpha-=.06f;
					*/
					y+=risespeed;
					risespeed++;
					alpha-=.06f;
				} else {
					y-=risespeed;
					risespeed++;
					alpha-=.06f;	
				}
				
			} else {
				if(type!=0){
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
								g.drawImageRotateHue(border,x,y,degree,new float[]{.2f,.2f,1f,alpha});
						} else {
								g.drawImageRotateHue(border,x,y,degree+new Random().nextInt(30)-15,new float[]{.2f,.2f,1f,alpha});
						}
					
					} else{
						alphaSwing +=.05f;
							
							g.drawImageRotateHue(animalIcons[animal],x,y,degree, new float[]{alphaSwing,1f,alphaSwing,alpha});	
				
						
						g.drawImageRotateHue(border,x,y,degree, new float[]{1f,1f,1f,alpha});
					}
					
			
					for(int i = 0;i<brokenWords.length;i++){
						g.drawBitmapStringStroke(brokenWords[i],x+76,y+50-i*20,1,new float[]{.6f,1f,.6f,alpha},new float[]{0f,0f,0f,alpha});
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
								g.drawImageRotateHue(border,x,y,degree,new float[]{.2f,.2f,1f,alpha});
						} else {
								g.drawImageRotateHue(border,x,y,degree+new Random().nextInt(30)-15,new float[]{.2f,.2f,1f,alpha});
						}
						for(int i = 0;i<brokenWords.length;i++){
							g.drawBitmapStringStroke(brokenWords[i],x+76,y+50-i*20,1,new float[]{1f,1f,1f,alpha},new float[]{0f,0f,0f,alpha});
						}
					//	g.drawBitmapStringStroke(words,x+76,y+20,1,new float[]{1f,1f,1f,1f},new float[]{0f,0f,0f,1f});
					} else {
						
						alphaSwing +=.05f;

							g.drawImageRotateHue(animalIcons[animal],x,y,degree,new float[]{1f,alphaSwing,alphaSwing,alpha});
					
						
						g.drawImageRotateHue(border,x,y,degree, new float[]{1f,1f,1f,alpha});
						for(int i = 0;i<brokenWords.length;i++){
							g.drawBitmapStringStroke(brokenWords[i],x+76,y+50-i*20,1,new float[]{1f,.6f,.6f,alpha},new float[]{0f,0f,0f,alpha});
						}
					//	g.drawBitmapStringStroke(words,x+76,y+20,1,new float[]{1f,.6f,.6f,1f},new float[]{0f,0f,0f,1f});
					}
					
				break;
				case 2:
				break;
			}
		}
	}

	class ActiveDialogue extends Thread{
		int lastMousePress[] = new int[]{0,0};
		Animal animal;
		InGameState igs;
		Dialogue dialogue;
		int place = 0;
		final int NOT_TALKED_TO = 0;
		final int WAITING_FOR_ITEM = 1;
		final int READY_TO_SAVE = 2;
		final int JUST_GAVE_ITEM = 3;
		final int SAVED_IN_BUSH = 4;
		final int SAVED_IN_PARTY = 5;
		final int JUST_SAVED = 6;
	
		public ActiveDialogue(Animal animal, InGameState igs){
			this.animal = animal;
			this.igs = igs;
			this.dialogue = ((Dialogue)igs.gui.get(1));
			lastMousePress[0] = Kernel.userInput.mousePress[0];
			lastMousePress[1] = Kernel.userInput.mousePress[1];
		}
		
		public void add(String s){
			if(place == 0){
				((Dialogue)igs.gui.get(1)).renderDialogue.add(new DialogueInfo(place,animal.animalId,0,s,place));
			} else {
				((Dialogue)igs.gui.get(1)).renderDialogue.add(new DialogueInfo(place,0,1,s,place));
			}
			place++;
		}
		
		public void clear(){
			dialogue.clearDialogue();
		}
		
		public void run(){
		

			switch(animal.animalId){
			case 9: // elephant
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						sm.playVoice(elephantVoiceOvers[0]);
						add("What do you vish of me, comrade?");
						add("We're all getting out of here, Vladikov. We need your help!");
						switch(selection()){}
						sm.playVoice(elephantVoiceOvers[1]);
						add("Then get me out of here, little birdie, and I vill CRUSH Vhatever is in the vay.");
						add("Why haven't you already broken out?");
						switch(selection()){}
						sm.playVoice(elephantVoiceOvers[2]);
						add("Fence is electrified, Poncho. I can't touch it with the generator running.");
						add("I'm on it");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Elephant is Ready to Escape");
						questLogUpdated.playLoud();
					break;
					case READY_TO_SAVE:
						sm.playVoice(elephantVoiceOvers[2]);
						add("Fence is electrified, Poncho. I can't touch it with the generator running.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_SAVED:
						sm.playVoice(elephantVoiceOvers[3]);
						add("Now, ve charge gates and sqvish puny little ones!");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The Elephant has Escaped");
						questLogUpdated.playLoud();
						animal.relocateToBush();
						if(igs.inventory.animals.size()<3){
							igs.inventory.animals.add(animal);
							animal.state = SAVED_IN_PARTY;
						} else {
							animal.state = SAVED_IN_BUSH;
						}	
					break;
					
				} // end switch animal.state
			
			break; // end case 9 elephant	
 
 
 
 
 		case 8: // woodpecker
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						woodpeckerVoiceOvers[0].playLoud();
						add("Hey, there!  What's up?  What brings you to our little neck of the woods, Pinky?");
						add("We need to leave NOW!  Otherwise we're all going to become food!");
						add("Oh I'm in your neck of the Woods? I better get on out of here then.");
						switch(selection()){
							case 2:
							break;
							case 1:
								woodpeckerVoiceOvers[1].playLoud();
								add("Food?  FOOD!  Food is good!  Delicious little grubs!  FOOD! Why, that was a capital idea Pinky. ");
								add("Um...hey, I know where there are millions of trees chock full of bugs!  Help me out and I'll show you where they are!");
								add("We have to leave, you guys.  Bad things are about to happen.");
								switch(selection()){
									case 1:
										woodpeckerVoiceOvers[2].playLoud();
										add("FOOD!  FOOD!  We like food!  We LOVE food! Don't we, guys?");
										add("Alright... in no time you guys will have all the food you want");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The WoodPecker is Ready to Escape");
										questLogUpdated.play();
									break;
									case 2:
										woodpeckerVoiceOvers[3].playLoud();
										add("You're no fun.  You should probably just leave. Besides, even if you're telling the truth, we can just fly away.  Why should we care about what the boring, no-fun Pinky wants?");
										add("I don't mean to be boring, but we need your help!");
										switch(selection()){}
										woodpeckerVoiceOvers[4].playLoud();
										add("You want us to help you, boring Pinky?  You get us...something that we can play with. ");
										add("Okay, occupy yourselves for the time being");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The WoodPecker Needs Convincing to Escape");
										questLogUpdated.play();
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						woodpeckerVoiceOvers[5].playLoud();
						add("GAH!  Boring.  That's what you are.  Boring, boring BORING!  Find us fun, or we stay here!");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						woodpeckerVoiceOvers[7].playLoud();
						add("We're waiting, Pancho.  But we're not gonna keep waiting for long.  Hurry up, or we're going back to chopping down the trees in Sector B.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						woodpeckerVoiceOvers[6].playLoud();
						add("WHEE!  That's LOT'S of fun.  We can do all sorts of things now.  Alright, let us out.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The WoodPecker is Ready to Escape");
						questLogUpdated.playLoud();
					break;
					case JUST_SAVED:
						woodpeckerVoiceOvers[8].playLoud();
						add("Alright!  It's so big out here!  We can fly, and...fly more!  Alright, let's go Pancho.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The WoodPecker has Escaped");
						questLogUpdated.playLoud();
						animal.relocateToBush();
						if(igs.inventory.animals.size()<3){
							igs.inventory.animals.add(animal);
							animal.state = SAVED_IN_PARTY;
						} else {
							animal.state = SAVED_IN_BUSH;
						}	
					break;
					
				} // end switch animal.state
			
			break; // end case 8 woodpecker	



 		case 7: // meerkat
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						meerkatVoiceOvers[0].playLoud();
						add("Um...hi...can I help you?");
						add("Hello Meerkat, I was just passing through");
						add("WE NEED TO LEAVE NOW!");
						switch(selection()){
							case 1:
							break;
							case 2:
								add("...");
								add("Listen, we need to leave, and we need to go fast.  They'll be here soon!");
								add("I didn't mean to frighten you, but we need to leave soon, or we're all going to have a very, very bad day.");
								switch(selection()){
									case 1:
										meerkatVoiceOvers[1].playLoud();
										add("I don't know.  Sounds like it might be a long trip. I can't go very long without food.  Find me some and I'll go with you.");
										add("Okay, I'll look for some food.  Be ready.");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Meerkat Needs Convincing to Escape");
										questLogUpdated.play();
									break;
									case 2:
										meerkatVoiceOvers[2].playLoud();
										add("Oh...okay...just don't be so loud next time.  Get us out of here and we'll leave with you.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Meerkat is Ready to Escape");
										questLogUpdated.playLoud();
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						meerkatVoiceOvers[3].playLoud();
						add("No way, Pancho.  We're gonna be stuck with a bunch of big hungry animals, and we won't get any food on the trip.  Get us food now, or we won't go.");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						meerkatVoiceOvers[5].playLoud();
						add("Hurry up!  You sure are slow for being so big.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						meerkatVoiceOvers[4].playLoud();
						add("There we go, now we can have a snack before we leave.  Or just use it to beat off the tiger, tee hee.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Meerkat is Ready to Escape");
						questLogUpdated.playLoud();
					break;
					case JUST_SAVED:
						meerkatVoiceOvers[6].playLoud();
						add("AHA!  Now we can get out of here and find more food!  I mean...escape...yeah.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The Meerkat has Escaped");	
						questLogUpdated.playLoud();
						animal.relocateToBush();
						if(igs.inventory.animals.size()<3){
							igs.inventory.animals.add(animal);
							animal.state = SAVED_IN_PARTY;
						} else {
							animal.state = SAVED_IN_BUSH;
						}
					break;
					
				} // end switch animal.state
			
			break; // end case 7 meerkat		
 
 

			case 6: // penguins
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						penguinVoiceOvers[0].playLoud();
						add("Hey, man.  What's goin' on?  Hey, I heard you let the turtles out.  Is it true they spend a long time staying still?  I could use 'em for target practice.");
						add("No time for that now!  We need to leave or we'll all be sleeping with the fishes!");
						add("Yeah I did, I might let you out too, but later.");
						
						switch(selection()){
							case 2:
							break;
							case 1:
								penguinVoiceOvers[1].playLoud();
								add("What are you talking about?  Why am I gonna be sleeping with my food?  Not that I'd mind, I get hungry in the night sometimes.");
								add("I don't mean it like that.  I mean if we don't break out of here we're going to be turned into food!  We've been sold!");
								add("We need to leave here by tonight or else none of us are going to eat again.  The only food we need to worry about is the food we get turned into if we don't go now!");
								switch(selection()){
									case 1:
										penguinVoiceOvers[2].playLoud();
										add("Oh!  Well, in that case, I'd better go wake up the others.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Penguines are Ready to Escape");
										questLogUpdated.play();
									break;
									case 2:
										penguinVoiceOvers[3].playLoud();
										add("Not talk about food?  Food is everything!  I'm not going anywhere with a food-hating weirdo like you!");
										add("I'm sorry, I love food.  We really do need to go though, Penguin.");
										switch(selection()){}
										add("Not until you prove you're a food lover!  Go and get me some nicetasty food!");
										add("(Done)");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Penguins Needs Convincing to Escape");
										questLogUpdated.playLoud();
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						penguinVoiceOvers[4].playLoud();
						add("Where's the food, eh?  Trying to pull a fast one on me, are you?  Leave now, or I will pelt you with snowballs.");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						penguinVoiceOvers[6].playLoud();
						add("I'm waiting, pinky.  Get me out of here, will ya?");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						penguinVoiceOvers[5].playLoud();
						add("FISH!  I love fish!  GIVE ME THE FISH!  Alright, I'll help you, just get me out of here and give me the fish.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Penguines are Ready to Escape");
						questLogUpdated.playLoud();
					break;
					case JUST_SAVED:
						penguinVoiceOvers[7].playLoud();
						add("WOO HOO!  Let's roll!  Time to have fun, ya know?");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The Penguins have Escaped");
						questLogUpdated.playLoud();
						animal.relocateToBush();
						if(igs.inventory.animals.size()<3){
							igs.inventory.animals.add(animal);
							animal.state = SAVED_IN_PARTY;
						} else {
							animal.state = SAVED_IN_BUSH;
						}
					break;
					
				} // end switch animal.state
			
			break; // end case 6 penguins		


			case 5: // tiger
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						tigerVoiceOvers[0].playLoud();
						add("Well...it's been a long time, Flamingo, hasn't it?");
						add("Hasn't been long enough.");
						add("gulp... he...hello, Tiger.");
						switch(selection()){
							case 1:
							break;
							case 2:
								tigerVoiceOvers[1].playLoud();
								add("What brings you to this part of the zoo?");
								add("I...gulp...");
								add("I...thought...you might want to know that the zoo is being sold and we have been too and we're going to be turned into food and I thought you would want us to help you escape and maybe help us back.");
								switch(selection()){
									case 1:
										tigerVoiceOvers[2].playLoud();
										add("Look, I know about your little plan.  I also know what the zoo owners did.  I'll help you out with the others if you can prove that I won't get bored.  So, amuse me by answering me a riddle.");
										add("Okay...I'll try.");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Tiger Needs Convincing to Escape");
										questLogUpdated.playLoud();
									break;
									case 2:
										tigerVoiceOvers[3].playLoud();
										add("My, you are an excitable one, aren't you?  Well, I suppose I might be willing to go along with this trip of yours.  After all, at least I'll be amused.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Tiger is Ready to Escape");
										questLogUpdated.playLoud();
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						tigerVoiceOvers[4].playLoud();
						add("Not up to the challenge, then?  That's alright, I guess I can't expect much from the commoners.");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						tigerVoiceOvers[6].playLoud();
						add("I'm not used to waiting for those lower on the food chain than myself.  Do try and hurry.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						tigerVoiceOvers[5].playLoud();
						add("My, I'm impressed.  I thought it would take you much longer.  I guess I can go with you after all.  Now free me, please.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Tiger is Ready to Escape");
						questLogUpdated.playLoud();
					break;
					case JUST_SAVED:
						tigerVoiceOvers[7].playLoud();
						add("Ah, that's much better.  Now, shall we gather the others and find our way to a land without bars?");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The Tiger has Escaped");
						questLogUpdated.playLoud();	
						animal.relocateToBush();
						if(igs.inventory.animals.size()<3){
							igs.inventory.animals.add(animal);
							animal.state = SAVED_IN_PARTY;
						} else {
							animal.state = SAVED_IN_BUSH;
						}		
					break;
					
				} // end switch animal.state
			
			break; // end case 5 tiger		


	 		case 4: // giraffe
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						sm.playVoice(giraffeVoiceOvers[0]);
						add("Why are you bothering an old man?");
						add("Sorry, Giraffe, but I needed to tell you that we need to leave before the zoo is sold!");
						add("I really didn't mean to, in any case, I'll be on my way.");
						
						switch(selection()){
							case 2:
							break;
							case 1:
								sm.playVoice(giraffeVoiceOvers[1]);
								add("Well why should I go?  I'm just gonna let 'em move me to a new zoo.");
								add("We've all been sold and I need your help to get everyone out of here!");
								add("They're selling us too!  We're going to be made into food!");
								switch(selection()){
									case 1:
										sm.playVoice(giraffeVoiceOvers[2]);
										add("I'll do my best, youngin, but I don't know how much my back can take.  If you could find me something to fix my back, I could help you!");
										add("Alright, I'll be back as soon as I can get something.");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Giraffe Needs Convincing to Escape");
										questLogUpdated.playLoud();
									break;
									case 2:
										sm.playVoice(giraffeVoiceOvers[3]);
										add("Well...in that case, I guess I can make these tired old legs work for just a little bit longer.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Giraffe is Ready to Escape");
										questLogUpdated.playLoud();
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						sm.playVoice(giraffeVoiceOvers[4]);
						add("What's takin' ya so long, sonny?  My back's still hurtin'!");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						sm.playVoice(giraffeVoiceOvers[6]);
						add("Yer' slower than I am laddie, hurry up and get me out of here.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						sm.playVoice(giraffeVoiceOvers[5]);
						add("Ah, thanks, that's alot better.  Now I feel fit as a fiddle.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Giraffe is Ready to Escape");
						questLogUpdated.play();
					break;
					case JUST_SAVED:
						sm.playVoice(giraffeVoiceOvers[7]);
						add("Thanks, sonny.  I think it's time for us to get on out of here now.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The Giraffe has Escaped");	
						questLogUpdated.playLoud();
						animal.relocateToBush();
						if(igs.inventory.animals.size()<3){
							igs.inventory.animals.add(animal);
							animal.state = SAVED_IN_PARTY;
						} else {
							animal.state = SAVED_IN_BUSH;
						}
					break;
					
				} // end switch animal.state
			
			break; // end case 4 giraffe			
	 	
	 	
	 	
	 		case 3: // kangaroo
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						kangarooVoiceOvers[0].playLoud();
						add("HEY!  Hey Flamingo!  What's up man?  How are you?  What's going on?  Wanna play tag?");
						add("Oh no, not right now, sorry.");
						add("Wow...someone had a little too much sugar in their cereal.");
						switch(selection()){
							case 1:
							break;
							case 2:
								kangarooVoiceOvers[1].playLoud();
								add("Sorry, you know me, I'm a little hyper and stuff.  Okay, alot hyper.  Well...alot alot hyper.  Why are you out of your cage?");
								add("That's okay.  Look, Roger, we need to get out of here.  They're going to sell us all!");
								add("We're going to be sent away and be turned into food.  You need to go with me now!");
								switch(selection()){
									case 1:
										kangarooVoiceOvers[2].playLoud();
										add("Wow!  You mean an adventure?  This'll be great!");
										add("Done");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Kangaroo is Ready to Escape");
										questLogUpdated.playLoud();
									break;
									case 2:
										kangarooVoiceOvers[3].playLoud();
										add("AGH!  That's scary.  I'm not going unless you can disguise me.  Find me a disguise and I'll leave.");
										add("Okay, I'll find one for you, but then we need to move fast.");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Kangaroo Needs Convincing to Escape");
										questLogUpdated.playLoud();
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						kangarooVoiceOvers[4].playLoud();
						add("I don't think so, you didn't get me a disguise yet!  I'm not taking any chances.");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						kangarooVoiceOvers[6].playLoud();
						add("Um...okay...I'm ready to go now, could you hurry up and do something?  This is getting boring.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						kangarooVoiceOvers[5].playLoud();
						add("Ah, that's awesome, now I can get out of here and no one will recognize me.  Okay, find me a way out.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Kangaroo is Ready to Escape");
						questLogUpdated.playLoud();
					break;
					case JUST_SAVED:
						kangarooVoiceOvers[7].playLoud();
						add("Thanks! Now we can save everybody else and get the heck out of here.");
						add("(Done)");
						finishQuest("Saved","The Kangaroo has Escaped");
						questLogUpdated.playLoud();
						switch(selection()){}
						animal.relocateToBush();
						if(igs.inventory.animals.size()<3){
							igs.inventory.animals.add(animal);
							animal.state = SAVED_IN_PARTY;
						} else {
							animal.state = SAVED_IN_BUSH;
						}		
					break;
					
				} // end switch animal.state
			
			break; // end case 3 kangaroo
			
			
			case 2: // panda
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						pandaVoiceOvers[0].playLoud();
						add("What can I help you with, my fine feathered friend?");
						add("They're gonna sell us all for food!  We need to run!  Run, I tell you!");
						add("Oh nothing, I was just passing by.");
						switch(selection()){
							case 2:
							break;
							case 1:
								pandaVoiceOvers[1].playLoud();
								add("Calm down, young one.  Speak slowly.  What is wrong?");
								add("I'm sorry.  We need to leave because the zoo has been sold and we are going to be sold as food.");
								add("There's no time to talk, we need to go now before it's too late!");
								switch(selection()){
									case 1:
										pandaVoiceOvers[2].playLoud();
										add("Very well then, young one.  Let us make haste.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Panda is Ready to Escape");
										questLogUpdated.playLoud();
										
									break;
									case 2:
										pandaVoiceOvers[3].playLoud();
										add("You speak too swiftly, and your thoughts can not keep up.  Try speaking again when you are clear headed.");
										add("I apologize.  I meant no offense.  We need to get out of the zoo before we are sold for food.");
										switch(selection()){
											case 1:
												pandaVoiceOvers[4].playLoud();
												add("Very well then. I shall go with you.");
												add("(Done)");
												switch(selection()){}
												animal.state = READY_TO_SAVE;
												updateQuest("Ready","The Panda is Ready to Escape");
												questLogUpdated.playLoud();
											break;
										}
									break;
								}
							break;
						}
					break;
					case READY_TO_SAVE:
						pandaVoiceOvers[5].playLoud();
						add("Hurry, young one.  Now is not the time to dawdle.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_SAVED:
						pandaVoiceOvers[6].playLoud();
						add("Thank you, youngling.  Now we must make haste and get the others.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved", "The Panda has Escaped");
						questLogUpdated.playLoud();
						animal.relocateToBush();
						if(igs.inventory.animals.size()<3){
							igs.inventory.animals.add(animal);
							animal.state = SAVED_IN_PARTY;
						} else {
							animal.state = SAVED_IN_BUSH;
						}
					break;
				} // end switch animal.state
			
			break; // end case 2 panda

			case 1: // turtle
				switch(animal.state) { // find out what state it is in
					case NOT_TALKED_TO://not talked to
						sm.playVoice( turtleVoiceOvers[0] );
						add("What do you want, Flamingo?  We're trying to swim here!");
						add("Oh sorry, I'll let you guys get back to swimming.");
						add("We need to get out of here NOW!");

						switch(selection()){
							case 1:
							break;
							case 2:
								sm.playVoice( turtleVoiceOvers[1] );
								add("What are you going on about now, ya durn whimpersnapper?");
								add("They're going to sell us for food tomorrow!  We're all going to be eaten if we don't leave!");
								add("Well, I was going to say that we're all going to be sold tomorrow and I'm here to help you escape, but I think I've changed my mind.");
								switch(selection()){
									case 1:
										sm.playVoice( turtleVoiceOvers[2] );
										add("Well why didn't ya say so in the first place! Come on, boys, we gotta get outta here!");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Turtles are Ready to Escape");
										questLogUpdated.playLoud();
									break;
									case 2:
										sm.playVoice( turtleVoiceOvers[3] );
										add("Now, now, don't be so hasty!  Respect yer elders and get us out of here.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Turtles are Ready to Escape");
										questLogUpdated.playLoud();
									break;
								}
							break;
								
						}
					break;
					
					case READY_TO_SAVE://in cage waiting
						sm.playVoice( turtleVoiceOvers[4] );
						add("What's takin' ya so gol' durn' long!  Hurry up and get us out of here!");
						add("(Done)");
						switch(selection()){}
						
					break;
					
					case JUST_SAVED:
						sm.playVoice( turtleVoiceOvers[5] );
						add("Thanks, nice to see some of you youngin's still have respect fer the old folks.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved", "The Turtles have Escaped");
						questLogUpdated.playLoud();
						animal.relocateToBush();
						if(igs.inventory.animals.size()<3){
							igs.inventory.animals.add(animal);
							animal.state = SAVED_IN_PARTY;
						} else {
							animal.state = SAVED_IN_BUSH;
						}
					
						
							
					break;
				} // end switch animal.state
				
			break; // end case 1: turtle

			}
			clear();
			igs.camera.distance = 10;
			igs.gui.get(InGameState.GUI_GAME).picked = false;
			((InGameState)gameState).changeGuiState(0);
			
			sm.stopMusic( backgroundMusic[animal.animalId] );
				
			sm.loopMusic(igs.backgroundMusic);
			
			((InGameGUI)igs.gui.get(0)).ph.inSelectionMode = false;
		}
		
		
		public void finishQuest(String subtit, String quest){
			 igs.questlog.finishQuest(animal.animalId,animal.name, subtit,quest); 
		}
		
		public void updateQuest(String subtit, String quest){
			 igs.questlog.updateQuest(animal.animalId,animal.name, subtit,quest); 
		}
		
		public void pause(int miliseconds){
			try{
				this.sleep(miliseconds);			
			} catch(Exception e){
				
			}
		}
		
		public int selection(){
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
			for(int i = 0;i < dialogue.renderDialogue.size();i++){
				DialogueInfo di = dialogue.renderDialogue.get(i);
				di.falling = true;
			}
			pause(200);/// normally 1000
			
			place = 0;
			switch(animal.animalId){
				case 1:
					for(int i = 0;i<turtleVoiceOvers.length;i++){
						turtleVoiceOvers[i].stop();
					}
				break;
				case 2:
					for(int i = 0;i<pandaVoiceOvers.length;i++){
						pandaVoiceOvers[i].stop();
					}
				break;
				case 3:
					for(int i = 0;i<kangarooVoiceOvers.length;i++){
						kangarooVoiceOvers[i].stop();
					}
				break;
				case 4:
					for(int i = 0;i<giraffeVoiceOvers.length;i++){
						giraffeVoiceOvers[i].stop();
					}
				break;
				case 5:
					for(int i = 0;i<tigerVoiceOvers.length;i++){
						tigerVoiceOvers[i].stop();
					}
				break;
				case 6:
					for(int i = 0;i<penguinVoiceOvers.length;i++){
						penguinVoiceOvers[i].stop();
					}
				break;
				case 7:
					for(int i = 0;i<meerkatVoiceOvers.length;i++){
						meerkatVoiceOvers[i].stop();
					}
				break;
				case 8:
					for(int i = 0;i<woodpeckerVoiceOvers.length;i++){
						woodpeckerVoiceOvers[i].stop();
					}
				break;
				case 9:
					for(int i = 0;i<elephantVoiceOvers.length;i++){
						elephantVoiceOvers[i].stop();
					}
				break;
			}
			if(dialogue.renderDialogue.size()==3){
				if(id==1)igs.inventory.PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_ONE++;	
				if(id==2)igs.inventory.PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_TWO++;
			}
			clear();
			return id;
		}
		
		public void setFalling(){
			for(int i = 0;i < dialogue.renderDialogue.size();i++){
				DialogueInfo di = dialogue.renderDialogue.get(i);
				di.falling = true;
			}
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
			for(int i = 0;i < dialogue.renderDialogue.size();i++){
				DialogueInfo di = dialogue.renderDialogue.get(i);
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


/*
 	public void createQuest(int id,String title, String subtitle, String information, boolean done){
 */