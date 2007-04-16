package org.cart.igd.gui;


import org.cart.igd.core.Kernel;
import org.cart.igd.util.Texture;
import org.cart.igd.gl2d.GLGraphics;
import org.cart.igd.gl2d.GUITextList;
import org.cart.igd.gl2d.UIButton;
import org.cart.igd.gl2d.UIComponent;
import org.cart.igd.gl2d.UIWindow;
import org.cart.igd.input.*;
import org.cart.igd.states.InGameState;
import org.cart.igd.states.GameState;

import java.awt.event.*;

public class InGameGUI extends GUI
{
	private UserInput input;
	
	/* textures */
	private Texture texBush;
	private Texture texQuestLog;
	private Texture texButtonFlamingo;
	private Texture texItemIco[] = new Texture[8];
	private Texture texAnimalIco[] = new Texture[9];

	/* button containers */
	private UIWindow hudBottom;// quest log and item buttons
	private UIWindow hudLeft; // bush button with animals
	private UIWindow hudGroup;
	
	/* buttons */
	private UIButton btBush;
	private UIButton btQuestLog;
	private UIButton btBushAnimals[] = new UIButton[9];
	private UIButton btItems[] = new UIButton[8];
	private UIButton btGroupAnimals[] = new UIButton[4];

	/* game actions */
	private GameAction useItem[] = new GameAction[8];
	private GameAction selectBushAnimal[] = new GameAction[9];
	private GameAction addGroupAnimal[] = new GameAction[4];
	private GameAction activateGroupAnimal[] = new GameAction[4];

	// other game actions
	private GameAction mouseSelect;
	private GameAction mouseReleased;
	private GameAction pressQuestLog;

	/* other gui components */
	private UIComponent selectedButton;

	private GUITextList textList = new GUITextList(100, 600, 16, 20);
	
	
	private GameAction testChangeGui = new GameAction("test swap",false);

	public InGameGUI(GameState igs) {
		super(igs);
		input = Kernel.userInput;
		loadGameActions();
		loadImages();
		loadGUI();
	}

	public void render(GLGraphics g) {
		// move this outside to game state input check and render separate
		// process
		handleInput();

		g.glgBegin();

		if (selectedButton != null) {
			selectedButton.draw(g,300, 300, 1f);
		}
		// g.drawImageHue(texUIButton, 0, 0, new float[] { 1f, 0f, 0f });
		// g.drawBitmapString("Button", 3, 3);
		// g.drawImage(texAnimalButton, 200,200);
		hudLeft.updateAndDraw(g);
		hudBottom.updateAndDraw(g);

		hudGroup.setX( (Kernel.display.getScreenWidth() - 200) );
		hudGroup.updateAndDraw(g);

		textList.draw(g);
		g.glgEnd();
	}

	public void update(long elapsedTime) {
		textList.update(elapsedTime);
	}

	public void handleInput() {
		boolean animalPickedUp = false;
		
		if(testChangeGui.isActive()){
			((InGameState)gameState).changeGuiState(1);
		}
		
		if (pressQuestLog.isActive()) {
			textList.addText(pressQuestLog.getInfo());
		}

		if (selectBushAnimal[1].isActive()) {
			textList.addText(activateGroupAnimal[1].getInfo());
		}
		if (selectBushAnimal[2].isActive()) {
			textList.addText(activateGroupAnimal[2].getInfo());
		}
		if (selectBushAnimal[3].isActive()) {
			textList.addText(activateGroupAnimal[3].getInfo());
		}

		// check bottom hud input
		if (mouseSelect.isActive()) {
			// check for bottom hud buttons
			for (int i = 0; i < hudBottom.components.size(); i++) {
				if(input.isSquareButtonPressed(hudBottom.components.get(i)))
				{
					hudBottom.components.get(i).activate();// triger GameAction										// with the button
				}
			}
			// check for left bud buttons
			for (int i = 0; i < hudLeft.components.size(); i++) {
				if (Kernel.userInput.isSquareButtonPressed(hudLeft.components.get(i))) 
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
				addGroupAnimal[0].activate();
				textList.addText("flamingo selected");
			}
			
			/* check for selected animal dropoff*/
			for (int iG = 1; iG < hudGroup.components.size(); iG++) {
				if (input.isSquareButtonPressed(hudGroup.components.get(iG)))
				{
					addGroupAnimal[iG].activate();
					if (selectedButton != null) {
						(hudGroup.components.get(iG)).setTexture( selectedButton.getTexture());
						(hudGroup.components.get(iG))
								.setAction(( selectedButton).getAction());

						textList.addText("animal added to group");
					}

					else if (!animalPickedUp) {
						hudGroup.components.get(iG).activate();
						//hudGroup.components.get(iG)
						textList.addText("animal selected");
					}
				}
			}
			
			/**
			 * make sure the button is not still attached when its not dropped off at
			 * a proper location in the paw button 
			 **/
			if (!animalPickedUp) {
				selectedButton = null;
			}
		} // end if(mouseSelect.isActive())

		// move the selected button
		if (selectedButton != null) {
			selectedButton.setXY(input.mousePos[0] - 32,
				input.mousePos[1] - 32);
		}

		if (addGroupAnimal[1].isActive()) {
			System.out.println("added animal to group slot 1");
		}

		if (mouseReleased.isActive()) {
			System.out.println("release");
		}
		
		
	}// end handleInput()

