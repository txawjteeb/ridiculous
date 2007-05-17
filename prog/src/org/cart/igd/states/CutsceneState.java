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
	private CutscenePlayer player;
	
	public CutsceneState(GL gl)
	{
		player = new CutscenePlayer();
		
		player.loadMovie("data/movies/movie02/1.avi");
	}
	
	public void loadCutscene(String fn)
	{
		player.loadMovie(fn);
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
		
	}
	
	public void handleInput(long elapsedTime)
	{
		if(Kernel.userInput.keys[KeyEvent.VK_SPACE])
		{
			if(player.isStopped)
				player.playMovie();
			else
				player.stopMovie();
		}
	}
}