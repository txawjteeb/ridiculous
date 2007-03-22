package org.cart.igd;

public class Sprite3D
{
	private int modelStateIndex = 0;
	private float animationSpeed = 7.0f;
	private float deltaTime;
	
	public void update(float elapsedTime)
	{
		deltaTime = elapsedTime;
	}
}
