package org.cart.igd.ui;

import org.cart.igd.Display;
import org.cart.igd.util.ColorRGBA;

public class UITextField extends UIComponent
{
	private float[] fontColor = new float[] {0f,0f,0f};
	private boolean password = false;
	
	public UITextField(String value, int x, int y)
	{
		this(value,x,y,128,16,false);
	}
	
	public UITextField(String value, int x, int y, int width, int height)
	{
		this(value,x,y,width,height,false);
	}
	
	public UITextField(String value, int x, int y, int width, int height, boolean password)
	{
		this.width = width;
		this.height = height;
		this.value = value;
		this.rel_x = x;
		this.rel_y = y;
		this.rgb = new float[] { 0f,0f,0f };
		this.password = password;
	}
	
	public void draw(int x, int y, float alpha)
	{
		//GLEngine.g.drawImageHue( Images.UITextField, x+rel_x, y+rel_y, new float[]{rgb[0],rgb[1],rgb[2],alpha}, new float[] { ((float)width)/128f, ((float)height)/16f } );
		int bx = x+rel_x;
		int by = y+rel_y+((int)(((float)height)/2f))-8;
		//GLEngine.g.drawRect(x+rel_x, y+rel_y, width, height, new float[] {(31f/255f),(83f/255f),(124f/255f)});
		
		if(focused) Display.renderer.getGLG().fillRect(x+rel_x, y+rel_y, width, height, new float[]{rgb[0],rgb[1],rgb[2]});
		Display.renderer.getGL().glColor3f(fontColor[0], fontColor[1], fontColor[2]);
		Display.renderer.getGL().glLineWidth(2f);
		Display.renderer.getGLG().drawLine(x+rel_x, y+rel_y, x+rel_x+width, y+rel_y);
		Display.renderer.getGL().glLineWidth(1f);
		
		String newVal = "";
		if(password)
			for(int i=0; i<value.length(); i++) newVal += "*";
		else
			newVal = value;
		
		if(!focused)
			Display.renderer.getGLG().drawBitmapString( value, bx, by, new float[] {fontColor[0],fontColor[1],fontColor[2],1f} );
		else
		{
			int length = Display.renderer.getGLG().drawBitmapStringStroke( value, bx, by, 1, new float[] {fontColor[0],fontColor[1],fontColor[2],1f}, ColorRGBA.Black.getRGBA() );
			Display.renderer.getGLG().drawBitmapString( "|", bx+length, by, new float[] {1f,1f,1f,1f} );
		}
	}
	
	public void getFocus() { focused=true; fontColor = new float[] {1f,1f,1f}; rgb = new float[] { (0f/255f), (16f/255f), (51f/255f) }; }
	
	public void dropFocus() { focused=false; fontColor = new float[] {0f,0f,0f}; rgb = new float[] {0f,0f,0f}; }
}
