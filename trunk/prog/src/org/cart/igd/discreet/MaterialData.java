package org.cart.igd.discreet;

public class MaterialData
{
    /** The name of the material to use from the material library */
    public String materialName;

    /** The number of faces this material node effects */
    public int numFaces;

    /** A listing of each face index this material belongs to */
    public int[] faceList;
}
