package org.cart.igd.discreet;

import java.util.ArrayList;
import java.util.Iterator;

public class ObjectMesh
{
    /** Flag instructing use of the bitmap background */
    public static final int USE_BITMAP = 0x01;

    /** Flag instructing use of the gradient background */
    public static final int USE_GRADIENT = 0x02;

    /** Flag instructing use of the solid colour background */
    public static final int USE_SOLID_BG = 0x04;

    /** Flag instructing use of the bitmap fog */
    public static final int USE_LINEAR_FOG = 0x01;

    /** Flag instructing use of the gradient fog */
    public static final int USE_LAYER_FOG = 0x02;

    /** Flag instructing use of the solid colour fog */
    public static final int USE_DISTANCE_FOG = 0x04;


    /** The master (uniform) scale to apply to all the objects */
    public float masterScale;

    /** The version of the mesh that was read */
    public int meshVersion;

    /** Listing of all the sub-objects in the mesh */
    public ArrayList<ObjectBlock> blocks;

    /** Number of valid blocks available */
    public int getNumBlocks()
    {
    	return (blocks==null?0:blocks.size());
    }

    /** Listing of all material information */
    //public MaterialBlock[] materials;
    public ArrayList<MaterialBlock> materials;

    /** The number of valid materials available */
    public int getNumMaterials()
    {
    	return (materials==null?0:materials.size());
    }

    /** Listing of all keyframe blocks available */
    public ArrayList<KeyframeBlock> keyframes;

    /** The number of valid keyframe blocks available */
    public int getNumKeyframes()
    {
    	return (keyframes==null?0:keyframes.size());
    }

    /** Ambient light setting. Null if not set */
    public float[] ambientLight;

    /** Flag which of the 3 background structures should be used */
    public int selectedBackground;

    /** The name of the bitmap to use for the background, if set */
    public String backgroundBitmap;

    /**
     * Colours of the background. Has either linear or standard value,
     * depending on the release version of the file.
     */
    public float[] solidBackgroundColor;

    /**
     * Colours of the gradient background. Has either linear or standard value,
     * depending on the release version of the file. [0] is top color, [1] is
     * mid colour, [2] is bottom color.
     */
    public float[][] gradientBackgroundColors;

    /** Percentage location of the mid point, [0,1] */
    public float backgroundMidpoint;

    /**
     * Details of a basic global fog instruction. <br>
     * linearFogDetails[0] is near fog distance<br>
     * linearFogDetails[1] is near density (0-1)<br>
     * linearFogDetails[2] is far fog distance<br>
     * linearFogDetails[3] is far density.
     */
    public float[] linearFogDetails;

    /** Colour of the fog, if needed. Only used for linear and layered fog. */
    public float[] fogColor;

    /** Flag to indicate that the background should also be fogged */
    public boolean fogBackground;

    /**
     * Details of a vertically layered constant atmospheric fog.<br>
     * layerFogDetails[0] lower Y boundary<br>
     * layerFogDetails[1] upper Y boundary<br>
     * layerFogDetails[2] fog density.
     */
    public float[] layerFogDetails;

    /**
     * Flags associated with the layered fog. Bit 20 (0x100000) is set for
     * background fogging, bit 0 (0x1) is set for bottom falloff, and bit 1
     * (0x2) is set for top falloff.
     */
    public int layerFogFlags;

    /**
     * Details of a distance-cued fog instruction. (I think this means
     * exponential) [0] is near fog distance, [1] is near dimming factor (0-1).
     * [2] is far fog distance, [3] is far dimming factor.
     */
    public float[] distanceFogDetails;

    /** Flag which of the 3 fog structures should be used */
    public int selectedFog;

    /**
     * Construct a new instance with blocks initialised to a size of 8.
     */
    public ObjectMesh()
    {
    	blocks = new ArrayList<ObjectBlock>();
    	materials = new ArrayList<MaterialBlock>();
        //blocks = new ObjectBlock[8];
        //materials = new MaterialBlock[4];
        masterScale = 1;

        selectedBackground = USE_SOLID_BG;
        selectedFog = USE_LINEAR_FOG;
        fogBackground = false;
    }
    
    public ObjectBlock getObjectByName(String name)
    {
    	Iterator<ObjectBlock> it = blocks.iterator();
    	while(it.hasNext())
    	{
    		ObjectBlock tmpBlock = it.next();
    		if(tmpBlock.name.equals(name))
    		{
    			return tmpBlock;
    		}
    	}
    	return null;
    }
    
    public void printData()
    {
    	for(int i=0; i<blocks.size(); i++)
    	{
    		System.out.println("-[Start Object]-----------");
    		System.out.println(blocks.get(i).toString());
    		System.out.println("-[End Object]-------------\n");
    	}
    	
    	for(int i=0; i<materials.size(); i++)
    	{
    		System.out.println("-[Start Material]-----------");
    		System.out.println(materials.get(i).toString());
    		System.out.println("-[End Material]-------------\n");
    	}
    	
    	for(int i=0; i<keyframes.size(); i++)
    	{
    		System.out.println("-[Start Keyframe]-----------");
    		System.out.println(keyframes.get(i).toString());
    		System.out.println("-[End Keyframe]-------------\n");
    	}
    }
}
