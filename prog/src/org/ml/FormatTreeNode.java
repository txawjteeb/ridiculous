package org.cart.igd.ml;

import javax.swing.tree.TreeNode;

public class FormatTreeNode extends BaseTreeNode
{
	public FormatTreeNode(TreeNode parentNode, String treeName)
	{
		super(parentNode, treeName);
	}

	public boolean isLeaf()
	{
		return false; 
	}
}
