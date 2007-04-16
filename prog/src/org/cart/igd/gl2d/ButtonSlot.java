package org.cart.igd.gl2d;

import org.cart.igd.input.GameAction;
import org.cart.igd.util.Texture;

public class ButtonSlot extends UIComponent
{
	UIButton button;
	
	public ButtonSlot(Texture tex, int x, int y, int width, int height)
	{
		super(x,y,width,height);
		setTexture(tex);
		setAction(new GameAction("no button selected",false));
		this.value = "";
		this.rgb = new float[] { 1f,1f,1f };
	}
	
	public void draw(GLGraphics g, int x, int y,float alpha){
		
	}
	
	public void setButton(UIButton button){
		this.button = button;
	}
	
	public UIButton getButton(){
		return button;
	}
	
	public void getFocus(){
		
	}
	public void dropFocus(){
		
	}
	
	public void activate(){
		if(button != null){
			button.activate();
		}
	}
		

}
