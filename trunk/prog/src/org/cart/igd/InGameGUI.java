package org.cart.igd;

import org.cart.igd.util.Texture;
import org.cart.igd.ui.UIButton;
import org.cart.igd.ui.UIWindow;
import org.cart.igd.input.*;
import java.awt.event.*;

public class InGameGUI extends GUI
{
	private Texture texUIButton;
	private Texture texInvButton;
	private UIButton questLog;
	
	private UIWindow logAndItemsWindow;
	
	private GameAction pressQuestLog;
	private GameAction mouseViewRotate;
	
	private GameAction mouseSelect;
	
	private UIButton inventoryButton[] = new UIButton[5];
	
	

    public InGameGUI()
    {
    	
    	loadGameActions();
    	
    	try
    	{
    		loadImages();
    		loadGUI();
    	}
    	catch(java.io.IOException e)
    	{
    		e.printStackTrace();
    	}
    	
    	
    	
    }
    
    public void loadGameActions(){
    	pressQuestLog = new GameAction("open the quest log",UserInput.BUTTON_QUEST_LOG);
    	mouseViewRotate = new GameAction("mouse rotation mode", 0);
    	mouseSelect = new GameAction("mouse press",0 );
    	
    	Driver.userInput.bindToButton(pressQuestLog);
    	Driver.userInput.bindToKey(pressQuestLog, KeyEvent.VK_Q);
    	Driver.userInput.bindToMouse(mouseViewRotate,MouseEvent.BUTTON3 );
    	Driver.userInput.bindToMouse(mouseSelect,MouseEvent.BUTTON1 );
    }
    
    public void render(GLGraphics g)
    {    	
    	g.glgBegin();
    	//g.drawImageHue(texUIButton, 0, 0, new float[] { 1f, 0f, 0f });
    	//g.drawBitmapString("Button", 3, 3);
    	//g.drawImage(texAnimalButton, 200,200);
    	//questLog.draw(64,64,1f);
    	logAndItemsWindow.updateAndDraw();
    	g.glgEnd();
    	
    	//move this outside to game state are
    	handleInput();
    }
    
    public void handleInput()
    {
    	//chekck for press
    	for(int i = 0;i<logAndItemsWindow.components.size();i++ ){
    		if(mouseSelect.isPerformed() && UserInput.isSquareButtonPressed(
    			logAndItemsWindow.components.get(i).rel_x,
    			logAndItemsWindow.components.get(i).rel_y,32,
    			Driver.userInput.mousePos[0],
    			Driver.userInput.mousePos[1]) )
    		{
    			System.out.println("handle input item window");
    			logAndItemsWindow.components.get(i).action();//triger GameAction with the button
    		}
    	}
    	
    	for(int i = 0;i<logAndItemsWindow.components.size();i++ ){
    		
    		logAndItemsWindow.components.get(i).action();//triger GameAction with the button
    		
    	}
    	
    	//
    	if(pressQuestLog.isPerformed()){
    		 System.out.println("Q pressed quest log");
    	}
    	
    	if(mouseViewRotate.isActive() ){
    		System.out.println("mouseViewRotate"+
    			(Driver.userInput.getXDif()) +" "+
    			(Driver.userInput.getYDif())
    		);
    		
    	}
    
    }
    
    /** load texture for the gui components */
    public void loadImages() throws java.io.IOException
    {
    	texUIButton = Display.renderer.textureLoader.getTexture("data/images/buttons/questlog_ico.png", Display.renderer.gl, Display.renderer.glu);
    	texInvButton = Display.renderer.textureLoader.getTexture("data/images/buttons/button_01.png", Display.renderer.gl, Display.renderer.glu);
    }
    
    /** load the gui components after textures are loaded*/
    public void loadGUI()
    {
    	/**** init gui elements ****/
    	// invisible pane that holds the elements
    	logAndItemsWindow = new UIWindow("",0,0,false);
    	// add buttons
    	questLog = new UIButton(texUIButton, pressQuestLog,128,0,32,32);
    	
    	for(int i = 0;i<inventoryButton.length; i++){
    		inventoryButton[i]= new UIButton(texInvButton, pressQuestLog, 192+(64*i),0, 64, 64);
    	}
    	
    	// add gui elements 
    	logAndItemsWindow.add(questLog);
    	
    	for(int i = 0;i<inventoryButton.length; i++){
    		logAndItemsWindow.add(inventoryButton[i]);
    	}
    }// end load gui
}