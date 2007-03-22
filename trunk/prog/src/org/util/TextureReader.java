package org.cart.igd.util;

import com.sun.opengl.util.BufferUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TextureReader
{
	public static Texture readTexture(String filename) throws IOException
	{
		return readTexture(filename, false);
	}
	
	public static Texture readTexture(String filename, boolean alpha) throws IOException
	{
		BufferedImage bi;
		if(filename.endsWith(".bmp"))
			bi = BitmapLoader.loadBitmap(filename);
		else
			bi = readImage(filename);
		return readPixels(bi, alpha);
	}
	
	private static BufferedImage readImage(String filename) throws IOException
	{
		return ImageIO.read(ResourceRetriever.getResourceAsStream(filename));
	}
	
	private static Texture readPixels(BufferedImage img, boolean alpha)
	{
		int[] pp = new int[img.getWidth()*img.getHeight()];
		PixelGrabber pg = new PixelGrabber(img, 0, 0, img.getWidth(), img.getHeight(), pp, 0, img.getWidth());
		try { pg.grabPixels(); }
		catch(InterruptedException e) { throw new RuntimeException(); }
		
		int bpp = alpha?4:3;
		ByteBuffer upp = BufferUtil.newByteBuffer(pp.length*bpp);
		
		for(int r=img.getHeight()-1; r>=0; r--)
		{
			for(int c=0; c<img.getWidth(); c++)
			{
				int p = pp[r*img.getWidth()+c];
				upp.put((byte)((p>>16)&0xFF));
				upp.put((byte)((p>>8)&0xFF));
				upp.put((byte)((p>>0)&0xFF));
				if(alpha) upp.put((byte)((p>>24)&0xFF));
			}
		}
		
		upp.flip();
		return new Texture(upp, img.getWidth(), img.getHeight());
	}
	
	public static class Texture
	{
		private ByteBuffer pixels;
		private int width, height;
		
		public Texture(ByteBuffer pixels, int width, int height)
		{
			this.height = height;
			this.width = width;
			this.pixels = pixels;
		}
		
		public int getHeight() { return height; }
		
		public int getWidth() { return width; }
		
		public ByteBuffer getPixels() { return pixels; }
	}
}
