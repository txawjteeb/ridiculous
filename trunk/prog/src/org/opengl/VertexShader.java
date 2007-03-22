package org.cart.igd.opengl;

import com.sun.opengl.util.BufferUtil;
import javax.media.opengl.*;
import java.io.*;
import java.nio.*;

import org.cart.igd.util.ResourceRetriever;

public class VertexShader
{
	public static final int SIZE = 64;
	public static final float TWO_PI = (float) (Math.PI*2);
	
	public boolean vertexShaderSupported;
	public boolean vertexShaderEnabled;
	
	private int programObject;
	private int waveAttrib;
	
	private float[][][] mesh = new float[SIZE][SIZE][3];
	private float wave_movement = 0.0f;
	
	public VertexShader(boolean b)
	{
		vertexShaderEnabled = b;
	}
	
	public void init(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();
		
		String extensions = gl.glGetString(GL.GL_EXTENSIONS);
		vertexShaderSupported = extensions.indexOf("GL_ARG_vertex_shader")!=-1;
		
		if(vertexShaderSupported)
		{
			String shaderSource;
			try
			{
				BufferedReader shaderReader = new BufferedReader(new InputStreamReader( ResourceRetriever.getResourceAsStream("data/shaders/Wave.glsl")));
				StringWriter shaderWriter = new StringWriter();
				String line = shaderReader.readLine();
				while(line!=null)
				{
					shaderWriter.write(line);
					shaderWriter.write("\n");
					line = shaderReader.readLine();
				}
				shaderSource = shaderWriter.getBuffer().toString();
			}
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
			
			if(shaderSource!=null)
			{
				int shader = gl.glCreateShaderObjectARB(GL.GL_VERTEX_SHADER_ARB);
				gl.glShaderSourceARB(shader, 1, new String[]{shaderSource}, (int[])null, 0);
				gl.glCompileShaderARB(shader);
				checkLogInfo(gl, shader);
				programObject = gl.glCreateProgramObjectARB();
				gl.glAttachObjectARB(programObject, shader);
				gl.glLinkProgramARB(programObject);
				gl.glValidateProgramARB(programObject);
				checkLogInfo(gl, programObject);
				waveAttrib = gl.glGetAttribLocationARB(programObject,"wave");
			}
		}
		
		for(int x=0; x<SIZE; x++)
		{
			for(int z=0; z<SIZE; z++)
			{
				mesh[x][z][0] = (float)(SIZE/2)-x;
				mesh[x][z][1] = 0.0f;
				mesh[x][z][2] = (float)(SIZE/2)-z;
			}
		}
	}
	
	public void display(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();
		
		if(vertexShaderEnabled)
			gl.glUseProgramObjectARB(programObject);
		
		gl.glColor3f(0.5f, 1f, 0.5f);
		for(int x=0; x<SIZE-1; x++)
		{
			gl.glBegin(GL.GL_TRIANGLE_STRIP);
			for(int z=0; z<SIZE-1; z++)
			{
				if(vertexShaderEnabled)
					gl.glVertexAttrib1f(waveAttrib, wave_movement);
				gl.glVertex3f(mesh[x][z][0], mesh[x][z][1], mesh[x][z][2]);        // Draw Vertex
				gl.glVertex3f(mesh[x + 1][z][0], mesh[x + 1][z][1], mesh[x + 1][z][2]);    // Draw Vertex
				wave_movement += 0.00001f;                                    // Increment Our Wave Movement
				if(wave_movement > TWO_PI) wave_movement = 0.0f;
			}
			gl.glEnd();
		}
		
		if(vertexShaderEnabled)
			gl.glUseProgramObjectARB(0);
	}
	
	private void checkLogInfo(GL gl, int obj)
	{
		IntBuffer iVal = BufferUtil.newIntBuffer(1);
		gl.glGetObjectParameterivARB(obj, GL.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);
		int length = iVal.get();
		if(length<=1) return;
		ByteBuffer infoLog = BufferUtil.newByteBuffer(length);
		iVal.flip();
		gl.glGetInfoLogARB(obj, length, iVal, infoLog);
		byte[] infoBytes = new byte[length];
		infoLog.get(infoBytes);
		System.out.println("GLSL Validation >> "+new String(infoBytes));
	}
}
