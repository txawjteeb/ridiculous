package org.cart.igd.models.obj;

import javax.media.opengl.GL;

import org.cart.igd.math.Vector3f;

/**
 * OBJAnimation.java
 * 
 * General Function: Animates a series of OBJ models. 
 */
public class OBJAnimation
{
	/* Frame delay. */
	private long frameDelay;
	
	/* Time left. */
	private long timeLeft;
	
	/* Current index. */
	public int modelIndex = 0;
	
	private boolean looping = true;
	
	public boolean finished = false;
	
	/* OBJModel array. */
	private OBJModel model[];
	
	/* pause animation */
	public boolean pause = false;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of OBJAnimation.
	 */
	public OBJAnimation( GL gl, int numFrames, String filePath, long delay )
	{
		frameDelay = delay;
		timeLeft = delay;
		model = new OBJModel[numFrames];
		for(int i = 0; i<model.length; i++)
		{
			model[i] = new OBJModel(gl,filePath+i, 2f, true);
		}
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of OBJAnimation.
	 */
	public OBJAnimation( GL gl, int numFrames, String filePath, long delay,
			float scale)
	{
		frameDelay = delay;
		timeLeft = delay;
		model = new OBJModel[numFrames];
		for(int i = 0; i<model.length; i++)
		{
			model[i] = new OBJModel(gl,filePath+i, scale, true);
		}
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of OBJAnimation.
	 */
	public OBJAnimation( GL gl, int numFrames, String filePath, long delay, 
			float scale, boolean loop)
	{
		frameDelay = delay;
		timeLeft = delay;
		model = new OBJModel[numFrames];
		looping = loop;
		for(int i = 0; i<model.length; i++)
		{
			model[i] = new OBJModel(gl,filePath+i, scale, true);
		}
	}
	
	public void start(){
		if(!looping){
			finished = false;
			modelIndex = 0;
		}
	}
	
	public boolean isLooping(){
		return looping;
	}
	
	/**
	 * update
	 *
	 * General Function: Updates the frame information and current frame.
	 */
	public void update(long elapsedTime)
	{
		if(!pause)
		{
			timeLeft -=elapsedTime;
			if(timeLeft < 0){
				if(modelIndex < model.length-1)
				{
					modelIndex ++;
				}
				else
				{
					modelIndex = 0;
					if (!looping){
						finished = true;
					}
				}
				
				timeLeft = frameDelay;
			}			
		}

	}
	
	/**
	 * render
	 *
	 * General Function: Renders the correct OBJModel to GL.
	 */
	public void render(GL gl, Vector3f pos, float fd)
	{
		gl.glPushMatrix();
		gl.glTranslatef(pos.x, pos.y -2f, pos.z);
		gl.glRotatef( fd , 0f, -1f, 0f);
		model[modelIndex].draw(gl);
		gl.glPopMatrix();
	}

}
