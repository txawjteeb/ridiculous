package org.cart.igd.util;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class TextureLoader
{
	private HashMap<String, Texture> table = new HashMap<String, Texture>();
	private ColorModel glAlphaColorModel;
	private ColorModel glColorModel;
	
	public TextureLoader()
	{
		glAlphaColorModel = new ComponentColorModel
			(
				ColorSpace.getInstance( ColorSpace.CS_sRGB ),
				new int[] { 8, 8, 8, 8 },
				true,
				false,
				ComponentColorModel.TRANSLUCENT,
				DataBuffer.TYPE_BYTE
			);
		
		glColorModel = new ComponentColorModel
			(
				ColorSpace.getInstance( ColorSpace.CS_sRGB ),
				new int[] { 8, 8, 8, 8 },
				false,
				false,
				ComponentColorModel.OPAQUE,
				DataBuffer.TYPE_BYTE
			);
	}
	
	private int createTextureID(GL gl)
	{
		int[] tmp = new int[ 1 ];
		gl.glGenTextures( 1, IntBuffer.wrap( tmp ) );
		return tmp[ 0 ];
	}
	
	public Texture getTexture( String resourceName, GL gl, GLU glu ) throws IOException
	{
		Texture tex = table.get( resourceName );
		if( tex != null ) return tex;
		tex = getTexture( resourceName, GL.GL_TEXTURE_2D, GL.GL_RGBA, GL.GL_LINEAR, GL.GL_LINEAR, gl, glu );
		table.put( resourceName, tex );
		
		System.out.println(tex.getId()+" "+resourceName);
		
		return tex;
	}
	
	public Texture getTexture( String resourceName, int angle, GL gl, GLU glu ) throws IOException
	{
		Texture tex = table.get( resourceName + "." + angle );
		if( tex != null ) return tex;
		tex = getTexture( resourceName, GL.GL_TEXTURE_2D, GL.GL_RGBA, GL.GL_LINEAR, GL.GL_LINEAR, angle, gl, glu );
		table.put( resourceName + "." + angle, tex );
		
		return tex;
	}
	
	public Texture getTexture( BufferedImage bi, GL gl, GLU glu )
	{
		final int target = GL.GL_TEXTURE_2D;
		final int dstPixelFormat = GL.GL_RGBA;
		final int minFilter = GL.GL_NEAREST;
		final int magFilter = GL.GL_NEAREST;
		int srcPixelFormat = 0;
		
		int textureID = createTextureID(gl);
		Texture texture = new Texture( target, textureID );
		
		gl.glBindTexture( target, textureID );
		
		BufferedImage bufferedImage = bi;
		texture.setWidth( bufferedImage.getWidth() );
		texture.setHeight( bufferedImage.getHeight() );
		texture.imageWidth = bufferedImage.getWidth();
		texture.imageHeight = bufferedImage.getHeight();
		
		if( bufferedImage.getColorModel().hasAlpha() )
			srcPixelFormat = GL.GL_RGBA;
		else
			srcPixelFormat = GL.GL_RGB;
		
		ByteBuffer textureBuffer = convertImageData( bufferedImage, texture );
		
		/*
		if( target == GL.GL_TEXTURE_2D )
		{
			gl.glTexParameteri( target, GL.GL_TEXTURE_MIN_FILTER, minFilter );
			gl.glTexParameteri( target, GL.GL_TEXTURE_MAG_FILTER, magFilter );
		}*/
		
		//gl.glTexEnvf( GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
	    glu.gluBuild2DMipmaps( GL.GL_TEXTURE_2D, dstPixelFormat, bufferedImage.getWidth(), bufferedImage.getHeight(), srcPixelFormat, GL.GL_UNSIGNED_BYTE, textureBuffer );
		
		/*gl.glTexImage2D
		(
			target,
			0,
			dstPixelFormat,
			 bufferedImage.getWidth() ,
			 bufferedImage.getHeight() ,
			0,
			srcPixelFormat,
			GL.GL_UNSIGNED_BYTE,
			textureBuffer
		);*/
		
		return texture;
	}
	
	public Texture getTexture( String resourceName, int target, int dstPixelFormat, int minFilter, int magFilter, GL gl, GLU glu ) throws IOException
	{
		int srcPixelFormat = 0;
		int textureID = createTextureID(gl);
		Texture texture = new Texture( target, textureID );
		
		gl.glBindTexture( target, textureID );
		
		BufferedImage bufferedImage = copyImage( loadImage( resourceName ) );
		texture.setWidth(  bufferedImage.getWidth()  );
		texture.setHeight(  bufferedImage.getHeight()  );
		texture.imageWidth = bufferedImage.getWidth();
		texture.imageHeight = bufferedImage.getHeight();
		
		if( bufferedImage.getColorModel().hasAlpha() )
			srcPixelFormat = GL.GL_RGBA;
		else
			srcPixelFormat = GL.GL_RGB;
		
		ByteBuffer textureBuffer = convertImageData( bufferedImage, texture );
		
		/*
		if( target == GL.GL_TEXTURE_2D )
		{
			gl.glTexParameteri( target, GL.GL_TEXTURE_MIN_FILTER, minFilter );
			gl.glTexParameteri( target, GL.GL_TEXTURE_MAG_FILTER, magFilter );
			gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
			gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
		}
		
		gl.glTexImage2D
		(
			target,
			0,
			dstPixelFormat,
			bufferedImage.getWidth(),
			bufferedImage.getHeight(),
			0,
			srcPixelFormat,
			GL.GL_UNSIGNED_BYTE,
			textureBuffer
		);*/
		
		//gl.glTexEnvf( GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
	    glu.gluBuild2DMipmaps( GL.GL_TEXTURE_2D, dstPixelFormat, bufferedImage.getWidth(), bufferedImage.getHeight(), srcPixelFormat, GL.GL_UNSIGNED_BYTE, textureBuffer );
		
		return texture;
	}
	
	public Texture getTexture( String resourceName, int target, int dstPixelFormat, int minFilter, int magFilter, int angle, GL gl, GLU glu ) throws IOException
	{
		int srcPixelFormat = 0;
		int textureID = createTextureID(gl);
		Texture texture = new Texture( target, textureID );
		
		gl.glBindTexture( target, textureID );
		
		BufferedImage bufferedImage = copyImageAndRotate( loadImage( resourceName ), angle );
		texture.setWidth(  bufferedImage.getWidth()  );
		texture.setHeight( bufferedImage.getHeight()  );
		texture.imageWidth = bufferedImage.getWidth();
		texture.imageHeight = bufferedImage.getHeight();
		
		if( bufferedImage.getColorModel().hasAlpha() )
			srcPixelFormat = GL.GL_RGBA;
		else
			srcPixelFormat = GL.GL_RGB;
		
		ByteBuffer textureBuffer = convertImageData( bufferedImage, texture );
		
		/*
		if( target == GL.GL_TEXTURE_2D )
		{
			gl.glTexParameteri( target, GL.GL_TEXTURE_MIN_FILTER, minFilter );
			gl.glTexParameteri( target, GL.GL_TEXTURE_MAG_FILTER, magFilter );
			gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
			gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
		}
		
		gl.glTexImage2D
		(
			target,
			0,
			dstPixelFormat,
			bufferedImage.getWidth(),
			bufferedImage.getHeight(),
			0,
			srcPixelFormat,
			GL.GL_UNSIGNED_BYTE,
			textureBuffer
		);*/
		//gl.glTexEnvf( GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
	    gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
	    glu.gluBuild2DMipmaps( GL.GL_TEXTURE_2D, dstPixelFormat, bufferedImage.getWidth(), bufferedImage.getHeight(), srcPixelFormat, GL.GL_UNSIGNED_BYTE, textureBuffer );
		
		return texture;
	}
	
//	private int get2Fold( int fold )
//	{
	//	int ret = 2;
////		while( ret<fold ) ret *= 2;
	//	return ret;
//	}
	
	private BufferedImage copyImage( BufferedImage src )
	{
		BufferedImage bi = new BufferedImage( src.getHeight(), src.getWidth(), src.getTransparency() );
		bi.getGraphics().drawImage( src, 0, 0, null );
		return bi;
	}
	
	private BufferedImage copyImageAndRotate( BufferedImage src, int angle )
	{
		BufferedImage bi = new BufferedImage( src.getHeight(), src.getWidth(), src.getTransparency() );

		Graphics2D g = (Graphics2D) bi.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.rotate( Math.toRadians(angle), src.getWidth()/2, src.getWidth()/2 );
		g.drawImage( src, 0, 0, null );
		g.dispose();
		return bi;
	}
	
	private ByteBuffer convertImageData( BufferedImage bufferedImage, Texture texture )
	{
		WritableRaster raster;
		BufferedImage texImage;
		
		int texWidth = 2;
		int texHeight = 2;
		
		while( texWidth < bufferedImage.getWidth() ) texWidth *= 2;
		while( texHeight < bufferedImage.getHeight() ) texHeight *= 2;
		
		texture.setTextureHeight( texHeight );
		texture.setTextureWidth( texWidth );
		
		if( bufferedImage.getColorModel().hasAlpha() )
		{
			raster = Raster.createInterleavedRaster( DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null );
			texImage = new BufferedImage( glAlphaColorModel, raster, false, new Hashtable() );
		}
		else
		{
			raster = Raster.createInterleavedRaster( DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null );
			texImage = new BufferedImage( glColorModel, raster, false, new Hashtable() );
		}
		
		Graphics2D g = ( Graphics2D ) texImage.getGraphics();
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g.setColor( new Color( 0.0f, 0.0f, 0.0f, 0.0f ) );
		g.fillRect( 0, 0, texWidth, texHeight );
		g.drawImage( bufferedImage, 0, 0, null );
		
		byte[] data = ( ( DataBufferByte ) texImage.getRaster().getDataBuffer() ).getData();
		
		return ByteBuffer.wrap( data );
	}
	
	private BufferedImage loadImage( String ref ) throws IOException
	{
		BufferedImage bi = ImageIO.read( ResourceRetriever.getResourceAsStream(ref) );
		return bi;
	}
}