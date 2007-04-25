package org.cart.igd.gl2d;

import org.cart.igd.util.Texture;
import org.cart.igd.input.*;

public class UIButton extends UIComponent
{
	public boolean enabled = true;
	
	public UIButton(String value, int x, int y)
	{
		this(value, x, y, 64, 64);
	}
	
	public UIButton(String value, int x, int y, int width, int height)
	{
		super(x,y,width,height);
		this.value = value;
		this.rgb = new float[] { 1f,1f,1f };
	}
	
	public UIButton(Texture tex, GameAction action, int x, int y, int width, int height)
	{
		super(x,y,width,height);
		setTexture( tex );
		setAction( action );
		this.value = "";
		this.rgb = new float[] { 1f,1f,1f };
	}
	
	
	/**
	 * Draw the buttons at forced location regardless of location.
	 * Will prevent from using press detection methods correctly.
	 **/
	public void draw(GLGraphics g, int x, int y)
	{
		if(enabled == false){
			g.drawImageHue(getTexture(),x,y,new float[]{.5f,.5f,.5f,.5f} );
		} else {
			g.drawImage(getTexture(),x,y,scale,scale);
		}
	}
	
	
	/**
	 * Draw the button at predefined location defined during creation
	 * */
	public void draw(GLGraphics g)
	{
		if(enabled == false){
			g.drawImageHue(getTexture(),getX(),getY(),new float[]{.5f,.5f,.5f,.5f} );
		} else {
			g.drawImage(getTexture(),getX(),getY(),scale,scale);
		}
	}
	
	public void draw(GLGraphics g, int x, int y, float alpha)
	{
		g.drawImage(getTexture(),getX(),getY(),scale,scale);
	}
	
	public void activate()
	{
		super.activate();
		if(getAction() != null){
			
			//System.out.println(action.getInfo());
			getAction().activate();
			System.out.println("UIButton.activate get action text: "
					+getAction().getInfo());
		}
		if(getAction() == null){
			System.out.println("UIButton: GameAction action is null");
		}
	}
	
	public void moveTo(int x, int y){
		this.setXY(x,y);
	}
	
	public void getFocus()
	{
		rgb = new float[] { (102f/255f), (153f/255f), (204f/255f) };
		enabled = true;
	}
	
	public void dropFocus()
	{
		rgb = new float[] {1f,1f,1f};
	}
}