package org.cart.igd;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.util.Texture;

public class GLGraphics
{
	private static Texture[] Font = new Texture[94];
	
	public static final int DEFAULT_BLEND = GL.GL_ONE_MINUS_SRC_ALPHA;
	public static int BLENDING=1;
	
	public GLGraphics(GL gl, GLU glu) throws java.io.IOException
	{
		for(int i=0; i<94; i++)
		{
			Font[i] = Display.renderer.textureLoader.getTexture("data/images/font/"+(i+2)+".png", gl, glu);
		}
	}
	
	public void glgBegin()
	{
		final GL gl = Display.renderer.gl;
		final GLU glu = Display.renderer.glu;
		
		int viewport[] = new int[4];
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		glu.gluOrtho2D(0, viewport[2], 0, viewport[3]);
		gl.glDepthFunc(GL.GL_ALWAYS);
		
		gl.glColor4f(1f, 1f, 1f, 0f);

		gl.glDisable(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_LIGHT0);
	}
	
	public void glgEnd()
	{
		final GL gl = Display.renderer.gl;
		
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
	
		gl.glDepthFunc(GL.GL_LESS);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix();
	}
	
	public static final float[]	DEFAULT = new float[1];
	public static final float	TEX_Z = 0f;
	public static final float	GEO_Z = -.5f;
	public static final float	STR_Z = -1f;
	public static final float 	PART_Z = 0.1f;
	public static final float	SHIP_Z = 1.f;

	public void drawImageHue(Texture tex, float x, float y, float z, float[] rgba, int blend)
	{
		drawImage( tex, new float[] {x,y,z}, DEFAULT, rgba, new int[] {GL.GL_MODULATE,blend} );
	}
	
	public void drawImageHue(Texture tex, int x, int y, float z, float[] rgba,int blend)
	{
		drawImage( tex, new float[] {x,y,z}, DEFAULT, rgba, new int[] {GL.GL_MODULATE,blend} );
	}
	
	public void drawImageHue(Texture tex, int x, int y, float z, float[] rgba)
	{
		drawImage( tex, new float[] {x,y,z}, DEFAULT, rgba, GL.GL_MODULATE );
	}

	private void drawImage( Texture tex, float[] xyza, float[] size, float[] rgb, int[] mode )
	{
		final GL gl = Display.renderer.gl;
		if(size.length!=2) size = new float[] {1f,1f};
		if(rgb.length<3) rgb = new float[] {1f,1f,1f,0f};
		
		gl.glPushMatrix();
		if(mode[1]!=DEFAULT_BLEND) Display.renderer.gl.glBlendFunc( GL.GL_SRC_ALPHA, mode[1] );
		gl.glTranslatef( xyza[0], xyza[1], xyza[2] );
		
		gl.glEnable( GL.GL_TEXTURE_2D );
		gl.glEnable( GL.GL_BLEND );
		tex.bind(gl);
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, mode[0]);
		
		double ky = ( double ) tex.getImageHeight() / ( double ) tex.getTextureHeight();
		double kx = ( double ) tex.getImageWidth() / ( double ) tex.getTextureWidth();
		
		gl.glBegin( GL.GL_TRIANGLE_STRIP );
			if( rgb.length==3 ) gl.glColor3f( rgb[0], rgb[1], rgb[2] );
			if( rgb.length==4 ) gl.glColor4f( rgb[0], rgb[1], rgb[2], rgb[3] );
		
			gl.glTexCoord2d( kx, 0 );
			gl.glVertex3f( tex.getImageWidth()*size[0], tex.getImageHeight()*size[1], 0f );
			
			gl.glTexCoord2d( 0, 0 );
			gl.glVertex3f( 0f, tex.getImageHeight()*size[1], 0f );
			
			gl.glTexCoord2d( kx, ky );
			gl.glVertex3f( tex.getImageWidth()*size[0], 0f, 0f );
			
			gl.glTexCoord2d( 0, ky );
			gl.glVertex3f( 0f, 0f, 0f );
		
