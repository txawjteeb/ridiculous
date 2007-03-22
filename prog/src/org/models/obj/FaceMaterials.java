package org.cart.igd.models.obj;

import java.util.*;

public class FaceMaterials
{
	private HashMap<Integer, String>faceMats;

	private HashMap<String, Integer>matCount; 

	public FaceMaterials()
	{
		faceMats = new HashMap<Integer, String>();
		matCount = new HashMap<String, Integer>();
	}


	public void addUse(int faceIdx, String matName)
	{
		if(faceMats.containsKey(faceIdx))
			System.out.println("Face index " + faceIdx + " changed to use material " + matName);
		faceMats.put(faceIdx, matName);
		
		if(matCount.containsKey(matName))
		{
			int i = (Integer) matCount.get(matName) + 1;
			matCount.put(matName, i);
		}
		else
			matCount.put(matName, 1);
	}
	
	public String findMaterial(int faceIdx)
	{
		return (String) faceMats.get(faceIdx);
	}
	
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