package org.cart.igd.ml;

import java.util.*;
import javax.swing.tree.TreeNode;

public class ModelTreeNode extends BaseTreeNode
{
	private List meshes;
	private List skins;
	
	private IBaseTreeNode meshNode;
	private IBaseTreeNode skinNode;

	public ModelTreeNode(TreeNode parentNode, String treeName) {
		super(parentNode, treeName);
		meshes = new LinkedList();
		skins = new LinkedList();
		meshNode = new BaseTreeNode(this, "Mesh(es)");
		skinNode = new BaseTreeNode(this, "Skin(s)");
		addChild(meshNode);
		addChild(skinNode);
	}
	
	public int getChildCount()
	{
		return 2;
	}

	public boolean getAllowsChildren()
	{
		return true;
	}

	public boolean isLeaf()
	{
		return false;
	}
	
	public void addMesh(MeshTreeNode meshTreeNode)
	{
		meshNode.addChild(meshTreeNode);
	}
	
	public void addSkin(SkinTreeNode skinTreeNode)
	{
		skinNode.addChild(skinTreeNode);
	}
}