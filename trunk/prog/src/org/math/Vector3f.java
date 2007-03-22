package org.cart.igd.math;

public class Vector3f
{
	public float x, y, z;

	public Vector3f()
	{
		x=y=z=0f;
	}
	
	public Vector3f(Vector3f v)
	{
		this(v.x, v.y, v.z);
	}
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector3f cross(Vector3f v1, Vector3f v2)
	{
		Vector3f Result = new Vector3f();
		Result.x = (v1.y * v2.z) - (v1.z * v2.y);
		Result.y = (v1.z * v2.x) - (v1.x * v2.z);
		Result.z = (v1.x * v2.y) - (v1.y * v2.x);
		return Result;
	}

	public static float dot(Vector3f v1, Vector3f v2)
	{
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z + v2.z);
	}
	
	public static float angle(Vector3f v1, Vector3f v2)
	{
		return (float)Math.acos(Math.toRadians(Vector3f.dot(v1, v2)));
	}
	
	public static Vector3f divideByScalar(Vector3f v1, float scalar)
	{
		Vector3f result = new Vector3f();
		result.x = v1.x / scalar;
		result.z = v1.z / scalar;
		result.y = v1.y / scalar;
		return result;
	}
	
	public static Vector3f multiplyByScalar(Vector3f v1, float scalar)
	{
		Vector3f result = new Vector3f();
		result.x = v1.x * scalar;
		result.z = v1.z * scalar;
		result.y = v1.y * scalar;
		return result;
	}
	
	public static Vector3f changeSign(Vector3f v1)
	{
		v1.x = -v1.x;
		v1.x = -v1.y;
		v1.x = -v1.z;
		return v1;
	}
	
	public void divideByScalar(float scalar)
	{
		x /= scalar;
		y /= scalar;
		z /= scalar;
	}
	
	public void multiplyByScalar(float scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}
	
	public void normalize()
	{
        float length = length();
        if(length==0) return;
        x /= length;
        y /= length;
        z /= length;
    }
	
	public void changeSign()
	{
        x = -x;
        y = -y;
        z = -z;
    }
	
	public static float distanceBetween(Vector3f a, Vector3f b)
	{
        float x = a.x - b.x;
        float y = a.y - b.y;
        float z = a.z - b.z;
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }
	
	public Vector3f subtract(Vector3f v)
	{
		return new Vector3f(x-v.x, y-v.y, z-v.z);
	}
	
	public Vector3f add(Vector3f v)
	{
		return new Vector3f(x+v.x, y+v.y, z+v.z);
	}

	public float length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vector3f divide(Vector3f v)
	{
		return new Vector3f(x/v.x, y/v.y, z/v.z);
	}
	
	public Vector3f multiply(Vector3f v)
	{
		return new Vector3f(x*v.x, y*v.y, z*v.z);
	}
	
	public float normal()
	{
		return (x*x + y*y + z*z);
	}
	
	public float[] toArray()
	{
		return new float[] { x, y, z };
	}
	
	public boolean equals(Object o)
	{
        if(this==o)
        	return true;
        
        if(!(o instanceof Vector3f))
            return false;
        
        Vector3f v = (Vector3f) o;
        if(	Float.floatToIntBits(this.x)==Float.floatToIntBits(v.x)
        	&& Float.floatToIntBits(this.y)==Float.floatToIntBits(v.y)
        	&& Float.floatToIntBits(this.z)==Float.floatToIntBits(v.z))
        {
            return true;
        }
        return false;
    }
	
	public int hashCode()
	{
        int result = 17;
        result *= 37 + Float.floatToIntBits(this.x);
        result *= 37 + Float.floatToIntBits(this.y);
        result *= 37 + Float.floatToIntBits(this.z);
        return result;
    }
}