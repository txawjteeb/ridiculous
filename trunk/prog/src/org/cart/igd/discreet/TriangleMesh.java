package org.cart.igd.discreet;

import java.util.ArrayList;
import javax.media.opengl.GL;

public class TriangleMesh
{
    /** Vertex coordinates, flat style */
    public float[] vertices;

    /** The number of valid vertices in this mesh */
    public int numVertices;

    /** 2D texture coordinates for this object */
    public float[] texCoords;

    /** The number of texture coordinates */
    public int numTexCoords;

    /** Index lists for each face */
    public int[] faces;

    /** Total number of valid faces. */
    public int numFaces;

    /** Index lists for the groups of faces that should be smooth shaded */
    public int[] smoothgroups;

    /** Listing of normals for each vertex */
    public float[] normals;

    /** Listing of tangents for each vertex */
    public float[] tangents;

    /** Listing of binormals for each vertex */
    public float[] binormals;

    /** Local coordinate system reference (a 4x3 matrix)*/
    public float[] localCoords;

    /** Listing of the material groups needed */
    public ArrayList<MaterialData> materials;

    /** Number of valid material groups available */
    public int getNumMaterials()
    {
    	return (materials==null?0:materials.size());
    }

    /**
     * Names of the materials used for a cubic environment map. This will be
     * mapped in the order: front, back, left, right, top, bottom. Will be null
     * if nothing declared.
     */
    public String[] boxMapMaterials;
    
    public void render(GL gl)
    {
        for(int i = 0; i < numFaces; i++)
        {
            int v0 = faces[i * 3];
            int v1 = faces[i * 3 + 1];
            int v2 = faces[i * 3 + 2];
            
            //gl.glNormal3f( normals[v0 * 3], normals[v0 * 3 + 1], normals[v0 * 3 + 2] );
            //gl.glTexCoord2f( texCoords[i * 2], texCoords[i * 2 + 1] );
            gl.glVertex3f( vertices[v0 * 3], vertices[v0 * 3 + 1], vertices[v0 * 3 + 2] );
            
            //gl.glNormal3f( normals[v1 * 3], normals[v1 * 3 + 1], normals[v1 * 3 + 2] );
            gl.glVertex3f( vertices[v1 * 3], vertices[v1 * 3 + 1], vertices[v1 * 3 + 2] );
            
            //gl.glNormal3f( normals[v2 * 3], normals[v2 * 3 + 1], normals[v2 * 3 + 2] );
            gl.glVertex3f( vertices[v2 * 3], vertices[v2 * 3 + 1], vertices[v2 * 3 + 2] );
        }
    }
}
