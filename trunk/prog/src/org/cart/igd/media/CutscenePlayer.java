package org.cart.igd.media;

import org.cart.igd.util.JMFSnapper;
import org.cart.igd.util.Texture;
import org.cart.igd.gl2d.GLGraphics;

import org.cart.igd.util.ColorRGBA;
import org.cart.igd.core.Kernel;

public class CutscenePlayer
{
	public JMFSnapper snapper;
	public boolean isStopped = true;
//	public JPanel cutscenePanel;
	
	private int i =0;
	
	public CutscenePlayer()
	{
		
	}
	
	public void loadMovie(String fnm)
	{
		snapper = new JMFSnapper(fnm);
	}
	
	public void update(long elapsedTime)
	{
		
	}
	
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
		glg.drawImage(frame, 0, 0, new float[] {(Kernel.display.getScreenWidth()/frame.imageWidth), (Kernel.display.getScreenHeight()/frame.imageHeight)});
		glg.drawBitmapString(""+i, 0, 0, ColorRGBA.White.getRGBA());
		i++;
		glg.glgEnd();
	}
	
	public void playMovie()
	{
		isStopped = false;
	}
	
	public void stopMovie()
	{
		isStopped = true;
	}
}