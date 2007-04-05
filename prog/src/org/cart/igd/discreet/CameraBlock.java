package org.cart.igd.discreet;

public class CameraBlock
{
    /** The location of the the camera, */
    public float[] location;

    /** The location the camera is pointing at */
    public float[] target;

    /**
     * The rotation angle in degrees for the camera. It is an angle relative
     * to the local up vector (0,1,0).
     */
    public float bankAngle;

    /** The focus distance for the camera (in millimetres) */
    public float focus;

    /** Flag indicating whether the camera view cone should be shown */
    public boolean seeOutline;

    /**
     * Atmospheric effect ranges for the camera. Null if not set. Length 2 if
     * set. [0] is near raidus of effect. [0] is far radius of effect. Both
     * must be greater than or equal to zero.
     */
    public float[] ranges;

    /**
     * Create a new camera block and set it up with basic details.
     */
    public CameraBlock()
    {
        target = new float[3];
        location = new float[3];
        seeOutline = false;
    }
}
