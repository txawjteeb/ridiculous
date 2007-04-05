package org.cart.igd.discreet;

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
    public KeyframeFrameBlock[] frames;

    /** The number of valid frames in this block */
    public int numFrames;

    /** Information about the camera tracks */
    public KeyframeCameraBlock[] cameraInfo;

    /** number of valid camera blocks to read */
    public int numCameras;

    /** Information about the camera target track */
    public KeyframeCameraTargetBlock[] cameraTargetInfo;

    /** Number of valid camera targets to read */
    public int numCameraTargets;

    /** Information about the light tracks */
    public KeyframeLightBlock[] lightInfo;

    /** number of valid light blocks to read */
    public int numLights;

    /** Information about spotlight tracks */
    public KeyframeSpotlightBlock[] spotlightInfo;

    /** Number of valid spotlights to read */
    public int numSpotlights;

    /** Information about the spotlight target tracks */
    public KeyframeSpotlightTargetBlock[] spotlightTargetInfo;

    /** Number of valid spotlight targets to read */
    public int numSpotlightTargets;

    /** Information about the ambient light tracks */
    public KeyframeAmbientBlock[] ambientInfo;

    /** The number of valid ambient light blocks */
    public int numAmbients;
}
