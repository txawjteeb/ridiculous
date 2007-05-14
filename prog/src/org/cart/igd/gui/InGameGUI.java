package org.cart.igd.gui;


import org.cart.igd.core.Kernel;
import org.cart.igd.entity.Entity;
import org.cart.igd.util.Texture;
import org.cart.igd.game.Animal;
import org.cart.igd.gl2d.GLGraphics;
import org.cart.igd.gl2d.GUITextList;
import org.cart.igd.gl2d.UIButton;
import org.cart.igd.gl2d.UIComponent;
import org.cart.igd.gl2d.UIWindow;
import org.cart.igd.input.*;
import org.cart.igd.states.InGameState;
import org.cart.igd.states.GameState;
import org.cart.igd.game.*;
import java.util.ArrayList;

import java.awt.event.*;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.util.ColorRGBA;

/**
 * InGameGUI.java
 * 
 * General Purpose: 
 * Handles input and rendering of GUI for most of the InGameState
 */
public class InGameGUI extends GUI
{
	private UserInput input;
	
	/* textures */
	private Texture texQuestDone,texQuestNotDone;
	private Texture texQuestLogIco;
	private Texture texQuestLog;
	private Texture texEmptySlot;
	private Texture texItemIco[] = new Texture[9];
	private Texture texAnimalIco[] = new Texture[10];
	private Texture texAnimalIcoQuestLog[] = new Texture[11];
	private Texture texAnimalQuestLogFree[] = new Texture[10];
	private Texture texQuestLogCage;
	private Texture texQuestLogAnimalImages[] = new Texture[12];
	private Texture texAnimalCursors[] = new Texture[10];
	private Texture texPaw[] = new Texture[10];

	/* button containers */
	private UIWindow hudLeft; 
	private UIWindow hudGroup;

	private UIButton btBushAnimals[] = new UIButton[10];
	private UIButton btItems[] = new UIButton[9];
	private UIButton btGroupAnimals[] = new UIButton[4];

	/* game actions */
	private GameAction useItem[] = new GameAction[9];
	private GameAction selectBushAnimal[] = new GameAction[10];

	// other game actions
	private GameAction mouseSelect;
	private GameAction mouseCameraRotate;
	private GameAction pressQuestLog;

	/* other gui components */
	private UIComponent selectedButton;

	private GUITextList textList = new GUITextList(100, 600, 16, 20);
	
	private InGameState igs;
	
	PickingHandler ph;
	
	private float rgbaCursor[]={1f,1f,1f,1f};

	
	
	
	public InGameGUI(GameState igs,GL gl,GLU glu){
		super(igs);
		this.igs = ((InGameState)igs);
		input = Kernel.userInput;
		
		ph = new PickingHandler(gl, glu, this.igs.interactiveEntities, this);
		
		loadGameActions();
		loadImages();
		loadGUI();
	}
	
	/** load game actions before adding them to UIButtons */
	public void loadGameActions() {
		// GameAction( String details, boolean continuous )
		pressQuestLog = new GameAction("open the quest log", false);
		mouseSelect = new GameAction("mouse press", false);	
		
		/* setup input */
		mouseCameraRotate = new GameAction("mouse rotation mode",true);
			

		for (int iEvt = 0; iEvt < useItem.length; iEvt++) {
			useItem[iEvt] = new GameAction("use item: " + (iEvt + 1), false);
			input.bindToButton(useItem[iEvt], 31 + iEvt);
		}

		/* create action buttons with id ref to animal*/
		for (int iEvt = 0; iEvt < selectBushAnimal.length; iEvt++) {
			selectBushAnimal[iEvt] = new GameAction("select animal: " 
					+ iEvt, false, iEvt);
		}

		input.bindToKey(pressQuestLog, KeyEvent.VK_L);
		input.bindToMouse(mouseSelect, MouseEvent.BUTTON1);
		input.bindToMouse(mouseCameraRotate,MouseEvent.BUTTON2 );
		input.bindToMouse(mouseCameraRotate,MouseEvent.BUTTON3 );
		// Kernel.userInput.bindToMouse(mouseReleased, MouseEvent.BUTTON1 );
	}// end loadGameActions()

