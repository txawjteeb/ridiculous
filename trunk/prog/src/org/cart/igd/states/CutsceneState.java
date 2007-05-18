package org.cart.igd.states;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import java.awt.event.KeyEvent;

import org.cart.igd.core.Kernel;
import org.cart.igd.gl2d.GLGraphics;
import org.cart.igd.media.CutscenePlayer;
import org.cart.igd.util.ColorRGBA;

public class CutsceneState extends GameState
{
	public CutscenePlayer player;
	
	public CutsceneState(GL gl)
	{
		player = new CutscenePlayer();
		
		player.loadMovie("data/movies/movie02/1.avi");
	}
	
	public void loadCutscene(String fn)
	{
		player.loadMovie(fn);
	}
	
	public void loadCutscene(int index)
	{
		String path = "data/movies/movie02/1.avi";
		
		if(index <10){
			path = "data/movies/movie0"+ Kernel.global.nextMovieIndex+"/1.avi";
		} else {
			path = "data/movies/movie"+ Kernel.global.nextMovieIndex+"/1.avi";
		}
		
		player.loadMovie(path);
	}
	
	public void display(GL gl, GLU glu)
	{
		if(!player.isStopped)
		{
			player.render(Kernel.display.getRenderer().getGLG());
		}
		Kernel.display.getRenderer().getGLG().drawBitmapString((player.isStopped?"Stopped":"Playing"),0,0,ColorRGBA.Red.getRGBA());
	}
	
	public void update(long elapsedTime)
	{
		if(player.isStopped)
		{
			Kernel.display.renderer.stateManager.setCurrentState("InGameState");
		}
		
		if(Kernel.userInput.keys[KeyEvent.VK_ENTER])
		{
			if(player.isStopped)
				player.playMovie();
			else
				player.stopMovie();
		}	
	}
	
	public void handleInput(long elapsedTime)
	{
			
	}
}