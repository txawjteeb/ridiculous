package org.cart.igd;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import java.awt.event.KeyEvent;
import com.sun.opengl.util.GLUT;

import org.cart.igd.core.Kernel;
import org.cart.igd.gl2d.GLGraphics;
import org.cart.igd.states.*;
import org.cart.igd.util.TextureLoader;
import org.cart.igd.util.Texture;

import java.awt.image.BufferedImage;

/**
 * Renderer.java
 *
 * General Function:
 * This class handles where to pass game control to,
 * using a GameStateManager.  The Renderer also keeps
 * track of FPS and sets of the OpenGL environments.
 */
public class Renderer implements GLEventListener
{
	/* GameStateManager instance. */
	public GameStateManager stateManager;
	
	/* TextureLoader instance. */
	private TextureLoader textureLoader;

	/* GLU instance. */
	private GLU glu = new GLU();
	
	/* GLUT instance. */
	private GLUT glut = new GLUT();
	
	/* 2D extension for easier rendering through GL. */
	private GLGraphics g;
	
	/* GL instance. */
	private GL gl;										
	
	/* Ambient Light is 20% white */
	private float lightAmbient[] = {0.2f, 0.2f, 0.2f};
	
	/* Diffuse Light is white */
    private float lightDiffuse[] = {1.0f, 1.0f, 1.0f};
    
    /* Position is somewhat in front of screen */
    private float lightPosition[] = {0.0f, 10.0f, 0.0f};
	
	/* Frame count for each second. */
	public int frameCount = 0;
	
	/* Frames per second of the engine. */
	public int fps = 0;
	
	/* The last system time in milliseconds that the FPS was calculated. */
	public long lastFPSCheck;
	
	/* Polygon counter. */
	public int polyCount = 0;
	
	/* Last system time, set by each engine loop. */
	public long lastTime;
	
	/* Debuging information array */
	public static String info[] = { "","","","","","","","","","","","" };
	
	/*
	 * Constructor
	 * 
	 * General function: Creates a new Renderer object.
	 */
	public Renderer()
	{
		textureLoader = new TextureLoader();
		stateManager = new GameStateManager();
	}	
	