	/** called from in game gui mousePressed block */
	public void pick(){
		ph.mousePress(input.mousePress[0], input.mousePress[1]);
	}
	
	/** renders gui, called by Renderer thread*/
	public void render(GLGraphics g)
	{
		if(Inventory.canPick)ph.pickModels();
		g.glgBegin();

		if((Kernel.userInput.isRoundButtonPressed(900,50,45,Kernel.userInput.mousePos[0],Kernel.userInput.mousePos[1]))){
			g.drawImageRotateHueSize(texPaw[0], 850,0,0,ColorRGBA.Green.getRGBA(),new float[]{1.1f,1.1f});
		} else{
			g.drawImage(texPaw[0], 855,5);
		}
		if((Kernel.userInput.isRoundButtonPressed(900,50,45,Kernel.userInput.mousePress[0],Kernel.userInput.mousePress[1]))){
			igs.inventory.setCurrentCursor(Inventory.FLAMINGO);
			igs.inventory.PSYCH_UNNECESSARY_CLICKS++;
			Kernel.userInput.mousePress[0] = -100;
			Kernel.userInput.mousePress[1] = -100;
		}
		
		for(int i = 0;i<igs.inventory.animals.size();i++){
			Animal animal = igs.inventory.animals.get(i);
			if(i == 0){
				if((Kernel.userInput.isRoundButtonPressed(830,100,32,Kernel.userInput.mousePos[0],Kernel.userInput.mousePos[1]))){
				g.drawImageRotateHueSize(texPaw[animal.animalId],793,63,0,new float[]{1f,.4f,.4f,1f},new float[]{1.1f,1.1f});
				} else{
					g.drawImage(texPaw[animal.animalId], 798,68);
				}
				if((Kernel.userInput.isRoundButtonPressed(830,100,32,Kernel.userInput.mousePress[0],Kernel.userInput.mousePress[1]))){
					igs.inventory.setCurrentCursor(animal.animalId);
				}
			} else if(i==1){
				if((Kernel.userInput.isRoundButtonPressed(900,140,32,Kernel.userInput.mousePos[0],Kernel.userInput.mousePos[1]))){
				g.drawImageRotateHueSize(texPaw[animal.animalId],863,103,0,new float[]{1f,.4f,.4f,1f},new float[]{1.1f,1.1f});
				} else{
					g.drawImage(texPaw[animal.animalId], 868,108);
				}
				if((Kernel.userInput.isRoundButtonPressed(900,140,32,Kernel.userInput.mousePress[0],Kernel.userInput.mousePress[1]))){
					igs.inventory.setCurrentCursor(animal.animalId);
				}
			} else if(i == 2){
				if((Kernel.userInput.isRoundButtonPressed(970,100,32,Kernel.userInput.mousePos[0],Kernel.userInput.mousePos[1]))){
				g.drawImageRotateHueSize(texPaw[animal.animalId],933,63,0,new float[]{1f,.4f,.4f,1f},new float[]{1.1f,1.1f});
				} else{
					g.drawImage(texPaw[animal.animalId], 938,68);
				}
				if((Kernel.userInput.isRoundButtonPressed(970,100,32,Kernel.userInput.mousePress[0],Kernel.userInput.mousePress[1]))){
					igs.inventory.setCurrentCursor(animal.animalId);
				}
			}
			
		}
		

		/* draw Items that are picked up from the Items arraylist in IGS */
		for(int i = 0;i<((InGameState)gameState).inventory.items.size();i++){
			Item item = ((InGameState)gameState).inventory.items.get(i);
			item.display2d(g,texItemIco[item.itemId]);
		}
		
		

		igs.questlog.display(g,texQuestLogIco,texQuestLog,texQuestLogAnimalImages);
	
		textList.draw(g);
		
		/* drawCursor */
		if(igs.inventory.currentItem==-1){
			g.drawImage(texAnimalCursors[igs.inventory.currentCursor], 
					input.mousePos[0],input.mousePos[1],
					GLGraphics.Cursor.imageWidth, GLGraphics.Cursor.imageHeight,
					0, rgbaCursor, new float[] {1f,1f} );
		}
		
		g.glgEnd();
	}

