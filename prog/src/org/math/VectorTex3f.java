package org.cart.igd.math;

public class VectorTex3f extends Vector3f
{
	public float u;
	public float v;
	
	public VectorTex3f()
	{}
	
	public VectorTex3f(Vector3f v1, float u, float v)
	{
		this(v1.x, v1.y, v1.z, u, v);
	}
	
	public VectorTex3f(Vector3f v)
	{
		this(v.x, v.y, v.z, 0f, 0f);
	}
	
	public VectorTex3f(VectorTex3f v)
	{
		this(v.x, v.y, v.z, v.u, v.v);
	}
	
	public VectorTex3f(float x, float y, float z, float u, float v)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
		this.v = v;
	}
}
