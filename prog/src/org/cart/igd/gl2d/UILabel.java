package org.cart.igd.gl2d;

import org.cart.igd.util.ColorRGBA;

public class UILabel extends UIComponent
{
	public UILabel(String value, int x, int y)
	{
		this(value,x,y,128,16);
	}
	
	public UILabel(String value, int x, int y, int width, int height)
	{
		super(x,y,width,height);
		this.value = value;
		
		this.rgb = new float[] { 0f,0f,0f };
	}
	
	public void draw(GLGraphics g,int x, int y, float alpha)
	{
		g.drawBitmapStringStroke( value, getX(), getY(), 1, new float[] {rgb[0],rgb[1],rgb[2],alpha}, ColorRGBA.White.getRGBA() );
	}
	
	public void activate(){
		
	}
	
	public void getFocus() {  }
	
	public void dropFocus() {  }
}