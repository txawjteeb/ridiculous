package org.cart.igd.ml;

import javax.swing.tree.TreeNode;

public interface IBaseTreeNode extends TreeNode
{
	public void addChild(TreeNode child);
}