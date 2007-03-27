package org.cart.igd.opengl;

import org.cart.igd.Display;
import org.cart.igd.util.Texture;
import javax.media.opengl.GL;

public class SkyBox
{
	private final int SKYFRONT = 0;
	private final int SKYBACK = 1;
	private final int SKYLEFT = 2;
	private final int SKYRIGHT = 3;
	private final int SKYUP = 4;
	private final int SKYDOWN = 5;
	
	private Texture[] texture = new Texture[6];
	
	public SkyBox(String[] fnm, GL gl)
	{
		try
		{
			for(int i=0; i<6; i++)
				texture[i] = Display.renderer.textureLoader.getTexture(fnm[i], gl, Display.renderer.glu);
		}
		catch(java.io.IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void render(GL gl, float x, float y, float z, float width, float height, float length)
	{
		x-=width/2f;
		y-=height/2f;
		z-=length/2f;
		
//		 Draw Front side
		texture[SKYFRONT].bind(gl);
		gl.glBegin(GL.GL_QUADS);
			//gl.glColor4fv(ColorRGBA.Blue.getFloatBuffer());
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(x,		  y,		z+length);
			
			//gl.glColor4fv(ColorRGBA.Black.getFloatBuffer());
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(x,		  y+height, z+length);
			
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(x+width, y+height, z+length);
			
			//gl.glColor4fv(ColorRGBA.Blue.getFloatBuffer());
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(x+width, y,		z+length);
		gl.glEnd();

		// Draw Back side
		texture[SKYBACK].bind(gl);
		gl.glBegin(GL.GL_QUADS);
			//gl.glColor4fv(ColorRGBA.Blue.getFloatBuffer());
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(x+width, y,		z);
			
			//gl.glColor4fv(ColorRGBA.Black.getFloatBuffer());
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(x+width, y+height, z);
			
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(x,		  y+height,	z);
			
			//gl.glColor4fv(ColorRGBA.Blue.getFloatBuffer());
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(x,		  y,		z);
		gl.glEnd();

		// Draw Left side
		texture[SKYLEFT].bind(gl);
		gl.glBegin(GL.GL_QUADS);
			//gl.glColor4fv(ColorRGBA.Black.getFloatBuffer());
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(x,		  y+height,	z);
			
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(x,		  y+height,	z+length); 
			
			//gl.glColor4fv(ColorRGBA.Blue.getFloatBuffer());
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(x,		  y,		z+length);
			
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(x,		  y,		z);		
		gl.glEnd();

		// Draw Right side
		texture[SKYRIGHT].bind(gl);
		gl.glBegin(GL.GL_QUADS);
			//gl.glColor4fv(ColorRGBA.Blue.getFloatBuffer());
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(x+width, y,			z);
			
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(x+width, y,			z+length);
			
			//gl.glColor4fv(ColorRGBA.Black.getFloatBuffer());
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(x+width, y+height,	z+length); 
			
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(x+width, y+height,	z);
		gl.glEnd();

		// Draw Up side
		texture[SKYDOWN].bind(gl);
		gl.glBegin(GL.GL_QUADS);
			//gl.glColor4fv(ColorRGBA.Black.getFloatBuffer());
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(x+width,   y+height, z);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(x+width,   y+height, z+length); 
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(x,		  y+height,	z+length);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(x,		  y+height,	z);
		gl.glEnd();

		// Draw Down side
		texture[SKYUP].bind(gl);
		gl.glBegin(GL.GL_QUADS);
			//gl.glColor4fv(ColorRGBA.Blue.getFloatBuffer());
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(x,	    y,		z);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(x,	    y,		z+length);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(x+width, y,		z+length); 
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(x+width, y,		z);
		gl.glEnd();
	}
}
