package org.cart.igd.entity;

import java.io.File;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.util.ColorRGBA;
import org.cart.igd.math.Vector3f;
import org.cart.igd.core.Kernel;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.discreet.Model;
import java.util.*;
//import org.cart.igd.model.ModelManager;
//import org.cart.igd.bsp.BSPObject;

public abstract class Entity
{
	public static ArrayList <Entity>allEntities = new ArrayList<Entity>();
	public static int globalIdCounter=0;
	public int globalId;
	
	public Vector3f position;
	public Vector3f lastPosition;
	public Vector3f scale = new Vector3f(1f,1f,1f);
	public float facingDirection = 0.0f;
	public int id = 0;
	
	public float speed = 0.01f;
	public float turnSpeed = 0.1f;
	
	public OBJModel modelObj;
	public Model model3ds;

	protected float deltaTime;
	protected float animationSpeed = 7.0f;
	
	protected GL gl;
	protected GLU glu;
	
	protected ColorRGBA color = new ColorRGBA(1f, 1f, 1f, 0.9f);
	protected boolean drawBoundingSphere;
	
	public float boundingSphereRadius;
	
	public float yRotationRad = 0f;
	
	public Entity(Vector3f pos, float fD, float bsr)//, int id, File meshFile, File skinFile)// throws EntityException
	{
		globalId = globalIdCounter++;
		gl = Kernel.display.getRenderer().getGL();
		/*if(this.gl==null)
			throw new EntityException("**** No drawable.gl given ****");
		*/
		glu = Kernel.display.getRenderer().getGLU();
		/*if(this.glu==null)
			throw new EntityException("**** No drawable.glu given ****");
		*/
		position = pos;
		lastPosition = pos;
		/*if(this.position==null)
			throw new EntityException("**** No position vector given ****");
		*/
		
		this.facingDirection = fD;
		//this.id = id;
		this.boundingSphereRadius = bsr;
		
		/*
		if(meshFile!=null)
			loadModel(meshFile, skinFile);
		*/
	}
	
	/**
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param Model model: .obj format file data
	 **/
	public Entity(Vector3f pos, float fD, float bsr, OBJModel model)//, int id, File meshFile, File skinFile)// throws EntityException
	{
		this(pos,fD,bsr);
		this.modelObj = model;
		globalId = globalIdCounter++;
	}
	public Entity(Vector3f pos, float fD, float bsr, OBJModel model,float scale)//, int id, File meshFile, File skinFile)// throws EntityException
	{
		this(pos,fD,bsr);
		this.modelObj = model;
		this.scale.x = scale;
		this.scale.y = scale;
		this.scale.z = scale;
		globalId = globalIdCounter++;
	}
	
	public Entity(Vector3f pos, float fD, float bsr, Model model)//, int id, File meshFile, File skinFile)// throws EntityException
	{
		this(pos,fD,bsr);
		this.model3ds = model;
		globalId = globalIdCounter++;
	}
	
	public void update(long elapseTime){
		
	}
	
	/*
	public Entity(float[] position, float facingDirection, float bsr, int id, File meshFile, File skinFile) //throws EntityException
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
	*/
	
	public final int getID()
	{
		return id;
	}
	
	/*
	protected boolean isVisible(Vector3f cameraPosition)
	{
		return Driver.display.renderer.sphereInFrustrum(position, boundingSphereRadius) && Driver.display.renderer.getWorld().getTerrain().lineOfSight(position, cameraPosition);
	}
	*/
	
	public final Vector3f getPosition()
	{
		return position;
	}
	
	public final float getFacingDirection()
	{
		return facingDirection;
	}
	
	
	public final void setYRotationDeg(float deg){
		yRotationRad = deg*0.0174532925f;
	}
	
	public final void setYRotationRad(float rad){
		this.yRotationRad = rad;
	}
	
	public final float getYRotationDeg(){
		return yRotationRad*57.29577951f;
	}
	
	public final float getYRotationRad(){
		return yRotationRad;
	}
	
	public final void walkForward(long elapsedTime)
	{
		position.x += ( ((float)elapsedTime * speed) * (float)Math.cos(facingDirection * 0.0174f) );
		position.z += ( ((float)elapsedTime * speed) * (float)Math.sin(facingDirection * 0.0174f) );
	}
	
	public final void walkBackward(long elapsedTime)
	{
		position.x -= ( ((float)elapsedTime * speed) * (float)Math.cos(facingDirection * 0.0174f) );
		position.z -= ( ((float)elapsedTime * speed) * (float)Math.sin(facingDirection * 0.0174f) );
	}
	
	public final void strafeLeft(long elapsedTime)
	{
		position.x += ( ((float)elapsedTime * speed) * (float)Math.cos((facingDirection-90) * 0.0174f) );
		position.z += ( ((float)elapsedTime * speed) * (float)Math.sin((facingDirection-90) * 0.0174f) );
	}
	
	public final void strafeRight(long elapsedTime)
	{
		position.x += ( ((float)elapsedTime * speed) * (float)Math.cos((facingDirection+90) * 0.0174f) );
		position.z += ( ((float)elapsedTime * speed) * (float)Math.sin((facingDirection+90) * 0.0174f) );
	}
	
	public final void turnRight(long elapsedTime)
	{
		facingDirection += ((float)elapsedTime * turnSpeed);
	}
	
	public final void turnLeft(long elapsedTime)
	{
		facingDirection -= ((float)elapsedTime * turnSpeed);
	}
	
	/**
	 * render the model depending on which type was asigned
	 * */
	public void render(GL gl){
		if(model3ds!= null){
			gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, position.z);
			gl.glRotatef(facingDirection, 0f, -1f, 0f);
			//gl.glScalef(.1f,.1f,.1f);
			model3ds.render(gl);
			gl.glPopMatrix();
		}
		if(modelObj!= null){
			gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y -2f, position.z);
			gl.glRotatef(facingDirection, 0f, -1f, 0f);
			//gl.glScalef(scale.x,scale.y,scale.z);
			modelObj.draw(gl);
			gl.glPopMatrix();
		}
	}
	
	public String getName(){
		return "obj @ x: "+position.x+" y: "+position.y+" z "+position.z;
	}
}