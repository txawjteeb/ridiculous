package org.cart.igd.models.obj;

import java.util.*;
import javax.media.opengl.*;

/**
 * Faces.java
 *
 * General Function: Holds all face information for an Object Model.
 */
public class Faces
{
	private static final float DUMMY_Z_TC = -5.0f;
	
	/* Collection of face vertex indices. */
	private ArrayList<int[]> facesVertIdxs;
	
	/* Collection of face texture indices. */
	private ArrayList<int[]> facesTexIdxs;
	
	/* Collection of face normal indices. */
	private ArrayList<int[]> facesNormIdxs;
	
	/* Collection of vertices. */
	private ArrayList<Tuple3> verts;
	
	/* Collection of normals. */
	private ArrayList<Tuple3> normals;
	
	/* Collection of texture coords. */
	private ArrayList<Tuple3> texCoords;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Faces.
	 */
	public Faces(ArrayList<Tuple3> vs, ArrayList<Tuple3> ns, ArrayList<Tuple3> ts)
	{
		verts = vs;
		normals = ns;
		texCoords = ts;
		
		facesVertIdxs = new ArrayList<int[]>();
		facesTexIdxs = new ArrayList<int[]>();
		facesNormIdxs = new ArrayList<int[]>();
	}
	
	/**
	 * addFace
	 *
	 * General Function: Adds a face to the collection.
	 */
	public boolean addFace(String line)
	{
		try
		{
			line = line.substring(2);   // skip the "f "
			StringTokenizer st = new StringTokenizer(line, " ");
			int numTokens = st.countTokens();   // number of v/vt/vn tokens
			// create arrays to hold the v, vt, vn indicies
			int v[] = new int[numTokens]; 
			int vt[] = new int[numTokens];
			int vn[] = new int[numTokens];
			
			for(int i=0; i<numTokens; i++)
			{
				String faceToken = addFaceVals(st.nextToken());  // get a v/vt/vn token
				// System.out.println(faceToken);
				StringTokenizer st2 = new StringTokenizer(faceToken, "/");
				int numSeps = st2.countTokens();  // how many '/'s are there in the token
				
				v[i] = Integer.parseInt(st2.nextToken());
				vt[i] = (numSeps > 1) ? Integer.parseInt(st2.nextToken()) : 0;
				vn[i] = (numSeps > 2) ? Integer.parseInt(st2.nextToken()) : 0;
				// add 0's if the vt or vn index values are missing;
				// 0 is a good choice since real indicies start at 1
			}
			
			facesVertIdxs.add(v);
			facesTexIdxs.add(vt);
			facesNormIdxs.add(vn);
		}
		catch(NumberFormatException e)
		{
			System.out.println("Incorrect face index");
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * addFaceVals
	 *
	 * General Function: Creates values from a string.
	 */
	private String addFaceVals(String faceStr)
	{
		char chars[] = faceStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		char prevCh = 'x';   // dummy value
		
		for(int k=0; k<chars.length; k++)
		{
			if(chars[k]=='/' && prevCh=='/')
				sb.append('0');   // add a '0'
			prevCh = chars[k];
			sb.append(prevCh);
		}
		return sb.toString();
	}
	
	/**
	 * renderFace
	 *
	 * General Function: Renders the face to GL.
	 */
	public void renderFace(GL gl, int i)
	{
		if(i >= facesVertIdxs.size()) return;
		
		int[] vertIdxs = (int[]) (facesVertIdxs.get(i));
		
		int polytype;
		if(vertIdxs.length == 3)
			polytype = GL.GL_TRIANGLES;
		else if(vertIdxs.length == 4)
			polytype = GL.GL_QUADS;
		else
			polytype = GL.GL_POLYGON;
		
		gl.glBegin(polytype);
		int[] normIdxs = (int[]) (facesNormIdxs.get(i));
		int[] texIdxs = (int[]) (facesTexIdxs.get(i));
		
		Tuple3 vert, norm, texCoord;
		for(int f=0; f<vertIdxs.length; f++)
		{
			if(normIdxs[f]!=0)
			{
				norm = (Tuple3) normals.get(normIdxs[f] - 1);
				gl.glNormal3f(norm.getX(), norm.getY(), norm.getZ());
			}
			if(texIdxs[f]!=0)
			{
				texCoord = (Tuple3) texCoords.get(texIdxs[f] - 1);
				if(texCoord.getZ()==DUMMY_Z_TC)
					gl.glTexCoord2f(texCoord.getX(), texCoord.getY());
				else
					gl.glTexCoord3f(texCoord.getX(), texCoord.getY(), texCoord.getZ());
			}
			
			vert = (Tuple3) verts.get(vertIdxs[f] - 1);  // render the vertices
			gl.glVertex3f(vert.getX(), vert.getY(), vert.getZ());
		}
		
		gl.glEnd();
	}

	/**
	 * getNumFaces
	 *
	 * General Function: Returns the number of faces.
	 */
	public int getNumFaces()
	{
		return facesVertIdxs.size();
	}
}