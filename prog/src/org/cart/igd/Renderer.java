/*
 * Renderer
 *
 * {version info}
 * March 31, 2007 Spencer Allen  (major revision, comments, general function )
 * April 01, 2007 Vitaly Maximov (comments/suggestions(NOTE) for future revision)
 *
 * {copyright}
 */
package org.cart.igd;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import java.awt.event.KeyEvent;
import com.sun.opengl.util.GLUT;

import org.cart.igd.gl2d.GLGraphics;
import org.cart.igd.states.*;
import org.cart.igd.util.TextureLoader;
import org.cart.igd.util.Texture;

public class Renderer implements GLEventListener
{
	private GameStateManager stateManager;
	private TextureLoader textureLoader;

	private GLU glu = new GLU();
	private GLUT glut = new GLUT();
	private GLGraphics g;
	private GL gl;
	
	private float lightAmbient[] = {0.2f, 0.2f, 0.2f};		// Ambient Light is 20% white
    private float lightDiffuse[] = {1.0f, 1.0f, 1.0f};		// Diffuse Light is white
    private float lightPosition[] = {0.0f, 10.0f, 0.0f};	// Position is somewhat in front of screen
	
	public int frameCount = 0;
	public int fps = 0;
	public long lastFPSCheck;
	public int polyCount = 0;
	public long lastTime;
	
	public static String info[]= {
		"","","","","",
	};
	
	
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
	 */
	public void init(GLAutoDrawable drawable)
	{
		gl = drawable.getGL();
		
		//printAAStats(gl);	// Print out the stats for Multi-Sampling and buffers

		gl.glClearColor( 0f, 0f, 0f, 1f );	// Background
		gl.glClearDepth(1.0f);				// Depth Buffer Setup
		gl.glClearStencil(0);				// Clear the Stencil Buffer to 0
		
		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, new float[] {0.25f,0.25f,0.25f,1f}, 0 );
		gl.glShadeModel(GL.GL_SMOOTH);		// Enable Smooth Shading
		gl.glDepthFunc(GL.GL_LEQUAL);		// Depth Testing Type
		gl.glFrontFace(GL.GL_CCW);
	    gl.glCullFace(GL.GL_BACK);
	    gl.glEnable(GL.GL_CULL_FACE);		// Enable Culling of Faces
		gl.glEnable(GL.GL_DEPTH_TEST);		// Enable Depth Testing
		gl.glEnable(GL.GL_AUTO_NORMAL);		// Auto-Normal Lighting
		gl.glEnable(GL.GL_NORMALIZE);		// Normalize for Lighting
		gl.glEnable(GL.GL_MULTISAMPLE);		// Enable Multi-Sampling
		
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		initLighting(gl);
		
		/* Create the GLGraphics object for rendering 2D GUI. */
		g = new GLGraphics(gl,glu,glut);
		
		initGameStates(gl);

		/* Set initial time variable for FPS calculations. */
		lastFPSCheck = System.currentTimeMillis();
	}
	
	/** 
	 * Initialize Game States 
	 * check for completion of game state before attempting to update 
	 **/
	public void initGameStates(GL gl){
		stateManager.addGameState(new MenuState(gl),"MenuState");
		stateManager.addGameState(new InGameState(gl),"InGameState");
		stateManager.setCurrentState("MenuState");
		stateManager.initStates(gl, glu);
	}
	
	
	public GameStateManager getStateManager(){
		return stateManager;
	}
	
	/** 
	 * Enable and setup lighting 
	 **/
	private void initLighting(GL gl)
	{
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_LIGHTING);
		
		float[] ambientLight	= new float[] { 0.2f, 0.2f, 0.2f, 1.0f };
		float[] diffuseLight	= new float[] { 0.8f, 0.8f, 0.8f, 1.0f };
		float[] specularLight	= new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
		float[] position		= new float[] { -1.5f, 1.0f, -4.0f, 1.0f };
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specularLight, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
	}
	
	
	/*
	 * display
	 *
	 * General function: Windows Callback for drawing every frame
	 */
	public void display(GLAutoDrawable drawable)
	{
		gl = drawable.getGL();	// Update GL instance
		
		long elapsedTime = getElapsedTime();	// calculate elapsed time
		
		/* Call current game state methods */
		GameState currentState = stateManager.getCurrentState();
		if(currentState.changeState){
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
		long currTime = System.currentTimeMillis();
		long elapsedTime = currTime-lastFPSCheck;
		if(elapsedTime>=1000)
		{
			fps = frameCount;
			frameCount = 0;
			lastFPSCheck = System.currentTimeMillis();
		}
		elapsedTime = currTime - lastTime;
		lastTime = currTime;
		return elapsedTime;
	}
	
	
	/*
	 * printAAStats
	 *
	 * Expected Input: GL reference object.
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
		for(String s: info){
			inc ++;
			if( !s.equals("") && s != null){
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
	
	
	public TextureLoader getTextureLoader()
	{
		return textureLoader;
	}
	
	
	public GameStateManager getGameStateManager()
	{
		return stateManager;
	}
	
	
	public GLGraphics getGLG()
	{
		return g;
	}
	
	
	public GL getGL()
	{
		return gl;
	}
	
	
	public GLU getGLU()
	{
		return glu;
	}
	
	
	public GLUT getGLUT()
	{
		return glut;
	}
	
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
	
	/*
	public void addFog(GL gl)
	{
		gl.glFogi(GL.GL_FOG_MODE, GL.GL_EXP);
		gl.glFogfv(GL.GL_FOG_COLOR, ColorRGBA.Gray.rgba, 0);
		gl.glFogf(GL.GL_FOG_DENSITY, 0.0005f); 
		gl.glFogf(GL.GL_FOG_START, 0f);  // start depth
		gl.glFogf(GL.GL_FOG_END, 5f);	   // end depth
		gl.glHint(GL.GL_FOG_HINT, GL.GL_NICEST);
		gl.glEnable(GL.GL_FOG);
	}
	*/
}
