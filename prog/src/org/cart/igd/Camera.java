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
	
	public float distance = 5.0f;
	public float cameraHeight = 3.0f;
	public float facingOffset = 0.0f;
	
	private GameAction mouseCameraRotate;
	
	public Camera(Entity player, float distance, float cameraHeight)
	{
		cameraPos = new Vector3f();
		cameraUp = new Vector3f(0f, 1f, 0f);
		this.cameraHeight = cameraHeight;
		this.distance = distance;
		
		mouseCameraRotate = new GameAction("mouse rotation mode", 0);
		Driver.userInput.bindToMouse(mouseCameraRotate, MouseEvent.BUTTON3);
	}
	
	public void setCameraHeight(float height)
	{
		cameraHeight = height;
	}
	
	public float getCameraHeight()
	{
		return cameraHeight;
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
	
	public void lookAt(GLU glu, Entity player)
	{
		if(mouseCameraRotate.isActive())
		{
			facingOffset -= ( Driver.userInput.getXDif()*0.5f );
		}
		else if(!mouseCameraRotate.isActive() && facingOffset!=0f)
		{
			if(facingOffset>0f && facingOffset<180f)
			{
				facingOffset-=8f;
			}
			else if(facingOffset>180f && facingOffset<360f)
			{
				facingOffset+=8f;
			}
			if(facingOffset<8f && facingOffset>-8f)
			{
				facingOffset = 0f;
			}
		}
		
		cameraPos.x = player.position.x + ( distance * (float)Math.cos((player.facingDirection+facingOffset-180f) * 0.0174f) );
		cameraPos.y = cameraHeight;
		cameraPos.z = player.position.z + ( distance * (float)Math.sin((player.facingDirection+facingOffset-180f) * 0.0174f) );
		
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
}