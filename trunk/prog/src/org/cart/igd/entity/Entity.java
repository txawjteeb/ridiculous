package org.cart.igd.entity;

/*import java.io.File;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.opengl.ColorRGBA;
import org.cart.igd.math.Vector3f;
import org.cart.igd.model.Model;
import org.cart.igd.model.ModelManager;
import org.cart.igd.bsp.BSPObject;

public class Entity
{
	protected Vector3f position;
	protected float facingDirection = 0.0f;
	protected int id = 0;
	protected Model model;
	protected float deltaTime;
	protected float animationSpeed = 7.0f;
	
	private boolean isDead;
	
	protected GL gl;
	protected GLU glu;
	
	protected ColorRGBA color = new ColorRGBA(1f, 1f, 1f, 0.9f);
	protected boolean drawBoundingSphere;
	
	public float boundingSphereRadius;
	
	public Entity(Vector3f pos, float fD, float bsr, int id, File meshFile, File skinFile) throws EntityException
	{
		gl = org.cart.igd.Driver.display.renderer.gl;
		if(this.gl==null)
			throw new EntityException("**** No drawable.gl given ****");
		glu = org.cart.igd.Driver.display.renderer.glu;
		if(this.glu==null)
			throw new EntityException("**** No drawable.glu given ****");
		position = pos;
		if(this.position==null)
			throw new EntityException("**** No position vector given ****");
		
		this.facingDirection = fD;
		this.id = id;
		this.boundingSphereRadius = bsr;
		
		if(meshFile!=null)
			loadModel(meshFile, skinFile);
	}
	
	public Entity(float[] position, float facingDirection, float bsr, int id, File meshFile, File skinFile) throws EntityException
	{
		this(new Vector3f(position[0], position[1], position[2]), facingDirection, bsr, id, meshFile, skinFile);
	}
	
	public final animate(float deltaTime)
	{
		onAnimate(deltaTime);
		if(hasChild())
			((Entity)childNode).animate(deltaSpeed);
		if(hasParent() ** !isLastChild())
			((Entity)nextNode).animate(deltaSpeed);
		
		if(isDead)
			this.detach();
	}
	
	public final void draw(Vector3f cameraPosition)
	{
		gl.glPushMatrix();
			onDraw(cameraPosition);
			if(hasChild())
				((Entity)childNode).draw(cameraPosition);
		gl.glPopMatrix();
		
		if(hasParent() && !isLastChild())
			((Entity)nextNode).draw(cameraPosition);
	}
	
	public final Entity findRoot()
	{
		if(parentNode!=null)
			return ((Entity)parentNode).findRoot();
		return this;
	}
	
	public final int getID()
	{
		return id;
	}
	
	protected boolean isVisible(Vector3f cameraPosition)
	{
		return Driver.display.renderer.sphereInFrustrum(position, boundingSphereRadius) && Driver.display.renderer.getWorld().getTerrain().lineOfSight(position, cameraPosition);
	}
	
	public final Vector3f getPosition()
	{
		return position;
	}
	
	public final float getFacingDirection()
	{
		return facingDirection;
	}
}
*/