package org.cart.igd.math;

public class Vector4f
{
	public float w, x, y, z;

	public Vector4f()
	{
		w=x=y=z=0f;
	}
	
	public Vector4f(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public float[] toArray()
	{
		return new float[] { x, y, z, w };
	}
}