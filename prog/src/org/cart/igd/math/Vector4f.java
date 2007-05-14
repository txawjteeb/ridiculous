package org.cart.igd.math;

/**
 * Vector4f.java
 *
 * General Function: Holds 4 float values.
 */
public class Vector4f
{
	/* Extra value. */
	public float w;
	
	/* X-Axis value. */
	public float x;
	
	/* Y-Axis value. */
	public float y;
	
	/* Z-Axis value. */
	public float z;

	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Vector4f.
	 */
	public Vector4f()
	{
		w=x=y=z=0f;
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Vector4f.
	 */
	public Vector4f(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * toArray
	 *
	 * General Function: Returns a float array of the vector values.
	 */
	public float[] toArray()
	{
		return new float[] { x, y, z, w };
	}
}