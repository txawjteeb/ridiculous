package org.cart.igd.gl2d;

import javax.media.opengl.GL;

import org.cart.igd.Display;
import org.cart.igd.util.ColorRGBA;

public class UITextField extends UIComponent
{
	private float[] fontColor = new float[] {0f,0f,0f};
	private boolean password = false;
	private GL gl;
	
	public UITextField(GL gl, String value, int x, int y, int width, int height, boolean password)
	{
		super(x,y,width,height);
		this.gl = gl;
		this.value = value;
		this.rgb = new float[] { 0f,0f,0f };
		this.password = password;
	}
	
	public void draw(GLGraphics g,int x, int y, float alpha)
	{
		//GLEngine.g.drawImageHue( Images.UITextField, x+rel_x, y+rel_y, new float[]{rgb[0],rgb[1],rgb[2],alpha}, new float[] { ((float)width)/128f, ((float)height)/16f } );
		int bx = getX();
		int by = getY()+((int)(((float)getHeight())/2f))-8;
		/*GLEngine.g.drawRect(x+rel_x, y+rel_y, width, height, 
				new float[] {(31f/255f),(83f/255f),(124f/255f)}); */
		
		if(focused){
			g.fillRect(getX(), getY(), getWidth(), getHeight(), 
					new float[]{rgb[0],rgb[1],rgb[2]});
		}
		
		gl.glColor3f(fontColor[0], fontColor[1], fontColor[2]);
		gl.glLineWidth(2f);
		g.drawLine(getX(), getY(), getX()+getWidth(), getY());
		gl.glLineWidth(1f);
		
		String newVal = "";
		if(password)
			for(int i=0; i<value.length(); i++) newVal += "*";
		else
			newVal = value;
		
		if(!focused)
			g.drawBitmapString( value, bx, by, new float[] {fontColor[0],fontColor[1],fontColor[2],1f} );
		else
		{
			int length = g.drawBitmapStringStroke( 
					value, 
					bx, 
					by, 
					1, 
					new float[] {fontColor[0],fontColor[1],fontColor[2],1f}, 
					ColorRGBA.Black.getRGBA() );
			
			g.drawBitmapString( "|", bx+length, by, new float[] {1f,1f,1f,1f} );
		}
	}
	
	public void getFocus() { 
		focused=true; 
		fontColor = new float[] {1f,1f,1f}; 
		rgb = new float[] { (0f/255f), (16f/255f), (51f/255f) }; 
	}
	
	public void dropFocus() {
		focused=false; 
		fontColor = new float[] {0f,0f,0f}; 
		rgb = new float[] {0f,0f,0f}; 
	}
}
