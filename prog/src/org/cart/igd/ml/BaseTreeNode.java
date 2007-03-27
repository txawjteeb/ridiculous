package org.cart.igd.ml;

import java.util.*;
import javax.swing.tree.TreeNode;

public class BaseTreeNode implements IBaseTreeNode
{
	private List children;
	private TreeNode parentNode;
	private String treeName;
	
	public BaseTreeNode(TreeNode parentNode, String treeName)
	{
		this.parentNode = parentNode;
		this.treeName = treeName;
		this.children = new LinkedList();;
	}

	public int getChildCount()
	{
		return children.size();
	}

	public boolean getAllowsChildren()
	{
		return true;
	}

	public boolean isLeaf()
	{
		if (children.size() == 0) return true;
		return false;
	}

	public Enumeration children()
	{
		return ((Vector)children).elements();
	}

	public TreeNode getParent()
	{
		return parentNode;
	}

	public TreeNode getChildAt(int childIndex)
	{
		return (TreeNode)children.get(childIndex);
	}

	public int getIndex(TreeNode node)
	{
		return children.indexOf(node);
	}
	
	public void addChild(TreeNode child)
	{
		children.add(child);
	}

	public String toString()
	{
		return treeName;
	}
}