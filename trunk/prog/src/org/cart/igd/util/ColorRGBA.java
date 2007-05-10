package org.cart.igd.util;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * ColorRGBA.java
 *
 * General Function: Holds color data and preset colors.
 */
public class ColorRGBA
{
	/* Preset Black Color Data */
	public final static ColorRGBA Black = new ColorRGBA(0f,0f,0f);
	
	/* Preset White Color Data */
	public final static ColorRGBA White = new ColorRGBA(1f,1f,1f);
	
	/* Preset Blue Color Data */
	public final static ColorRGBA Blue = new ColorRGBA(0f,0f,1f);
	
	/* Preset Red Color Data */
	public final static ColorRGBA Red = new ColorRGBA(1f,0f,0f);
	
	/* Preset Green Color Data */
	public final static ColorRGBA Green = new ColorRGBA(0f,1f,0f);
	
	/* Preset Yellow Color Data */
	public final static ColorRGBA Yellow = new ColorRGBA(1f,1f,0f);
	
	/* Preset Pink Color Data */
	public final static ColorRGBA Pink = new ColorRGBA(1f,0f,1f);
	
	/* Preset Orange Color Data */
	public final static ColorRGBA Orange = new ColorRGBA(0.8f,0.6f,0f);
	
	/* Preset Purple Color Data */
	public final static ColorRGBA Purple = new ColorRGBA(0.6f,0f,0.8f);
	
	/* Preset Gray Color Data */
	public final static ColorRGBA Gray = new ColorRGBA(0.5f,0.5f,0.5f);
	
	/* ArrayList of Custom Color Datas */
	private static ArrayList<ColorRGBA> Colors = new ArrayList<ColorRGBA>();
	
	/**
	 * getColor
	 *
	 * General Function: Returns a color by its name.
	 *
	 * @param name The name of the color to grab.
	 */
	public static ColorRGBA getColor(String name)
	{
		for(int i=0; i<Colors.size(); i++)
		{
			if(Colors.get(i).name==name) return Colors.get(i);
		}
		return White;
	}
	
	/**
	 * addColor
	 *
	 * General Function:
	 *
	 * @param c The color data to add to the color ArrayList.
	 */
	public static void addColor(ColorRGBA c)
	{
		Colors.add(c);
	}
	
	/* The 3 to 4 float RGBA data. */
	public float[] rgba;
	
	/* A given color name to identify the color data. */
	public String name;
	
	/**
	 * Constructor
	 *
	 * General Function: Create an instance of ColorRGBA.
	 *
	 * @param r The red color value.	(0-1)
	 * @param g The green color value.	(0-1)
	 * @param b The blue color value.	(0-1)
	 */
	public ColorRGBA(float r, float g, float b)
	{
		this(r,g,b,1f);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Create an instance of ColorRGBA.
	 *
	 * @param r The red color value.	(0-1)
	 * @param g The green color value.	(0-1)
	 * @param b The blue color value.	(0-1)
	 * @param a The alpha value.		(0-1)
	 */
	public ColorRGBA(float r, float g, float b, float a)
	{
		this("", r, g, b, a);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Create an instance of ColorRGBA.
	 *
	 * @param r The red color value.	(0-255)
	 * @param g The green color value.	(0-255)
	 * @param b The blue color value.	(0-255)
	 */
	public ColorRGBA(int r, int g, int b)
	{
		this((float)r/255f, (float)b/255f, (float)b/255f);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Create an instance of ColorRGBA.
	 *
	 * @param r The red color value.	(0-255)
	 * @param g The green color value.	(0-255)
	 * @param b The blue color value.	(0-255)
	 * @param a The alpha value.		(0-255)
	 */
	public ColorRGBA(int r, int g, int b, int a)
	{
		this((float)r/255f, (float)b/255f, (float)b/255f, (float)a/255f);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Create an instance of ColorRGBA.
	 *
	 * @param name The name to identify the color data by.
	 * @param r The red color value.	(0-1)
	 * @param g The green color value.	(0-1)
	 * @param b The blue color value.	(0-1)
	 */
	public ColorRGBA(String name, float r, float g, float b)
	{
		this(name, r, g, b, 1f);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Create an instance of ColorRGBA.
	 *
	 * @param name The name to identify the color data by.
	 * @param r The red color value.	(0-1)
	 * @param g The green color value.	(0-1)
	 * @param b The blue color value.	(0-1)
	 * @param a The alpha value.		(0-1)
	 */
	public ColorRGBA(String name, float r, float g, float b, float a)
	{
		this.name = name;
		this.rgba = new float[] {r,g,b,a};
	}
	
	/**
	 * getRed
	 *
	 * General Function: Returns the red color value.
	 */
	public float getRed()
	{
		return rgba[0];
	}
	
	/**
	 * getGreen
	 *
	 * General Function: Returns the green color value.
	 */
	public float getGreen()
	{
		return rgba[1];
	}
	
	/**
	 * getBlue
	 *
	 * General Function: Returns the blue color value.
	 */
	public float getBlue()
	{
		return rgba[2];
	}
	
	/**
	 * getAlpha
	 *
	 * General Function: Returns the alpha color value.
	 */
	public float getAlpha()
	{
		return rgba[3];
	}
	
	/**
	 * getRGBA
	 *
	 * General Function: Returns the array of color data.
	 */
	public float[] getRGBA()
	{
		return rgba;
	}
	
	/**
	 * getFloatBuffer
	 *
	 * General Function: Returns a FloatBuffer of the color data.
	 */
	public FloatBuffer getFloatBuffer()
	{
		return FloatBuffer.wrap(rgba);
	}
}