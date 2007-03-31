package org.cart.igd.opengl;

import org.cart.igd.math.*;
import org.cart.igd.util.ColorRGBA;
import javax.media.opengl.GL;

public class SkyDome
{
	public float radius;
	public int divisions;
	public double zAngleStart;
	public double zAngleEnd;
	public double yAngleStart;
	public double yAngleEnd;
	
	private int displayList;
	
	private Vector3f[][][] vertices;
	private Vector3f[][][] normals;
	private ColorRGBA[][][] colors;
	private Point2d[][][] texCoords;
	
	private ColorRGBA skyColor = new ColorRGBA(0.17f, 0.65f, 0.92f, 1.0f);
	
	public SkyDome(double yAngleStart, int divisions, float radius, ColorRGBA sc, GL gl)
	{
		this.skyColor = sc;
		
		this.zAngleStart = 0;
	    this.zAngleEnd = Math.PI*2;
	    
	    this.yAngleStart = yAngleStart;
	    this.yAngleEnd = Math.PI;

	    double rho, drho, theta, dtheta;
	    double vx, vy, vz;
	    double s, t, ds, dt;
	    int i, j;
	    double sign;

	    this.radius = radius;
	    this.divisions = divisions;
	    
	    ColorRGBA startColor = new ColorRGBA( 0, 51, 51 );
	    ColorRGBA endColor = new ColorRGBA( 0, 102, 102 );
	    
	    float colorSteps = 50.0f;
	    float[] colorChange = new float[]
	    {
	    	(startColor.getRed()-endColor.getRed()) / colorSteps,
	    	(startColor.getGreen()-endColor.getGreen()) / colorSteps,
	    	(startColor.getBlue()-endColor.getBlue()) / colorSteps
	    };
	    
	    sign = -1.0;
	    dtheta = Math.abs(zAngleEnd-zAngleStart) / (double)divisions;
	    drho = Math.abs(yAngleEnd-zAngleStart) / (double)divisions;
	    
	    t = 0.0;
	    ds = 1.0 / divisions;
	    dt = 1.0 / divisions;
	    
	    vertices = new Vector3f[divisions+1][divisions+1][2];
	    normals = new Vector3f[divisions+1][divisions+1][2];
	    texCoords = new Point2d[divisions+1][divisions+1][2];
	    colors = new ColorRGBA[divisions+1][divisions+1][2];
	    for(i=divisions; i>0; i--)
	    {
	    	rho = yAngleEnd - ((double)i * drho);
	    	s = 0.0;
	    	for(j=0; j<=divisions; j++)
	    	{
	    		if(j==divisions)
	    			theta = zAngleStart;
	    		else
	    			theta = zAngleStart + ((double)j) * dtheta;
	    		
	    		vz = -Math.sin(theta) * Math.sin(rho);
	    		vx = Math.cos(theta) * Math.sin(rho);
	    		vy = Math.cos(rho);
	    		
	    		normals[i][j][0] = new Vector3f( (float)(vx*sign), (float)(vy*sign), (float)(vz*sign) );
	    		texCoords[i][j][0] = new Point2d( s, t+dt );
	    		colors[i][j][0] = new ColorRGBA( startColor.getRed(), startColor.getGreen(), startColor.getBlue() );
	    		vertices[i][j][0] = new Vector3f( (float)(vx*radius), (float)(vy*radius), (float)(vz*radius) );
	    		
	    		vz = -Math.sin(theta) * Math.sin(rho+drho);
	    		vx = Math.cos(theta) * Math.sin(rho+drho);
	    		vy = Math.cos(rho+drho);
	    		
	    		normals[i][j][1] = new Vector3f( (float)(vx*sign), (float)(vy*sign), (float)(vz*sign) );
	    		texCoords[i][j][1] = new Point2d( s, t );
	    		colors[i][j][1] = new ColorRGBA( startColor.getRed()-colorChange[0], startColor.getGreen()-colorChange[1], startColor.getBlue()-colorChange[2] );
	    		vertices[i][j][1] = new Vector3f( (float)(vx*radius), (float)(vy*radius), (float)(vz*radius) );
	    		
	    		s += ds;
	    	}
	    	
	    	if(i>divisions-colorSteps)
	    	{
	    		startColor.rgba[0] -= colorChange[0];
	    		startColor.rgba[1] -= colorChange[1];
	    		startColor.rgba[2] -= colorChange[2];
	    	}
	    	else if(i==divisions-colorSteps)
	    	{
	    		startColor.rgba[0] -= colorChange[0];
	    		startColor.rgba[1] -= colorChange[1];
	    		startColor.rgba[2] -= colorChange[2];
	    		colorChange[0] = startColor.rgba[0] - skyColor.getRed();
	    		colorChange[1] = startColor.rgba[1] - skyColor.getGreen();
	    		colorChange[2] = startColor.rgba[2] - skyColor.getBlue();
	    	}
	    	else if(i==divisions-colorSteps-1)
	    	{
	    		colorChange[0] = 0f;
	    		colorChange[1] = 0f;
	    		colorChange[2] = 0f;
	    		startColor.rgba[0] = skyColor.getRed();
	    		startColor.rgba[1] = skyColor.getGreen();
	    		startColor.rgba[2] = skyColor.getBlue();
	    	}
	    	t += dt;
	    }
	    
	    createDisplayList(gl);
	}
	
	public void render(GL gl)
	{
		gl.glCallList(displayList);
	}
	
	public void createDisplayList(GL gl)
	{
		displayList = gl.glGenLists(1);
		gl.glNewList(displayList, GL.GL_COMPILE);
		for(int i=divisions; i>0; i--)
		{
			gl.glBegin(GL.GL_QUAD_STRIP);
			for(int j=0; j<=divisions; j++)
			{
				for(int k=0; k<2; k++)
				{
				gl.glNormal3f(normals[i][j][k].x, normals[i][j][k].y, normals[i][j][k].z);
				gl.glTexCoord2d(texCoords[i][j][k].x, texCoords[i][j][k].y);
				gl.glColor4fv(colors[i][j][k].rgba, 0);
				gl.glVertex3f(vertices[i][j][k].x, vertices[i][j][k].y, vertices[i][j][k].z);
				}
			}
			gl.glEnd();
		}
		gl.glEndList();
	}
}