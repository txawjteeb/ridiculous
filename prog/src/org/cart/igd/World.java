package org.cart.igd;

/*
import org.cart.igd.math.Vector3f;
import org.cart.igd.math.Vector4f;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.GLUT;

import java.util.List;

public class World
{
	private static final float ATTENUATION_CONSTANT = 1.0f;
	private static final float ATTENUATION_LINEAR = 0.0f;
	private static final float ATTENUATION_QUADRATIC = 0.0f;
	private static final int MIN_DISTANCE = 50;
	private static final int MAX_DISTANCE = 500;
	private static final int LIGHT_STEP = 32;
	
	private float angle = 135.0f;
	private float radians = (float)Math.PI/4.0f;
	private int mouseY = 150;
	
	private Vector3f cameraPos;
	private Vector3f cameraLook;
	private Vector4f lightPos, diffuse;
	private float attenuation_constant = ATTENUATION_CONSTANT;
	private float attenuation_linear = ATTENUATION_LINEAR;
	private float attenuation_quadratic = ATTENUATION_QUADRATIC;
	
	private double start;
	private int countFPS, fps;
	
	private boolean drawModel = true;
	private boolean gouraudShading = true;
	private boolean lighting = true;
	
	private Sprite3D player;
	
	public World()
	{
		cameraPos = new Vector3f();
		cameraLook = new Vector3f();
		
		lightPos = new Vector4f( 32.0f, 0.0f, 0.0f, 1.0f );
		diffuse = new Vector4f( 1.0f, 1.0f, 1.0f, 1.0f );
	}
	
	public boolean init(GL gl, GLU glu)
	{
		gl.glViewport(0, 0, Driver.display.getScreenWidth(), Driver.display.getScreenHeight());
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(54.0, Driver.display.getScreenWidth()/Driver.display.getScreenHeight(), 1.0, 1000.0);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		return true;
	}
	
	public void render(GL gl, GLU glu, GLUT glut)
	{
		radians = (float)Math.PI * (angle-90.0f) / 180.0f;
		
		cameraPos.x = cameraLook.x + (float)Math.sin(radians) * mouseY;
		cameraPos.z = cameraLook.z + (float)Math.cos(radians) * mouseY;
		cameraPos.y = cameraLook.y + mouseY / 2.0f;
		
		cameraLook.x = 0.0f;
		cameraLook.y = 0.0f;
		cameraLook.z = 0.0f;
		
		if(gouraudShading)
			gl.glShadeModel(GL.GL_SMOOTH);
		else
			gl.glShadeModel(GL.GL_FLAT);
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuse.toArray(), 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos.toArray(), 0);
		gl.glLightf(GL.GL_LIGHT0, GL.GL_CONSTANT_ATTENUATION, attenuation_constant);
		gl.glLightf(GL.GL_LIGHT0, GL.GL_LINEAR_ATTENUATION, attenuation_linear);
		gl.glLightf(GL.GL_LIGHT0, GL.GL_QUADRATIC_ATTENUATION, attenuation_quadratic);
		gl.glEnable(GL.GL_LIGHT0);
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		glu.gluLookAt(cameraPos.x, cameraPos.y, cameraPos.z, cameraLook.x, cameraLook.y, cameraLook.z, 0f, 1f, 0f);
		
		if(player!=null && drawModel)
		{
			if(lighting)
				gl.glEnable(GL.GL_LIGHTING);
			else
				gl.glDisable(GL.GL_LIGHTING);
			gl.glPushMatrix();
			player.render(gl);
			gl.glPopMatrix();
		}
		
		drawInfo(gl, glu, glut);
	}
	
	private void drawInfo(GL gl, GLU glu, GLUT glut)
	{
		float[] globalAmbientLight = new float[4];
		gl.glGetFloatv(GL.GL_LIGHT_MODEL_AMBIENT, globalAmbientLight, 0);
		
		gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, 1);
		byte[] localViewer = new byte[1];
		gl.glGetBooleanv(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, localViewer, 0);
		
		byte[] twoSided = new byte[1];
		gl.glGetBooleanv(GL.GL_LIGHT_MODEL_TWO_SIDE, twoSided, 0);

		// calculate FPS
		if(start==0)
			start = System.currentTimeMillis();

		if(System.currentTimeMillis()-start>=500)
		{
		  fps		= countFPS << 1;
		  start		= 0;
		  countFPS	= 0;
		}
		countFPS++;

		// print info
		int viewport[] = new int[4];
		int y = 0;
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix ();
		gl.glLoadIdentity();

		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		glu.gluOrtho2D(0, viewport[2], viewport[3], 0);
		gl.glDepthFunc(GL.GL_ALWAYS);
		gl.glColor3f (1,1,1);
		
		gl.glRasterPos2f(15, y += 18);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "FPS: " + fps);
	
		if(player != null)
		{
			gl.glRasterPos2f(15, y += 18);
			glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Number of polygons: " + player.getNumberOfPolygons());
		}

		gl.glRasterPos2f(15, y += 36);
		glut.glutBitmapString(gl, GLUT.BITMAP_HELVETICA_12, "Global ambient light: [ " + globalAmbientLight[0] + " / " + globalAmbientLight[1] + " / " + globalAmbientLight[2] + " / " + globalAmbientLight[3] + "]");

		gl.glRasterPos2f(15, y += 18);
		glut.glutBitmapString(gl, GLUT.BITMAP_HELVETICA_12, "Local viewer: " + localViewer[0]);

		gl.glRasterPos2f(15, y += 18);
		glut.glutBitmapString(gl, GLUT.BITMAP_HELVETICA_12, "Two-sided lighting: " + twoSided[0]);
		
		gl.glRasterPos2f(15, y += 18);
		if (drawModel) glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Model shown (F3)");
		else glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Model hidden (F3)");

		gl.glRasterPos2f(15, y += 18);
		if (gouraudShading) glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Gouraud Shading (F4)");
		else glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Flat Shading (F4)");

		gl.glRasterPos2f(15, y += 18);
		if (lighting) glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Lighting enabled (F5)");
		else glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Lighting disabled (F5)");
		
		gl.glRasterPos2f(15, y += 18);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Constant Attenuation: " + attenuation_constant + " (U/J)");

		gl.glRasterPos2f(15, y += 18);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Linear Attenuation: " + attenuation_linear + " (I/K)");

		gl.glRasterPos2f(15, y += 18);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Quadriatic Attenuation: " + attenuation_quadratic + " (O/L)");
		
		gl.glRasterPos2f(15, y += 18);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Model State: " + player.modelStateIndex + " (B)");
		
		gl.glDepthFunc(GL.GL_LESS);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix ();
	}
	
	public void reshape(int x, int y, int width, int height, GL gl, GLU glu)
	{
		if(height==0) height = 1;
		
		gl.glViewport(x, y, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(54.0, width/height, 1.0, 1000.0);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public void turnLeft()
	{
		if(angle-5<0) angle += 360.0f;
		angle -= 5;
	}
	
	public void turnRight()
	{
		if(angle+5>360) angle -= 360.0f;
		angle += 5;
	}
	
	public void zoomIn()
	{
		mouseY -= 5;
		if(mouseY<MIN_DISTANCE) mouseY = MIN_DISTANCE;
	}
	
	public void zoomOut()
	{
		mouseY += 5;
		if(mouseY>MAX_DISTANCE) mouseY = MAX_DISTANCE;
	}
	
	public void toggleDrawModel()
	{
		drawModel = !drawModel;
	}
	
	public void toggleGouraudShading()
	{
		gouraudShading = !gouraudShading;
	}
	
	public void toggleLighting()
	{
		lighting = !lighting;
	}
	
	public void lightLeft()
	{
		lightPos.z += LIGHT_STEP;
	}
	
	public void lightRight()
	{
		lightPos.z -= LIGHT_STEP;
	}
	
	public void lightUp()
	{
		lightPos.y += LIGHT_STEP;
	}
	
	public void lightDown()
	{
		lightPos.y -= LIGHT_STEP;
	}
	
	public void increaseLinear()
	{
		attenuation_linear += 0.01;
	}
	
	public void decreaseLinear()
	{
		attenuation_linear -= 0.01;
		if(attenuation_linear<0) attenuation_linear = 0;
	}
	
	public void increaseConstant()
	{
		attenuation_constant += 0.1;
	}
	
	public void decreaseConstant()
	{
		attenuation_constant -= 0.1;
		if(attenuation_constant<0) attenuation_constant = 0;
	}
	
	public void increaseQuadric()
	{
		attenuation_quadratic += 0.01;
	}
	
	public void decreaseQuadric()
	{
		attenuation_quadratic -= 0.01;
		if(attenuation_quadratic<0) attenuation_quadratic = 0;
	}
	
	public List getEntities()
	{
		return null;
	}
	
	
}
*/