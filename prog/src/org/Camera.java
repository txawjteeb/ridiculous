package org.cart.igd;

import java.awt.Point;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.GL;

import org.cart.igd.math.ArcBall;
import org.cart.igd.math.Matrix4f;
import org.cart.igd.math.Quat4f;
import org.cart.igd.math.Vector3f;

public class Camera
{
	//private final float PI_180 = (float)(Math.PI/180.0f);
	private final static float SPEED = 0.6f;   // for camera movement
	//private final static float LOOK_AT_DIST = 50.0f;
	//private final static float Z_POS = 9.0f;
	private final static float ANGLE_INCR = 2.85f;   // degrees
	
	public Vector3f cameraPos = new Vector3f();
	public Vector3f cameraView = new Vector3f();
	public Vector3f cameraUp = new Vector3f();
	public Vector3f cameraRot = new Vector3f();
	public Point lastDragPoint = new Point(0,0);
	
	public ArcBall arcBall;
	public Matrix4f lastRotation = new Matrix4f();
	public Matrix4f currRotation = new Matrix4f();
	public final Object matrixLock = new Object();
	public float[] matrix = new float[16];
	
	public float angle;
	public float viewAngle;
	public float xStep, zStep;
	public int x, y;
	
	public Camera(int w, int h)
	{
		arcBall = new ArcBall(w, h);
		
		cameraPos.x = 0.0f;
		cameraPos.y = Display.renderer.heightMap.interpolateGridHeight(0f, 4f, 1)+10f;
		cameraPos.z = 0.0f;
		
		cameraView.x = 0f;
		cameraView.y = cameraPos.y;
		cameraView.z = 4.0f;
		
		cameraUp.x = 0f;
		cameraUp.y = 1.0f;
		cameraUp.z = 0f;
		
		x = 0;
		y = 0;
		
		//viewAngle = -90.0f;
		//xStep = (float)Math.cos(Math.toRadians((double)viewAngle));
		//zStep = (float)Math.sin(Math.toRadians((double)viewAngle));
		
		//cameraView.x = cameraPos.x + (LOOK_AT_DIST*xStep);
		//cameraView.y = 0f;
		//cameraView.z = cameraPos.z + (LOOK_AT_DIST*zStep);
	}
	
	public void init()
	{
		lastRotation.setIdentity();
		currRotation.setIdentity();
		currRotation.get(matrix);
	}
	
	public void reshape(float w, float h)
	{
		arcBall.setBounds(w, h);
	}
	
	public void update()
	{
		//cameraView.x = cameraPos.x + (xStep*LOOK_AT_DIST);
		//cameraView.z = cameraPos.z + (zStep*LOOK_AT_DIST);
	}
	
	public void render(GL gl, GLU glu)
	{
		try
		{
			cameraView.y = cameraPos.y = Display.renderer.heightMap.interpolateGridHeight(cameraPos.x, cameraPos.z, 1)+3f;
			glu.gluLookAt(cameraPos.x, cameraPos.y, cameraPos.z, cameraView.x, cameraView.y, cameraView.z, cameraUp.x, cameraUp.y, cameraUp.z);
			gl.glRotatef(cameraRot.x, 1f, 0f, 0f);
			//gl.glRotatef(cameraRot.y, 0f, 1f, 0f);
			gl.glRotatef(cameraRot.y, 0f, 0f, 1f);
		}
		catch(Exception e) {}
	}
	
	public void moveCamera(float speed)
	{
		Vector3f v = cameraView.subtract(cameraPos);
		cameraPos.x += v.x*speed;
		cameraPos.z += v.z*speed;
		cameraView.x += v.x*speed;
		cameraView.z += v.z*speed;
	}
	
	public void rotateView(float speed)
	{
		Vector3f v = cameraView.subtract(cameraPos);
		cameraView.z = (float)(cameraView.z+Math.sin(speed)*v.x+Math.cos(speed)*v.z);
		cameraView.x = (float)(cameraView.x+Math.cos(speed)*v.x-Math.sin(speed)*v.z);
	}
	
	public void rotatePosition(float speed)
	{
		if(speed<0) angle += ANGLE_INCR;
		else if(speed>0) angle -= ANGLE_INCR;
		if(angle==360f) angle = 0f;
		else if(angle==0f) angle = 359f;
		Vector3f v = cameraPos.subtract(cameraView);
		cameraPos.z = (float)(cameraView.z+Math.sin(speed)*v.x+Math.cos(speed)*v.z);
		cameraPos.x = (float)(cameraView.x+Math.cos(speed)*v.x-Math.sin(speed)*v.z);
	}
	
	public void strafeCamera(float speed)
	{
		Vector3f vec = cameraView.subtract(cameraPos);
		Vector3f orthoVec = new Vector3f();
		
		orthoVec.x = -vec.z;
		orthoVec.z = vec.x;
		
		cameraPos.x += orthoVec.x*speed;
		cameraPos.z += orthoVec.z*speed;
		
		cameraView.x += orthoVec.x*speed;
		cameraView.z += orthoVec.z*speed;
	}
	
	public void turnLeft()
	{
		viewAngle -= ANGLE_INCR;
		xStep = (float)Math.cos(Math.toRadians(viewAngle));
		zStep = (float)Math.sin(Math.toRadians(viewAngle));
	}
	
	public void turnRight()
	{
		viewAngle += ANGLE_INCR;
		xStep = (float)Math.cos(Math.toRadians(viewAngle));
		zStep = (float)Math.sin(Math.toRadians(viewAngle));
	}
	
	public void mouseMove(Point p)
	{
		/*
		int mid_x = Display.getScreenWidth();// >> 1;
		int mid_y = Display.getScreenHeight() >> 1;
		
		//if(p.x>Display.getScreenHeight()/2)
			
		float angle_y = 0.0f;
		float angle_z = 0.0f;
		
		if(p.x==mid_x && p.y==mid_y) return;
		//org.cart.igd.input.UserInput.robot.mouseMove(Display.frame.getX()+Display.getScreenWidth()/2, Display.frame.getY()+Display.getScreenHeight()/2);
		
		angle_y = (float)(mid_x-p.x)/3000f;
		angle_z = (float)(mid_y-p.y)/1000f;
		
		cameraView.y += angle_z;
		
		if(cameraView.y>3.5f) cameraView.y = 3.5f;
		if(cameraView.y<0.4f) cameraView.y = 0.4f;
		
		rotatePosition(-angle_y);
		*/
	}
	
	public void stepForward()
	{
		cameraPos.x += xStep * SPEED;
		cameraPos.z += zStep * SPEED;
	}
	
	public void stepBackward()
	{
		cameraPos.x -= xStep * SPEED;
		cameraPos.z -= zStep * SPEED;
	}
	
	public void strafeLeft()
	{
		cameraPos.x += zStep * SPEED;
		cameraPos.z -= xStep * SPEED;
	}
	
	public void strafeRight()
	{
		cameraPos.x -= zStep * SPEED;
		cameraPos.z += xStep * SPEED;
	}
	
	public void resetRotation()
	{
		synchronized(matrixLock)
		{
			lastRotation.setIdentity();
			currRotation.setIdentity();
		}
	}
	
	public void startDrag(Point p)
	{
		synchronized(matrixLock)
		{
			lastRotation.set(currRotation);
		}
		arcBall.click(p);
	}
	
	public void drag(Point p)
	{
		Quat4f q = new Quat4f();
		arcBall.drag(p,q);
		synchronized(matrixLock)
		{
			currRotation.setRotation(q);
			currRotation.mul(currRotation,lastRotation);
		}
	}
}