package org.cart.igd.gl2d;

import org.cart.igd.input.UserInput;
import org.cart.igd.util.Texture;

/**
 * GLRolloverButton a cleaner version of UIButton for simple rollover function
 */
public class GLRolloverButton extends UIComponent{
	public boolean mouseOver = false;
	Texture tex[] = new Texture[2];
	
	private float[] rgba2 = new float[]{.5f,.5f,.5f,1f};
	private float[] rgba = new float[]{1f,.9f,.9f,1f};
	
	public GLRolloverButton(Texture tex1,Texture tex2, int x, int y, int w, int h){
		super(x,y,w,h);
		tex[0]=tex1;
		tex[1]=tex2;
	}

	public void draw(GLGraphics g) {
		if(mouseOver){
			g.drawImageRotateHueSize(tex[0],x-2,y-2,2,rgba,new float[]{.55f,.55f});
			
	//g.drawImageRotateHueSize(texture,x,y,degree, new float[]{1f,alphaSwing,alphaSwing,alpha}, new float[]{1.3f,1.3f});
			
			//g.drawImage( tex[0],this.x, this.y, 64, 64,
				//	0, rgba, size );
			
			
		} else {
			//g.drawImage( tex[0], this.x, this.y, 64,64,
					//0, rgba2, size );
			g.drawImageRotateHueSize(tex[0],x,y,0,rgba2,new float[]{.5f,.5f});
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
