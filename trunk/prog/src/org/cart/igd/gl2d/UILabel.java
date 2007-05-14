package org.cart.igd.gl2d;

import org.cart.igd.util.ColorRGBA;

public class UILabel extends UIComponent
{
	String text = "";
	public UILabel(String value, int x, int y)
	{
		this(value,x,y,128,16);
	}
	
	public UILabel(String value, int x, int y, int width, int height)
	{
		super(x,y,width,height);
		this.text = value;
	}
	
	public void draw(GLGraphics g)
	{
		if(size[0] == 1f){
			g.drawBitmapStringStroke( text, getX(), getY(), 1, 
					rgbaDefault,
					ColorRGBA.White.getRGBA() );
		} else {
			g.drawBitmapString( text, getX(), getY(), new float[]{1,1,1,}, 
					size );
			
		}
	}
	
	public void activate(){
		
	}

}