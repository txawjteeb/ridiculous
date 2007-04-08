package org.cart.igd.entity;

public class Node implements NodeInterface
{/*
	protected Node parentNode;
	protected Node childNode;
	protected Node previousNode;
	protected Node nextNode;
	
	public Node()
	{
		parentNode = childNode = null;
		previousNode = nextNode = this;
	}
	
	public Node(Node node)
	{
		parentNode = childNode = null;
		previousNode = nextNode = this;
		attachTo(node);
	}
	
	public final void attach(Node newChild)
	{
		if(newChild.hasParent())
			newChild.detach();
		
		newChild.parentNode = this;
		
		if(childNode!=null)
		{
			newChild.previousNode = childNode.previousNode;
			newChild.nextNode = childNode;
			childNode.previousNode.nextNode = newChild;
			childNode.previousNode = newChild;
		}
		else
		{
			childNode = newChild;
		}
	}
	
	public final void attachTo(Node newParent)
	{
		if(parentNode!=null) detach();
		
		parentNode = newParent;
		
		if(parentNode.childNode!=null)
		{
			previousNode = parentNode.childNode.previousNode;
			nextNode = parentNode.childNode;
			parentNode.childNode.previousNode.nextNode = this;
			parentNode.childNode.previousNode = this;
		}
		else
		{
			parentNode.childNode = this;
		}
	}
	
	public final int countNodes()
	{
		if(childNode!=null)
			return childNode.countNodes()+1;
		return 1;
	}
	
	public final void detach()
	{
		System.out.println("Node.detach(): " + ((Entity)this).getID());
		if((parentNode!=null) && parentNode.childNode==this)
		{
			if(nextNode!=this)
				parentNode.childNode = nextNode;
			else
				parentNode.childNode = null;
		}
		
		previousNode.nextNode = nextNode;
		nextNode.previousNode = previousNode;
		
		previousNode = this;
		nextNode = this;
	}
	
	protected final void finalize()
	{
		System.out.println("Node.finalize(): "+((Entity)this).getID());
		detach();
		while(childNode!=null)
			childNode.detach();
	}
	
	public final boolean hasChild()
	{
		return childNode!=null;
	}
	
	public final boolean hasParent()
	{
		return parentNode!=null;
	}
	
	public final boolean isFirstChild()
	{
		if(parentNode!=null)
			return parentNode.childNode==this;
		return false;
	}
	
	public final boolean isLastChild()
	{
		if(parentNode!=null)
			return parentNode.childNode.previousNode==this;
		return false;
	}
	
	public final Node getFirstChild()
	{
		return childNode;
	}
	*/
}