	/*
	 * init
	 *
	 * General function: Windows Callback for GL initialization
	 *
	 * @param drawable The GLAutoDrawable to grab the GL instance from.
	 */
	public void init(GLAutoDrawable drawable)
	{
		/* Grab the GL instance from GLAutoDrawable */
		gl = drawable.getGL();
		
		gl.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );	/* Background */
		gl.glClearDepth(1.0f);				/* Depth Buffer Setup */
		gl.glClearStencil(0);				/* Clear the Stencil Buffer to 0 */
		
		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, new float[] {0.25f,0.25f,0.25f,1f}, 0 );
		gl.glShadeModel(GL.GL_SMOOTH);		/* Enable Smooth Shading */
		gl.glDepthFunc(GL.GL_LEQUAL);		/* Depth Testing Type */
		gl.glFrontFace(GL.GL_CCW);
	    gl.glCullFace(GL.GL_BACK);			/* Set Cull Face to Back faces only */
	    gl.glEnable(GL.GL_CULL_FACE);		/* Enable Culling of Faces */
		gl.glEnable(GL.GL_DEPTH_TEST);		/* Enable Depth Testing */
		gl.glEnable(GL.GL_AUTO_NORMAL);		/* Auto-Normal Lighting */
		gl.glEnable(GL.GL_NORMALIZE);		/* Normalize for Lighting */
		gl.glEnable(GL.GL_MULTISAMPLE);		/* Enable Multi-Sampling */
		
		/* Set perspective correction to Nicest setting. */
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		/* Initialize the 3d environment lighting. */
		initLighting(gl);
	//	addFog(gl);
		
		/* Create the GLGraphics object for rendering 2D GUI. */
		g = new GLGraphics(gl,glu,glut);
		
		initGameStates(gl);

		/* Set initial time variable for FPS calculations. */
		lastFPSCheck = System.currentTimeMillis();
	}
	
	/** 
	 * initGameStates
	 * 
	 * General Function: Checks for completion of game state before attempting to update 
	 *
	 * @param gl The GL instance to render to.
	 */
	public void initGameStates(GL gl)
	{
		stateManager.addGameState(new MenuState(gl),"MenuState");
		stateManager.addGameState(new InGameState(gl),"InGameState");
		stateManager.addGameState(new MiniGame(gl),"MiniGame");
		stateManager.addGameState(new CutsceneState(gl),"CutscenePlayer");
	//	stateManager.setCurrentState("CutscenePlayer");
		stateManager.setCurrentState("MenuState");
		stateManager.initStates(gl, glu);
	}
	
	/**
	 * getStateManager
	 *
	 * General Function: Returns the renderer's GameStateManager.
	 */
	public GameStateManager getStateManager()
	{
		return stateManager;
	}
	
	/**
	 * initLighting
	 *
	 * General Function: Enable and setup lighting.
	 *
	 * @param gl The GL instance to render to.
	 */
	private void initLighting(GL gl)
	{
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_LIGHTING);
		
		float[] ambientLight	= new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
		float[] diffuseLight	= new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
		float[] specularLight	= new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
		float[] position		= new float[] { 0.0f, 0.0f, 10.0f, 1.0f };
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specularLight, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
	}
	
	/*
	 * display
	 *
	 * General function: Windows Callback for drawing every frame
	 *
	 * @param drawable The GLAutoDrawable to grab the GL instance from.
	 */
	public void display(GLAutoDrawable drawable)
	{
		/* Update GL instance */
		gl = drawable.getGL();
		
		/* calculate elapsed time */
		long elapsedTime = getElapsedTime();
		
		/* Call current game state methods */
		GameState currentState = stateManager.getCurrentState();
		if(currentState.changeState)
		{
			currentState.changeState = false;
			stateManager.setCurrentState(currentState.nextState);
			currentState = stateManager.getCurrentState();
		}
		currentState.update(elapsedTime);
		currentState.display(gl, glu);
		
		/* Render Running stats */
		renderStats(gl);
	}
	
	
	/*
	 * reshape
	 *
	 * General function: Windows Callback for window reshape
	 *
	 * @param drawable The GLAutoDrawable to grab GL from.
	 * @param x The new x value for the display.
	 * @param y The new y value for the display.
	 * @param width The new width for the display.
	 * @param height The new height for the display.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		gl = drawable.getGL();
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(45f, (float)width/(float)height, 0.1f, 1000f);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/*
	 * displayChanged
	 *
	 * General function: Windows Callback for display change
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
	{
		init(drawable);
	}

	/*
	 * getElapsedTime
	 *
	 * General function: Calculates the engine specs, such as the FPS and PolyCount.
	 */
	private long getElapsedTime()
	{
		polyCount = 0;
		frameCount++;
		long currTime = Kernel.profiler.currentTime;
		long elapsedTime = currTime-lastFPSCheck;
		if(elapsedTime>=1000)
		{
			fps = frameCount;
			frameCount = 0;
			lastFPSCheck = Kernel.profiler.currentTime;
		}
		elapsedTime = currTime - lastTime;
		lastTime = currTime;
		return elapsedTime;
	}
	
	/*
	 * printAAStats
	 *
	 * General function: Prints out stats that have to deal with Multi-Sampling for anti-aliasing.
	 */
	private void printAAStats(GL gl)
	{
		int buf[] = new int[1];
		int sbuf[] = new int[1];
		
		String extensions = gl.glGetString(GL.GL_EXTENSIONS);     //Get all supported extensions
	    boolean multiTexturingSupported = (extensions.indexOf("GL_ARB_multitexture") !=-1);
	    if(!multiTexturingSupported) System.out.println("Multi-Texturing not supported...");
	    int[] maxTextureUnits = new int[1];
	    gl.glGetIntegerv(GL.GL_MAX_TEXTURE_UNITS, maxTextureUnits, 0);
	    int nbTextureUnits = maxTextureUnits[0];
	    System.out.println("Texture Units available = "+nbTextureUnits);

		gl.glGetIntegerv(GL.GL_SAMPLE_BUFFERS, buf, 0);
		System.out.println("number of sample buffers is "+buf[0]);
		gl.glGetIntegerv(GL.GL_SAMPLES, sbuf, 0);
		System.out.println("number of samples is "+sbuf[0]);
	}
	
	/*
	 * renderStats
	 *
	 * General function: Prints out a report of engine FPS and PolyCount.
	 */
	public void renderStats(GL gl)
	{
		int viewport[] = new int[4];
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix ();
		gl.glLoadIdentity();

		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		glu.gluOrtho2D(0, viewport[2], viewport[3], 0);
		gl.glDepthFunc(GL.GL_ALWAYS);
		
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glDisable(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_LIGHT0);
		
		gl.glColor3f(1f, 0f, 0f);
		
		gl.glRasterPos2f(15, 16);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "FPS: " + fps);
		
		gl.glRasterPos2f(15, 32);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Polygon(s): " + polyCount);
		
		int inc = 0;
		for(String s: info)
		{
			inc ++;
			if( !s.equals("") && s != null)
			{
				gl.glRasterPos2f(15, 36+(inc*16));
				glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, s);
			}
		}
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		
		gl.glDepthFunc(GL.GL_LESS);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix ();
	}
	
	/**
	 * getTextureLoader
	 *
	 * General Function: Returns a reference to TextureLoader.
	 */
	public TextureLoader getTextureLoader()
	{
		return textureLoader;
	}
	
	/**
	 * getGameStateManager
	 *
	 * General Function: Return a reference to GameStateManager.
	 */
	public GameStateManager getGameStateManager()
	{
		return stateManager;
	}
	
	/**
	 * getGLG
	 *
	 * General Function: Returns a reference to GLGraphics.
	 */
	public GLGraphics getGLG()
	{
		return g;
	}
	
	/**
	 * getGL
	 *
	 * General Function: Returns a reference to GL.
	 */
	public GL getGL()
	{
		return gl;
	}
	
	/**
	 * getGLU
	 * 
	 * General Function: Returns a reference to GLU.
	 */
	public GLU getGLU()
	{
		return glu;
	}
	
	/**
	 * getGLUT
	 *
	 * General Function: Returns a reference to GLUT.
	 */
	public GLUT getGLUT()
	{
		return glut;
	}
	
	/**
	 * loadImage
	 *
	 * General Function: Easy access method for other classes to load an image as a Texture.
	 */
	public Texture loadImage(String resourceName)
	{
		try
		{
			return textureLoader.getTexture(resourceName, gl, glu);
		}
		catch(java.io.IOException e)
		{
			System.out.println("err "+resourceName);
			//e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * loadImageFromBuffer
	 *
	 * General Function: Loads a Texture from a BufferedImage.
	 *
	 * @param bufImg The BufferedImage to load as a Texture.
	 */
	public Texture loadImageFromBuffer(BufferedImage bufImg)
	{
		return textureLoader.getTexture(bufImg, gl, glu);
	}
	
	/**
	 * addFog
	 *
	 * General Function: Adds OpenGL fog to the 3d scene.
	 *
	 * @param gl The GL instance.
	 */
	public void addFog(GL gl)
	{
		gl.glFogi(GL.GL_FOG_MODE, GL.GL_EXP);
		gl.glFogfv(GL.GL_FOG_COLOR, org.cart.igd.util.ColorRGBA.Blue.rgba, 0);
		gl.glFogf(GL.GL_FOG_DENSITY, 0.005f); 
		gl.glFogf(GL.GL_FOG_START, 0f);  // start depth
		gl.glFogf(GL.GL_FOG_END, 5f);	   // end depth
		gl.glHint(GL.GL_FOG_HINT, GL.GL_NICEST);
		gl.glEnable(GL.GL_FOG);
	}
}