		gl.glEnd();
		
		gl.glDisable( GL.GL_TEXTURE_2D );
		gl.glDisable( GL.GL_BLEND );
		if(mode[1]!=DEFAULT_BLEND) gl.glBlendFunc( GL.GL_SRC_ALPHA, GLGraphics.DEFAULT_BLEND );
		gl.glPopMatrix();
	}
	
	public void drawImage(Texture tex, float x, float y, float w, float h)
	{
		drawImage( tex, new float[] {x,y,TEX_Z}, new float[] {w,h}, DEFAULT, GL.GL_REPLACE );
	}
	
	public void drawImage(Texture tex, float x, float y, float z)
	{
		drawImage( tex, new float[] {x,y,z}, DEFAULT, DEFAULT, GL.GL_REPLACE );
	}
	
	public void drawImageHue(Texture tex, float x, float y, float[] rgba)
	{
		drawImage( tex, new float[] {x,y,TEX_Z}, DEFAULT, rgba, GL.GL_MODULATE );
	}
	
	public void drawImageAlpha(Texture tex, int x, int y, float alpha)
	{
		drawImageAlpha(tex,(float)x,(float)y,alpha);
	}
	
	public void drawImageAlpha(Texture tex, float x, float y, float alpha)
	{
		drawImage( tex, new float[] {x,y,TEX_Z}, DEFAULT, new float[] {1f,1f,1f,alpha}, GL.GL_MODULATE );
	}
	
	public void drawImageAlpha(Texture tex, float x, float y, float z, float alpha)
	{
		drawImage( tex, new float[] {x,y,z}, DEFAULT, new float[] {1f,1f,1f,alpha}, GL.GL_MODULATE );
	}
	
	public void drawImage(Texture tex, float x, float y)
	{
		drawImage( tex, x, y, TEX_Z );
	}
	
	private void drawImage(Texture tex, float[] xyza, float[] size, float[] rgb, int mode)
	{
		if(BLENDING!=-1)
		{
			drawImage(tex, xyza, size, rgb, new int[] { mode, BLENDING });
			BLENDING = -1;
		}
		else
			drawImage(tex, xyza, size, rgb, new int[] { mode, DEFAULT_BLEND });
	}
	
	public void drawImageHue(Texture tex, float x, float y, float[] rgba, float[] size)
	{
		drawImage( tex, new float[] {x,y,TEX_Z}, size, rgba, GL.GL_MODULATE );
	}
	
	public void drawImageHue(Texture tex, float x, float y, float[] rgba, int blend)
	{
		drawImage( tex, new float[] {x,y,TEX_Z}, DEFAULT, rgba, new int[] {GL.GL_MODULATE,blend} );
	}
	
	public void setBlending(int blend)
	{
		GLGraphics.BLENDING = blend;
	}

	public void drawLine( int x1, int y1, int x2, int y2 )
	{
		Display.renderer.gl.glBegin(GL.GL_LINES);
		Display.renderer.gl.glVertex2i( x1, y1 );
		Display.renderer.gl.glVertex2i( x2, y2 );
		Display.renderer.gl.glEnd();
	}

	public void drawLineStrip( int[][] i )
	{
		Display.renderer.gl.glBegin( GL.GL_LINE_STRIP );
		for( int a=0; a<i.length; a++ )
		{
			Display.renderer.gl.glVertex2i( i[ a ][ 0 ], i[ a ][ 1 ] );
		}
		Display.renderer.gl.glEnd();
	}

	public void drawPoint( float x, float y, float z, float size )
	{
		final GL gl = Display.renderer.gl;
		gl.glEnable( GL.GL_POINT_SMOOTH );
		gl.glPointSize( size );
		gl.glBegin( GL.GL_POINTS );
			gl.glVertex3f(  x,  y, z );
		gl.glEnd();
		gl.glDisable( GL.GL_POINT_SMOOTH );
	}
	
	public void drawPointParticle(int x, int y, float size)
	{
		drawPoint( (float)x, (float)y, PART_Z, size );
	}
	
	public void drawPointParticle(float x, float y, float size)
	{
		drawPoint( x, y, PART_Z, size );
	}

	public void drawPoint(int x, int y, float size)
	{
		drawPoint( (float)x, (float)y, GEO_Z, size );
	}
	
	public void drawPoint(float x, float y, float size)
	{
		drawPoint( x, y, GEO_Z, size );
	}
	
	public void drawPointColor(float x, float y, float size, float[]rgb)
	{
		if(rgb.length==3)
		{
			Display.renderer.gl.glColor3f( rgb[0], rgb[1], rgb[2] );
		}
		else if(rgb.length==4)
		{
			Display.renderer.gl.glColor4f( rgb[0], rgb[1], rgb[2], rgb[3] );
		}
		drawPoint( x, y, GEO_Z, size );
	}

	public void drawPointOld( int x, int y, float size )
	{
		Display.renderer.gl.glPointSize( size );
		Display.renderer.gl.glBegin( GL.GL_POINTS );
		Display.renderer.gl.glVertex3f( ( float ) x, ( float ) y, 0.0f );
		Display.renderer.gl.glEnd();
	}

	public void drawPointOld( float x, float y, float size )
	{
		Display.renderer.gl.glPointSize( size );
		Display.renderer.gl.glBegin( GL.GL_POINTS );
		Display.renderer.gl.glVertex3f(  x,  y, 0.0f );
		Display.renderer.gl.glEnd();
	}

	public void drawLineLoop( double[][] i )
	{
		Display.renderer.gl.glBegin( GL.GL_LINE_LOOP );
		for( int a=0; a<i.length; a++ )
		{
			Display.renderer.gl.glVertex2d( i[ a ][ 0 ], i[ a ][ 1 ] );
		}
		Display.renderer.gl.glEnd();
	}

	public void fillCircle( double x, double y, int radius )
	{
		Display.renderer.gl.glBegin( GL.GL_TRIANGLE_STRIP );
		double lastVX = ( double ) x;
		double lastVY = ( double ) y;
		for( int i=0; i<360; i++ )
		{
			float angle = ( float ) ( ( ( double ) i ) / 57.29577957795135 );
			double vx = x + (radius * ( float ) Math.sin( ( double ) angle ) );
			double vy = y + (radius * ( float ) Math.cos( ( double ) angle ) );
			Display.renderer.gl.glVertex2d( ( double ) x, ( double ) y );
			Display.renderer.gl.glVertex2d( lastVX, lastVY );
			Display.renderer.gl.glVertex2d( vx, vy );
			lastVX = vx;
			lastVY = vy;
		}
		Display.renderer.gl.glEnd();
	}

	public void drawCircle( double x, double y, int radius )
	{
		double[][] points = new double[ 360 ][ 2 ];
		double lastVX = ( double ) x;
		double lastVY = ( double ) y;
		for( int i=0; i<360; i++ )
		{
			points[ i ][ 0 ] = lastVX;
			points[ i ][ 1 ] = lastVY;
			
			float angle = ( float ) ( ( ( double ) i+1 ) / 57.29577957795135 );
			double vx = x + ( radius * ( float ) Math.sin( ( double ) angle ) );
			double vy = y + ( radius * ( float ) Math.cos( ( double ) angle ) );
			
			lastVX = vx;
			lastVY = vy;
		}
		drawLineLoop( points );
	}

	public void fillRect( int x, int y, int w, int h )
	{
		Display.renderer.gl.glBegin( GL.GL_QUADS );
		Display.renderer.gl.glVertex2i( x, y );
		Display.renderer.gl.glVertex2i( x + w, y );
		Display.renderer.gl.glVertex2i( x + w, y + h );
		Display.renderer.gl.glVertex2i( x, y + h );
		Display.renderer.gl.glEnd();
	}
	
	public void fillRect(int x, int y, int w, int h, float[] rgb)
	{
		Display.renderer.gl.glColor3f(rgb[0], rgb[1], rgb[2]);
		fillRect(x,y,w,h);
	}

	public void drawRect( int x, int y, int w, int h )
	{
		drawLineLoop( new double[][] { { x, y }, { x + w, y }, { x + w, y + h }, { x, y + h } } );
	}
	
	public void drawRect(int x, int y, int w, int h, float[] rgb)
	{
		Display.renderer.gl.glColor3f(rgb[0], rgb[1], rgb[2]);
		drawRect(x,y,w,h);
	}

	public void fillTri( int oX, int oY, int lX, int lY, int rX, int rY )
	{
		Display.renderer.gl.glBegin( GL.GL_TRIANGLES );
		Display.renderer.gl.glVertex3i( oX, oY, 0 );
		Display.renderer.gl.glVertex3i( lX, lY, 0 );
		Display.renderer.gl.glVertex3i( rX, rY, 0 );
		Display.renderer.gl.glEnd();
	}

	public void drawTri( int x1, int y1, int x2, int y2, int x3, int y3 )
	{
		drawLineLoop( new double[][] {	{ x1, y1 }, { x2, y2 }, { x3, y3 } } );
	}

	public void drawString( String str, int x, int y, int font )
	{
		Display.renderer.gl.glRasterPos2i( x, y );
		Display.renderer.glut.glutBitmapString( font, str );
	}

	public int drawBitmapString(String text, int x, int y)
	{
		return drawBitmapString(text,x,y,12, new float[]{1.0f,1.0f,1.0f,1.0f}, new float[] {1f,1f}, false);
	}
	
	public int drawBitmapString(String text, int x, int y, float[] rgb)
	{
		return drawBitmapString(text, x, y, 12, rgb, new float[] {1f,1f}, false);
	}
	
	public int drawBitmapString(String text, int x, int y, float[] rgb, float[] size)
	{
		return drawBitmapString(text,x,y,(int)(12f*size[0]),rgb,size,false);
	}
	
 	public int drawBitmapString(String text, int x, int y, int spacing, float[] rgba, boolean flash)
 	{
 		return drawBitmapString(text,x,y,12,rgba,new float[]{1f,1f},flash);
 	}
 	
	public int drawBitmapString(String text, float x, float y, int spacing, float[] rgba, boolean flash)
	{
		return drawBitmapString(text,(int)x,(int)y,12,rgba,new float[]{1f,1f},flash);
	}
	
	public int drawBitmapStringStroke(String text, int x, int y, int ss, float[] rgba, float[] strokergba)
	{
		drawBitmapString(text, x+ss, y, strokergba);
		drawBitmapString(text, x-ss, y, strokergba);
		drawBitmapString(text, x, y+ss, strokergba);
		drawBitmapString(text, x, y-ss, strokergba);
		return drawBitmapString(text, x, y, rgba);
	}
	
	public int drawBitmapStringShadow(String text, int x, int y, int ss, int dir, float[] rgb, float[] shadowrgb)
	{
		int nx = 0;
		int ny = 0;
		if(dir%2==0)
			nx=ss;
		else
		{
			nx=ss; ny=ss/2;
		}
		if(dir>=5 && dir<=7)
			ny*=-1;
		if(dir==2||dir==6)
			nx=0;
		if(dir>=3 && dir<=5)
			nx*=-1;
		drawBitmapString(text, x+nx, y+ny, rgb);
		return drawBitmapString(text, x, y, rgb);
	}

	private boolean isCompressed(int c)
	{
		return (c=='i'||c=='I'||c=='1'||c=='.'||c==','||c=='|'||c==':'||c==';');
	}
	
	public int drawBitmapString(String text, int x, int y, int spacing, float[] rgba, float[] size, boolean flash)
	{
		try
		{
			int iX = x;
			for(int i=0; i<text.length(); i++)
			{
				int c = text.charAt(i);
				if(c==' ') { x+=spacing; continue; }		//if space continue
				if(c<33||c>126) c='?';		//unknown character
				//if(c>=97&&c<=122) c-=32;
				
				if(i>0)
				{
					if(isCompressed(text.charAt(i-1))) x-=4;
					if(isCompressed(c)) x-=4;
				}
				c-=33;
	 
				if(flash)
					drawImageHue( GLGraphics.Font[c], x, y, new float[]{rgba[0],rgba[1],rgba[2], 1.0f - ((float)Math.random())/4} , size);
				else 
					drawImageHue( GLGraphics.Font[c], x, y, rgba, size ); 
				
				x+=spacing;
			}
			return x-iX;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public void setColor( int r, int g, int b )
	{
		Display.renderer.gl.glColor3i( r, g, b );
	}
	
	public void setColor( int r, int g, int b, int a )
	{
		Display.renderer.gl.glColor4i( r, g, b, a );
	}
	
	public void setColor( float r, float g, float b )
	{
		Display.renderer.gl.glColor3f( r, g, b );
	}
	
	public void setColor( float r, float g, float b, float a )
	{
		Display.renderer.gl.glColor4f( r, g, b, a );
	}

	public void drawImageHueRandomAlpha( Texture tex, float x, float y, float[] rgb )
	{
		final GL gl = Display.renderer.gl;
		float[] rgba = new float[]{rgb[0],rgb[1],rgb[2],1.0f-((float)Math.random())/4};
		gl.glEnable( GL.GL_TEXTURE_2D );
		gl.glEnable( GL.GL_BLEND );
		tex.bind(gl);
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
		
		double ky = ( double ) tex.getImageHeight() / ( double ) tex.getTextureHeight();
		double kx = ( double ) tex.getImageWidth() / ( double ) tex.getTextureWidth();
		
		gl.glBegin( GL.GL_TRIANGLE_STRIP );
			gl.glColor4f( rgba[0],rgba[1],rgba[2],rgba[3] );
			gl.glTexCoord3d( kx, 0, 0 );
			gl.glVertex3f( x + tex.getImageWidth(), y + tex.getImageHeight(), 0.0f );
			
			gl.glTexCoord3d( 0, 0, 0 );
			gl.glVertex3f( x, y + tex.getImageHeight(), 0.0f );
			
			gl.glTexCoord3d( kx, ky, 0 );
			gl.glVertex3f( x + tex.getImageWidth(), y, 0.0f );
			
			gl.glTexCoord3d( 0, ky, 0 );
			gl.glVertex3f( x, y, 0.0f );
		gl.glEnd();
		
		gl.glDisable( GL.GL_TEXTURE_2D );
		gl.glDisable( GL.GL_BLEND );
	}
	
	public void drawImageRandomAlpha( Texture tex, float x, float y)
	{
		final GL gl = Display.renderer.gl;
		float[] rgba = new float[]{1f,1f,1f,1.0f-((float)Math.random())/4};
		gl.glEnable( GL.GL_TEXTURE_2D );
		gl.glEnable( GL.GL_BLEND );
		tex.bind(gl);
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
		
		double ky = ( double ) tex.getImageHeight() / ( double ) tex.getTextureHeight();
		double kx = ( double ) tex.getImageWidth() / ( double ) tex.getTextureWidth();
		
		gl.glBegin( GL.GL_TRIANGLE_STRIP );
			gl.glColor4f( rgba[0],rgba[1],rgba[2],rgba[3] );
			gl.glTexCoord3d( kx, 0, 0 );
			gl.glVertex3f( x + tex.getImageWidth(), y + tex.getImageHeight(), 0.0f );
			
			gl.glTexCoord3d( 0, 0, 0 );
			gl.glVertex3f( x, y + tex.getImageHeight(), 0.0f );
			
			gl.glTexCoord3d( kx, ky, 0 );
			gl.glVertex3f( x + tex.getImageWidth(), y, 0.0f );
			
			gl.glTexCoord3d( 0, ky, 0 );
			gl.glVertex3f( x, y, 0.0f );
		gl.glEnd();
		
		gl.glDisable( GL.GL_TEXTURE_2D );
		gl.glDisable( GL.GL_BLEND );
	}

	public void drawImageRotate(Texture text, float x, float y, int degree)
	{
		drawImageRotate(text,x,y,degree, new float[]{1f,1f,1f,1f},1f,0,0);
	}
	
	public void drawImageRotateAlpha(Texture text, float x, float y, int degree, float alpha)
	{
		drawImageRotate(text,x,y,degree,new float[]{1f,1f,1f,1f},alpha,0,1);
	}
	
	public void drawImageRotateHue(Texture text, float x, float y, int degree, float[] rgba){drawImageRotate(text,x,y,degree,rgba,1f,0,2);}
	
	public void drawImageRotateRandomAlpha(Texture text, float x, float y, int degree){drawImageRotate(text,x,y,degree, new float[]{1f,1f,1f,1f},1f,0,3);}
	
	public void drawImageRotateHueRandomAlpha(Texture text, float x, float y, int degree, float[] rgba){drawImageRotate(text,x,y,degree,rgba,1f,0,4);}	
	
	public void drawImageRotate(Texture text, int x, int y, int degree){drawImageRotate(text,x,y,degree, new float[]{1f,1f,1f,1f},1f,0,0);}
	
	public void drawImageRotateAlpha(Texture text, int x, int y, int degree, float alpha){drawImageRotate(text,x,y,degree,new float[]{1f,1f,1f,1f},alpha,0,1);}

	public void drawImageRotateRandomAlpha(Texture text, int x, int y, int degree){drawImageRotate(text,x,y,degree, new float[]{1f,1f,1f,1f},1f,0,3);}
	
	public void drawImageRotateHueRandomAlpha(Texture text, int x, int y, int degree, float[] rgba){drawImageRotate(text,x,y,degree,rgba,1f,0,4);}
	
	public void drawImageRotateHue(Texture text, int x, int y, int degree, float[] rgba){drawImageRotate(text,x,y,degree,rgba,1f,0,2);}	
	
	public void drawImageRotateHue(Texture text, int x, int y, int degree, float[] rgba, int blend){drawImageRotate(text,x,y,degree,rgba,1f,blend,5);}
	
	public void drawImageRotateHue(Texture text, float x, float y, int degree, float[] rgba, int blend){drawImageRotate(text,x,y,degree,rgba,1f,blend,5);}

	public void drawImageRotateHue	(Texture tex, float x, float y, float z, float degree, float[] rgba,int blend) { drawImageRotateBlendZ( tex,x,y,z,degree, rgba, blend);}
	
	public void drawImageRotateHue	(Texture tex, float x, float y, float z, float degree, float[] rgba) { drawImageRotateZ( tex,x,y,z,degree, rgba);}

	public void drawImageRotateBlendZ(Texture text, float x, float y, float z, float degree, float[] rgba, int blend)
	{
		Display.renderer.gl.glTranslatef(x + text.imageHeight/2, y + text.imageHeight/2, 0);
		Display.renderer.gl.glRotatef(degree,0,0,1);
		
		drawImage( text, new float[] {-text.imageHeight/2,-text.imageWidth/2,z}, DEFAULT, rgba, 0 );

		Display.renderer.gl.glRotatef(-degree,0,0,1);
		Display.renderer.gl.glTranslatef(-(x + text.imageHeight/2), -(y + text.imageHeight/2), 0);
	}

	public void drawImageRotateZ(Texture text, float x, float y, float z, float degree, float[] rgba)
	{
		Display.renderer.gl.glTranslatef(x + text.imageHeight/2, y + text.imageHeight/2, 0);
		Display.renderer.gl.glRotatef(degree,0,0,1);

		drawImage( text, new float[] {-text.imageHeight/2,-text.imageWidth/2,z}, DEFAULT, rgba, 0 );

		Display.renderer.gl.glRotatef(-degree,0,0,1);
		Display.renderer.gl.glTranslatef(-(x + text.imageHeight/2), -(y + text.imageHeight/2), 0);
	}

	public void drawImageRotate(Texture text, float x, float y, int degree, float[] rgba,float alpha, int blend, int type)
	{
		Display.renderer.gl.glTranslatef(x + text.imageHeight/2, y + text.imageHeight/2, 0);
		Display.renderer.gl.glRotatef(degree,0,0,1);

		switch(type)
		{
		case 0:
			drawImage(text,-text.imageHeight/2,-text.imageWidth/2);
			break;
		case 1:
			drawImageAlpha(text,-text.imageHeight/2,-text.imageWidth/2,alpha);
			break;
		case 2:
			drawImageHue(text,-text.imageHeight/2,-text.imageWidth/2,rgba);
			break;
		case 3:
			drawImageRandomAlpha(text,-text.imageHeight/2,-text.imageWidth/2);
			break;
		case 4:
			drawImageHueRandomAlpha(text,-text.imageHeight/2,-text.imageWidth/2,rgba);
			break;
		case 5:
			drawImage( text, new float[] {-text.imageHeight/2,-text.imageWidth/2,TEX_Z}, DEFAULT, rgba, new int[] {GL.GL_MODULATE,blend} );
			break;
		}

		Display.renderer.gl.glRotatef(-degree,0,0,1);
		Display.renderer.gl.glTranslatef(-(x + text.imageHeight/2), -(y + text.imageHeight/2), 0);
	}

	public void drawImageRotateHuef(Texture text, float x, float y, float degree, float[] rgba)
	{
		drawImageRotatef(text,x,y,degree,rgba,1f,0);
	}
	
	public void drawImageRotateHuef(Texture text, float x, float y, float degree, float[] rgba,int blend)
	{
		drawImageRotatef(text,x,y,degree,rgba,1f,blend);
	}
	
	public void drawImageRotateHuef(Texture text, float x, float y, float z, float degree, float[] rgba,int blend)
	{
		drawImageRotatefZ(text,x,y,z,degree,rgba,1f,blend);
	}
	
	public void drawImageRotateHuefZ(Texture text, float x, float y,float z, float degree, float[] rgba)
	{
		drawImageRotatefZ(text,x,y,z,degree,rgba,1f,0);
	}

	public void drawImageRotatef(Texture text, float x, float y, float degree, float[] rgba,float alpha, int blend)
	{
		Display.renderer.gl.glTranslatef(x + text.imageHeight/2, y + text.imageHeight/2, 0);
		Display.renderer.gl.glRotatef(degree,0,0,1);
		
		if(blend!=0)
			drawImage( text, new float[] {-text.imageHeight/2,-text.imageWidth/2,TEX_Z}, DEFAULT, rgba, new int[] {GL.GL_MODULATE,blend} );
		else
			drawImageHue(text,-text.imageHeight/2,-text.imageWidth/2,rgba);	
		
		Display.renderer.gl.glRotatef(-degree,0,0,1);
		Display.renderer.gl.glTranslatef(-(x + text.imageHeight/2), -(y + text.imageHeight/2), 0);
	}

	public void drawImageRotatefZ(Texture text, float x, float y,float z, float degree, float[] rgba,float alpha, int blend)
	{
		Display.renderer.gl.glTranslatef(x + text.imageHeight/2, y + text.imageHeight/2, 0);
		Display.renderer.gl.glRotatef(degree,0,0,1);
		
		if(blend!=0)
			drawImage( text, new float[] {-text.imageHeight/2,-text.imageWidth/2,z}, DEFAULT, rgba, new int[] {GL.GL_MODULATE,blend} );
		else
			drawImageHue(text,-text.imageHeight/2,-text.imageWidth/2,z,rgba);	

		Display.renderer.gl.glRotatef(-degree,0,0,1);
		Display.renderer.gl.glTranslatef(-(x + text.imageHeight/2), -(y + text.imageHeight/2), 0);
	}
}