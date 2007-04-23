package org.cart.igd.gl2d;

import org.cart.igd.util.Texture;
import org.cart.igd.input.*;

public class UIButton extends UIComponent
{
	private boolean pressed = false;
	
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
	
	public void draw(GLGraphics g, int x, int y)
	{
		g.drawImage(getTexture(),x,y,scale,scale);
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
	
	public void getFocus()
	{
		rgb = new float[] { (102f/255f), (153f/255f), (204f/255f) };
		pressed = true;
	}
	
	public void dropFocus()
	{
		rgb = new float[] {1f,1f,1f};
	}
}