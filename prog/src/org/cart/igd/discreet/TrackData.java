package org.cart.igd.discreet;

public abstract class TrackData
{
    /** The frame number of this item of data */
    public int frameNumber;

    /** keys for working with the spline information. See spec for more info. */
    public int splineFlags;

    /** Data that is set if splineFlags is non-zero. Array is length 5. */
    public float[] splineData;
}
