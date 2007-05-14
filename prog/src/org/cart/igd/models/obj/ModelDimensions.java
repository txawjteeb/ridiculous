package org.cart.igd.models.obj;

import java.text.DecimalFormat;

/**
 * ModelDimensions.java
 *
 * General Function: Holds object model dimensions.
 */
public class ModelDimensions
{
	// edge coordinates
	private float leftPt, rightPt;   // on x-axis
	private float topPt, bottomPt;   // on y-axis
	private float farPt, nearPt;     // on z-axis
	private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp

	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of ModelDimensions.
	 */
	public ModelDimensions()
	{
		leftPt = 0.0f;  rightPt = 0.0f;
		topPt = 0.0f;  bottomPt = 0.0f;
		farPt = 0.0f;  nearPt = 0.0f;
	}

	/**
	 * set
	 *
	 * General Function: Sets the dimensions values.
	 */
	public void set(Tuple3 vert)
	{
		rightPt		= vert.getX();
		leftPt		= vert.getX();

		topPt		= vert.getY();
		bottomPt	= vert.getY();
		
		nearPt		= vert.getZ();
		farPt		= vert.getZ();
	}

	/**
	 * update
	 *
	 * General Function: Updates the dimension values.
	 */
	public void update(Tuple3 vert)
	{
		if(vert.getX() > rightPt) rightPt = vert.getX();
		if(vert.getX() < leftPt) leftPt = vert.getX();
            
		if(vert.getY() > topPt) topPt = vert.getY();
		if(vert.getY() < bottomPt) bottomPt = vert.getY();
            
		if(vert.getZ() > nearPt) nearPt = vert.getZ();
		if(vert.getZ() < farPt) farPt = vert.getZ();
	}

	/**
	 * getWidth
	 *
	 * General Function: Returns the width.
	 */
	public float getWidth()
	{
		return (rightPt - leftPt);
	}

	/**
	 * getHeight
	 *
	 * General Function: Returns the height.
	 */
	public float getHeight()
	{
		return (topPt - bottomPt);
	}

	/**
	 * getDepth
	 *
	 * General Function: Returns the depth.
	 */
	public float getDepth()
	{
		return (nearPt - farPt);
	}

	/**
	 * getLargest
	 *
	 * General Function: Returns the largest dimension.
	 */
	public float getLargest()
	{
		float height = getHeight();
		float depth = getDepth();
		float largest = getWidth();
		
		if(height > largest) largest = height;
		if(depth > largest) largest = depth;
		
		return largest;
	}

	/**
	 * getCenter
	 *
	 * General Function: Returns the center Tuple3.
	 */
	public Tuple3 getCenter()
	{
		float xc = (rightPt + leftPt)/2.0f; 
		float yc = (topPt + bottomPt)/2.0f;
		float zc = (nearPt + farPt)/2.0f;
		return new Tuple3(xc, yc, zc);
	}

	/**
	 * reportDimensions
	 *
	 * General Function: Prints out a report of the dimensions.
	 */
	public void reportDimensions()
	{
		Tuple3 center = getCenter();
		
		System.out.println("x Coords: " + df.format(leftPt) + " to " + df.format(rightPt));
		System.out.println("  Mid: " + df.format(center.getX()) + "; Width: " + df.format(getWidth()) );
		
		System.out.println("y Coords: " + df.format(bottomPt) + " to " + df.format(topPt));
		System.out.println("  Mid: " + df.format(center.getY()) + "; Height: " + df.format(getHeight()) );
		
		System.out.println("z Coords: " + df.format(nearPt) + " to " + df.format(farPt));
		System.out.println("  Mid: " + df.format(center.getZ()) + "; Depth: " + df.format(getDepth()) );
	}
}