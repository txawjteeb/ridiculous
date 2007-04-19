package org.cart.igd.discreet;

public class ScaleData extends TrackData
{
    /** The x scale value */
    public float xScale;

    /** The y scale value */
    public float yScale;

    /** The z scale value */
    public float zScale;
    
    public String toString()
    {
    	return "xScale:"+xScale+"; yScale:"+yScale+"; zScale:"+zScale+";";
    }
}
