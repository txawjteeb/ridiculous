package org.cart.igd;

import org.cart.igd.util.Texture;
import org.cart.igd.ui.UIButton;
import org.cart.igd.ui.UIWindow;

public class InGameGUI extends GUI
{
	private Texture texUIButton;
	private Texture texAnimalButton;
	private UIButton questLog;
	
	private UIWindow logAndItemsWindow;

    public InGameGUI()
    {
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
    
    public void render(GLGraphics g)
    {
    	g.glgBegin();
    	//g.drawImageHue(texUIButton, 0, 0, new float[] { 1f, 0f, 0f });
    	//g.drawBitmapString("Button", 3, 3);
    	//g.drawImage(texAnimalButton, 200,200);
    	//questLog.draw(64,64,1f);
    	logAndItemsWindow.updateAndDraw();
    	g.glgEnd();
    }
    
    public void handleInput()
    {
    	//if(UserInput.isSquareButtonPressed(questLog.x,questLog.y,32,mousePos[0],mousePos[1]) )
    	//{
    	//	System.out.println("mouse over");
    	//}
    	
    	
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
    	
    	questLog = new UIButton(texUIButton,128,0,32,32){
    		public void action(){
    			System.out.println("quest log open");
    		}
    	};
    	
    	logAndItemsWindow.add(questLog);
    	//end quest log and items pane
    	
    	
    }// end load gui
}