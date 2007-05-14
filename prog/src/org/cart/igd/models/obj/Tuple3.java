package org.cart.igd.models.obj;

/**
 * Tuple3.java
 *
 * General Function: Simple three float vector.
 */
public class Tuple3 
{
	/* X-Axis value. */
	private float x;
	
	/* Y-Axis value. */
	private float y;
	
	/* Z-Axis value. */
	private float z;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Tuple3.
	 */
	public Tuple3(float xc, float yc, float zc)
	{
		x = xc;
		y = yc;
		z = zc;
	}
	
	/**
	 * toString
	 *
	 * General Function: Returns a String of the contained data.
	 */
	public String toString()
	{
		return "( " + x + ", " + y + ", " + z + " )";
	}
	
	/**
	 * setX
	 *
	 * General Function: Sets the x value.
	 */
	public void setX(float xc)
	{
		x = xc;
	}
	
	/**
	 * getX
	 *
	 * General Function: Gets the x value.
	 */
	public float getX()
	{
		return x;
	}
	
	/**
	 * setY
	 *
	 * General Function: Sets the y value.
	 */
	public void setY(float yc)
	{
		y = yc;
	}
	
	/**
	 * getY
	 *
	 * General Function: Gets the y value.
	 */
	public float getY()
	{
		return y;
	}
	
	/**
	 * setZ
	 *
	 * General Function: Sets the z value.
	 */
	public void setZ(float zc)
	{
		z = zc;
	}
	
	/**
	 * getZ
	 *
	 * General Function: Gets the z value.
	 */
	public float getZ()
	{
		return z;
	}
}