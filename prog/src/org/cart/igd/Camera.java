package org.cart.igd;

import java.awt.Point;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.GL;

import org.cart.igd.math.Vector3f;
import org.cart.igd.entity.Entity;

public class Camera
{
	private Entity player;
	private Vector3f cameraPos = new Vector3f();
	private Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
	
	public float distance = 25.0f;
	public float cameraHeight = 50f;
	
	public Camera(Entity player, float distance, float cameraHeight)
	{
		cameraPos = new Vector3f();
		cameraUp = new Vector3f(0f, 1f, 0f);
		this.cameraHeight = cameraHeight;
		this.distance = distance;
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
	
	public void lookAt(GLU glu, Entity player)
	{
		cameraPos.x = player.position.x + ( distance * (float)Math.cos((player.facingDirection-180f) * 0.0174f) );
		cameraPos.y = cameraHeight;
		cameraPos.z = player.position.z + ( distance * (float)Math.sin((player.facingDirection-180f) * 0.0174f) );
		
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