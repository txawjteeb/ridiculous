package org.cart.igd.gl2d;

import org.cart.igd.input.UserInput;
import org.cart.igd.util.Texture;

public class GLRolloverButton extends UIComponent{
	public boolean mouseOver = false;
	Texture tex[] = new Texture[2];
	
	private float[] rgba = new float[]{1f,1f,1f,.6f};
	
	public GLRolloverButton(Texture tex1,Texture tex2, int x, int y, int w, int h){
		super(x,y,w,h);
		tex[0]=tex1;
		tex[1]=tex2;
	}

	public void draw(GLGraphics g) {
		if(mouseOver){
			g.drawImage( tex[1],this.x, this.y, 64, 64,
					0, rgba, size );
			
			
		} else {
			g.drawImage( tex[0], this.x, this.y, 64,64,
					0, rgba, size );
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
