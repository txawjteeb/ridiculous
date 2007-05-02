package org.cart.igd.gui;


import org.cart.igd.core.Kernel;
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

import java.awt.event.*;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class InGameGUI extends GUI
{
	private UserInput input;
	
	/* textures */
	private Texture texQuestDone,texQuestNotDone;
	private Texture texQuestLogIco;
	private Texture texBush;
	private Texture texQuestLog;
	private Texture texEmptySlot;
	private Texture texItemIco[] = new Texture[9];
	private Texture texAnimalIco[] = new Texture[10];
	private Texture texAnimalIcoQuestLog[] = new Texture[11];
	private Texture texAnimalQuestLogFree[] = new Texture[10];
	private Texture texQuestLogCage;

	/* button containers */
	private UIWindow hudBottom;// quest log and item buttons
	private UIWindow hudLeft; // bush button with animals
	private UIWindow hudGroup;
	
	/* buttons */
	private UIButton btBush;

	private UIButton btBushAnimals[] = new UIButton[10];
	private UIButton btItems[] = new UIButton[9];
	private UIButton btGroupAnimals[] = new UIButton[4];

	/* game actions */
	private GameAction useItem[] = new GameAction[9];
	private GameAction selectBushAnimal[] = new GameAction[10];

	// other game actions
	private GameAction mouseSelect;
	private GameAction pressQuestLog;

	/* other gui components */
	private UIComponent selectedButton;

	private GUITextList textList = new GUITextList(100, 600, 16, 20);
	
	private InGameState igs;
	
	PickingHandler ph;

	
	
	
	public InGameGUI(GameState igs,GL gl,GLU glu){
		super(igs);
		this.igs = ((InGameState)igs);
		input = Kernel.userInput;
		
		ph = new PickingHandler(gl, glu, this.igs.animals, this);
		
		loadGameActions();
		loadImages();
		loadGUI();
	}
	
	/** load game actions before adding them to UIButtons */
	public void loadGameActions() {
		// GameAction( String details, boolean continuous )
		pressQuestLog = new GameAction("open the quest log", false);
		mouseSelect = new GameAction("mouse press", false);	

		for (int iEvt = 0; iEvt < useItem.length; iEvt++) {
			useItem[iEvt] = new GameAction("use item: " + (iEvt + 1), false);
			input.bindToButton(useItem[iEvt], 31 + iEvt);
		}

		// select animal on press
		for (int iEvt = 0; iEvt < selectBushAnimal.length; iEvt++) {
			selectBushAnimal[iEvt] = new GameAction("select animal: "
					+ (iEvt + 1), false, GameAction.ON_PRESS_ONLY);
			input.bindToButton(selectBushAnimal[iEvt], 11 + iEvt);
		}

		input.bindToButton(pressQuestLog, GUIEvent.BT_QUEST_LOG);
		input.bindToKey(pressQuestLog, KeyEvent.VK_L);
		input.bindToKey(pressQuestLog, KeyEvent.VK_TAB);
		input.bindToMouse(mouseSelect, MouseEvent.BUTTON1);
		// Kernel.userInput.bindToMouse(mouseReleased, MouseEvent.BUTTON1 );
	}// end loadGameActions()

	/** called from in game gui mousePressed block */
	public void pick(){
		ph.mousePress(input.mousePress[0], input.mousePress[1]);
	}
	
	/** renders gui, called by Renderer thread*/
	public void render(GLGraphics g)
	{
		/* PICK MODELS*/
		ph.pickModels();
		
		g.glgBegin();

		if (selectedButton != null) {
			selectedButton.draw(g);
		}
		// g.drawImageHue(texUIButton, 0, 0, new float[] { 1f, 0f, 0f });
		// g.drawBitmapString("Button", 3, 3);
		// g.drawImage(texAnimalButton, 200,200);
		
		/* draw the animal selection, faded buttons for unavailable animals */
		if(((InGameState)gameState).nearBush)
		{
			for(int i = 0;i<((InGameState)gameState).animals.size();i++)
			{
				Animal a = ((InGameState)gameState).animals.get(i);
				
				if( a.getState() == Animal.SAVED_BUSH )
				{
					btBushAnimals[a.id].enabled = true;
					btBushAnimals[a.id].draw(g);
				} else {
					btBushAnimals[a.id].enabled = false;
					btBushAnimals[a.id].draw(g);
				}
			}
		}
		
		hudLeft.draw(g);
		
		hudBottom.updateAndDraw(g);

		hudGroup.setX( (Kernel.display.getScreenWidth() - 200) );
		hudGroup.updateAndDraw(g);
	
		/* draw Items that are picked up from the Items arraylist in IGS */
		for(int i = 0;i<((InGameState)gameState).inventory.items.size();i++){
			Item item = ((InGameState)gameState).inventory.items.get(i);
			item.display2d(g,texItemIco[item.id]);
		}
		
		
		((InGameState)gameState).questlog.display(g,texQuestLogIco,texQuestLog, texQuestDone, texQuestNotDone,texAnimalIcoQuestLog,texAnimalQuestLogFree,texQuestLogCage);
		
		textList.draw(g);
		g.glgEnd();
	}

	/** updates gui, called by Renderer thread*/
	public void update(long elapsedTime) {
		textList.update(elapsedTime);
		if(igs.currentGuiState != 1){
			pick();
		}
	}

	/** handle input, called by the InputHandler thread*/
	public void handleInput(long elapsedTime) {
		if(Kernel.userInput.keys[KeyEvent.VK_TAB]){
			((InGameState)gameState).changeGuiState(2);
		}
		
		boolean animalPickedUp = false;

		/* check for click on the gui elements once */
		if (mouseSelect.isActive()) {
			// check for bottom hud buttons
			for (int i = 0; i < hudBottom.components.size(); i++) {
				if(input.isSquareButtonPressed(hudBottom.components.get(i)))
				{
					hudBottom.components.get(i).activate();// triger GameAction										// with the button
				}
			}
			// check for left bud buttons except for bush
			for (int i = 1; i < hudLeft.components.size(); i++) {
				if (input.isSquareButtonPressed(hudLeft.components.get(i))) 
				{
					animalPickedUp = true;
					selectedButton = new UIButton(
						((UIButton) hudLeft.components.get(i)).getTexture(),
						((UIButton) hudLeft.components.get(i)).getAction(),
						Kernel.userInput.mousePos[0] - 32,
						Kernel.userInput.mousePos[1] - 32, 64, 64);
				}
			}

			/* flamingo select */
			if (input.isSquareButtonPressed(hudGroup.components.get(0)))
			{
				textList.addText("flamingo selected");
			}
			
			/* check for selected animal dropoff*/
			for (int iG = 1; iG < hudGroup.components.size(); iG++) {
				if (input.isSquareButtonPressed(hudGroup.components.get(iG)))
				{
					if (selectedButton != null) {
						(hudGroup.components.get(iG)).
								setTexture( selectedButton.getTexture());
						(hudGroup.components.get(iG))
								.setAction(( selectedButton).getAction());

						textList.addText("animal added to group");
					}

					else if (!animalPickedUp) {
						hudGroup.components.get(iG).activate();
						//hudGroup.components.get(iG)
						textList.addText(hudGroup.components.get(iG).
								getAction().getInfo());
					}
				}
			}
			
			/* make sure the button is not still attached when its not dropped 
			 * off at a proper location in the paw button */
			if (!animalPickedUp) {
				selectedButton = null;
			}
			
			
			/* pick animal */
			if(picked && igs.currentGuiState!=1 ){
				picked = false;
				Animal animal = null;
				boolean match = false;
				for(Animal a: ((InGameState)gameState).animals){
					if(a.id == pickedId)
						animal = a;
					match = true;
					
					System.out.println(" InGameGUI.handleInput()"+pickedId);
					break;
				}
				if(match){
					((Dialogue)((InGameState)gameState).gui.get(1)).createDialogue(animal,(InGameState)gameState);
					((InGameState)gameState).changeGuiState(1);
				}
			}
			
		} // end if(mouseSelect.isActive())

		
		
		/* update the hud buttons so they rotate */
		for (int i = 0; i < hudLeft.components.size(); i++) {
			((UIButton)hudLeft.components.get(i)).update(input, elapsedTime);
		}
		
		// move the selected button
		if (selectedButton != null) {
			selectedButton.setXY(input.mousePos[0] - 32,
				input.mousePos[1] - 32);
		}
	}// end handleInput()

	/** load texture for the gui components */
	public void loadImages()
	{
		
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
		texBush = Kernel.display.getRenderer().loadImage(
			"data/images/buttons/bush_ico_big.png");
			
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

		

		texAnimalIco[0] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/flamingo_vm.png");
		texAnimalIco[1] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/turtle_sa.png");
		texAnimalIco[2] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/woodpecker_sa.png");
		texAnimalIco[3] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/meerkat_sa.png");
		texAnimalIco[4] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/panda_sa.png");
		texAnimalIco[5] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/tiger_sa.png");
		texAnimalIco[6] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/kangaroo_sa.png");
		texAnimalIco[7] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/giraffe_sa.png");
		texAnimalIco[8] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/penguin_sa.png");
		texAnimalIco[9] = Kernel.display.getRenderer().loadImage(
				"data/images/gui/elephant_sp.png");
		
	}

	/** load the gui components after Textures and GameActions are loaded */
	public void loadGUI() {
		/** ** init gui elements *** */
		// invisible pane that holds the elements
		hudBottom = new UIWindow("", 0, 0, false);
		hudLeft = new UIWindow("", 0, 0, false);
		hudGroup = new UIWindow("", 0, 0, false);

		// add buttons
		//btBush = new UIButton(texBush, pressQuestLog, 0, 0, 128, 128);

		
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