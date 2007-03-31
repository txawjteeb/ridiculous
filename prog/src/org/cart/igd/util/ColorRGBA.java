package org.cart.igd.util;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class ColorRGBA
{
	public final static ColorRGBA Black = new ColorRGBA(0f,0f,0f);
	public final static ColorRGBA White = new ColorRGBA(1f,1f,1f);
	public final static ColorRGBA Blue = new ColorRGBA(0f,0f,1f);
	public final static ColorRGBA Red = new ColorRGBA(1f,0f,0f);
	public final static ColorRGBA Green = new ColorRGBA(0f,1f,0f);
	public final static ColorRGBA Yellow = new ColorRGBA(1f,1f,0f);
	public final static ColorRGBA Pink = new ColorRGBA(1f,0f,1f);
	public final static ColorRGBA Orange = new ColorRGBA(0.8f,0.6f,0f);
	public final static ColorRGBA Purple = new ColorRGBA(0.6f,0f,0.8f);
	public final static ColorRGBA Gray = new ColorRGBA(0.5f,0.5f,0.5f);
	
	private static ArrayList<ColorRGBA> Colors = new ArrayList<ColorRGBA>();
	public static ColorRGBA getColor(String name)
	{
		for(int i=0; i<Colors.size(); i++)
		{
			if(Colors.get(i).name==name) return Colors.get(i);
		}
		return White;
	}
	public static void addColor(ColorRGBA c) { Colors.add(c); }
	
	public float[] rgba;
	public String name;
	
	public ColorRGBA(float r, float g, float b) { this(r,g,b,1f); }
	
	public ColorRGBA(float r, float g, float b, float a) { this("", r, g, b, a); }
	
	public ColorRGBA(int r, int g, int b)
	{
		this((float)r/255f, (float)b/255f, (float)b/255f);
	}
	
	public ColorRGBA(int r, int g, int b, int a)
	{
		this((float)r/255f, (float)b/255f, (float)b/255f, (float)a/255f);
	}
	
	public ColorRGBA(String name, float r, float g, float b) { this(name, r, g, b, 1f); }
	
	public ColorRGBA(String name, float r, float g, float b, float a)
	{
		this.name = name;
		this.rgba = new float[] {r,g,b,a};
	}
	
	public float getRed() { return rgba[0]; }
	
	public float getGreen() { return rgba[1]; }
	
	public float getBlue() { return rgba[2]; }
	
	public float getAlpha() { return rgba[3]; }
	
	public float[] getRGBA() { return rgba; }
	
	public FloatBuffer getFloatBuffer() { return FloatBuffer.wrap(rgba); }
}