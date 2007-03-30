package org.cart.igd;

import org.cart.igd.util.Texture;
import org.cart.igd.ui.UIButton;
import org.cart.igd.ui.UIWindow;
import org.cart.igd.input.*;
import java.awt.event.*;

public class InGameGUI extends GUI
{
	private Texture texUIButton;
	private Texture texAnimalButton;
	private UIButton questLog;
	
	private UIWindow logAndItemsWindow;
	
	private GameAction pressQuestLog;
	
	private GameAction mouseViewRotate;
	
	

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
    	
    	Driver.userInput.bindToButton(pressQuestLog);
    	Driver.userInput.bindToKey(pressQuestLog, KeyEvent.VK_Q);
    	Driver.userInput.bindToMouse(mouseViewRotate,MouseEvent.BUTTON3 );
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
    	if(UserInput.isSquareButtonPressed(
    		logAndItemsWindow.components.get(0).rel_x,
    		logAndItemsWindow.components.get(0).rel_y,32,
    		Driver.userInput.mousePos[0],
    		Driver.userInput.mousePos[1]) )
    	{
    		System.out.println("mouse over");
    	}
    	
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
    	texAnimalButton = Display.renderer.textureLoader.getTexture("data/images/buttons/button_01.png", Display.renderer.gl, Display.renderer.glu);
    }
    
    /** load the gui components after textures are loaded*/
    public void loadGUI()
    {
    	//quest log and items pane
    	logAndItemsWindow = new UIWindow("",0,0,false);
    	
    	questLog = new UIButton(texUIButton, pressQuestLog,128,0,32,32){
    		public void action(){
    			System.out.println("quest log open");
    		}
    	};
    	
    	logAndItemsWindow.add(questLog);
    	//end quest log and items pane
    	
    	
    }// end load gui
}