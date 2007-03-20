package org.cart.igd.util;

import java.awt.Robot;
import java.awt.event.MouseEvent;

import org.cart.igd.math.Vector3f;

public class Camera
{
	private static final float PI_2 = (float)Math.PI/2.0f;
	
	private Vector3f eyePosition;
	private Vector3f lookAtPosition;
	private Vector3f viewVector;
	
	private Vector3f forwardVector;
	private Vector3f upVector;
	private Vector3f sideVector;
	
	private float pitch;
	private float yaw;
	
	private Robot robot;
	private boolean robotMove;
	
	private int screenCenterX;
	private int screenCenterY;
	private int windowCenterX;
	private int windowCenterY;
	
	public Camera(int screenWidth, int screenHeight)
	{
		setEyePosition(new Vector3f(0f, 0f, 0f));
		setViewVector(new Vector3f(0f, 1f, 5f));
		setForwardVector(new Vector3f());
		setUpVector(new Vector3f(0f, 1f, 0f));
		setSideVector(new Vector3f(1f, 0f, 0f));
		
		try
		{
			robot = new Robot();
		}
		catch(Exception e)
		{
			System.out.println("*** Can't instantiate robot ***");
			e.printStackTrace();
		}
		
		screenCenterX = screenWidth / 2;
		screenCenterY = screenHeight / 2;
	}
	
	public float getPitch() { return pitch; }
	
	public float getYaw() { return yaw; }
	
	public void centerMouse()
	{
		System.out.println("centerMouse()");
		System.out.println("screenCenterX: "+screenCenterX);
		System.out.println("screenCenterY: "+screenCenterY);
		robotMove = true;
		robot.mouseMove(screenCenterX, screenCenterY);
	}
	
	public Vector3f calculateMoveCameraHorizontal(float speed)
	{
		Vector3f direction = new Vector3f();
		direction.x = getViewVector().x - getEyePosition().x;
		direction.y = getViewVector().y - getEyePosition().y;
		direction.z = getViewVector().z - getEyePosition().z;
		
		direction.normalize();
		
		Vector3f newPosition = new Vector3f();
		newPosition.x = getEyePosition().x + direction.x * speed;
		newPosition.y = getEyePosition().y;
		newPosition.z = getEyePosition().z + direction.z * speed;
		
		return newPosition;
	}
	
	public Vector3f getViewDirection()
	{
		Vector3f direction = new Vector3f();
		direction.x = getViewVector().x - getEyePosition().x;
		direction.y = getViewVector().y - getEyePosition().y;
		direction.z = getViewVector().z - getEyePosition().z;
		return direction;
	}
	
	public void moveCameraHorizontal(float speed)
	{
		Vector3f direction = new Vector3f();
        direction.x = getViewVector().x - getEyePosition().x;
        direction.y = getViewVector().y - getEyePosition().y;
        direction.z = getViewVector().z - getEyePosition().z;

        direction.normalize();

        getEyePosition().x = getEyePosition().x + direction.x * speed;
        getEyePosition().z = getEyePosition().z + direction.z * speed;
        getViewVector().x = getViewVector().x + direction.x * speed;
        getViewVector().z = getViewVector().z + direction.z * speed;
	}
	
	public void moveCameraVertical(float speed)
	{
		getEyePosition().y = getEyePosition().y + speed;
		getViewVector().y = getViewVector().y + speed;
	}
	
	public void mouseMoved(MouseEvent mouseEvent, float rotateSpeed)
	{
		float deltaY = 0.0f;
		float rotateY = 0.0f;
		if(robotMove)
		{
			robotMove = false;
			return;
		}
		rotateY = windowCenterX - mouseEvent.getX();
		deltaY  = windowCenterY - mouseEvent.getY();

		getViewVector().y = getViewVector().y + deltaY * (rotateSpeed * 2);

		if((getViewVector().y - getEyePosition().y) > 10)
			getViewVector().y = getEyePosition().y + 10;

		if((getViewVector().y - getEyePosition().y) < -10)
            getViewVector().y = getEyePosition().y - 10;

		if(rotateY < 0)
			rotateView(0.0f, rotateSpeed * -rotateY, 0.0f);
		if(rotateY > 0)
			rotateView(0.0f, -rotateSpeed * rotateY, 0.0f);
		
		centerMouse();
	}
	
	public void positionCamera(	float positionX, float positionY, float positionZ,
								float viewX,     float viewY,     float viewZ,
								float upVectorX, float upVectorY, float upVectorZ)
	{
		getEyePosition().x = positionX;
		getEyePosition().y = positionY;
		getEyePosition().z = positionZ;

		getViewVector().x = viewX;
		getViewVector().y = viewY;
		getViewVector().z = viewZ;

		getUpVector().x = upVectorX;
		getUpVector().y = upVectorY;
		getUpVector().z = upVectorZ;
	}
	
