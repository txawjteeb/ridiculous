package org.cart.igd.models.obj;

import java.io.*;
import java.util.*;

import javax.media.opengl.*;
import org.cart.igd.util.Texture;


public class Materials
{
	private ArrayList<Material> materials;
	private String renderMatName = null;
	private boolean usingTexture = false;
	
	public Materials(String mtlFnm)
	{
		materials = new ArrayList<Material>();
		
		String mfnm = "data/models/"+mtlFnm;
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

	public void switchOffTex(GL gl)
	{
		if(usingTexture)
		{
			gl.glDisable(GL.GL_TEXTURE_2D);
			usingTexture = false;
		}
	}

	private void switchOnTex(GL gl, Texture tex)
	{
		gl.glEnable(GL.GL_TEXTURE_2D);
		usingTexture = true;
		tex.bind(gl);
	}

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