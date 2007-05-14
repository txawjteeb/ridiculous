package org.cart.igd.models.obj;

import java.io.*;
import javax.media.opengl.*;
import org.cart.igd.core.Kernel;
import org.cart.igd.util.Texture;

public class Material
{
	/* Material name. */
	private String name;
	
	/* Ambient color. */
	private Tuple3 ka;
	
	/* Diffuse color. */
	private Tuple3 kd;
	
	/* Specular color. */
	private Tuple3 ks;
	
	/* Shininess value. */
	private float ns;
	
	/* Alpha value. */
	private float d;
	
	/* Texture file name. */
	private String texFnm;
	
	/* Texture reference. */
	private Texture texture;
	
	public String texDir = "data/obj/textures/";
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Material.
	 */
	public Material(String nm)
	{
		name = nm;
		
		d = 1.0f; ns = 0.0f;
		ka = null; kd = null; ks = null;
		
		texFnm = null;
		texture = null;
	}

	/**
	 * showMaterial
	 *
	 * General Function: Prints of the Material information.
	 */
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

	/**
	 * hasName
	 *
	 * General Function: Returns whether the material has a name value.
	 */
	public boolean hasName(String nm)
	{
		return name.equals(nm);
	}

	/**
	 * setD
	 *
	 * General Function: Sets the alpha value.
	 */
	public void setD(float val)
	{
		d = val;
	}
	
	/**
	 * getD
	 *
	 * General Function: Gets the alpha value.
	 */
	public float getD()				{ return d; }
	
	/**
	 * setNs
	 *
	 * General Function: Sets the shininess value.
	 */
	public void setNs(float val)
	{
		ns = val;
	}
	
	/**
	 * getNs
	 *
	 * General Function: Gets the shininess value.
	 */
	public float getNs()
	{
		return ns;
	}
	
	/**
	 * setKa
	 *
	 * General Function: Sets the ambient value.
	 */
	public void setKa(Tuple3 t)
	{
		ka = t;
	}
	
	/**
	 * getKa
	 *
	 * General Function: Gets the ambient value.
	 */
	public Tuple3 getKa()
	{
		return ka;
	}
	
	/**
	 * setKd
	 *
	 * General Function: Sets the diffuse value.
	 */
	public void setKd(Tuple3 t)
	{
		kd = t;
	}
	
	/**
	 * getKd
	 *
	 * General Function: Gets the diffuse value.
	 */
	public Tuple3 getKd()
	{
		return kd;
	}
	
	/**
	 * setKs
	 *
	 * General Function: Sets the specular value.
	 */
	public void setKs(Tuple3 t)
	{
		ks = t;
	}
	
	/**
	 * getKs
	 *
	 * General Function: Gets the specular value.
	 */
	public Tuple3 getKs()
	{
		return ks;
	}
	
	/**
	 * setMaterialColors
	 *
	 * General Function: Sets colors in GL.
	 */
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
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			gl.glColor4f(1.0f, 1.0f, 1.0f, d);
		}
	}

	/**
	 * loadTexture
	 *
	 * General Function: Loads a texture.
	 */
	public void loadTexture(String fnm)
	{
		try
		{
			texture = Kernel.display.getRenderer().loadImage(texDir+fnm);
		}
		catch(Exception e)
		{
			System.out.println("Error loading texture " + texFnm);
		}
	}
	
	/**
	 * setTexture
	 *
	 * General Function: Sets the texture object.
	 */
	public void setTexture(Texture t)
	{
		texture = t;
	}
	
	/**
	 * getTexture
	 *
	 * General Function: Returns the Texture.
	 */
	public Texture getTexture()
	{
		return texture;
	}
}