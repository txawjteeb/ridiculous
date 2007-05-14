package org.cart.igd.models.obj;

import java.io.*;
import java.util.*;

import javax.media.opengl.*;
import org.cart.igd.math.Vector3f;

/**
 * OBJModel.java
 *
 * General Function: Hold the model data of an OBJ Model.
 */
public class OBJModel
{
	private static final float DUMMY_Z_TC = -5.0f;

	/* Collection of vertices. */
	private ArrayList<Tuple3> verts;
  
	/* Collection of normals. */
	private ArrayList<Tuple3> normals;
  
	/* Collection of texture coords. */
	private ArrayList<Tuple3> texCoords;
  
	/* Whether the model uses 3D or 2D tex coords. */
	private boolean hasTCs3D = true;  

	/* Model faces. */
	private Faces faces;
	
	/* Materials used by faces. */
	private FaceMaterials faceMats;
	
	/* Materials defined in MTL file. */
	private Materials materials;
	
	/* Model dimensions. */
	private ModelDimensions modelDims;

	/* Model filename. */
	private String modelNm;
	
	/* For scaling the model. */
	private float maxSize;

	/* The model's display list. */
	private int modelDispList;

	/* The mesh directory. */
	public String meshDir = "data/obj/meshes/"; 

	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of OBJModel.
	 */
	public OBJModel(GL gl, String nm)
	{
		this(gl, nm, 1.0f, false);
	}
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of OBJModel.
	 */
	public OBJModel(GL gl, String nm, float sz, boolean showDetails)
	{
		modelNm = meshDir + nm;
		maxSize = sz;
		initModelData( modelNm );
		
		loadModel( modelNm );
		centerScale();
		drawToList(gl);
		
		if(showDetails) reportOnModel();
	}

	/**
	 * initModelData
	 *
	 * General Function: Initializes collections and objects.
	 */
	private void initModelData(String modelNm)
	{
		verts = new ArrayList<Tuple3>();
		normals = new ArrayList<Tuple3>();
		texCoords = new ArrayList<Tuple3>();

		faces = new Faces(verts, normals, texCoords);
		faceMats = new FaceMaterials();
		modelDims = new ModelDimensions();
	}
	
	/**
	 * height
	 *
	 * General Function: Returns the height.
	 */
	public Vector3f height(Vector3f v)
	{
		Iterator<Tuple3> i = verts.iterator();
		while(i.hasNext())
		{
			Tuple3 t = i.next();
			if(t.getX()==v.x&&t.getZ()==v.z)
			{
				v.y = t.getY();
				return v;
			}
		}
		return v;
	}

