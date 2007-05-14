package org.cart.igd.math;

/**
 * Point2d.java
 *
 * General Function: A simple X and Y Double point.
 */
public class Point2d
{
	/* X-Axis Position. */
	public double x;
	
	/* Y-Axis Position. */
	public double y;

	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Point2d.
	 *
	 * @param x The x-axis position.
	 * @param y The y-axis position.
	 */
	public Point2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}