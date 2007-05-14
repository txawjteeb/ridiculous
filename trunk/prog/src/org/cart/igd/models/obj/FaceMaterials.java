package org.cart.igd.models.obj;

import java.util.*;

/**
 * FaceMaterials.java
 *
 * General Function: Holds Material indices for Faces.
 */
public class FaceMaterials
{
	/* Collection of face material indices. */
	private HashMap<Integer, String>faceMats;

	/* Collection of material counts. */
	private HashMap<String, Integer>matCount; 

	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of FaceMaterials.
	 */
	public FaceMaterials()
	{
		faceMats = new HashMap<Integer, String>();
		matCount = new HashMap<String, Integer>();
	}

	/**
	 * addUse
	 *
	 * General Function: Adds a face material to use.
	 */
	public void addUse(int faceIdx, String matName)
	{
		if(faceMats.containsKey(faceIdx))
		{
			System.out.println("Face index " + faceIdx + " changed to use material " + matName);
		}
		faceMats.put(faceIdx, matName);
		
		if(matCount.containsKey(matName))
		{
			int i = (Integer) matCount.get(matName) + 1;
			matCount.put(matName, i);
		}
		else
		{
			matCount.put(matName, 1);
		}
	}
	
	/**
	 * findMaterial
	 *
	 * General Function: Finds a face material from the given index.
	 *
	 * @param faceIdx The index to find in the faceMats collection.
	 */
	public String findMaterial(int faceIdx)
	{
		return (String) faceMats.get(faceIdx);
	}
	
	/**
	 * showUsedMaterials
	 *
	 * General Function:
	 */
	public void showUsedMaterials()
	{
		System.out.println("No. of materials used: " + matCount.size());
		
		Set<String> keys = matCount.keySet();
		Iterator<String> iter = keys.iterator(); 
		
		String matName;
		int count;
		while(iter.hasNext())
		{
			matName = iter.next();
			count = (Integer) matCount.get( matName ); 
			System.out.print( matName + ": " + count);
			System.out.println();
		}
	}

}