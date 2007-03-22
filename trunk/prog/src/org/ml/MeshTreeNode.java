package org.cart.igd.ml;

import java.io.File;
import javax.swing.tree.TreeNode;

public class MeshTreeNode extends BaseTreeNode
{
	private String modelName;
	private String meshFormat;
	private File meshFile;
	 
	public MeshTreeNode(TreeNode parentNode, String modelName, String meshFormat, File meshFile)
	{
		super(parentNode, meshFile.getName());
		this.modelName = modelName;
		this.meshFormat = meshFormat;
		this.meshFile = meshFile;
	}
	
	public boolean isLeaf()
	{
		return true; 
	}
	
	public File getMeshFile()
	{
		return meshFile;
	}
	
	public String getMeshFormat()
	{
		return meshFormat;
	}
	
	public String getModelName()
	{
		return modelName;
	}
}