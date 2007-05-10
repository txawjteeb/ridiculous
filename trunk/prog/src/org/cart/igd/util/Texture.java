package org.cart.igd.util;

import java.nio.IntBuffer;
import javax.media.opengl.GL;

/**
 * Texture.java
 *
 * General Function: Holds texture data for OpenGL use.
 */
public class Texture
{
	/* Texture Target */
	private int target;
	
	/* OpengGL Texture ID */
	private int textureID;
	
	/* Height of the Image. */
	private int height;
	
	/* Width of the Image. */
	private int width;
	
	/* Width of the Texture. */
	private int texWidth;
	
	/* Height of the Texture. */
	private int texHeight;
	
	/* Width Ratio. */
	private float widthRatio;
	
	/* Height Ratio. */
	private float heightRatio;
	
	/* Simple Image Width. */
	public int imageWidth;
	
	/* Simple Image Height. */
	public int imageHeight;
	
	/* MultTexture GL variables. */
	private final int[] multiTex = new int[]
	{
		GL.GL_TEXTURE0,
		GL.GL_TEXTURE1,
		GL.GL_TEXTURE2,
		GL.GL_TEXTURE3
	};
	
	/**
	 * Constructor
	 *
	 * General Function: Creates a Texture instance.
	 */
	protected Texture( int target, int textureID )
	{
		this.target = target;
		this.textureID = textureID;
	}
	
	/**
	 * bindAsMulti
	 *
	 * General Function: Binds a texture to GL as a multi-texture.
	 *
	 * @param gl The GL instance to render to.
	 * @param i The GL MultiTexture index.
	 */
	public void bindAsMulti(GL gl, int i)
	{
		gl.glActiveTexture(multiTex[i]);
		gl.glEnable(target);
		bind(gl);
		gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_OBJECT_LINEAR);
		gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, GL.GL_OBJECT_LINEAR);
		gl.glEnable(GL.GL_TEXTURE_GEN_S);
		gl.glEnable(GL.GL_TEXTURE_GEN_T);
	}
	
	/**
	 * bind
	 *
	 * General Function: Binds a texture to GL.
	 *
	 * @param gl The GL instance to render to.
	 */
	public void bind(GL gl)
	{
		gl.glBindTexture( target, textureID );
	}
	
	/**
	 * delete
	 *
	 * General Function: Deletes a texture from GL space.
	 *
	 * @param gl The GL isntance to render to.
	 */
	public void delete(GL gl)
	{
		gl.glDeleteTextures( 1, IntBuffer.wrap(new int[] {textureID}) );
	}
	
	/**
	 * setHeight
	 *
	 * General Function: Sets the height of the texture.
	 *
	 * @param height The height to set.
	 */
	public void setHeight(int height)
	{
		this.height = height;
		setHeight();
	}
	
	public void setWidth(int width)
	{
		this.width = width;
		setWidth();
	}
	
	public void setTextureHeight(int height)
	{
		texHeight = height;
		setHeight();
	}
	
	public void setTextureWidth(int width)
	{
		texWidth = width;
		setWidth();
	}
	
	public int getTextureWidth()
	{
		return texWidth;
	}
	
	public int getTextureHeight()
	{
		return texHeight;
	}
	
	public int getImageWidth()
	{
		return width;
	}
	
	public int getImageHeight()
	{
		return height;
	}
	
	public float getWidth()
	{
		return widthRatio;
	}
	
	public float getHeight()
	{
		return heightRatio;
	}
	
	private void setWidth()
	{
		if(texWidth!=0)
			widthRatio = ((float)width/(float)texWidth);
	}
	
	private void setHeight()
	{
		if(texHeight!=0)
			heightRatio = ((float)height/(float)texHeight);
	}
	
	public int getId()
	{
		return target;
	}
}