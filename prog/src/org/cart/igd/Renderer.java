package org.cart.igd;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import java.awt.event.KeyEvent;
import com.sun.opengl.util.GLUT;

//import org.cart.igd.math.Vector3f;
import org.cart.igd.input.UserInput;
import org.cart.igd.opengl.*;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.math.Vector3f;
import org.cart.igd.util.TextureLoader;
import org.cart.igd.util.Texture;

public class Renderer implements GLEventListener
{
	public TextureLoader textureLoader;
	public GLUquadric quadratic;
	public GLU glu = new GLU();
	public GLUT glut = new GLUT();
	public GL gl;
	
	private final float CAMERA_SPEED = 1f;
	public HeightMap heightMap;
	public Camera camera;
	public Texture landTex, skyTex;
	public OBJModel martin;
	public SkyDome skyDome;
	
	public int frameCount = 0;
	public int fps = 0;
	public long lastFPSCheck;
	public int polyCount = 0;
	private int pOffsetY = 0;
	
	private Vector3f[] lightVec = new Vector3f[] {
		new Vector3f(0.0f, 0.0f, 2.0f),	// pos
		new Vector3f(1.0f, 1.0f, 1.0f),	// amb
		new Vector3f(0.5f, 0.5f, 0.5f),	// diff
	};
	
	public Renderer()
	{
		textureLoader = new TextureLoader();
	}
	
	private void handleInput()
	{
		if(UserInput.keys[java.awt.event.KeyEvent.VK_ESCAPE]) Driver.display.stop();
		
		if(UserInput.keys[KeyEvent.VK_W])
			camera.moveCamera(CAMERA_SPEED);
			//camera.stepForward();
		else if(UserInput.keys[KeyEvent.VK_S])
			camera.moveCamera(-CAMERA_SPEED);
			//camera.stepBackward();
		
		if(UserInput.keys[KeyEvent.VK_D])
			camera.rotatePosition(CAMERA_SPEED/8);
			//camera.turnRight();
		else if(UserInput.keys[KeyEvent.VK_A])
			camera.rotatePosition(-CAMERA_SPEED/8);
			//camera.turnLeft();
		
		if(UserInput.keys[KeyEvent.VK_Q])
			camera.strafeCamera(-CAMERA_SPEED);
		else if(UserInput.keys[KeyEvent.VK_E])
			camera.strafeCamera(CAMERA_SPEED);
		
		if(UserInput.keys[KeyEvent.VK_SPACE])
		{
			pOffsetY = 10;
		}
	}
	
	public void display(GLAutoDrawable drawable)
	{
		polyCount = 0;
		frameCount++;
		if(System.currentTimeMillis()-lastFPSCheck>=1000)
		{
			fps = frameCount;
			frameCount = 0;
			lastFPSCheck = System.currentTimeMillis();
		}
		
		handleInput();
		camera.update();
		
		synchronized(camera.matrixLock) { camera.currRotation.get(camera.matrix); }
		
		gl = drawable.getGL();
		gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		
		camera.render(gl, glu);
		
		gl.glPushMatrix();
		gl.glTranslatef(camera.cameraPos.x, camera.cameraPos.y, camera.cameraPos.z+3);
		martin.draw(gl);
		gl.glPopMatrix();

		gl.glDisable(GL.GL_CULL_FACE);
		skyDome.render(gl);
		gl.glDisable(GL.GL_CULL_FACE);

		heightMap.render(gl);
		
		renderStats(gl);
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		gl = drawable.getGL();
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(45f, (float)width/(float)height, 0.1f, 20000f);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		camera.reshape((float)width, (float)height);
	}
	
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
	{
		init(drawable);
	}
	
	public void init(GLAutoDrawable drawable)
	{
		gl = drawable.getGL();
		
		int buf[] = new int[1];
		int sbuf[] = new int[1];
		
		String extensions = gl.glGetString(GL.GL_EXTENSIONS);     //Get all supported extensions
	    boolean multiTexturingSupported = (extensions.indexOf("GL_ARB_multitexture") !=-1);
	    if(!multiTexturingSupported) System.out.println("Multi-Texturing not supported...");
	    int[] maxTextureUnits = new int[1];
	    gl.glGetIntegerv(GL.GL_MAX_TEXTURE_UNITS, maxTextureUnits, 0);
	    int nbTextureUnits = maxTextureUnits[0];
	    System.out.println("Texture Units available = "+nbTextureUnits);
		
		gl.glClearColor( 0.17f, 0.65f, 0.92f, 1.0f );
		gl.glGetIntegerv(GL.GL_SAMPLE_BUFFERS, buf, 0);
		System.out.println("number of sample buffers is "+buf[0]);
		gl.glGetIntegerv(GL.GL_SAMPLES, sbuf, 0);
		System.out.println("number of samples is "+sbuf[0]);
		
		gl.glClearDepth(1f);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glFrontFace(GL.GL_CCW);
	    gl.glCullFace(GL.GL_BACK);
	    gl.glEnable(GL.GL_CULL_FACE); 
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glShadeModel(GL.GL_SMOOTH);
		//gl.glEnable(GL.GL_POLYGON_SMOOTH);
		//gl.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl.glEnable(GL.GL_MULTISAMPLE);
		
		martin = new OBJModel(gl, "models/scale_model_km");
		try
		{
			skyTex = Display.renderer.textureLoader.getTexture("data/images/sky.png", gl, Display.renderer.glu);
			landTex = Display.renderer.textureLoader.getTexture("data/images/land.jpg", gl, Display.renderer.glu);
		}
		catch(java.io.IOException e)
		{
			e.printStackTrace();
		}
		
		skyDome = new SkyDome(0, 90, 10000f, new ColorRGBA(70, 100, 128), gl);
		heightMap = new HeightMap(gl, "data/models/Terrain.raw", "data/images/land_texture.png", 2048, 12, 0.3f);
		camera = new Camera(Display.getScreenWidth(), Display.getScreenHeight());
		camera.init();
		
		addLight(gl);
		addFog(gl);
		
		lastFPSCheck = System.currentTimeMillis();
	}
	
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
		
		gl.glRasterPos2f(15, 18);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "FPS: " + fps);
		
		gl.glRasterPos2f(15, 36);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Polygon(s): " + polyCount);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		
		gl.glDepthFunc(GL.GL_LESS);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix ();
	}
	
	public void addLight(GL gl)
	{
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, new float[] {0.1f, 0.1f, 0.1f, 1.0f}, 0);
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, ColorRGBA.White.getRGBA(), 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, ColorRGBA.White.getRGBA(), 0);
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[] {1.0f, 0.5f, 1.0f, 0.0f}, 0);
	}
	
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
}
