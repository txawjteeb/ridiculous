package org.cart.igd.gl2d;

import org.cart.igd.util.Texture;
import org.cart.igd.input.*;

public class UIButton extends UIComponent
{
	/* button special effect options*/
	public static final int BUTTON_NORMAL = 1;
	public static final int BUTTON_ROCK = 2;
	public static final int BUTTON_SCALE = 3;
	public static final int BUTTON_ROCK_SCALE = 4;
	
	/* general */
	public int mouseOverEffect = BUTTON_ROCK_SCALE;
	public boolean enabled = true;
	public boolean selectable = true;
	public boolean mouseOver = false;
	
	/* special effect related*/
	private long timeToUpdate = 0;
	private long updateTime = 30;
	
	private String toolTip = "button";
	private float toolTipRgba[] ={1f,1f,0.6f,0f} ;
	private float toolTipStroke[] = {0f,0f,0f,0f};
	private int delay = 0;
	private int rockIndex = 0;
	
	private float rgba[] = { 1f,1f,1f,1f };
	
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
	
	public UIButton(Texture tex, GameAction action, int x, int y, 
			int width, int height)
	{
		super(x,y,width,height);
		setTexture( tex );
		setAction( action );
		this.rgb = new float[] { 1f,1f,1f };
	}
	
	public void update(UserInput input,long elapsedTime)
	{
		if(input.isMouseOver(this)){
			mouseOver = true;
		} else {
			mouseOver = false;
		}
		
		if(enabled){
		switch(mouseOverEffect) {
			case 1:
				timeToUpdate -= elapsedTime;
				if(timeToUpdate <= 0)
				{	
					if(mouseOver)
					{
						toolTipRgba[3] = 1f;
						toolTipStroke[3] = 1f;
					} else {
						toolTipRgba[3] -= .055f;
						toolTipStroke[3] -=.04f;
					}
				} 
				break;
			case 2:
				timeToUpdate -= elapsedTime;
				if(timeToUpdate <= 0)
				{
					if(mouseOver)
					{
						toolTipRgba[3] = 1f;
						toolTipStroke[3] = 1f;
					
						if(rockIndex < rockDegreeCycle.length-1){
							rockIndex++;
						} else {
							rockIndex = 0;
						}
						timeToUpdate = updateTime;
						delay = 3;	
					} else {					
						toolTipRgba[3] -= .035f;
						toolTipStroke[3] -=.04f;;
						if(delay > 0 ){
							if(rockIndex < rockDegreeCycle.length-1){
								rockIndex++;
							} else {
								rockIndex = 0;
								delay--;
							}
						}
					}
				}// end if(timeToUpdate <= 0)
				break;
			case 3:
				timeToUpdate -= elapsedTime;
				if(timeToUpdate <= 0)
				{
					if(mouseOver)
					{
						toolTipRgba[3] = 1f;
						toolTipStroke[3] = 1f;
					
						size[0]=1.2f;
						size[1]=1.2f;
						delay = 3;	
					} else {					
						toolTipRgba[3] -= .035f;
						toolTipStroke[3] -=.04f;
						if(delay > 0 ){
							delay--;
						}
						size[0]=1f;
						size[1]=1f;
					}
					timeToUpdate = updateTime;
				}// end if(timeToUpdate <= 0)
				break;
			case 4:
				timeToUpdate -= elapsedTime;
				if(timeToUpdate <= 0)
				{
					if(mouseOver)
					{
						toolTipRgba[3] = 1f;
						toolTipStroke[3] = 1f;
					
						if(rockIndex < rockDegreeCycle.length-1){
							rockIndex++;
						} else {
							rockIndex = 0;
						}
						size[0]=1.2f;
						size[1]=1.2f;
						delay = 3;	
					} else {					
						toolTipRgba[3] -= .035f;
						toolTipStroke[3] -=.04f;
						if(delay > 0 ){
							if(rockIndex < rockDegreeCycle.length-1){
								rockIndex++;
							} else {
								rockIndex = 0;
								delay--;
							}
						}
						size[0]=1f;
						size[1]=1f;
					}
					timeToUpdate = updateTime;
				}// end if(timeToUpdate <= 0)
				break;
			default:
					System.out.println("Invalid Button Action");
			break;
		}// end switch(mouseOverEffect)
		}// end if(enabled);
	}
	/**
	 * Draw the button at predefined location defined during creation
	 * */
	public void draw(GLGraphics g)
	{
		g.drawImage( getTexture(), getX(), getY(), getWidth(), getHeight(),
				rockDegreeCycle[rockIndex], rgba, size );
			
		if( !toolTip.equals("") || toolTipRgba[3] < 0f )
		{
			g.drawBitmapStringStroke( toolTip, getX(), getY()+getHeight(), 1,
					toolTipRgba, toolTipStroke);
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
	
	public void setAvailable(boolean aval){
		if(aval){
			enabled = true;
			rgba[0]=1f;
			rgba[1]=1f;
			rgba[2]=1f;   
		} else {
			enabled = false;
			rgba[0]=.2f;
			rgba[1]=.2f;
			rgba[2]=.2f;   
		}
	}
}