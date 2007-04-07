package org.cart.igd.ui;

import org.cart.igd.Display;
import org.cart.igd.util.Texture;
import org.cart.igd.input.*;

public class UIButton extends UIComponent
{
	public Texture texture;
	private GameAction action;
	
	private boolean pressed = false;
	
	public UIButton(String value, int x, int y)
	{
		this(value, x, y, 64, 64);
	}
	
	public UIButton(String value, int x, int y, int width, int height)
	{
		this.width = width;
		this.height = height;
		this.value = value;
		this.rel_x = x;
		this.rel_y = y;
		this.rgb = new float[] { 1f,1f,1f };
	}
	
	public UIButton(Texture tex, GameAction action, int x, int y, int width, int height)
	{
		texture = tex;
		this.action =action;
		this.width = width;
		this.height = height;
		this.value = "";
		this.rel_x = x;
		this.rel_y = y;
		this.rgb = new float[] { 1f,1f,1f };
	}
	
	public void draw(int x, int y, float alpha)
	{
		//Display.renderer.g.drawImageHue( UIButton.texture, x+rel_x, y+rel_y, new float[]{rgb[0],rgb[1],rgb[2],alpha}, new float[] { ((float)width)/128f, ((float)height)/20f } );
		//Display.renderer.g.drawBitmapString( value, x+rel_x+5, y+rel_y+((int)(((float)height)/2f))-6, new float[] {0f,0f,0f,1f} );
		Display.renderer.getGLG().drawImage(texture,x+rel_x,y+rel_y);
	}
	
	public void setTexture(Texture tex){
		texture = tex;
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public void setAction(GameAction act){
		action = act;
	}
	public GameAction getAction(){
		return action;
	}
	
	
	public void activate()
	{
		if(action != null){
			
			//System.out.println(action.getInfo());
			action.activate();
		}
		if(action == null){
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