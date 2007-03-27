package org.cart.igd.util;

import java.nio.IntBuffer;
import javax.media.opengl.GL;

public class Texture
{
	private int target;
	private int textureID;
	private int height, width;
	private int texWidth, texHeight;
	private float widthRatio, heightRatio;
	public int imageWidth, imageHeight;
	
	private final int[] multiTex = new int[]
	{
		GL.GL_TEXTURE0,
		GL.GL_TEXTURE1,
		GL.GL_TEXTURE2,
		GL.GL_TEXTURE3
	};
	
	protected Texture( int target, int textureID )
	{
		this.target = target;
		this.textureID = textureID;
	}
	
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
	
	public void bind(GL gl)
	{
		gl.glBindTexture( target, textureID );
	}
	
	public void delete(GL gl)
	{
		gl.glDeleteTextures( 1, IntBuffer.wrap(new int[] {textureID}) );
	}
	
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