	/** updates gui, called by Renderer thread*/
	public void update(long elapsedTime)
	{	  
		
	  
	//	System.out.println("PSYCH_FIRST_ANIMAL_TALKED_TO "+igs.inventory.PSYCH_FIRST_ANIMAL_TALKED_TO);
	//	System.out.println("PSYCH_FIRST_ANIMAL_TALKED_TO_LETTER "+igs.inventory.PSYCH_FIRST_ANIMAL_TALKED_TO_LETTER);
	//	System.out.println("PSYCH_FOUND_HIDDEN_POPPERS "+igs.inventory.PSYCH_FOUND_HIDDEN_POPPERS);
	//	System.out.println("PSYCH_FOUND_FAKE_SOLUTION "+igs.inventory.PSYCH_FOUND_FAKE_SOLUTION);
	//	System.out.println("PSYCH_UNNECESSARY_CLICKS "+igs.inventory.PSYCH_UNNECESSARY_CLICKS);
	//	System.out.println("PSYCH_TIME_IN_UNIMPORTANT_PLACES_ON_MAP "+igs.inventory.PSYCH_TIME_IN_UNIMPORTANT_PLACES_ON_MAP);
	//	System.out.println("PSYCH_ENTERED_UNIMPORTANT_PLACES_ON_MAP "+igs.inventory.PSYCH_ENTERED_UNIMPORTANT_PLACES_ON_MAP);
	//	System.out.println("PSYCH_FOOD_WATER_AFFINITY "+igs.inventory.PSYCH_FOOD_WATER_AFFINITY);
	//	System.out.println("PSYCH_AMOUNT_OF_ITEMS_COLLECTED "+igs.inventory.PSYCH_AMOUNT_OF_ITEMS_COLLECTED);
	//	System.out.println("PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_ONE "+igs.inventory.PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_ONE);
	//	System.out.println("PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_TWO "+igs.inventory.PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_TWO);
		
		//System.out.println("PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN "+igs.inventory.PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN);
		//System.out.println("PSYCH_PREFERABLE_QUADRANT_OF_SCREEN "+igs.inventory.PSYCH_PREFERABLE_QUADRANT_OF_SCREEN);
	//	System.out.println("PSYCH_TOTAL_CLICKS "+ igs.inventory.PSYCH_TOTAL_CLICKS[0]+ " " + igs.inventory.PSYCH_TOTAL_CLICKS[1] + " " + igs.inventory.PSYCH_TOTAL_CLICKS[2] + " " +igs.inventory.PSYCH_TOTAL_CLICKS[3]);
		
	//	System.out.println("PSYCH_WASTED_POPPERS "+igs.inventory.PSYCH_WASTED_POPPERS);	
	//	System.out.println("PSYCH_FIRST_DIRECTION "+igs.inventory.PSYCH_FIRST_DIRECTION);		
	//System.out.println("PSYCH_CAUGHT_BEFORE_FREEING_TURTLES "+igs.inventory.PSYCH_CAUGHT_BEFORE_FREEING_TURTLES);			
		

		/* W/S - Move player forward/back. Resets camera offset to back view*/
		if(Kernel.userInput.keys[KeyEvent.VK_W])
		{
			igs.player.walkForward(elapsedTime);
			
			if(!mouseCameraRotate.isActive() && igs.camera.facingOffset!=0f)
			{
				if(igs.player.position.x==igs.player.lastPosition.x && 
						igs.player.position.z==igs.player.lastPosition.z)
					{
						igs.camera.moveToBackView(8f);
					}
			}
		}else if(Kernel.userInput.keys[KeyEvent.VK_S]){
			igs.player.walkBackward(elapsedTime);
		}
			
		/* D/A - Rotate the player on y axis */
		if(Kernel.userInput.keys[KeyEvent.VK_D])
			igs.player.turnRight(elapsedTime);
		else if(Kernel.userInput.keys[KeyEvent.VK_A])
			igs.player.turnLeft(elapsedTime);
		
		/* Q/E Strafe left and right */
		if(Kernel.userInput.keys[KeyEvent.VK_Q])
			igs.player.strafeLeft(elapsedTime);
		else if(Kernel.userInput.keys[KeyEvent.VK_E])
			igs.player.strafeRight(elapsedTime);
		
		for(int i = 0; i<igs.entities.size(); i++){
			Entity entity = igs.entities.get(i);
			entity.update(elapsedTime);
		}
		
		/* update gui related objects */
		textList.update(elapsedTime);
		
		//update button hue
		for(Entity e:igs.interactiveEntities){
			if(e instanceof Animal){
				Animal a = ((Animal)e);
				btBushAnimals[a.animalId].setAvailable(false);
				if(a.state == Animal.SAVED_BUSH){
					btBushAnimals[a.animalId].setAvailable(true);
				}
				if(a.state == Animal.SAVED_PARTY){
					btBushAnimals[a.animalId].setAvailable(true);//already in group
				}
			}
			
		}
		
		if(igs.currentGuiState != 1){
			pick();
		}
		
		boolean animalPickedUp = false;
		if (mouseSelect.isActive()) {

			/* make sure the button is not still attached when its not dropped 
			 * off at a proper location in the paw button */
			if (!animalPickedUp) {
				selectedButton = null;
			}
			
			/* ----------- handle picking selection of entities ------------ */
			Entity e = null;
			if(picked){
				e = igs.interactiveEntities.get(pickedId);
			}
			if(picked && e instanceof Animal){
				Animal a = ((Animal)e);
				System.out.println("animal entity: " + a.animalId);
				if(igs.inventory.currentItem==-1){
					float xDiff = Math.abs(igs.player.position.x - a.position.x);
					float zDiff = Math.abs(igs.player.position.z - a.position.z);
					if(xDiff < a.boundingSphereRadius && zDiff<a.boundingSphereRadius ){
						
						//if(a.talkable()&&igs.inventory.currentCursor==Inventory.FLAMINGO){
						if(a.talkable()){
							if(igs.inventory.PSYCH_FIRST_ANIMAL_TALKED_TO==0){
								igs.inventory.PSYCH_FIRST_ANIMAL_TALKED_TO=a.animalId;
								igs.inventory.PSYCH_FIRST_ANIMAL_TALKED_TO_LETTER=(a.name.substring(0,1));
							}
							((Dialogue)igs.gui.get(1)).createDialogue(a,igs);
							igs.changeGuiState(1);
						} else {
							if(a.state == Inventory.SAVED_IN_BUSH){
								if(igs.inventory.animals.size()<3){
									a.state = Inventory.SAVED_IN_PARTY;
									igs.inventory.animals.add(a);
								}else if(igs.inventory.currentCursor!=0){
									ArrayList<Animal> tempAnimals = new ArrayList<Animal>();
									tempAnimals.add(igs.inventory.animals.get(0));
									tempAnimals.add(igs.inventory.animals.get(1));
									tempAnimals.add(igs.inventory.animals.get(2));
									igs.inventory.animals.clear();
									for(int i = 0;i<tempAnimals.size();i++){
										Animal b = tempAnimals.get(i);
										if(b.animalId==igs.inventory.currentCursor){
											b.state = Inventory.SAVED_IN_BUSH;
											igs.inventory.animals.add(a);
											a.state = Inventory.SAVED_IN_PARTY;
											igs.inventory.currentCursor = a.animalId;
										} else{
											igs.inventory.animals.add(b);
										}
									}	
								}
							}
						}
					}	
				} else {
					float xDiff = Math.abs(igs.player.position.x - a.position.x);
					float zDiff = Math.abs(igs.player.position.z - a.position.z);
					if(xDiff < a.boundingSphereRadius && zDiff<a.boundingSphereRadius){
						if(a.state == Inventory.WAITING_FOR_ITEM&&a.itemWanted == igs.inventory.currentItem){
							a.state = Inventory.JUST_GAVE_ITEM;
							igs.giveItem.play();
							((Dialogue)igs.gui.get(1)).createDialogue(a,igs);
							igs.changeGuiState(1);
							boolean found = false;
							Item removeItem = igs.items.get(0);
							for(int i=0;i<igs.inventory.items.size();i++){
								Item item = igs.inventory.items.get(i);
								if(found)item.x-=70;
								else if(item.itemId==a.itemWanted){
									found = true;
									removeItem = igs.inventory.items.get(i);
							}
						}
						if(found&&removeItem!=null){
							igs.inventory.items.remove(removeItem);
							igs.items.remove(removeItem);	
							igs.inventory.setCurrentItem(-1);
						}
						
						}
					}
					
				}
				
				picked = false;
			}
			if(picked && e instanceof TerrainEntity){
				TerrainEntity t = ((TerrainEntity)e);
				if(igs.inventory.currentItem==-1)t.save();
				System.out.println("terrain entity: " + t.terrainId);
				picked = false;
			}
				
				picked = false;
				pickedId = -1;
		} // end if(mouseSelect.isActive())	
		
	}

