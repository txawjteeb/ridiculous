package org.cart.igd.opengl;

import javax.media.opengl.*;

public class Multisampling
{
	public boolean arbMultisampleSupported = false;
	public int arbMultisampleFormat = 0;
	
	public Multisampling(GL gl)
	{
		String supported = gl.glGetString(GL.GL_EXTENSIONS);
		if(supported==null)
			arbMultisampleSupported = false;
		
	}
}
