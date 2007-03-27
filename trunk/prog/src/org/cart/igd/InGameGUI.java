package org.cart.igd;

import org.cart.igd.util.Texture;

public class InGameGUI extends GUI
{
	private Texture texUIButton;

    public InGameGUI()
    {
    	try
    	{
    		loadImages();
    	}
    	catch(java.io.IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void render(GLGraphics g)
    {
    	g.glgBegin();
    	g.drawImageHue(texUIButton, 0, 0, new float[] { 1f, 0f, 0f });
    	g.drawBitmapString("Button", 3, 3);
    	g.glgEnd();
    }
    
    public void handleInput()
    {
    }
    
    public void loadImages() throws java.io.IOException
    {
    	texUIButton = Display.renderer.textureLoader.getTexture("data/images/uibutton.png", Display.renderer.gl, Display.renderer.glu);
    }
}