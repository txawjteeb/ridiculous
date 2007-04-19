package org.cart.igd.discreet;

import javax.media.opengl.GL;

public class KeyframeFrameBlock extends KeyframeTag
{
    /** The pivot point coordinate for this block */
    public float[] pivotPoint;

    /** The name of the object instance */
    public String instanceName;

    /** Bounding box minimum positions of the mesh */
    public float[] minBounds;

    /** Bounding box maximum positions of the mesh */
    public float[] maxBounds;

    /** The track position info */
    public KeyframePositionBlock positions;

    /** The track rotation info */
    public KeyframeRotationBlock rotations;

    /** The track scale info */
    public KeyframeScaleBlock scales;

    /** The track morph info */
    public KeyframeMorphBlock morphs;

    /** Smoothing factor in radians when morphing. In degrees [0 - 180]. */
    public float morphSmoothingAngle;

    /**
     * Construct a new instance of this frame.
     */
    public KeyframeFrameBlock()
    {
        minBounds = new float[3];
        maxBounds = new float[3];
        pivotPoint = new float[3];
    }
    
    public void printData()
    {
    	System.out.println(">-[Start KeyframeFrameBlock]---");
    	System.out.println("> instanceName: "+instanceName);
    	System.out.println("> pivotPoint: "+pivotPoint[0]+","+pivotPoint[1]+","+pivotPoint[2]);
    	System.out.println("> minBounds: "+minBounds[0]+","+minBounds[1]+","+minBounds[2]);
    	System.out.println("> maxBounds: "+maxBounds[0]+","+maxBounds[1]+","+maxBounds[2]);
    	System.out.println("> morphSmoothingAngle: "+morphSmoothingAngle);
    	if(positions!=null) System.out.println("> KeyframePositionBlock: "+positions.toString());
    	if(rotations!=null) if(morphs!=null) System.out.println("> KeyframeRotationBlock: "+rotations.toString());
    	if(scales!=null) System.out.println("> KeyframeScaleBlock: "+scales.toString());
    	if(morphs!=null) System.out.println("> KeyframeMorphBlock: "+morphs.toString());
    	System.out.println(">-[End KeyframeFrameBlock]-----");
    }
}
