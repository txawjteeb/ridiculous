package org.cart.igd.ui;

public abstract class UIComponent
{
	public String value;
	public int rel_x, rel_y;
	public int width, height;
	public float[] rgb;
	public boolean focused;
	
	public abstract void draw(int x, int y, float alpha);
	public abstract void getFocus();
	public abstract void dropFocus();
	public abstract void activate();
}