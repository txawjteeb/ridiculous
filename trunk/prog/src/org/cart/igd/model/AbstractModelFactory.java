package org.cart.igd.model;

import java.io.File;

public interface AbstractModelFactory
{
	public Model loadModel(File modelFile, File skinFile);
}