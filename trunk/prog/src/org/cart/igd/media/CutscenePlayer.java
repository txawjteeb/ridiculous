package org.cart.igd.media;

import org.cart.igd.util.JMFSnapper;
import org.cart.igd.util.Texture;
import org.cart.igd.gl2d.GLGraphics;

import org.cart.igd.util.ColorRGBA;
import org.cart.igd.core.Kernel;

/**
 * CutscenePlayer.java
 *
 * General Function: OpenGL rendering for cutscenes.
 */
public class CutscenePlayer
{
	/* Instance of JMFSnapper to grab each frame. */
	public JMFSnapper snapper;
	
	/* A Flag that tells if the movie is stopped. */
	public boolean isStopped = true;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of CutscenePlayer.
	 */
	public CutscenePlayer()
	{
	}
	
	/**
	 * loadMovie
	 *
	 * General Function: Loads a movie into the JMFSnapper.
	 */
	public void loadMovie(String fnm)
	{
		isStopped = false;
		snapper = new JMFSnapper(fnm);
	}
	
	/**
	 * update
	 *
	 * General Function: Called before render to update game logic.
	 */
	public void update(long elapsedTime)
	{
	}
	
	/**
	 * render
	 *
	 * General Function: Renders to OpenGL buffer.
	 */
	public void render(GLGraphics glg)
	{
		if(snapper==null)
		{
			isStopped = true;
			return;
		}
		
		Texture frame = snapper.getFrame();
		if(frame==null)
			isStopped = true;
			
		glg.glgBegin();
		glg.drawImage(frame, 0, 0, ((float)Kernel.display.getScreenWidth()/(float)frame.imageWidth), ((float)Kernel.display.getScreenHeight()/(float)frame.imageHeight));
		glg.glgEnd();
	}
	
	/**
	 * playMovie
	 *
	 * General Function: Set isStopped to false.
	 */
	public void playMovie()
	{
		isStopped = false;
	}
	
	/**
	 * stopMovie
	 *
	 * General Function: Set isStopped to true.
	 */
	public void stopMovie()
	{
		isStopped = true;
	}
}