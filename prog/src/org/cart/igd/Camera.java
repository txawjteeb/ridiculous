package org.cart.igd;

import java.awt.Point;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.GL;

import org.cart.igd.math.Vector3f;
import org.cart.igd.entity.Entity;

import org.cart.igd.input.*;
import java.awt.event.MouseEvent;

public class Camera
{
	private Entity player;
	private Vector3f cameraPos = new Vector3f();
	private Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
	
	public float distance = 3f;
	public float posHeight = 0f;
	public float posDistance = 0f;
	
	public float facingOffset = 0.0f;
	public float verticalAngle = .785f;//aprox 45degrees
	
	
	
	public Camera(Entity player, float distance, float degCameraAngle)
	{
		this.player = player;
		cameraPos = new Vector3f();
		cameraUp = new Vector3f(0f, 2f, 0f);
		this.verticalAngle = degCameraAngle*.0174532925f;
		this.distance = distance;
	}
	
	/** rotate the camera around the charater*/
	public void arcRotateY(float amt)
	{
		facingOffset+=amt;
	}
	
	public void zoom(float amt){
		distance += amt;
	}
	
	/** return the camera to player's back view position*/
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
		
		/** Clamp facing offset */
		if(facingOffset<0f)
		{
			facingOffset+=360f;
		}
		else if(facingOffset>=360f)
		{
			facingOffset-=360f;
		}
	}
	
	public void setCameraHeight(float height)
	{
		posHeight = height;
	}
	
	public float getCameraHeight()
	{
		return posHeight;
	}
	
	public void setDistance(float distance)
	{
		this.distance = distance;
	}
	
	public float getDistance()
	{
		return distance;
	}
	
	public void setFacingOffset(float facingOffset)
	{
		this.facingOffset = facingOffset;
	}
	
	public float getFacingOffset()
	{
		return facingOffset;
	}
	
	/** change the verticalAngle by given amount of degrees */
	public void changeVerticalAngleDeg(float changeDeg){
		verticalAngle += changeDeg*.0174532925f;
	}
	
	public void setVerticalAngleDeg(float setDeg){
		verticalAngle = setDeg*.0174532925f;
	}
	
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
		catch(Exception e) {}
	}
}