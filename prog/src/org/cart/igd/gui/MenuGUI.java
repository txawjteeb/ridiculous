package org.cart.igd.gui;

import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;

public class MenuGUI extends GUI
{
	public static final int PLAY = 1;
	public static final int OPTIONS = 2;
	
	int selection = 0;
	
	Texture bg;
	Texture texMenuButtons[] = new Texture[4];
	GLRolloverButton btMenu[] = new GLRolloverButton[4];
	
	Texture texOn;
	Texture texOff;
	Texture texGbar;
	Texture texRbar;
	
	public static final int MUSIC = 0;
	public static final int VOICE = 1;
	public static final int SOUND = 2;
	
	
	GLRolloverButton btVol[][] = new GLRolloverButton[3][11];
	
	
	/** contains collision detection methods for UIComponents*/
	private UserInput input;
	
	/** 
	 * Constructor
	 * @param GameState gameState refference to allow state change 
	 */
	public MenuGUI(GameState gameState){
		super(gameState);//superclass GUI contain ref to GameState
		input = Kernel.userInput;
		loadImages();
	}
	
	public void loadImages()
	{
		texOn = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/volume_bt/bt32_on.png");
		texOff = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/volume_bt/bt32_off.png");
		texGbar = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/volume_bt/bt32_bar_g.png");
		texRbar = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/volume_bt/bt32_bar_r.png");
		
		
		bg = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/MainMenu_sa.png");
		
		texMenuButtons[0] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/start_button_jh.png");

		texMenuButtons[1] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/options_button_jh.png");

		texMenuButtons[2] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/credits_button_jh.png");

		texMenuButtons[3] = Kernel.display.getRenderer().loadImage(
			"data/images/menu_gui/exit_button_jh.png");
		
		btMenu[0] = new GLRolloverButton(texMenuButtons[0],
				new float[]{ .5f, .5f, .5f, 1f},new float[]{.50f,.50f},
				new float[]{ 1f, .8f, .8f, 1f}, new float[]{.55f,.55f},
				300,536,128,64);
		
		btMenu[1] = new GLRolloverButton(texMenuButtons[1],
				new float[]{ .5f, .5f, .5f, 1f},new float[]{.50f,.50f},
				new float[]{.8f,1f, .8f,1f}, new float[]{.55f,.55f},
				300,426,128,64);
		
		btMenu[2] = new GLRolloverButton(texMenuButtons[2],
				new float[]{ .5f, .5f, .5f, 1f},new float[]{.50f,.50f},
				new float[]{.8f,.8f, 1f,1f}, new float[]{.55f,.55f},
				300,316,128,64);
		
		btMenu[3] = new GLRolloverButton(texMenuButtons[3],
				new float[]{ .5f, .5f, .5f, 1f},new float[]{.50f,.50f},
				new float[]{1f,.8f, 1f,1f}, new float[]{.55f,.55f},
				300,206,128,64);
		
		btVol[MUSIC][0] = new GLRolloverButton(texOn,texOff,480,372-100,32,32);
		btVol[SOUND][0] = new GLRolloverButton(texOn,texOff,480,436-100,32,32);
		btVol[VOICE][0] = new GLRolloverButton(texOn,texOff,480,500-100,32,32);
		for(int i = 1; i<btVol[0].length; i++){
			btVol[MUSIC][i] = 
				new GLRolloverButton(texGbar,texRbar,480+(i*32),372-100,32,32);
			btVol[SOUND][i] = 
				new GLRolloverButton(texGbar,texRbar,480+(i*32),436-100,32,32);
			btVol[VOICE][i] = 
				new GLRolloverButton(texGbar,texRbar,480+(i*32),500-100,32,32);
		}
		
		for(int i = 0; i<3 ; i++){
			btVol[MUSIC][i].focused = true;
			btVol[SOUND][i].focused = true;
			btVol[VOICE][i].focused = true;
		}
		

	}
	
	public void update(long elapsedTime)
	{
		for(GLRolloverButton b: btMenu){
			b.update(input, elapsedTime);
		}
	}
	
	public void handleInput(long elapsedTime)
	{
		if(input.isSquareButtonPressed( btMenu[0] ) )
		{
			System.out.println();
			gameState.changeGameState("InGameState");
		}
		
		if(input.isSquareButtonPressed( btMenu[1] ) )
		{
			selection = OPTIONS;
			System.out.println("selection: "+selection);
		}
		
		if(input.isSquareButtonPressed( btMenu[2] ) )
		{
			selection = 3;
			System.out.println("selection: "+selection);
		}
		
		if(input.isSquareButtonPressed( btMenu[3] ) )
		{
			selection = 4;
			System.out.println("selection: "+selection);
		}
		
		/* sub menus */
		switch(selection){
			case OPTIONS:
				for(int iSound = 0; iSound<btVol.length; iSound++){
					for(int iMusic = 0; iMusic<btVol[0].length; iMusic++){
						if(input.isSquareButtonPressed(btVol[iSound][iMusic]) )
						{
							/* select/deselect buttons to make it look 
							 * like a percentage bar */
							int i = iMusic;
							while(i<btVol[iSound].length){
								btVol[iSound][i].focused = false;
								i++;
							}
							i = iMusic;
							while(i >= 0){
								btVol[iSound][i].focused = true;
								i--;
							}
							/* set the volume in the sound settings */
							switch(iSound){
							case SOUND:
								Kernel.soundSettings.seVol = (iMusic*10);break;
							case MUSIC:
								Kernel.soundSettings.bgVol = (iMusic*10);break;
							case VOICE:
								Kernel.soundSettings.voVol = (iMusic*10);break;
							}
							
						}
						iMusic++;
					}
					if(input.isSquareButtonPressed(btVol[iSound][0])){
						btVol[iSound][0].focused = false;
					}
				}
			break;
			
		}
	}
	
	public void render(GLGraphics g)
	{
		g.glgBegin();
		g.drawImage(bg, 0, 0);
		for( GLRolloverButton b: btMenu){
			b.draw(g);
		}
		
		switch(selection){
			case OPTIONS:
				g.drawBitmapString("Music Volume", 512, 404-100);
				g.drawBitmapString("Sound Volume", 512, 468-100);
				g.drawBitmapString("Voice Volume", 512, 532-100);
			
				for(int i = 0; i<btVol.length;i++){
					for(int j = 0; j<btVol[0].length;j++){
						btVol[i][j].draw(g);
					}
				}		
			break;	
		}
		
		
		g.glgEnd();
	}
		
}
