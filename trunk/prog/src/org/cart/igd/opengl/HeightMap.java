package org.cart.igd.opengl;

import javax.media.opengl.*;

import java.io.InputStream;
import java.io.IOException;

import org.cart.igd.util.ResourceRetriever;
import org.cart.igd.Display;
import org.cart.igd.util.Texture;
import org.cart.igd.math.Vector3f;

public class HeightMap
{
	
	private byte[] heightMap;
	//private Vector3f[] tangents;
	//private Vector3f[] binormals;
	//private Vector3f[] normals;
	
	public int mapSize = 2048;
	public int stepSize = 16;
	public float scaleValue = 40f;
	public float heightRatio = 1.5f;
	public Texture texture;
	private int displayList; 
	private int pc = 0;
	
	public HeightMap(GL gl, String fn, String tex, int mapSize, int stepSize, float scaleValue)
	{
		this.mapSize = mapSize;
		this.stepSize = stepSize;
		this.scaleValue = scaleValue;
		heightMap = new byte[mapSize*mapSize];
		
		try
		{
			loadRawFile(fn);
			texture = Display.renderer.textureLoader.getTexture("data/images/land.jpg", gl, Display.renderer.glu);
			//texture = TextureLoader.loadTexture(gl, tex, false);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		
		createDisplayList(gl);
	}
	
	public Vector3f getVertexPoint(int x, int y)
	{
		x*=stepSize;
		y*=stepSize;
		return new Vector3f(x, height(x,y), y);
	}
	
	public float interpolateGridHeight(float x, float z, int ss)
	{
		float h00 = height((int)x,(int)z);
		float h10 = height((int)x+ss,(int)z);
		float h01 = height((int)x,(int)z+ss);
		float h11 = height((int)x+ss,(int)z+ss);
		float h1=h00;
		float h2=h10;
		float h3=h01;
		float h4=h11;
		float a00 = h1;
		float a10 = h2-h1;
		float a01=h3-h1;
		float a11=h1-h2-h3+h4;
		float partialx=x-(int)x;
		float partialz =z-(int)z;
		float hi=a00+(a10*partialx)+(a01*partialz)+(a11*partialx*partialz);
		return hi;
	}
	
	public void render(GL gl)
	{
		gl.glCallList(displayList);
		org.cart.igd.Display.renderer.polyCount += pc;
	}
	
	public void createDisplayList(GL gl)
	{
		displayList = gl.glGenLists(1);
		gl.glNewList(displayList, GL.GL_COMPILE);
		
		gl.glPushMatrix();
		gl.glDisable(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_LIGHT0);
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		
		int pc = 0;
		
        for(int X=0; X<(mapSize-stepSize); X+=stepSize)
        {
        	gl.glBegin(GL.GL_TRIANGLE_STRIP);
        	for(int Y=0; Y<(mapSize-stepSize); Y+=stepSize)
        	{
        		texture.bind(gl);
        		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        		
                int x = X;
                int y = height(X, Y);
                int z = Y;
                
                gl.glTexCoord2d(0f, 0f);
                setVertexColor(gl, x, y);
                gl.glVertex3i(x, y, z);

                x = X;
                y = height(X, Y+stepSize);
                z = Y + stepSize;

                gl.glTexCoord2d(1f, 0f);
                setVertexColor(gl, x, y);
                gl.glVertex3i(x, y, z);

                x = X + stepSize;
                y = height(X+stepSize, Y);
                z = Y;

                gl.glTexCoord2d(0f, 1f);
                setVertexColor(gl, x, y);
                gl.glVertex3i(x, y, z);

                x = X + stepSize;
                y = height(X+stepSize, Y+stepSize);
                z = Y + stepSize;

                gl.glTexCoord2d(1f, 1f);
                setVertexColor(gl, x, y);
                gl.glVertex3i(x, y, z);
                
                pc++;
            }
        	gl.glEnd();
        }
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		
		gl.glPopMatrix();
		gl.glEndList();
		
		this.pc = pc / 3;
	}
	
	private void setVertexColor(GL gl, int x, int y)
	{
		//float fColor = -0.15f+(height(x,y)/256.0f);
		float fColor = (float)y/255.0f+0.25f;
		if(fColor>1f) fColor = 1f;
		gl.glColor3f(fColor, fColor, fColor);
	}
	
	private int height(int X, int Y)
	{
		int x = X % mapSize;
		int y = Y % mapSize;
		return heightMap[x+(y*mapSize)]&0xFF;
	}
	
	private void loadRawFile(String fn) throws IOException
	{
		InputStream input = ResourceRetriever.getResourceAsStream(fn);
		readBuffer(input, heightMap);
		input.close();
		for(int i=0; i<heightMap.length; i++)
			heightMap[i] &= 0xFF;
	}
	
	private static void readBuffer(InputStream input, byte[] buffer) throws IOException
	{
		int bytesRead = 0;
		int bytesToRead = buffer.length;
		while(bytesToRead>0)
		{
			int read = input.read(buffer, bytesRead, bytesToRead);
			bytesRead += read;
			bytesToRead -= read;
		}
	}
}
