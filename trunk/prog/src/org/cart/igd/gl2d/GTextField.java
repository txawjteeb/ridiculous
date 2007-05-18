package org.cart.igd.gl2d;

import java.awt.event.KeyEvent;

import org.cart.igd.input.UserInput;

public class GTextField extends UIComponent
{
	public String text = "";
	public GTextField(UserInput ui,int x, int y, int w, int h) {
		super(x, y, w, h);
		ui.addComponentForListening(this);
	}

	public void draw(GLGraphics g) {
		g.fillRect(getX(), getY(), getWidth(), getHeight(), 
				new float[]{0f,0f,0f});
		g.drawBitmapString(text, x, y);
		
	}
	
	public void update(long elapsedTime){
		
	}
	
	public void keyPressed(KeyEvent e){
		System.out.println("key recieved");
		if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
			text = text.substring(0, text.length()-1);
		} else {
			char key = e.getKeyChar();
			if(key>33&&key<126){
				text = (text+key);
			}
			
		}
	}

}
