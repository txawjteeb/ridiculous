package org.cart.igd;

import javax.media.opengl.glu.GLU;

import org.cart.igd.math.Vector3f;
import org.cart.igd.entity.Entity;

/**
 * Camera.java
 *
 * General Function: To set up a virtual camera at which OpenGL views the 3d evironment.
 */
public class Camera
{
	
	/* Camera's Position Vector. */
	private Vector3f cameraPos = new Vector3f();
	
	/* Camera's Up Vector. */
	private Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
	
	/* Distance of camera to player. */
	public float distance = 3f;
	
	/* Actual camera height. */
	public float posHeight = 0f;
	
	/* Actual camera distance. */
	public float posDistance = 0f;
	
	/* Camera facing offset. */
	public float facingOffset = 0.0f;
	
	/* Approx 45 degrees. */
	public float verticalAngle = .785f;

	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Camera.
	 *
	 * @param player The player entity reference.
	 * @param distance The distance from the camera to the player.
	 * @param degCameraAngle The camera angle in degrees.
	 */
	public Camera(Entity player, float distance, float degCameraAngle)
	{
		cameraPos = new Vector3f();
		cameraUp = new Vector3f(0f, 2f, 0f);
		this.verticalAngle = degCameraAngle*.0174532925f;
		this.distance = distance;
	}
	
	/**
	 * arcRotateY
	 *
	 * General Function: Rotate the camera around the player.
	 *
	 * @param amt The amount to arc rotate.
	 */
	public void arcRotateY(float amt)
	{
		facingOffset+=amt;
	}
	
	/**
	 * zoom
	 *
	 * General Function: Zooms the camera closer or farther from the player.
	 *
	 * @param amt The amount to zoom the camera.
	 */
	public void zoom(float amt)
	{
		distance += amt;
	}
	
	/**
	 * moveToBackView
	 * 
	 * General Function: Return the camera to player's back view position.
	 *
	 * @param speed The speed to move the camera.
	 */
	public void moveToBackView(float speed)
	{
		if(facingOffset>0f && facingOffset<180f)
		{
			facingOffset-=speed;
		}
		else if(facingOffset>180f && facingOffset<360f)
		{
			facingOffset+=speed;
		}
		if(facingOffset<speed && facingOffset>-speed)
		{
			facingOffset = 0f;
		}
		
		/* Clamp facing offset */
		if(facingOffset<0f)
		{
			facingOffset+=360f;
		}
		else if(facingOffset>=360f)
		{
			facingOffset-=360f;
		}
	}
	
	/**
	 * setCameraHeight
	 *
	 * General Function: Sets the Camera's height.
	 *
	 * @param height The height of the camera.
	 */
	public void setCameraHeight(float height)
	{
		posHeight = height;
	}
	
	/**
	 * getCameraHeight
	 *
	 * General Function: Returns the Camera's height.
	 */
	public float getCameraHeight()
	{
		return posHeight;
	}
	
	/**
	 * setDistance
	 *
	 * General Function: Sets the Camera's distance.
	 *
	 * @param distance The distance of the camera.
	 */
	public void setDistance(float distance)
	{
		this.distance = distance;
	}
	
	/**
	 * getDistance
	 *
	 * General Function: Returns the Camera's distance.
	 */
	public float getDistance()
	{
		return distance;
	}
	
	/**
	 * setFacingOffset
	 *
	 * General Function: Sets the facing offset.
	 *
	 * @param facingOffset The facing offset of the camera.
	 */
	public void setFacingOffset(float facingOffset)
	{
		this.facingOffset = facingOffset;
	}
	
	/**
	 * getFacingOffset
	 *
	 * General Function: Returns the facing offset.
	 */
	public float getFacingOffset()
	{
		return facingOffset;
	}
	
	/**
	 * changeVerticalAngleDeg
	 *
	 * General Function: Change the verticalAngle by given amount of degrees.
	 *
	 * @param changeDeg The angle in degrees to change the verticle angle by.
	 */
	public void changeVerticalAngleDeg(float changeDeg)
	{
		verticalAngle += changeDeg*.0174532925f;
	}
	
	/**
	 * setVerticalAngleDeg
	 *
	 * General Function: Set the vertical angle.
	 *
	 * @param setDeg The angle in degress to set the verticle angle to.
	 */
	public void setVerticalAngleDeg(float setDeg)
	{
		verticalAngle = setDeg*.0174532925f;
	}
	
	/**
	 * lookAt
	 *
	 * General Function: Adjusts the GLU camera to Camera's data.
	 *
	 * @param glu The GLU instance to apply the camera transform to.
	 * @param player The player entity reference.
	 */
	public void lookAt(GLU glu, Entity player)
	{
		posHeight = (float)Math.sin(verticalAngle)*distance;
		posDistance = (float)Math.cos(verticalAngle)*distance;
		
		cameraPos.x = player.position.x + ( posDistance * (float)Math.cos((player.facingDirection+facingOffset+180f) * 0.0174f) );
		cameraPos.y = posHeight + player.position.y;
		cameraPos.z = player.position.z + ( posDistance * (float)Math.sin((player.facingDirection+facingOffset+180f) * 0.0174f) );
		
		try
		{
			glu.gluLookAt
			(
				cameraPos.x, cameraPos.y, cameraPos.z,
				player.position.x, player.position.y, player.position.z,
				cameraUp.x, cameraUp.y, cameraUp.z
			);
		}
		catch(Exception e)
		{
		}
	}
}