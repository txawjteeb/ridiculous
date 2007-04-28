package org.cart.igd.gl2d;

import org.cart.igd.util.Texture;
import org.cart.igd.core.Kernel;
import org.cart.igd.input.*;

public class UIButton extends UIComponent
{
	/* general */
	public boolean selectable = true;
	public boolean enabled = true;
	public boolean reactOnMouseOver = true;
	
	/* special effect related*/
	private long timeToUpdate = 0;
	private long updateTime = 30;
	
	private String toolTip = "";
	private float toolTipAlpha = 1f;
	private boolean back = false;
	private int rockIndex = 0;
	
	private float rgba[] = { 1f,1f,1f,1f };
	private float size[] = { 1f,1f };
	
	/** rotation cycles when button is rocking back and forth */
	private int rockDegreeCycle[] =
	{
		0, 4, 8, 10, 8, 4, 0,-4,-8,-10,-8,-4,
	};
	
	public UIButton(String value, int x, int y)
	{
		this(value, x, y, 64, 64);
	}
	
	public UIButton(String value, int x, int y, int width, int height)
	{
		super(x,y,width,height);
		this.toolTip = value;
		this.rgb = new float[] { 1f,1f,1f };
	}
	
	public UIButton(Texture tex, GameAction action, int x, int y, int width, int height)
	{
		super(x,y,width,height);
		setTexture( tex );
		setAction( action );
		this.rgb = new float[] { 1f,1f,1f };
	}
	
	public void update(UserInput input,long elapsedTime){
		if(input.isMouseOver(this))
		{
			if(reactOnMouseOver)
			{
				timeToUpdate -= elapsedTime;
				if(timeToUpdate <= 0)
				{
					toolTipAlpha = 1f;
					
					if(rockIndex < rockDegreeCycle.length-1){
						rockIndex++;
					} else {
						rockIndex = 0;
					}
					
					timeToUpdate = updateTime;
				}
				
			}
		} else {
			toolTipAlpha -= .04f;
			rockIndex = 0;
		}
	}
	/**
	 * Draw the button at predefined location defined during creation
	 * */
	public void draw(GLGraphics g)
	{
		g.drawImageRotateHue(getTexture(),getX(),getY(),
			rockDegreeCycle[rockIndex], rgba );
			
		if( !toolTip.equals("") || toolTipAlpha < 0f )
		{
			g.drawBitmapString(toolTip, getX(), getY()+getHeight(), 
					16, rgba, size);
		}
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