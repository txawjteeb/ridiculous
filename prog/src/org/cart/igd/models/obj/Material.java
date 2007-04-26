package org.cart.igd.models.obj;

import java.io.*;
import javax.media.opengl.*;
import org.cart.igd.core.Kernel;
import org.cart.igd.util.Texture;

public class Material
{
	private String name;
	
	// colour info
	private Tuple3 ka, kd, ks;   // ambient, diffuse, specular colours
	private float ns, d;   // shininess and alpha
	
	// texture info
	private String texFnm;
	private Texture texture;
	
	public Material(String nm)
	{
		name = nm;
		
		d = 1.0f; ns = 0.0f;
		ka = null; kd = null; ks = null;
		
		texFnm = null;
		texture = null;
	}


	public void showMaterial()
	{
		System.out.println(name);
		if(ka != null) System.out.println("  Ka: " + ka.toString());
		if(kd != null) System.out.println("  Kd: " + kd.toString());
		if(ks != null) System.out.println("  Ks: " + ks.toString());
		if(ns != 0.0f) System.out.println("  Ns: " + ns);
		if(d != 1.0f)  System.out.println("  d: " + d);
		if(texFnm != null) System.out.println("  Texture file: " + texFnm);
	}


	public boolean hasName(String nm) { return name.equals(nm); }


  // --------- set/get methods for colour info --------------

	public void setD(float val)		{ d = val; }
	public float getD()				{ return d; }
	
	public void setNs(float val)	{ ns = val; }
	public float getNs()			{ return ns; }
	
	public void setKa(Tuple3 t)		{ ka = t; }
	public Tuple3 getKa()			{ return ka; }
	
	public void setKd(Tuple3 t)		{ kd = t; }
	public Tuple3 getKd()			{ return kd; }
	
	public void setKs(Tuple3 t)		{ ks = t; }
	public Tuple3 getKs()			{ return ks; }
	
	public void setMaterialColors(GL gl)
	{
		// ambient color
		if(ka != null)
		{
			float[] colorKa = { ka.getX(), ka.getY(), ka.getZ(), 1.0f };
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, colorKa, 0);
		}
		
		// diffuse color
		if(kd != null)
		{
			float[] colorKd = { kd.getX(), kd.getY(), kd.getZ(), 1.0f };
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, colorKd, 0);
		}
		
		// specular color
		if(ks != null)
		{
			float[] colorKs = { ks.getX(), ks.getY(), ks.getZ(), 1.0f };
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, colorKs, 0);
		}
		
		// shininess
		if(ns != 0.0f) gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, ns);
		
		// alpha
		if(d != 1.0f)
		{
			// not implemented
		}
	}

	public void loadTexture(String fnm)
	{
		try
		{
			texture = Kernel.display.getRenderer().loadImage("data/textures/"+fnm);
		}
		catch(Exception e)
		{
			System.out.println("Error loading texture " + texFnm);
		}
	}
	
	public void setTexture(Texture t)	{ texture = t; }
	public Texture getTexture()			{ return texture; }
}