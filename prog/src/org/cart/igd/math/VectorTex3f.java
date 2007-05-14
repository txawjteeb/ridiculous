package org.cart.igd.math;

/**
 * VectorTex3f.java
 *
 * General Function: Used to hold texture coords.
 */
public class VectorTex3f extends Vector3f
{
	/* U value. */
	public float u;
	
	/* V value. */
	public float v;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of VectorTex3f.
	 */
	public VectorTex3f()
	{
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of VectorTex3f.
	 */
	public VectorTex3f(Vector3f v1, float u, float v)
	{
		this(v1.x, v1.y, v1.z, u, v);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of VectorTex3f.
	 */
	public VectorTex3f(Vector3f v)
	{
		this(v.x, v.y, v.z, 0f, 0f);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of VectorTex3f.
	 */
	public VectorTex3f(VectorTex3f v)
	{
		this(v.x, v.y, v.z, v.u, v.v);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of VectorTex3f.
	 */
	public VectorTex3f(float x, float y, float z, float u, float v)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
		this.v = v;
	}
}
