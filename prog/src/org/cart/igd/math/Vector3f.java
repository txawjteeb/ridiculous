package org.cart.igd.math;

/**
 * Vector3f.java
 *
 * General Function:
 */
public class Vector3f
{
	/* X-Axis value. */
	public float x;
	
	/* Y-Axis value. */
	public float y;
	
	/* Z-Axis value. */
	public float z;

	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Vector3f.
	 */
	public Vector3f()
	{
		x=y=z=0f;
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Vector3f.
	 *
	 * @param v The Vector3f to copy values from.
	 */
	public Vector3f(Vector3f v)
	{
		this(v.x, v.y, v.z);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Vector3f.
	 *
	 * @param x The x value to inherit.
	 * @param y The y value to inherit.
	 * @param z The z value to inherit.
	 */
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * cross
	 *
	 * General Function: Returns the Cross of two vectors.
	 *
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 */
	public static Vector3f cross(Vector3f v1, Vector3f v2)
	{
		Vector3f Result = new Vector3f();
		Result.x = (v1.y * v2.z) - (v1.z * v2.y);
		Result.y = (v1.z * v2.x) - (v1.x * v2.z);
		Result.z = (v1.x * v2.y) - (v1.y * v2.x);
		return Result;
	}

	/**
	 * dot
	 *
	 * General Function: Returns the Dot Product of two vectors.
	 *
	 * @param v1 The first Vector3f.
	 * @param v2 The second Vector3f.
	 */
	public static float dot(Vector3f v1, Vector3f v2)
	{
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z + v2.z);
	}
	
	/**
	 * angle
	 *
	 * General Function: Returns the angle from one vector to another.
	 *
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 */
	public static float angle(Vector3f v1, Vector3f v2)
	{
		return (float)Math.acos(Math.toRadians(Vector3f.dot(v1, v2)));
	}
	
	/**
	 * divideByScalar
	 *
	 * General Function: Returns a vector divided by a scalar.
	 *
	 * @param v1 The vector to divide.
	 * @param scalar The scalar to divide by.
	 */
	public static Vector3f divideByScalar(Vector3f v1, float scalar)
	{
		Vector3f result = new Vector3f();
		result.x = v1.x / scalar;
		result.z = v1.z / scalar;
		result.y = v1.y / scalar;
		return result;
	}
	
	/**
	 * multiplyByScalar
	 *
	 * General Function: Returns a vector multiply by a scalar.
	 *
	 * @param v1 The vector to multiply.
	 * @param scalar The scalar to multiply by.
	 */
	public static Vector3f multiplyByScalar(Vector3f v1, float scalar)
	{
		Vector3f result = new Vector3f();
		result.x = v1.x * scalar;
		result.z = v1.z * scalar;
		result.y = v1.y * scalar;
		return result;
	}
	
	/**
	 * changeSign
	 *
	 * General Function: Returns a vector with its values x -1;
	 */
	public static Vector3f changeSign(Vector3f v1)
	{
		v1.x = -v1.x;
		v1.x = -v1.y;
		v1.x = -v1.z;
		return v1;
	}
	
	/**
	 * divideByScalar
	 *
	 * General Function: Divides this vector by a scalar.
	 */
	public void divideByScalar(float scalar)
	{
		x /= scalar;
		y /= scalar;
		z /= scalar;
	}
	
	/**
	 * multiplyByScalar
	 *
	 * General Function: Multiplies this vector by a scalar.
	 */
	public void multiplyByScalar(float scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}
	
	/**
	 * normalize
	 *
	 * General Function: Normalizes the vector.
	 */
	public void normalize()
	{
        float length = length();
        if(length==0) return;
        x /= length;
        y /= length;
        z /= length;
    }
	
	/**
	 * changeSign
	 *
	 * General Function: Changes the signs of the vector values.
	 */
	public void changeSign()
	{
        x = -x;
        y = -y;
        z = -z;
    }
	
	/**
	 * distanceBetween
	 *
	 * General Function: Returns the distance between two vectors.
	 */
	public static float distanceBetween(Vector3f a, Vector3f b)
	{
        float x = a.x - b.x;
        float y = a.y - b.y;
        float z = a.z - b.z;
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }
	
	/**
	 * subtract
	 *
	 * General Function: Returns the result of this vector minus another vector.
	 */
	public Vector3f subtract(Vector3f v)
	{
		return new Vector3f(x-v.x, y-v.y, z-v.z);
	}
	
	/**
	 * add
	 *
	 * General Function: Returns the result of this vector plus another vector.
	 */
	public Vector3f add(Vector3f v)
	{
		return new Vector3f(x+v.x, y+v.y, z+v.z);
	}

	/**
	 * length
	 *
	 * General Function: Returns the length of this vector.
	 */
	public float length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * divide
	 *
	 * General Function: Returns the result of this vector divided by another vector.
	 */
	public Vector3f divide(Vector3f v)
	{
		return new Vector3f(x/v.x, y/v.y, z/v.z);
	}
	
	/**
	 * multiply
	 *
	 * General Function: Returns the result of this vector times another vector.
	 */
	public Vector3f multiply(Vector3f v)
	{
		return new Vector3f(x*v.x, y*v.y, z*v.z);
	}
	
	/**
	 * normal
	 *
	 * General Function: Returns the normal.
	 */
	public float normal()
	{
		return (x*x + y*y + z*z);
	}
	
	/**
	 * toArray
	 *
	 * General Function: Returns a float array of the X, Y and Z values.
	 */
	public float[] toArray()
	{
		return new float[] { x, y, z };
	}
	
	/**
	 * equals
	 *
	 * General Function: Checks if this vector is the same as another vector.
	 */
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
	
	/**
	 * hashCode
	 *
	 * General Function: Returns the integer hash code.
	 */
	public int hashCode()
	{
        int result = 17;
        result *= 37 + Float.floatToIntBits(this.x);
        result *= 37 + Float.floatToIntBits(this.y);
        result *= 37 + Float.floatToIntBits(this.z);
        return result;
    }
}