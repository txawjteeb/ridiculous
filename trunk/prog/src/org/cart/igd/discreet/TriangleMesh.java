package org.cart.igd.discreet;

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
    public MaterialData[] materials;

    /** Number of valid material groups available */
    public int numMaterials;

    /**
     * Names of the materials used for a cubic environment map. This will be
     * mapped in the order: front, back, left, right, top, bottom. Will be null
     * if nothing declared.
     */
    public String[] boxMapMaterials;
}
