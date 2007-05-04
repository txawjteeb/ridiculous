package org.cart.igd.gui;

import java.util.*;
import java.awt.event.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;
import org.cart.igd.game.*;

public class Dialogue extends GUI {
	
	static ArrayList <DialogueInfo> renderDialogue = new ArrayList <DialogueInfo>();
	static ArrayList <Leaf> renderLeaves = new ArrayList <Leaf>();
	private UserInput input;
	private Texture[] animalIcons = new Texture[10];
	private Texture border;
	private Texture leaf,fog;
	private Texture leaves1024,background;
	private float fogdegree  = 0f;
	public float alphaBackground = 0f;
	public boolean down = false;
	public InGameState igs = null;
	private float rgbaCursor[]={1f,1f,1f,1f};
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
	public void initInput()	{
		input.bindToKey(testChangeGui, KeyEvent.VK_T);
	}
	
	public void createDialogue(Animal animal,InGameState igs){
		down = false;
		this.igs = igs;
		alphaBackground = 0f;
		animal.facingDirection = igs.player.facingDirection + 180f;
		new ActiveDialogue(animal,igs).start();
		igs.camera.distance = 5;
		
	}

	
	
	public void loadImages(){		
		for(int i = 0;i<animalIcons.length;i++){
			animalIcons[i] = Kernel.display.getRenderer().loadImage("data/images/dialogue/" + i + ".png");
		}
		//dialogueBackground = Kernel.display.getRenderer().loadImage("data/images/dialogue/background.png");
		border = Kernel.display.getRenderer().loadImage("data/images/dialogue/border.png");
		
		leaves1024 = Kernel.display.getRenderer().loadImage("data/images/dialogue/leaves1024_cw.png");
		background = Kernel.display.getRenderer().loadImage("data/images/dialogue/background.png");
		leaf = Kernel.display.getRenderer().loadImage("data/images/dialogue/leaf.png");
		fog = Kernel.display.getRenderer().loadImage("data/images/dialogue/fog.png");
		
	}
	
	public void update(long elapsedTime){
		
	}
	
	public void handleInput(long elapsedTime){
		if(testChangeGui.isActive()){
			((InGameState)gameState).changeGuiState(0);
		}
	}

	public void render(GLGraphics g){
		g.glgBegin();
			if(down){
				alphaBackground-=.15f;
			} else {
				alphaBackground+=.07f;
			}
			
			g.drawImageAlpha(background,0,0,alphaBackground);
			
		//	g.drawImageHue(leaves1024,0,0,new float[]{1f,1f,1f,.94f+new Random().nextFloat()/20});

			for(int i = 0;i<renderDialogue.size();i++){
				DialogueInfo di = renderDialogue.get(i);
				di.update();
				di.draw(g);
			}
		//	for(int i = 0;i<renderLeaves.size();i++){
		//		Leaf leaf = renderLeaves.get(i);
		//		leaf.update();
		//		leaf.draw(g);
		//	}
		//	g.drawImageRotate(fog,-300,0,(int)fogdegree);
		//	g.drawImageRotate(fog,0,0,(int)fogdegree);
		//	g.drawImageRotate(fog,300,300,(int)fogdegree);
		//	g.drawImageRotate(fog,-300,-300,(int)fogdegree);
		//	fogdegree+=.2f;
				g.drawImage( GLGraphics.Cursor, 
				input.mousePos[0],input.mousePos[1],
				GLGraphics.Cursor.imageWidth, GLGraphics.Cursor.imageHeight,
				0, rgbaCursor, new float[] {1f,1f} );

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
			lastMousePress[0] = Kernel.userInput.mousePress[0];
			lastMousePress[1] = Kernel.userInput.mousePress[1];
		}
		
		public void add(String s){
			if(place == 0){
				Dialogue.renderDialogue.add(new DialogueInfo(place,animal.animalId,0,s,place));
			} else {
				Dialogue.renderDialogue.add(new DialogueInfo(place,0,1,s,place));
			}
			place++;
		}
		
		public void clear(){
			Dialogue.clearDialogue();
		}
		
