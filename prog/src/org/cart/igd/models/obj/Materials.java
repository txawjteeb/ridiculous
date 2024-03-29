package org.cart.igd.models.obj;

import java.io.*;
import java.util.*;

import javax.media.opengl.*;
import org.cart.igd.util.Texture;

/**
 * Materials.java
 *
 * General Function: Holds the collection of all materials.
 */
public class Materials
{
	/* Collection of Material objects. */
	private ArrayList<Material> materials;
	
	/* The material name to render. */
	private String renderMatName = null;
	
	/* The flag that says to use texture or not. */
	private boolean usingTexture = false;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Materials.
	 */
	public Materials(String mtlFnm)
	{
		materials = new ArrayList<Material>();
		
		String mfnm = "data/obj/mtls"+mtlFnm;
		try
		{
			System.out.println("Loading material from " + mfnm);
			BufferedReader br = new BufferedReader( new FileReader(mfnm));
			readMaterials(br);
			br.close();
		}
		catch(IOException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * readMaterials
	 *
	 * General Function: Reads in the material data.
	 */
	private void readMaterials(BufferedReader br)
	{
		try
		{
			String line;
			Material currMaterial = null;  // current material
			
			while(((line = br.readLine()) != null))
			{
				line = line.trim();
				if(line.length() == 0) continue;
				
				if(line.startsWith("newmtl "))
				{
					if(currMaterial != null) materials.add(currMaterial);
					currMaterial = new Material(line.substring(7));
				}
				
				// texture filename
				else if(line.startsWith("map_Kd "))
				{
					String fileName = line.substring(7);
					currMaterial.loadTexture( fileName );
				}
				
				// ambient colour
				else if(line.startsWith("Ka "))
					currMaterial.setKa( readTuple3(line) );
				
				// diffuse colour
				else if(line.startsWith("Kd "))
					currMaterial.setKd( readTuple3(line) );
					
				// specular colour
				else if(line.startsWith("Ks "))
					currMaterial.setKs( readTuple3(line) );
					
				// shininess
				else if(line.startsWith("Ns "))
				{
					float val = Float.valueOf(line.substring(3)).floatValue();
					currMaterial.setNs( val );
				}
				
				// alpha
				else if(line.charAt(0) == 'd')
				{
					float val = Float.valueOf(line.substring(2)).floatValue();
					currMaterial.setD( val );
				}
				
				// illumination model
				else if(line.startsWith("illum "))
				{
					// not implemented
				}
				
				// comment line
				else if(line.charAt(0) == '#')
					continue;
				
				else
					System.out.println("Ignoring MTL line: " + line);
			}
			materials.add(currMaterial);
		}
		catch(IOException e) 
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * readTuple3
	 *
	 * General Function: Reads in a Tuple3.
	 */
	private Tuple3 readTuple3(String line)
	{
		StringTokenizer tokens = new StringTokenizer(line, " ");
		tokens.nextToken();    // skip MTL word
		
		try
		{
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());
			float z = Float.parseFloat(tokens.nextToken());
			
			return new Tuple3(x,y,z);
		}
		catch(NumberFormatException e) 
		{
			System.out.println(e.getMessage());
		}

		return null;   // means an error occurred
	}

	/**
	 * showMaterials
	 *
	 * General Function: Prints of all the material data.
	 */
	public void showMaterials()
	{
		System.out.println("No. of materials: " + materials.size());
		Material m;
		for(int i=0; i < materials.size(); i++)
		{
			m = (Material) materials.get(i);
			m.showMaterial();
			// System.out.println();
		}
	}

	/**
	 * renderWithMaterials
	 *
	 * General Function: Renders to GL with materials applied.
	 */
	public void renderWithMaterial(GL gl, String faceMat)
	{
		if(!faceMat.equals(renderMatName))
		{
			renderMatName = faceMat;
			switchOffTex(gl);
			
			Texture tex = getTexture(renderMatName);
			if(tex != null)
			{
				// System.out.println("Using texture with " + renderMatName);
				switchOnTex(gl, tex);
			}
			else setMaterialColors(gl, renderMatName);
		}
	}

	/**
	 * switchOffTex
	 *
	 * General Function: Switches off textures.
	 */
	public void switchOffTex(GL gl)
	{
		if(usingTexture)
		{
			gl.glDisable(GL.GL_TEXTURE_2D);
			usingTexture = false;
		}
	}
	
	/**
	 * switchOnTex
	 *
	 * General Function: Switches on textures.
	 */
	private void switchOnTex(GL gl, Texture tex)
	{
		gl.glEnable(GL.GL_TEXTURE_2D);
		usingTexture = true;
		tex.bind(gl);
	}

	/**
	 * getTexture
	 *
	 * General Function: Returns the texture of the given material name.
	 */
	private Texture getTexture(String matName) 
	{
		Material m;
		for(int i=0; i<materials.size(); i++)
		{
			m = (Material) materials.get(i);
			if(m.hasName(matName)) return m.getTexture();
		}
		return null;
	}

	/**
	 * setMaterialColors
	 *
	 * General Function: Sets all materials' colors.
	 */
	private void setMaterialColors(GL gl, String matName)
	{
		Material m;
		for(int i = 0; i < materials.size(); i++)
		{
			m = (Material) materials.get(i);
			if(m.hasName(matName)) m.setMaterialColors(gl);
		}
	}
}