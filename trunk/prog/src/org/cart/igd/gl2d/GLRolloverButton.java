package org.cart.igd.gl2d;

import org.cart.igd.input.UserInput;
import org.cart.igd.util.Texture;

/**
 * GLRolloverButton a cleaner version of UIButton for simple rollover function
 */
public class GLRolloverButton extends UIComponent{
	public boolean mouseOver = false;
	Texture tex[] = new Texture[2];
	
	private float[] rgba = new float[]{1f,1f,1f,1f};
	
	private float[] rgbaNormal = new float[]{.5f,.5f,.5f,1f};
	private float[] rgbaRollover = new float[]{1f,.9f,.9f,1f};
	
	private float[] sizeNormal =new float[]{.5f,.5f};
	private float[] sizeRollover =new float[]{.55f,.55f};
	
	public GLRolloverButton(Texture tex1,Texture tex2, int x, int y, int w, int h){
		super(x,y,w,h);
		this.size=new float[]{1f,1f};
		tex[0]=tex1;
		tex[1]=tex2;
	}
	
	public GLRolloverButton(Texture tex1, float[] nColor, float[]nSize, 
			float[] rColor,float[] rSize, int x, int y, int w, int h){
		super(x,y,w,h);
		tex[0]=tex1;
		this.rgbaNormal = nColor;
		this.rgbaRollover = rColor;
		this.sizeNormal = nSize;
		this.sizeRollover = rSize;
	}

	public void draw(GLGraphics g) {
		if(tex[1]==null){
			if(mouseOver){
				g.drawImage( tex[0], x, y, 64, 64, 2, rgbaRollover, sizeRollover );
			} else {
				g.drawImage( tex[0], x, y, 64, 64, 0, rgbaNormal, sizeNormal );
			}
		} else {
			if(focused){
				g.drawImage( tex[0], x, y, 32, 32, 0, rgba, rgba );
			} else {
				g.drawImage( tex[1], x, y, 32, 32, 0, rgba, rgba );
			}
		}
		
	}
	
	public void update(UserInput ui, long elapsedTime){
		if(ui.isMouseOver(this)){
			mouseOver = true;
		} else {
			mouseOver = false;
		}
	}

}