	/** handle input, called by the InputHandler thread*/
	public void handleInput(long elapsedTime) {
		if(Kernel.userInput.keys[KeyEvent.VK_TAB]){
			((InGameState)gameState).changeGuiState(2);
		}
		
		boolean animalPickedUp = false;

		/* check for click on the gui elements once */
		
		
		

		
		/** camera mouse rotation */

		
			if(mouseCameraRotate.isActive())
			{
				igs.camera.arcRotateY( - Kernel.userInput.getXDif()*0.5f );
				/** camera angle change */
				igs.camera.changeVerticalAngleDeg( Kernel.userInput.getYDif()*0.5f );
				rgbaCursor[3]=0f;
			} else {
				rgbaCursor[3]=1f;
			}
		
		
	}// end handleInput()

	/** load texture for the gui components */
	public void loadImages()
	{
		for(int i = 0;i<texPaw.length;i++){
			texPaw[i] = Kernel.display.getRenderer().loadImage(
			"data/images/gui/paw" + i + ".png");
		}
		
		for(int i = 0;i<texAnimalCursors.length;i++){
			texAnimalCursors[i] = Kernel.display.getRenderer().loadImage(
			"data/images/gui/cursors/" + i + ".png");
		}

		for(int i = 0;i<texQuestLogAnimalImages.length;i++){
			texQuestLogAnimalImages[i] = Kernel.display.getRenderer().loadImage(
			"data/images/gui/questlog/" + i + ".png");
		}
		
		texQuestLogCage = Kernel.display.getRenderer().loadImage(
			"data/images/gui/cage.png");
			
		for(int i = 0;i<texAnimalQuestLogFree.length;i++){
			texAnimalQuestLogFree[i] = Kernel.display.getRenderer().loadImage(
			"data/images/gui/quest_animal4.png");
		}
		
		for(int i = 0;i<texAnimalIcoQuestLog.length;i++){
			texAnimalIcoQuestLog[i] = Kernel.display.getRenderer().loadImage(
			"data/images/dialogue/old/" + i + ".png");
		}
		

		
		texQuestDone = Kernel.display.getRenderer().loadImage(
			"data/images/gui/quest_done.png");
		texQuestNotDone = Kernel.display.getRenderer().loadImage(
			"data/images/gui/quest_notdone.png");
			
		texQuestLog = Kernel.display.getRenderer().loadImage(
			"data/images/gui/questlog.png");
			
		texQuestLogIco = Kernel.display.getRenderer().loadImage(
			"data/images/gui/questlogicon.png");
		
		texEmptySlot = Kernel.display.getRenderer().loadImage(
			"data/images/buttons/button_slot_empty.png");
		
	
		texItemIco[0] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/paddleball_sp.png");
		texItemIco[1] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/fish_sp.png");
		texItemIco[2] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/hotdog_jh64.png");
		texItemIco[3] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/disguise_glasses64_jh.png");
		texItemIco[4] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/disguise_glasses64_jh.png");
		texItemIco[5] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/medicine_bottle_cw.png");
		texItemIco[6] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/paddleball_sp.png");
		texItemIco[7] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/toothpaste_sp.png");
		texItemIco[8] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/party_snappers64_cw.png");

		

		/*
	 	public static final int FLAMINGO = 0;
		public static final int TURTLES = 1; 
		public static final int PANDA = 2; 
		public static final int KANGAROO = 3; 
		public static final int GIRAFFE = 4; 
		public static final int TIGER = 5; 
		public static final int PENGUIN = 6; 
		public static final int MEERKAT = 7; 
		public static final int WOODPECKER = 8;
		public static final int ELEPHANT = 9;  
	*/
		
		texAnimalIco[0] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/flamingo_vm.png");
		texAnimalIco[1] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/turtle_sa.png");
		texAnimalIco[2] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/panda_sa.png");
		texAnimalIco[3] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/kangaroo_sa.png");
		texAnimalIco[4] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/giraffe_sa.png");
		texAnimalIco[5] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/tiger_sa.png");
		texAnimalIco[6] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/penguin_sa.png");
		texAnimalIco[7] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/meerkat_sa.png");
		texAnimalIco[8] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/woodpecker_sa.png");
		texAnimalIco[9] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/elephant_sp.png");
		
	}

