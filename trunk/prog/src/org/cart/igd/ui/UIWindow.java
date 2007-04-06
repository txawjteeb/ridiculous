package org.cart.igd.ui;

import java.util.ArrayList;
import org.cart.igd.util.Texture;
import org.cart.igd.Display;
import org.cart.igd.input.UserInput;
import org.cart.igd.core.*;

public class UIWindow
{
	public ArrayList<UIComponent> components = new ArrayList<UIComponent>();
	public String title;
	public int x, y;
	public int paddingLeft, paddingBottom, paddingTop, paddingRight;
	public float alpha, fade_vel;
	public boolean closing, opening;
	public boolean draggable, dragEnabled;
	public int dragPoint[] = new int[2];
	public int focused_component = -1;
	public int charSelected = -1;
	public Texture[] tex;
	public int colbreak = 1;
	public boolean typingEnabled;
	
	public UIWindow(String title, int x, int y, boolean draggable)
	{
		this(title, x, y, draggable, 0, 0, 0, 0);
	}
	
	public UIWindow(String title, int x, int y, boolean draggable, Texture[] tex, int colbreak)
	{
		this(title, x, y, draggable);
		this.tex = tex;
		this.colbreak = colbreak;
	}
	
	public UIWindow(String title, int x, int y, boolean draggable, int paddingLeft, int paddingBottom, int paddingRight, int paddingTop)
	{
		closing = false;
		opening = false;
		fade_vel = 1f / 40f;
		alpha = 1f;
		this.draggable = draggable;
		this.x = x;
		this.y = y;
		this.title = title;
		this.paddingLeft = paddingLeft;
		this.paddingRight = paddingRight;
		this.paddingBottom = paddingBottom;
		this.paddingTop = paddingTop;
	}
	
	public boolean isVisible() { return (alpha==1f); }
	
	public void close() { if(!opening) closing = true; }
	public void open() { if(!closing) opening = true; }
	
	public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom)
	{
		this.paddingBottom = paddingBottom;
		this.paddingLeft = paddingLeft;
		this.paddingRight = paddingRight;
		this.paddingTop = paddingTop;
	}
	
	public void updateAndDraw()
	{
		if(closing && alpha<=0f) { closing = false; alpha = 0f; }
		if(opening && alpha>=1f) { opening = false; alpha = 1f; }
		if(isVisible())
		{
			//update
			float mod = 0f;
			if(closing) mod = -1f;
			if(opening) mod = 1f;
			if(mod!=0f) alpha+=(fade_vel*mod);
			
			/*
			int k = 0;
			for(int i=0; i<tex.length/colbreak; i++)
			{
				for(int j=0; j<colbreak; j++)
				{
					Display.renderer.g.drawImageAlpha(tex[k], x+j*tex[k].imageWidth, y, 1f);
					k++;
				}
			}
							
			Display.renderer.g.drawBitmapString( title, x+paddingLeft, y+(tex[0].imageHeight*(tex.length/colbreak))-paddingTop, new float[] {1f,1f,1f,1f} );
			*/
				
			for(int i=0; i<components.size(); i++)
			components.get(i).draw(x+paddingLeft, y+paddingBottom, alpha);
		}
	}
	
	public void mouseReleased(int mx, int my)
	{
		my = Kernel.display.getScreenHeight()-my;//refference has to be changed
		if(dragEnabled) dragEnabled = false;
	}
	
	public void mouseMoved(int mx, int my)
	{
		if(draggable) return;
		my = Kernel.display.getScreenHeight()-my;//refference has to be changed
		if(dragEnabled)
		{
			x+=mx-dragPoint[0];
			dragPoint[0] = mx;
			y+=my-dragPoint[1];
			dragPoint[1] = my;
		}
	}
	
	public void mousePressed(int mx, int my)
	{
		my = Kernel.display.getScreenHeight()-my;//refference has to be changed
		if(mx>x && mx<x+1024 && my>y && my<y+512)
		{
			if(draggable && my>y+512-paddingTop) dragEnabled = true;
			/*if(mx>x+482 && mx<x+512 && my>y+482 && my<y+512)
			{
				close();
				return;
			}*/
			
			//press was inside window
			int tmp = focused_component;
			focused_component = -1;
			boolean earlyExit = false;
			for(int i=0; i<components.size(); i++)
			{
				if(earlyExit) break;
				UIComponent c = components.get(i);
				if(	mx>x+paddingLeft+c.rel_x &&
					mx<x+paddingLeft+c.rel_x+c.width &&
					my>y+paddingBottom+c.rel_y &&
					my<y+paddingBottom+c.rel_y+c.height)
				{
					focused_component = i;
					c.getFocus();
					if(tmp!=-1&&tmp!=i) components.get(tmp).dropFocus();
					earlyExit = true;
				}
			}
			typingEnabled = (focused_component!=-1);
			if(focused_component==-1)
			{
				for(int i=0; i<components.size(); i++)
				{
					components.get(i).dropFocus();
				}
			}
		}
	}
	
	public void add(UIComponent c) { components.add( c ); }
}