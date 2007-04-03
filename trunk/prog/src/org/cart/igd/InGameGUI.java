package org.cart.igd;

import org.cart.igd.util.Texture;
import org.cart.igd.ui.UIButton;
import org.cart.igd.ui.UIWindow;
import org.cart.igd.input.*;
import java.awt.event.*;
import org.cart.igd.states.InGameState;
import org.cart.igd.input.GUIEvent;

public class InGameGUI extends GUI
{
	private Texture texBush;
	private Texture texInvButton;
	private Texture texUIButton;
	
	private UIWindow hudBottom;
	
	private UIButton bush;
	private UIButton questLog;
	
	private UIButton inventoryButton[] = new UIButton[5];
	
	private GameAction mouseSelect;
	private GameAction pressQuestLog;
	private GameAction mouseViewRotate;

	private InGameState inGameState;

    public InGameGUI(InGameState igs)
    {
    	inGameState = igs;
    	loadGameActions();
    	loadImages();
    	loadGUI();
    }
    
    public void loadGameActions()
    {
    	pressQuestLog = new GameAction("open the quest log");
    	mouseViewRotate = new GameAction("mouse rotation mode");
    	mouseSelect = new GameAction("mouse press");
    	
    	Kernel.userInput.bindToButton(pressQuestLog, GUIEvent.BT_QUEST_LOG);
    	Kernel.userInput.bindToKey(pressQuestLog, KeyEvent.VK_Q);
    	Kernel.userInput.bindToMouse(mouseViewRotate,MouseEvent.BUTTON3 );
    	Kernel.userInput.bindToMouse(mouseSelect,MouseEvent.BUTTON1 );
    }
    
    public void render(GLGraphics g)
    {    
    	//move this outside to game state are
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
    	//chekck for press
    	for(int i = 0;i<hudBottom.components.size();i++ ){
    		if(mouseSelect.isPerformed() && Kernel.userInput.isSquareButtonPressed(
    			hudBottom.components.get(i).rel_x,
    			hudBottom.components.get(i).rel_y,
    			64,64,
    			Kernel.userInput.mousePos[0],
    			Kernel.userInput.mousePos[1]) )
    		{
    			System.out.println("handle input item window");
    			hudBottom.components.get(i).action();//triger GameAction with the button
    		}
    	}
    	
    	for(int i = 0;i<hudBottom.components.size();i++ ){
    		
    		hudBottom.components.get(i).action();//triger GameAction with the button
    		
    	}
    	
    	// check game action for activity
    	if(pressQuestLog.isPerformed()){
    		 System.out.println("Q pressed quest log");
    	}
    	
    	// mouse rotation
    	if(mouseViewRotate.isActive() ){
    		System.out.println("mouseViewRotate"+
    			(Kernel.userInput.getXDif()) +" "+
    			(Kernel.userInput.getYDif())
    		);
    		inGameState.rotateCamera(Kernel.userInput.getXDif());
    		
    	}
    
    }
    
    
    /** load texture for the gui components */
    public void loadImages()
    {
    	texUIButton = Display.renderer.loadImage("data/images/buttons/questlog_ico.png");
    	texInvButton = Display.renderer.loadImage("data/images/buttons/button_01.png");
    	texBush = Display.renderer.loadImage("data/images/buttons/bush_big.png");
    }
    
    
    /** load the gui components after textures are loaded*/
    public void loadGUI()
    {
    	/**** init gui elements ****/
    	// invisible pane that holds the elements
    	hudBottom = new UIWindow("",0,0,false);
    	
    	// add buttons
    	bush = new UIButton(texBush, pressQuestLog, 0,0,128,128);
    	questLog = new UIButton(texUIButton, pressQuestLog,128,0,64,64);
    	
    	for(int i = 0;i<inventoryButton.length; i++){
    		inventoryButton[i]= new UIButton(texInvButton, pressQuestLog, 192+(64*i),0, 64, 64);
    	}
    	
    	// add gui elements 
    	hudBottom.add(questLog);
    	
    	for(int i = 0;i<inventoryButton.length; i++){
    		hudBottom.add(inventoryButton[i]);
    	}
    }// end load gui
}