	/** load the gui components after Textures and GameActions are loaded */
	public void loadGUI() {
		/** ** init gui elements *** */
		// invisible pane that holds the elements
		hudLeft = new UIWindow("", 0, 0, false);
		hudGroup = new UIWindow("", 0, 0, false);
		
		for (int iItm = 0; iItm < btItems.length; iItm++) {
			btItems[iItm] = new UIButton(texItemIco[iItm], useItem[iItm],
					256 + (64 * iItm), 0, 64, 64);
		}
		
		/* make animal buttons */
		for (int iAnm = 0; iAnm < btBushAnimals.length; iAnm+=2)
		{
			btBushAnimals[iAnm] = new UIButton(texAnimalIco[iAnm],
					selectBushAnimal[iAnm], 64, 128 + (40 * iAnm), 64, 64);
					
			btBushAnimals[iAnm+1] = new UIButton(texAnimalIco[iAnm+1],
					selectBushAnimal[iAnm+1], 0, 160 + (40 * iAnm), 64, 64);
		}

		btGroupAnimals[0] = new UIButton(texAnimalIco[0], 
			new GameAction("activate flaming",false),0, 0, 128, 128);
		btGroupAnimals[1] = new UIButton(texEmptySlot, 
			new GameAction("empty",false),-32, 120, 64, 64);
		btGroupAnimals[2] = new UIButton(texEmptySlot, 
			new GameAction("empty",false),32, 130, 64, 64);
		btGroupAnimals[3] = new UIButton(texEmptySlot, 
			new GameAction("empty",false),96, 120, 64, 64);

		// add gui elements




		//addd some test items
		//hudBottom.add(btItems[0]);
		//hudBottom.add(btItems[1]);

		//hudLeft.add(btBush);

		/* add all buttons except the first, which is flamingo */
		for(int ibt = 1; ibt< btBushAnimals.length;ibt++){
			hudLeft.add(btBushAnimals[ibt]);
		}
		
		for (UIButton b : btGroupAnimals) {
			hudGroup.add(b);
		}
	}// end load gui
}