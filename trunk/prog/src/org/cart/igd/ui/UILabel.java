package org.cart.igd.ui;

import org.cart.igd.Display;
import org.cart.igd.util.ColorRGBA;

public class UILabel extends UIComponent
{
	public UILabel(String value, int x, int y)
	{
		this(value,x,y,128,16);
	}
	
	public UILabel(String value, int x, int y, int width, int height)
	{
		this.width = width;
		this.height = height;
		this.value = value;
		this.rel_x = x;
		this.rel_y = y;
		this.rgb = new float[] { 0f,0f,0f };
	}
	
	public void draw(int x, int y, float alpha)
	{
		Display.renderer.getGLG().drawBitmapStringStroke( value, x+rel_x, y+rel_y, 1, new float[] {rgb[0],rgb[1],rgb[2],alpha}, ColorRGBA.White.getRGBA() );
	}
	
	public void activate(){
		
	}
	
	public void getFocus() {  }
	
	public void dropFocus() {  }
}