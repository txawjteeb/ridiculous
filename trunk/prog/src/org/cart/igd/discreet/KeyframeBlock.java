package org.cart.igd.discreet;

import java.util.ArrayList;

public class KeyframeBlock
{
    /** The revision information */
    public int revision;

    /** A string referencing the external keyframes */
    public String filename;

    /** The length of this animation in frames */
    public int animationLength;

    /**
     * The current frame number. Used to determine which single frame will be
     * rendered or will be active when entering the Keyframer.
     */
    public int currentFrame;


    /** The start frame number for this animation. */
    public int startFrame;

    /** The last frame number for this animation. */
    public int endFrame;

    /** The listing of frames for this animation. Null if none set. */
    public ArrayList<KeyframeFrameBlock> frames;

    /** The number of valid frames in this block */
    public int getNumFrames()
    {
    	return (frames==null?0:frames.size());
    }

    /** Information about the camera tracks */
    public ArrayList<KeyframeCameraBlock> cameraInfo;

    /** number of valid camera blocks to read */
    public int getNumCameras()
    {
    	return (cameraInfo==null?0:cameraInfo.size());
    }

    /** Information about the camera target track */
    public ArrayList<KeyframeCameraTargetBlock> cameraTargetInfo;

    /** Number of valid camera targets to read */
    public int getNumCameraTargets()
    {
    	return (cameraTargetInfo==null?0:cameraTargetInfo.size());
    }

    /** Information about the light tracks */
    public ArrayList<KeyframeLightBlock> lightInfo;

    /** number of valid light blocks to read */
    public int getNumLights()
    {
    	return (lightInfo==null?0:lightInfo.size());
    }

    /** Information about spotlight tracks */
    public ArrayList<KeyframeSpotlightBlock> spotlightInfo;

    /** Number of valid spotlights to read */
    public int getNumSpotlights()
    {
    	return (spotlightInfo==null?0:spotlightInfo.size());
    }

    /** Information about the spotlight target tracks */
    public ArrayList<KeyframeSpotlightTargetBlock> spotlightTargetInfo;

    /** Number of valid spotlight targets to read */
    public int getNumSpotlightTargets()
    {
    	return (spotlightTargetInfo==null?0:spotlightTargetInfo.size());
    }

    /** Information about the ambient light tracks */
    public ArrayList<KeyframeAmbientBlock> ambientInfo;

    /** The number of valid ambient light blocks */
    public int getNumAmbients()
    {
    	return (ambientInfo==null?0:ambientInfo.size());
    }
    
    public String toString()
    {
    	return "animationLength: "+animationLength
    		+ "\nnumFrames: "+getNumFrames();
    }
}
