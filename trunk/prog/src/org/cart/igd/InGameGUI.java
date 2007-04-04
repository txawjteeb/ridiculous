package org.cart.igd;

import org.cart.igd.util.Texture;
import org.cart.igd.ui.UIButton;
import org.cart.igd.ui.UIWindow;
import org.cart.igd.ui.UIComponent;
import org.cart.igd.input.*;
import org.cart.igd.states.InGameState;
import org.cart.igd.input.GUIEvent;

import java.awt.event.*;

public class InGameGUI extends GUI
{
	private Texture texBush;
	private Texture texInvButton;
	private Texture texUIButton;
	
	private UIWindow hudBottom;
	
	private UIButton bush;
	private UIButton questLog;
	
	private GameAction useItem[] = new GameAction[5];
	private UIButton inventoryButton[] = new UIButton[5];
	
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
    	hudBottom.updateAndDraw();
    	g.drawImage(texInvButton,200,200,64,64);
    	
    	g.glgEnd();
    }
    
    public void handleInput()
    {
    	//check bottom hud input
    	if(mouseSelect.isActive())
    	{
    	
    		for(int i = 0;i<hudBottom.components.size();i++ ){
    			if(Kernel.userInput.isSquareButtonPressed(
    				hudBottom.components.get(i).rel_x,
    				hudBottom.components.get(i).rel_y,
    				64,64,
    				Kernel.userInput.mousePos[0],
    				Kernel.userInput.mousePos[1]) )
    			{
    				hudBottom.components.get(i).action();//triger GameAction with the button
    			}
    		}
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
    	
    	Kernel.userInput.bindToButton(pressQuestLog, GUIEvent.BT_QUEST_LOG);
    	Kernel.userInput.bindToKey(pressQuestLog, KeyEvent.VK_L);
    	Kernel.userInput.bindToKey(pressQuestLog, KeyEvent.VK_TAB);
    	Kernel.userInput.bindToMouse(mouseSelect, MouseEvent.BUTTON1 );
    }// end loadGameActions()
    
    /** load texture for the gui components */
    public void loadImages()
    {
    	texUIButton = Display.renderer.loadImage("data/images/buttons/questlog_ico.png");
    	texInvButton = Display.renderer.loadImage("data/images/buttons/button_01.png");
    	texBush = Display.renderer.loadImage("data/images/buttons/bush_big.png");
    }
    
    
    /** load the gui components after Textures and GameActions are loaded*/
    public void loadGUI()
    {
    	/**** init gui elements ****/
    	// invisible pane that holds the elements
    	hudBottom = new UIWindow("",0,0,false);
    	
    	// add buttons
    	bush = new UIButton(texBush, pressQuestLog, 0,0,128,128);
    	questLog = new UIButton(texUIButton, pressQuestLog,128,0,64,64);
    	
    	for(int iBt = 0;iBt<inventoryButton.length; iBt++){
    		inventoryButton[iBt]= new UIButton(texInvButton, useItem[iBt], 192+(64*iBt),0, 64, 64);
    	}
    	
    	// add gui elements 
    	hudBottom.add(bush);
    	hudBottom.add(questLog);
    	
    	for(UIButton b: inventoryButton){
    		hudBottom.add(b);
    	}
    }// end load gui
}