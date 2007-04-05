package org.cart.igd;

import org.cart.igd.util.Texture;
import org.cart.igd.ui.UIButton;
import org.cart.igd.ui.UIWindow;
import org.cart.igd.ui.UIComponent;
import org.cart.igd.input.*;
import org.cart.igd.states.InGameState;
import org.cart.igd.input.GUIEvent;
import org.cart.igd.core.Kernel;

import java.awt.event.*;

public class InGameGUI extends GUI
{
	private Texture texBush;
	private Texture texQuestLog;
	private Texture texItemIco[] = new Texture[8];
	private Texture texAnimalIco[] = new Texture[9];
	
	
	private UIWindow hudBottom;//quest log and item buttons
	private UIWindow hudLeft; //bush button with animals
	
	private UIButton btBush;
	private UIButton btQuestLog;
	
	private GameAction selectGroupAnimal[] = new GameAction[4];
	private UIButton btGroupAnimals[] = new UIButton[4];
	
	private GameAction useItem[] = new GameAction[8];
	private UIButton btItems[] = new UIButton[8];
	
	private GameAction selectBushAnimal[] = new GameAction[9];
	private UIButton btBushAnimals[] = new UIButton[9];
	
	
	
	private GameAction mouseSelect;
	private GameAction pressQuestLog;

	private InGameState inGameState;

    public InGameGUI(InGameState igs)
    {
    	inGameState = igs;
    	loadGameActions();
    	loadImages();
    	loadGUI();
    }
    
    public void render(GLGraphics g)
    {    
    	//move this outside to game state input check and render separate process
    	handleInput();
    		
    	g.glgBegin();
    	
    	//g.drawImageHue(texUIButton, 0, 0, new float[] { 1f, 0f, 0f });
    	//g.drawBitmapString("Button", 3, 3);
    	//g.drawImage(texAnimalButton, 200,200);
    	hudLeft.updateAndDraw();
    	hudBottom.updateAndDraw();
    	
    	g.glgEnd();
    }
    
    public void handleInput()
    {
    	//check bottom hud input
    	if(mouseSelect.isActive())
    	{
    		//check for bottom hud buttons
    		for(int i = 0;i<hudBottom.components.size();i++ ){
    			if(Kernel.userInput.isSquareButtonPressed(
    				hudBottom.components.get(i).rel_x,
    				hudBottom.components.get(i).rel_y,
    				64,64,
    				Kernel.userInput.mousePress[0],
    				Kernel.userInput.mousePress[1]) )
    			{
    				hudBottom.components.get(i).action();//triger GameAction with the button
    			}
    		}
    		//check for left bud buttons
    		for(int i = 0;i<hudLeft.components.size();i++ ){
    			if(Kernel.userInput.isRoundButtonPressed(
    				hudLeft.components.get(i).rel_x,
    				hudLeft.components.get(i).rel_y,
    				32,
    				Kernel.userInput.mousePos[0],
    				Kernel.userInput.mousePos[1]) )
    			{
    				hudLeft.components.get(i).action();//triger GameAction with the button
    			}
    		}
    		//check for release over the group buttons
    		
    		
    		
    	}
    }// end handleInput()
    
    /** load game actions before adding them to UIButtons*/
    public void loadGameActions()
    {
    	//GameAction( String details, boolean continuous )
    	pressQuestLog = new GameAction("open the quest log",false);
    	mouseSelect = new GameAction("mouse press",false);
    	
    	for(int iEvt = 0; iEvt<useItem.length; iEvt++){
    		useItem[iEvt]= new GameAction("use item: "+(iEvt+1) ,false);
    		Kernel.userInput.bindToButton(pressQuestLog, 31+iEvt );
    	}
    	
    	for(int iEvt = 0; iEvt<selectBushAnimal.length; iEvt++){
    		selectBushAnimal[iEvt]= new GameAction("select animal: "+(iEvt+1) ,false);
    		Kernel.userInput.bindToButton(pressQuestLog, 11+iEvt );
    	}
    	
    	Kernel.userInput.bindToButton(pressQuestLog, GUIEvent.BT_QUEST_LOG);
    	Kernel.userInput.bindToKey(pressQuestLog, KeyEvent.VK_L);
    	Kernel.userInput.bindToKey(pressQuestLog, KeyEvent.VK_TAB);
    	Kernel.userInput.bindToMouse(mouseSelect, MouseEvent.BUTTON1 );
    }// end loadGameActions()
    
    /** load texture for the gui components */
    public void loadImages()
    {
		texBush = Display.renderer.loadImage("data/images/buttons/bush_ico_big.png");
    	texQuestLog = Display.renderer.loadImage("data/images/buttons/questlog_ico.png");
    	
    	for(int iItm = 0; iItm<texItemIco.length; iItm++)
    	{
    		texItemIco[iItm] = Display.renderer.loadImage("data/images/buttons/item_ico_0"+(iItm+1)+".png");
    	}
    	
    	for(int iAnm = 0; iAnm<texAnimalIco.length; iAnm++)
    	{
    		texAnimalIco[iAnm] = Display.renderer.loadImage("data/images/buttons/animal_ico_0"+(iAnm+1)+".png");
    	}
    	
    }
    
    
    /** load the gui components after Textures and GameActions are loaded*/
    public void loadGUI()
    {
    	/**** init gui elements ****/
    	// invisible pane that holds the elements
    	hudBottom = new UIWindow("",0,0,false);
    	hudLeft = new UIWindow("",0,0,false);
    	
    	// add buttons
    	btBush = new UIButton(texBush, pressQuestLog, 0,0,128,128);
    	btQuestLog = new UIButton(texQuestLog, pressQuestLog,128,0,64,64);
    	
    	for(int iItm = 0;iItm<btItems.length; iItm++){
    		btItems[iItm]= new UIButton( texItemIco[iItm], useItem[iItm], 192+(64*iItm), 0, 64, 64 );
    	}
    	
    	for(int iAnm = 0;iAnm<btBushAnimals.length; iAnm++){
    		btBushAnimals[iAnm]= new UIButton( texAnimalIco[iAnm], selectBushAnimal[iAnm], 0, 128+(64*iAnm), 64, 64 );
    	}
    	
    	// add gui elements 
    	
    	hudBottom.add(btQuestLog);
    	
    	for(UIButton b: btItems){
    		hudBottom.add(b);
    	}
    	
    	hudLeft.add(btBush);
    	
    	for(UIButton b: btBushAnimals){
    		hudLeft.add(b);
    	}
    }// end load gui
}