	/**
	 * loadModel
	 *
	 * General Function: Loads The model data.
	 */
	private void loadModel(String modelNm)
	{
		String fnm = modelNm + ".obj";
		try
		{
			System.out.println("Loading model from " + fnm + " ...");
			BufferedReader br = new BufferedReader( new FileReader(fnm) );
			readModel(br);
			br.close();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());  
			System.exit(1);
		}
	}

	/**
	 * readModel
	 *
	 * General Function: Reads the model data.
	 */
	private void readModel(BufferedReader br)
	{
		boolean isLoaded = true;   // hope things will go okay

		int lineNum = 0;
		String line;
		boolean isFirstCoord = true;
		boolean isFirstTC = true;
		int numFaces = 0;

		try
		{
			while(((line = br.readLine()) != null) && isLoaded)
			{
				lineNum++;
				if(line.length() > 0)
				{
					line = line.trim();
					if(line.startsWith("v ")) {   // vertex
						isLoaded = addVert(line, isFirstCoord);
						if(isFirstCoord) isFirstCoord = false;
					}
					else if(line.startsWith("vt")) {   // tex coord
						isLoaded = addTexCoord(line, isFirstTC);
						if(isFirstTC) isFirstTC = false;
					}
					else if(line.startsWith("vn"))    // normal
						isLoaded = addNormal(line);
					else if(line.startsWith("f ")) {  // face
						isLoaded = faces.addFace(line);
						numFaces++;
					}
					else if(line.startsWith("mtllib "))   // load material
						materials = new Materials( line.substring(7) );
					else if(line.startsWith("usemtl "))   // use material
						faceMats.addUse( numFaces, line.substring(7));   
					else if(line.charAt(0) == 'g') {  // group name
						// not implemented
					}
					else if(line.charAt(0) == 's') {  // smoothing group
						// not implemented
					}
					else if(line.charAt(0) == '#')   // comment line
						continue;
					else
						System.out.println("Ignoring line " + lineNum + " : " + line);
				}
			}
		}
		catch(IOException e)
		{
			System.out.println( e.getMessage() );
			System.exit(1);
		}

		if(!isLoaded)
		{
			System.out.println("Error loading model");  
			System.exit(1);
		}
	}
	
	/**
	 * addVert
	 *
	 * General Function: Adds vertices to the collection.
	 */
	private boolean addVert(String line, boolean isFirstCoord)
	{
		Tuple3 vert = readTuple3(line);
		if(vert != null)
		{
			verts.add(vert);
			if(isFirstCoord) modelDims.set(vert);
			else modelDims.update(vert);
			return true;
		}
		return false;
	}

	/**
	 * readTuple3
	 *
	 * General Function: Reads a Tuple3f from a String line.
	 */
	private Tuple3 readTuple3(String line)
	{
		StringTokenizer tokens = new StringTokenizer(line, " ");
		tokens.nextToken();    // skip the OBJ word
		
		try
		{
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());
			float z = Float.parseFloat(tokens.nextToken());
			
			return new Tuple3(x,y,z);
		}
		catch(NumberFormatException e)
		{
			System.out.println(e.getMessage());
		}
		
		return null;   // means an error occurred
	}
	
	/**
	 * addTexCoord
	 *
	 * General Function: Adds a Texture Coord to the collection.
	 */
	private boolean addTexCoord(String line, boolean isFirstTC)
	{
		if(isFirstTC)
		{
			hasTCs3D = checkTC3D(line);
			System.out.println("Using 3D tex coords: " + hasTCs3D);
		}

		Tuple3 texCoord = readTCTuple(line);
		if(texCoord != null)
		{
			texCoords.add( texCoord );
			return true;
		}
		return false;
	}

	/**
	 * checkTC3D
	 *
	 * General Function: Checks for Texture Coords 3D is flagged.
	 */
	private boolean checkTC3D(String line)
	{
		String[] tokens = line.split("\\s+");
		return (tokens.length == 4);
	}

	/**
	 * readTCTuple
	 *
	 * General Function: Reads a Texture Coord Tuple3.
	 */
	private Tuple3 readTCTuple(String line)
	{
		StringTokenizer tokens = new StringTokenizer(line, " ");
		tokens.nextToken();    // skip "vt" OBJ word
		
		try
		{
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());

			float z = DUMMY_Z_TC;
			if(hasTCs3D) z = Float.parseFloat(tokens.nextToken());
			
			return new Tuple3(x,y,z);
		} 
		catch (NumberFormatException e) 
		{  System.out.println(e.getMessage());  }

		return null;   // means an error occurred
	}

	/**
	 * addNormal
	 *
	 * General Function: Adds a normal to the collection.
	 */
	private boolean addNormal(String line)
	{
		Tuple3 normCoord = readTuple3(line);
		if(normCoord != null)
		{
			normals.add( normCoord );
			return true;
		}
		return false;
	}
	
	/**
	 * centerScale
	 *
	 * General Function: Centers the scale.
	 */
	private void centerScale()
	{
		Tuple3 center = modelDims.getCenter();
		
		float scaleFactor = 1.0f;
		float largest = modelDims.getLargest();
		// System.out.println("Largest dimension: " + largest);
		if(largest != 0.0f) scaleFactor = (maxSize / largest);
		System.out.println("Scale factor: " + scaleFactor);
		
		Tuple3 vert;
		float x, y, z;
		for(int i = 0; i < verts.size(); i++)
		{
			vert = (Tuple3) verts.get(i);
			x = (vert.getX() - center.getX()) * scaleFactor;
			vert.setX(x);
			y = (vert.getY() - center.getY()) * scaleFactor;
			vert.setY(y);
			z = (vert.getZ() - center.getZ()) * scaleFactor;
			vert.setZ(z);
		}
	}
	
	/**
	 * drawToList
	 *
	 * General Function: Creates a display list.
	 */
	private void drawToList(GL gl)
	{
		modelDispList = gl.glGenLists(1);
		gl.glNewList(modelDispList, GL.GL_COMPILE);
		
		String faceMat;
		for(int i = 0; i < faces.getNumFaces(); i++)
		{
			faceMat = faceMats.findMaterial(i);       // get material used by face i
			if(faceMat != null) materials.renderWithMaterial(gl, faceMat);
			faces.renderFace(gl, i);                  // draw face i
		}
		if(materials!=null)
			materials.switchOffTex(gl);
		
		gl.glEndList();
	}
	
	/**
	 * draw
	 *
	 * General Function: Renders the display list.
	 */
	public void draw(GL gl)
	{
		gl.glCallList(modelDispList);
		org.cart.igd.core.Kernel.display.getRenderer().polyCount += faces.getNumFaces();
	}
	
	/**
	 * reportOnModel
	 *
	 * General Function: Prints a report onthe OBJModel.
	 */
	private void reportOnModel()
	{
		System.out.println("No. of vertices: " + verts.size());
		System.out.println("No. of normal coords: " + normals.size());
		System.out.println("No. of tex coords: " + texCoords.size());
		System.out.println("No. of faces: " + faces.getNumFaces());
		
		modelDims.reportDimensions();  
        // dimensions of model (before centering and scaling)

		if(materials != null) materials.showMaterials();   // list defined materials 
		faceMats.showUsedMaterials();  // show what materials have been used by faces
	}
}