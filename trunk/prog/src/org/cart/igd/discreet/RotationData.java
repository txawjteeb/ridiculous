package org.cart.igd.discreet;

public class RotationData extends TrackData
{
    /** The rotation value in radians */
    public float rotation;

    /** The x axis value */
    public float xAxis;

    /** The y axis value */
    public float yAxis;

    /** The z axis value */
    public float zAxis;
    
    public String toString()
    {
    	return "rotation:"+rotation+"; xAxis:"+xAxis+"; yAxis:"+yAxis+"; zAxis:"+zAxis+";";
    }
}
