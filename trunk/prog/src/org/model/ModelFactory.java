package org.cart.igd.model;

/*
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class ModelFactory implements AbstractModelFactory
{
	private Map<String,Model3ds> models;
	private static ModelFactory instance;
	
	private ModelFactory()
	{
		models = new HashMap<String,Model3ds>();
	}
	
	public static ModelFactory getInstance()
	{
		if(instance==null)
			instance = new ModelFactory();
		return instance;
	}
	
	public Model loadModel(File meshFile, File skinFile)
	{
		if(meshFile==null)
			throw new IllegalArgumentException("ModelFactory.loadModel(): meshFile is null");
		
		Model3ds model = null;
		String modelKey = meshFile.getName();
		if(models.containsKey(modelKey))
		{
			model = new Model3ds(models.get(modelKey));
		}
		else
		{
			model = new Model3ds(meshFile);
			models.put(modelKey, model);
		}
		return model;
	}
	
	
}
*/