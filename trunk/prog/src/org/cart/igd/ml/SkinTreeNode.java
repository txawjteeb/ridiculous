package org.cart.igd.ml;

import java.io.File;
import javax.swing.tree.TreeNode;

public class SkinTreeNode extends BaseTreeNode
{
	private File skinFile;

	public SkinTreeNode(TreeNode parentNode, String treeName, File skinFile)
	{
		super(parentNode, treeName);
		this.skinFile = skinFile;
	}

	public boolean isLeaf()
	{
		return true; 
	}
	
	public File getSkinFile()
	{
		return skinFile;
	}
}