	/** load game actions before adding them to UIButtons */
	public void loadGameActions() {
		// GameAction( String details, boolean continuous )
		pressQuestLog = new GameAction("open the quest log", false);
		mouseSelect = new GameAction("mouse press", false);
		mouseReleased = new GameAction("mouse release", false,
				GameAction.ON_RELEASE_ONLY);
				
		input.bindToKey(testChangeGui, KeyEvent.VK_G);
		

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

		addGroupAnimal[0] = new GameAction("activate leader", false,
				GameAction.ON_RELEASE_ONLY);
		addGroupAnimal[1] = new GameAction("activate animal: 1", false,
				GameAction.ON_RELEASE_ONLY);
		addGroupAnimal[2] = new GameAction("activate animal: 2", false,
				GameAction.ON_RELEASE_ONLY);
		addGroupAnimal[3] = new GameAction("activate animal: 3", false,
				GameAction.ON_RELEASE_ONLY);

		activateGroupAnimal[0] = new GameAction("activate leader", false,
				GameAction.ON_RELEASE_ONLY);
		activateGroupAnimal[1] = new GameAction("activate animal: 1", false,
				GameAction.ON_RELEASE_ONLY);
		activateGroupAnimal[2] = new GameAction("activate animal: 2", false,
				GameAction.ON_RELEASE_ONLY);
		activateGroupAnimal[3] = new GameAction("activate animal: 3", false,
				GameAction.ON_RELEASE_ONLY);

		// activating animal in the group
		input.bindToButton(activateGroupAnimal[0],
				GUIEvent.BT_GROUP_0);
		input.bindToButton(activateGroupAnimal[1],
				GUIEvent.BT_GROUP_1);
		input.bindToButton(activateGroupAnimal[2],
				GUIEvent.BT_GROUP_2);
		input.bindToButton(activateGroupAnimal[3],
				GUIEvent.BT_GROUP_3);

		input.bindToButton(pressQuestLog, GUIEvent.BT_QUEST_LOG);

		input.bindToButton(pressQuestLog, GUIEvent.BT_QUEST_LOG);
		input.bindToKey(pressQuestLog, KeyEvent.VK_L);
		input.bindToKey(pressQuestLog, KeyEvent.VK_TAB);
		input.bindToMouse(mouseSelect, MouseEvent.BUTTON1);
		// Kernel.userInput.bindToMouse(mouseReleased, MouseEvent.BUTTON1 );
	}// end loadGameActions()

	/** load texture for the gui components */
	public void loadImages()
	{
		texBush = Kernel.display.getRenderer().loadImage(
			"data/images/buttons/bush_ico_big.png");
			
		texQuestLog = Kernel.display.getRenderer().loadImage(
			"data/images/buttons/questlog_ico.png");
		
		texButtonFlamingo = Kernel.display.getRenderer().loadImage(
			"data/images/gui/button_flamingo.png");
		

		for (int iItm = 0; iItm < texItemIco.length; iItm++) {
			texItemIco[iItm] = Kernel.display.getRenderer().loadImage(
				"data/images/buttons/item_ico_0" + (iItm + 1)+ ".png");
		}

		for (int iAnm = 0; iAnm < texAnimalIco.length; iAnm++) {
			texAnimalIco[iAnm] = Kernel.display.getRenderer().loadImage(
				"data/images/buttons/animal_ico_0" + (iAnm + 1)+ ".png");
		}
	}

	/** load the gui components after Textures and GameActions are loaded */
	public void loadGUI() {
		/** ** init gui elements *** */
		// invisible pane that holds the elements
		hudBottom = new UIWindow("", 0, 0, false);
		hudLeft = new UIWindow("", 0, 0, false);
		hudGroup = new UIWindow("", 0, 0, false);

		// add buttons
		btBush = new UIButton(texBush, pressQuestLog, 0, 0, 128, 128);
		btQuestLog = new UIButton(texQuestLog, pressQuestLog, 128, 0, 64, 64);

		for (int iItm = 0; iItm < btItems.length; iItm++) {
			btItems[iItm] = new UIButton(texItemIco[iItm], useItem[iItm],
					192 + (64 * iItm), 0, 64, 64);
		}
		
		for (int iAnm = 0; iAnm < btBushAnimals.length; iAnm++) {
			btBushAnimals[iAnm] = new UIButton(texAnimalIco[iAnm],
					selectBushAnimal[iAnm], 0, 128 + (64 * iAnm), 64, 64);
		}

		btGroupAnimals[0] = new UIButton(texButtonFlamingo, activateGroupAnimal[0],
				0, 0, 128, 128);
		btGroupAnimals[1] = new UIButton(texItemIco[0], activateGroupAnimal[1],
				-32, 120, 64, 64);
		btGroupAnimals[2] = new UIButton(texItemIco[0], activateGroupAnimal[2],
				32, 130, 64, 64);
		btGroupAnimals[3] = new UIButton(texItemIco[0], activateGroupAnimal[3],
				96, 120, 64, 64);

		// add gui elements

		hudBottom.add(btQuestLog);

		for (UIButton b : btItems) {
			hudBottom.add(b);
		}

		hudLeft.add(btBush);

		for (UIButton b : btBushAnimals) {
			hudLeft.add(b);
		}

		for (UIButton b : btGroupAnimals) {
			hudGroup.add(b);
		}
	}// end load gui
}