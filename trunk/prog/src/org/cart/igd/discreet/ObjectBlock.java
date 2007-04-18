package org.cart.igd.discreet;

import java.util.ArrayList;

import javax.media.opengl.GL;

public class ObjectBlock
{
    /** The ID associated with this block. Needed for dealing with keyframes. */
    public int objectId;

    /** A name or label associated with this block. */
    public String name;

    /** Array of mesh instances forming this block. */
    public ArrayList<TriangleMesh> meshes;

    /** Number of valid items in the mesh list */
    public int getNumMeshes()
    {
    	return (meshes==null?0:meshes.size());
    }

    /** Set of lights surrounding this mesh */
    public ArrayList<LightBlock> lights;

    /** Listing of lights effecting this object */
    public int getNumLights()
    {
    	return (lights==null?0:lights.size());
    }

    /** Cameras (viewpoints) registered with this object */
    public ArrayList<CameraBlock> cameras;

    /** The number of valid camera objects */
    public int getNumCameras()
    {
    	return (cameras==null?0:cameras.size());
    }

    /**
     * Construct a new instance with meshes initialised to a size of 8.
     */
    public ObjectBlock()
    {
        meshes = new ArrayList<TriangleMesh>();
    }
    
    public String toString()
    {
    	return "name: "+name+"\nnumMeshes: "+getNumMeshes();
    }
    
    public void renderMeshes(GL gl)
    {
    	for(int i=0; i<meshes.size(); i++)
    	{
    		meshes.get(i).render(gl);
    	}
    }
}
