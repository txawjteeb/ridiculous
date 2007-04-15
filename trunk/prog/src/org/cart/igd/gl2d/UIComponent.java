package org.cart.igd.gl2d;
import org.cart.igd.input.GameAction;
import org.cart.igd.util.*;

public abstract class UIComponent
{
	public String value;
	private int x, y;
	private int width, height;
	public float[] rgb;
	public boolean focused;
	
	public abstract void draw(GLGraphics g,int x, int y, float alpha);
	public abstract void getFocus();
	public abstract void dropFocus();
	public abstract void activate();
	
	private UIComponent parent;
	
	private Texture texture;
	private GameAction gameAction;
	
	
	public UIComponent(int x,int y, int w,int h){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	public void setTexture(Texture texture){
		this.texture = texture;
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public void setAction(GameAction act){
		gameAction = act;
	}
	public GameAction getAction(){
		return gameAction;
	}
	
	/** returns x location relative to the canvas takes 
	 * the location of the parent(container) into account*/
	public int getX(){
		if(parent!= null){
			return parent.x +this.x;
		}
		return x;
	}
	
	/** returns y location relative to the canvas takes 
	 * the location of the parent(container) into account*/
	public int getY(){
		if(parent!= null){
			return parent.getY() +this.y;
		}
		return y;
	}
	
	public void setXY(int x,int y){
		this.x=x;
		this.y=y;
	}
	public void setX(int x){
		this.x=x;
	}
	
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	
	public void setParent(UIComponent parent){
		this.parent=parent;
	}
}