		public void run(){


			switch(animal.animalId){
	case 9: // elephant
	
				

				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						add("What do you vish of me, comrade?");
						add("We're all getting out of here, Vladikov. We need your help!");
						switch(selection()){}
						add("Then get me out of here, little birdie, and I vill CRUSH Vhatever is in the vay.");
						add("Why haven't you already broken out?");
						switch(selection()){}
						add("Fence is electrified, Poncho. I can't touch it with the generator running.");
						add("I'm on it");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Elephant is Ready to Escape");
					break;
					case READY_TO_SAVE:
						add("Fence is electrified, Poncho. I can't touch it with the generator running.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_SAVED:
						add("Now, ve charge gates and sqvish puny little ones!");
						add("(Done)");
						switch(selection()){}
						finishQuest("Ready","The Elephant has Escaped");	
					break;
					
				} // end switch animal.state
			
			break; // end case 9 elephant	
 
 
 
 
 		case 8: // woodpecker
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						add("Hey, there!  What's up?  What brings you to our little neck of the woods, Pinky?");
						add("Sorry, wrong cage.");
						add("We need to leave NOW!  Otherwise we're all going to become food!");
						switch(selection()){
							case 1:
							break;
							case 2:
								add("Food?  FOOD!  Food is good!  Delicious little grubs!  FOOD! Why, that was a capital idea Pinky. ");
								add("Um...hey, I know where there are millions of trees chock full of bugs!  Help me out and I'll show you where they are!");
								add("We have to leave, you guys.  Bad things are about to happen.");
								switch(selection()){
									case 1:
										add("FOOD!  FOOD!  We like food!  We LOVE food! Don't we, guys?");
										add("Alright... in no time you guys will have all the food you want");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The WoodPecker is Ready to Escape");
									break;
									case 2:
										add("You're no fun.  You should probably just leave. Besides, even if you're telling the truth, we can just fly away.  Why should we care about what the boring, no-fun Pinky wants?");
										add("I don't mean to be boring, but we need your help!");
										switch(selection()){}
										add("You want us to help you, boring Pinky?  You get us...something that we can play with. ");
										add("Okay, occupy yourselves for the time being");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The WoodPecker Needs Convincing to Escape");
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						add("GAH!  Boring.  That's what you are.  Boring, boring BORING!  Find us fun, or we stay here!");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						add("We're waiting, Pancho.  But we're not gonna keep waiting for long.  Hurry up, or we're going back to chopping down the trees in Sector B.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						add("WHEE!  That's LOT'S of fun.  We can do all sorts of things now.  Alright, let us out.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The WoodPecker is Ready to Escape");
					break;
					case JUST_SAVED:
						add("Alright!  It's so big out here!  We can fly, and...fly more!  Alright, let's go Pancho.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The WoodPecker has Escaped");		
					break;
					
				} // end switch animal.state
			
			break; // end case 8 woodpecker	



 		case 7: // meerkat
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						add("Um...hi...can I help you?");
						add("Sorry, wrong cage.");
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
										add("I don't know.  Sounds like it might be a long trip. I can't go very long without food.  Find me some and I'll go with you.");
										add("Okay, I'll look for some food.  Be ready.");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Meerkat Needs Convincing to Escape");
									break;
									case 2:
										add("Oh...okay...just don't be so loud next time.  Get us out of here and we'll leave with you.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Meerkat is Ready to Escape");
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						add("No way, Pancho.  We're gonna be stuck with a bunch of big hungry animals, and we won't get any food on the trip.  Get us food now, or we won't go.");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						add("Hurry up!  You sure are slow for being so big.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						add("There we go, now we can have a snack before we leave.  Or just use it to beat off the tiger, tee hee.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Meerkat is Ready to Escape");
					break;
					case JUST_SAVED:
						add("AHA!  Now we can get out of here and find more food!  I mean...escape...yeah.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The Meerkat has Escaped");		
					break;
					
				} // end switch animal.state
			
			break; // end case 7 meerkat		
 
 

			case 6: // penguins
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						add("Hey, man.  What's goin' on?  Hey, I heard you let the turtles out.  Is it true they spend a long time staying still?  I could use 'em for target practice.");
						add("Sorry, wrong cage.");
						add("No time for that now!  We need to leave or we'll all be sleeping with the fishes!");
						switch(selection()){
							case 1:
							break;
							case 2:
								add("What are you talking about?  Why am I gonna be sleeping with my food?  Not that I'd mind, I get hungry in the night sometimes.");
								add("I don't mean it like that.  I mean if we don't break out of here we're going to be turned into food!  We've been sold!");
								add("We need to leave here by tonight or else none of us are going to eat again.  The only food we need to worry about is the food we get turned into if we don't go now!");
								switch(selection()){
									case 1:
										add("Oh!  Well, in that case, I'd better go wake up the others.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Penguines are Ready to Escape");
									break;
									case 2:
										add("Not talk about food?  Food is everything!  I'm not going anywhere with a food-hating weirdo like you!");
										add("I'm sorry, I love food.  We really do need to go though, Penguin.");
										switch(selection()){}
										add("Not until you prove you're a food lover!  Go and get me some nicetasty food!");
										add("(Done)");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Penguins Needs Convincing to Escape");
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						add("Where's the food, eh?  Trying to pull a fast one on me, are you?  Leave no, or I will pelt you with snowballs.");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						add("I'm waiting, pinky.  Get me out of here, will ya?");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						add("FISH!  I love fish!  GIVE ME THE FISH!  Alright, I'll help you, just get me out of here and give me the fish.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Penguines are Ready to Escape");
					break;
					case JUST_SAVED:
						add("WOO HOO!  Let's roll!  Time to have fun, ya know?");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The Penguins have Escaped");
					break;
					
				} // end switch animal.state
			
			break; // end case 6 penguins		


			case 5: // tiger
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						add("Well...it's been a long time, Flamingo, hasn't it?");
						add("Sorry, wrong cage.");
						add("gulp... he...hello, Tiger.");
						switch(selection()){
							case 1:
							break;
							case 2:
								add("What brings you to this part of the zoo?");
								add("I...gulp...");
								add("I...thought...you might want to know that the zoo is being sold and we have been too and we're going to be turned into food and I thought you would want us to help you escape and maybe help us back.");
								switch(selection()){
									case 1:
										add("Look, I know about your little plan.  I also know what the zoo owners did.  I'll help you out with the others if you can prove that I won't get bored.  So, amuse me by answering me a riddle.");
										add("Okay...I'll try.");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Tiger Needs Convincing to Escape");
									break;
									case 2:
										add("My, you are an excitable one, aren't you?  Well, I suppose I might be willing to go along with this trip of yours.  After all, at least I'll be amused.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Tiger is Ready to Escape");
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						add("Not up to the challenge, then?  That's alright, I guess I can't expect much from the commoners.");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						add("I'm not used to waiting for those lower on the food chain than myself.  Do try and hurry.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						add("My, I'm impressed.  I thought it would take you much longer.  I guess I can go with you after all.  Now free me, please.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Tiger is Ready to Escape");
					break;
					case JUST_SAVED:
						add("Ah, that's much better.  Now, shall we gather the others and find our way to a land without bars?");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The Tiger has Escaped");			
					break;
					
				} // end switch animal.state
			
			break; // end case 5 tiger		


	 		case 4: // giraffe
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						add("Why are you bothering an old man?");
						add("Sorry, wrong cage.");
						add("Sorry, Giraffe, but I needed to tell you that we need to leave before the zoo is sold!");
						switch(selection()){
							case 1:
							break;
							case 2:
								add("Well why should I go?  I'm just gonna let 'em move me to a new zoo.");
								add("We've all been sold and I need your help to get everyone out of here!");
								add("They're selling us too!  We're going to be made into food!");
								switch(selection()){
									case 1:
										add("I'll do my best, youngin, but I don't know how much my back can take.  If you could find me something to fix my back, I could help you!");
										add("Alright, I'll be back as soon as I can get something.");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Giraffe Needs Convincing to Escape");
									break;
									case 2:
										add("Well...in that case, I guess I can make these tired old legs work for just a little bit longer.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Giraffe is Ready to Escape");
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						add("What's takin' ya so long, sonny?  My back's still hurtin'!");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						add("Yer' slower than I am laddie, hurry up and get me out of here.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						add("Ah, thanks, that's alot better.  Now I feel fit as a fiddle.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Giraffe is Ready to Escape");
					break;
					case JUST_SAVED:
						add("Thanks, sonny.  I think it's time for us to get on out of here now.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved","The Giraffe has Escaped");	
					break;
					
				} // end switch animal.state
			
			break; // end case 4 giraffe			
	 	
	 	
	 	
	 		case 3: // kangaroo
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						add("HEY!  Hey Flamingo!  What's up man?  How are you?  What's going on?  Wanna play tag?");
						add("Sorry, wrong cage.");
						add("Wow...someone had a little too much sugar in their cereal.");
						switch(selection()){
							case 1:
							break;
							case 2:
								add("Sorry, you know me, I'm a little hyper and stuff.  Okay, alot hyper.  Well...alot alot hyper.  Why are you out of your cage?");
								add("That's okay.  Look, Roger, we need to get out of here.  They're going to sell us all!");
								add("We're going to be sent away and be turned into food.  You need to go with me now!");
								switch(selection()){
									case 1:
										add("Wow!  You mean an adventure?  This'll be great!");
										add("Done");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Kangaroo is Ready to Escape");
									break;
									case 2:
										add("AGH!  That's scary.  I'm not going unless you can disguise me.  Find me a disguise and I'll leave.");
										add("Okay, I'll find one for you, but then we need to move fast.");
										switch(selection()){}
										animal.state = WAITING_FOR_ITEM;
										updateQuest("Needs Convincing","The Kangaroo Needs Convincing to Escape");
									break;
								}
							break;	
						}
						
					break;
					case WAITING_FOR_ITEM:
						add("I don't think so, you didn't get me a disguise yet!  I'm not taking any chances.");
						add("(Done)");
						switch(selection()){}
					break;
					case READY_TO_SAVE:
						add("Um...okay...I'm ready to go now, could you hurry up and do something?  This is getting boring.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_GAVE_ITEM:
						add("Ah, that's awesome, now I can get out of here and no one will recognize me.  Okay, find me a way out.");
						add("(Done)");
						switch(selection()){}
						animal.state = READY_TO_SAVE;
						updateQuest("Ready","The Kangaroo is Ready to Escape");
					break;
					case JUST_SAVED:
						add("Thanks! Now we can save everybody else and get the heck out of here.");
						add("(Done)");
						finishQuest("Saved","The Kangaroo has Escaped");
						switch(selection()){}		
					break;
					
				} // end switch animal.state
			
			break; // end case 3 kangaroo
			
			
			case 2: // panda
				switch(animal.state){ // find out what state it is in
					case NOT_TALKED_TO:
						add("What can I help you with, my fine feathered friend?");
						add("Sorry, wrong cage.");
						add("They're gonna sell us all for food!  We need to run!  Run, I tell you!");
						switch(selection()){
							case 1:
							break;
							case 2:
								add("Calm down, young one.  Speak slowly.  What is wrong?");
								add("I'm sorry.  We need to leave because the zoo has been sold and we are going to be sold as food.");
								add("There's no time to talk, we need to go now before it's too late!");
								switch(selection()){
									case 1:
										add("Very well then, young one.  Let us make haste.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Panda is Ready to Escape");
										
									break;
									case 2:
										add("You speak too swiftly, and your thoughts can not keep up.  Try speaking again when you are clear headed.");
										add("I apologize.  I meant no offense.  We need to get out of the zoo before we are sold for food.");
										switch(selection()){
											case 1:
												add("Very well then. I shall go with you.");
												add("(Done)");
												switch(selection()){}
												animal.state = READY_TO_SAVE;
												updateQuest("Ready","The Panda is Ready to Escape");
											break;
										}
									break;
								}
							break;
						}
					break;
					case READY_TO_SAVE:
						add("Hurry, young one.  Now is not the time to dawdle.");
						add("(Done)");
						switch(selection()){}
					break;
					case JUST_SAVED:
						add("Thank you, youngling.  Now we must make haste and get the others.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved", "The Panda has Escaped");
					break;
				} // end switch animal.state
			
			break; // end case 2 panda

			case 1: // turtle
				switch(animal.state) { // find out what state it is in
					case NOT_TALKED_TO://not talked to
						add("What do you want, Flamingo?  We're trying to swim here!");
						add("Sorry, wrong cage.");
						add("We need to get out of here NOW!");
						switch(selection()){
							case 1:
							break;
							case 2:
								add("What are you going on about now, ya durn whimpersnapper?");
								add("They're going to sell us for food tomorrow!  We're all going to be eaten if we don't leave!");
								add("Well, I was going to say that we're all going to be sold tomorrow and I'm here to help you escape, but I think I've changed my mind.");
								switch(selection()){
									case 1:
										pause(1000);
										clear();
										add("Well why didn't ya say so in the first place! Come on, boys, we gotta get outta here!");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Turtles are Ready to Escape");
									break;
									case 2:
										pause(1000);
										clear();
										add("Now, now, don't be so hasty!  Respect yer elders and get us out of here.");
										add("(Done)");
										switch(selection()){}
										animal.state = READY_TO_SAVE;
										updateQuest("Ready","The Turtles are Ready to Escape");
									break;
								}
							break;
								
						}
					break;
					
					case READY_TO_SAVE://in cage waiting
						add("What's takin' ya so gol' durn' long!  Hurry up and get us out of here!");
						add("(Done)");
						switch(selection()){}
						
					break;
					
					case JUST_SAVED:
						add("Thanks, nice to see some of you youngin's still have respect fer the old folks.");
						add("(Done)");
						switch(selection()){}
						finishQuest("Saved", "The Turtles have Escaped");
					break;
				} // end switch animal.state
				
			break; // end case 1: turtle

			}
			Dialogue.clearDialogue();
			igs.camera.distance = 10;
			igs.gui.get(InGameState.GUI_GAME).picked = false;
			((InGameState)gameState).changeGuiState(0);
			
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
		//	lastMousePress[0] = -100;
			//lastMousePress[1] = -100;
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
			pause(1000);
			clear();
			place = 0;
			return id;
		}
		
		public void setFalling(){
			for(int i = 0;i < Dialogue.renderDialogue.size();i++){
				DialogueInfo di = Dialogue.renderDialogue.get(i);
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


/*
 	public void createQuest(int id,String title, String subtitle, String information, boolean done){
 */