	public void setWindowCenter(int x, int y)
	{
		System.out.println("setWindowCenter()");
		System.out.println("x: "+x+"   y: "+y);
		windowCenterX = x;
		windowCenterY = y;
	}
	
	public void strafeCamera(float speed)
	{
		Vector3f direction = new Vector3f();
		direction.x = getViewVector().x - getEyePosition().x;
		direction.y = getViewVector().y - getEyePosition().y;
		direction.z = getViewVector().z - getEyePosition().z;
		float radius = (float) Math.sqrt(direction.x * direction.x + direction.z * direction.z);
		float alpha = (float) Math.acos(direction.x / radius) + PI_2;

		if(direction.z<0)
		{
			getEyePosition().x = (float) (getEyePosition().x + Math.cos(alpha) * speed);
			getEyePosition().z = (float) (getEyePosition().z - Math.sin(alpha) * speed);
			getViewVector().x = (float) (getViewVector().x + Math.cos(alpha) * speed);
			getViewVector().z = (float) (getViewVector().z - Math.sin(alpha) * speed);
		}
		else
		{
			getEyePosition().x = (float) (getEyePosition().x + Math.cos(alpha) * -speed);
			getEyePosition().z = (float) (getEyePosition().z + Math.sin(alpha) * -speed);
			getViewVector().x = (float) (getViewVector().x + Math.cos(alpha) * -speed);
			getViewVector().z = (float) (getViewVector().z + Math.sin(alpha) * -speed);
		}
	}
	
	public void rotateView(float x, float y, float z)
	{
		Vector3f direction = new Vector3f();
		direction.x = getViewVector().x - getEyePosition().x;
		direction.y = getViewVector().y - getEyePosition().y;
		direction.z = getViewVector().z - getEyePosition().z;
		if(x!=0)
		{
			getViewVector().y = (float)(getEyePosition().y + Math.cos(x) * direction.y - Math.sin(x) * direction.z);
			getViewVector().z = (float)(getEyePosition().z + Math.sin(x) * direction.y + Math.cos(x) * direction.z);
		}
		if(y!=0)
		{
			getViewVector().x = (float)(getEyePosition().x + Math.cos(y) * direction.x - Math.sin(y) * direction.z);
			getViewVector().z = (float)(getEyePosition().z + Math.sin(y) * direction.x + Math.cos(y) * direction.z);
		}
		if(z!=0)
		{
			getViewVector().x = (float)(getEyePosition().x + Math.sin(z) * direction.y + Math.cos(z) * direction.x);
			getViewVector().y = (float)(getEyePosition().y + Math.cos(z) * direction.y - Math.sin(z) * direction.x);
		}
	}
	
	public void setPosition(float x, float y, float z)
	{
		getEyePosition().x = x;
		getEyePosition().y = y;
		getEyePosition().z = z;
	}
	
	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}
	
	public void setYaw(float yaw)
	{
		this.yaw = yaw;
	}

	public void computeViewMatrix(float deltaTime)
	{
		if((yaw >= 360.0f) || (yaw <= -360.0f)) yaw = 0.0f;
		if(pitch > 60.0f) pitch = 60.0f;
		if(pitch < -60.0f) pitch = -60.0f;
		
		float cosYaw = (float) Math.cos(Math.toRadians(yaw));
		float sinYaw = (float) Math.sin(Math.toRadians(yaw));
		float sinPitch = (float) Math.sin(Math.toRadians(pitch));
		float cosPitch = (float) Math.cos(Math.toRadians(pitch));

		getForwardVector().x = sinYaw * cosPitch;
		getForwardVector().y = sinPitch;
		getForwardVector().z = cosPitch * -cosYaw;

		setLookAtPosition(getEyePosition().add(getForwardVector()));

		setSideVector(Vector3f.cross(getForwardVector(), getUpVector()));
	}
	
	public void setEyePosition(Vector3f eyePosition)
	{
		this.eyePosition = eyePosition;
	}
	
	public Vector3f getEyePosition()
	{
		return eyePosition;
	}

	public void setForwardVector(Vector3f forwardVector)
	{
		this.forwardVector = forwardVector;
	}

	public Vector3f getForwardVector()
	{
		return forwardVector;
	}

	public void setLookAtPosition(Vector3f lookAtPosition)
	{
		this.lookAtPosition = lookAtPosition;
	}

	public Vector3f getLookAtPosition()
	{
		return lookAtPosition;
	}

	public void setSideVector(Vector3f sideVector)
	{
		this.sideVector = sideVector;
	}

	public Vector3f getSideVector()
	{
		return sideVector;
	}

	public void setUpVector(Vector3f upVector)
	{
		this.upVector = upVector;
	}

	public Vector3f getUpVector()
	{
		return upVector;
	}

	public void setViewVector(Vector3f viewVector)
	{
		this.viewVector = viewVector;
	}

	public Vector3f getViewVector()
	{
		return viewVector;
	}
}