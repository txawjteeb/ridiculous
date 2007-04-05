package org.cart.igd.discreet;

public class KeyframeSpotlightBlock extends KeyframeTag
{
    /** The track position info */
    public KeyframePositionBlock positions;

    /** Spotlight hotspot track info */
    public KeyframeHotspotBlock hotspots;

    /** Spotlight falloff track info */
    public KeyframeFalloffBlock falloffs;

    /** Spotlight cone roll track info */
    public KeyframeRollBlock rolloffs;

    /** Colour track info */
    public KeyframeColorBlock colors;
}
