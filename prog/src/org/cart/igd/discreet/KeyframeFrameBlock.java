package org.cart.igd.discreet;

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
}
