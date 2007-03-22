package org.cart.igd.model;

/*
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

import org.cart.igd.math.Vector3f;
import org.cart.igd.util.WinDataInputStream;

public class Model3ds implements Model
{
    //>------ Primary Chunk, at the beginning of each file
    private static final int PRIMARY        = 0x4D4D;

    //>------ Main Chunks
    private static final int OBJECTINFO     = 0x3D3D; // This gives the version of the mesh and is found right before the material and object information
    private static final int VERSION        = 0x0002; // This gives the version of the .3ds file
    private static final int EDITKEYFRAME   = 0xB000; // This is the header for all of the key frame info

    //>------ sub defines of OBJECTINFO
    private static final int MATERIAL	= 0xAFFF; // This stored the texture info
    private static final int OBJECT	        = 0x4000; // This stores the faces, vertices, etc...

    //>------ sub defines of MATERIAL
    private static final int MATNAME        = 0xA000; // This holds the material name
    private static final int MATDIFFUSE     = 0xA020; // This holds the color of the object/material
    private static final int MATMAP         = 0xA200; // This is a header for a new material
    private static final int MATMAPFILE     = 0xA300; // This holds the file name of the texture

    private static final int OBJECT_MESH    = 0x4100; // This lets us know that we are reading a new object

    //>------ sub defines of OBJECT_MESH
    private static final int OBJECT_VERTICES = 0x4110; // The objects vertices
    private static final int OBJECT_FACES	 = 0x4120; // The objects faces
    private static final int OBJECT_MATERIAL = 0x4130; // This is found if the object has a material, either texture map or color
    private static final int OBJECT_UV       = 0x4140; // The UV texture coordinates

    // data typ length to load C++ files
    private static final int WORD = 2;
    private static final int DWORD = 4;
    
    private String meshName;
    private int fileVersion;
    
    public List<MaterialInfo3ds> materials;
    public List<Object3ds> objects;
    
    private WinDataInputStream winDataInputStream;
    private Chunk currentChunk;
    private Chunk tempChunk;
    
    private GL gl;
    
    public Model3ds(File meshFile)
    {
    	gl = org.cart.igd.Driver.display.renderer.gl;
    	
    	materials = new LinkedList<MaterialInfo3ds>();
    	objects = new LinkedList<Object3ds>();
    	
    	meshName = meshFile.getName().substring(0, meshFile.getName().length()-4);
    	
    	currentChunk = new Chunk();
    	tempChunk = new Chunk();
    	
    	FileInputStream fileInputStream;
    	byte[] buffer = null;
    	
    	try
    	{
    		fileInputStream = new FileInputStream(meshFile);
    		buffer = new byte[(int)meshFile.length()];
    		fileInputStream.read(buffer, 0, (int)meshFile.length());
    		fileInputStream.close();
    		winDataInputStream = new WinDataInputStream(new ByteArrayInputStream(buffer));
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		return;
    	}
    	
    	readChunk(currentChunk);
    	
    	if(currentChunk.ID!=PRIMARY)
    	{
    		System.out.println("LOG: <severe> Unable to load PRIMARY chunk from file: "+getName());
    		return;
    	}
    	
    	processNextChunk(currentChunk);
    	computeNormals();
    	
    	currentChunk = null;
    	tempChunk = null;
    	
    	for(int i=0; i<materials.size(); i++)
    	{
    		MaterialInfo3ds material = materials.get(i);
    		System.out.println("LOG: <fine> material.file: "+material.file);
    		if(material.file!=null)
    		{
    			String parentDirectoryName = meshFile.getParent();
    			File textureFile = new File(parentDirectoryName + File.separator + material.file);
    			System.out.println("LOG: <fine> textureFile: "+textureFile);
    			materials.get(i).texture = createTexture(textureFile);
    		}
    	}
    }
    
    public Model3ds(Model3ds model)
    {
    	gl = org.cart.igd.Driver.display.renderer.gl;
    	meshName = model.getName();
    	materials = model.materials;
    	objects = model.objects;
    }
    
    private void computeNormals()
    {
    	Vector3f v1, v2, v3;
    	Vector3f[] poly = new Vector3f[3];
    	
    	if(objects.size()==0) return;
    	
    	for(int index=0; index<object.size(); index++)
    	{
    		Object3ds object = objects.get(index);
    		
    		Vector3f[] normals = new Vector3f[object.numberOfFaces];
    		for(int i=0; i<object.numberOfFaces; i++)
    			normals[i] = new Vector3f();
    		
    		Vector3f[] tempNormals = new Vector3f[object.numberOfFaces];
    		for(int i=0; i<object.numberOfFaces; i++)
    			tempNormals[i] = new Vector3f();
    		
    		object.normals = new Vector3f[object.numberOfVertices];
    		for(int i=0; i<object.numberOfVertices; i++)
    			object.normals[i] = new Vector3f();
    		
    		for(int i=0; i<object.numberOfFaces; i++)
    		{
    			poly[0] = object.vertices[object.faces[i].vertexIndex[0]];
    			poly[1] = object.vertices[object.faces[i].vertexIndex[1]];
    			poly[2] = object.vertices[object.faces[i].vertexIndex[2]];
    			
    			v1 = poly[0].subtract(poly[2]);
    			v2 = poly[2].subtract(poly[1]);
    			
    			Vector3f normal;
    			Vector3f.cross(normal, v1, v2);
    			tempNormals[i] = new Vector3f(normal);
    			normal.normalize();
    			normals[i] = normal;
    		}
    		
    		Vector3f vSum = new Vector3f(0.0f, 0.0f, 0.0f);
    		int shared = 0;
    		
    		for(int i=0; i<objects.numberOfVertices; i++)
    		{
    			for(int j=0; j<object.numberOfFaces; j++)
    			{
    				if(object.faces[j].vertexIndex[0]==i || object.faces[j].vertexIndex[1]==i || object.faces[j].vertexIndex[1]==i)
    				{
    					vSum = vSum.add(tempNormals[j]);
    					shared++;
    				}
    			}
    			
    			vSum.divideByScalar(-shared);
    			object.normals[i] = vSum;
    			
    			object.normals[i].normalize();
    			
    			vSum = new Vector3f(0f, 0f, 0f);
    			shared = 0;
    		}
    		
    		tempNormals = null;
    		normals = null;
    	}
    }
    
    private Texture createTexture(File textureFile)
    {
    	System.out.println("LOG: <fine> createTexture");
    	
    	if(gl==null)
    		throw new IllegalStateException("Model3ds.createTexture(): gl is null");
    	
    	if(textureFile==null)
    		throw new IllegalArgumentException("Model3ds.createTexture(): textureFile is null");
    	
    	System.out.println("LOG: <fine> textureFile: "+textureFile);
    	
    	try
    	{
    		return TextureManager.getTexture(textureFile);
    	}
    	catch(TextureFormatException e)
    	{
    		System.out.println("LOG: <severe> "+e.getMessage());
    		return null;
    	}
    	catch(IOException ioe)
    	{
    		ioe.printStackTrace();
    		return null;
    	}
    }
    
    public void render(float percent)
    {
    	for(Iterator i = objects.iterator(); i.hasNext();)
    	{
    		Object3ds object = (Object3ds)i.next();
    		gl.glEnable(GL.GL_TEXTURE_2D);
    		int materialID = -1;
    		for(int j=0; j<object.numberOfFaces; j++)
    		{
    			if(object.faces[j].materialID!=materialID)
    			{
    				materialID = object.faces[j].materialID;
    				Texture texture = materials.get(materialID).texture;
    				if(texture!=null) texture.bind(gl);
    			}
    			gl.glBegin(GL.GL_TRIANGLES);
    			for(int whichVertex=0; whichVertex<3; whichVertex++)
    			{
    				int index = object.faces[j].vertexIndex[whichVertex];
    				gl.glNormal3f(object.normals[index].x, object.normals[index].y, object.normals[index].z);
    				gl.glTexCoord2f(object.vertices[index].u, object.vertices[index].v);
    				gl.glVertex3f(object.vertices[index].x, object.vertices[index].y, object.vertices[index].z);
    			}
    			gl.glEnd();
    		}
    	}
    }
    
    public int getMaximumNumberOfStates()
    {
    	return 1;
    }
    
    public String getName()
    {
    	return meshName;
    }
    
    public int getNextFrame()
    {
    	return 0;
    }
    
    public int getState()
    {
    	return 0;
    }
    
    public int getStateEndFrame()
    {
    	return 0;
    }
    
    public boolean isAnimationCompleted()
    {
    	return true;
    }
    
    private void processNextChunk(Chunk previousChunk)
    {
    	System.out.println("LOG: <fine> --- processNextChunk");
    	currentChunk = new Chunk();
    	
    	while(previousChunk.bytesRead<previousChunk.length)
    	{
    		readChunk(currentChunk);
    		switch(currentChunk.ID)
    		{
    		case VERSION:
    			System.out.println("LOG: <fine> VERSION");
    			try
    			{
    				fileVersion = winDataInputStream.readWinInt();
    				System.out.println("LOG: <fine> fileVersion: "+fileVersion);
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    			
    			if(fileVersion>0x03)
    				System.out.println("LOG: <severe> This 3DS file is over version 3 so it may load incorrectly");
    			break;
    		
    		case OBJECTINFO:
    			System.out.println("LOG: <fine> OBJECTINFO");
    			readChunk(tempChunk);
    			try
    			{
    				int meshVersion = winDataInputStream.readWinInt();
                    System.out.println("LOG: <fine> meshVersion: " + meshVersion);
                    tempChunk.bytesRead += DWORD;
                }
                catch (Exception e)
                {
                	e.printStackTrace();
                }
                currentChunk.bytesRead += tempChunk.bytesRead;
                processNextChunk(currentChunk);
                break;

            case MATERIAL:
            	System.out.println("LOG: <fine> MATERIAL");
                MaterialInfo3ds newTexture = new MaterialInfo3ds();
                materials.add(newTexture);
                processNextMaterialChunk(currentChunk);
                break;

            case OBJECT:
            	System.out.println("LOG: <fine> OBJECT");
                Object3ds newObject = new Object3ds();
                objects.add(newObject);
                try
                {
                    newObject.name = winDataInputStream.readNullTerminatedString();
                    System.out.println("LOG: <fine> newObject.name: " + newObject.name);
                    currentChunk.bytesRead += newObject.name.length() + 1;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                processNextObjectChunk(newObject, currentChunk);
                break;

            case EDITKEYFRAME:
            	System.out.println("LOG: <fine> EDITKEYFRAME");
                // Because I wanted to make this a SIMPLE tutorial as possible, I did not include
                // the key frame information.  This chunk is the header for all the animation info.
                // In a later tutorial this will be the subject and explained thoroughly.

                // ProcessNextKeyFrameChunk(pModel, m_CurrentChunk);

                // Read past this chunk and add the bytes read to the byte counter
                // If we didn't care about a chunk, then we get here.  We still need
                // to read past the unknown or ignored chunk and add the bytes read to the byte counter.
                try
                {
                    winDataInputStream.skipBytes(currentChunk.length - currentChunk.bytesRead);
                    currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                break;

            default:
                // If we didn't care about a chunk, then we get here.  We still need
                // to read past the unknown or ignored chunk and add the bytes read to the byte counter.
            	System.out.println("LOG: <fine> processNextChunk default");
                try
                {
                    winDataInputStream.skipBytes(currentChunk.length - currentChunk.bytesRead);
                    currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                break;
    		}
    		previousChunk.bytesRead += currentChunk.bytesRead;
    	}
    	currentChunk = previousChunk;
    }
    
    private void processNextMaterialChunk(Chunk previousChunk)
    {
    	currentChunk = new Chunk();
    	while(previousChunk.bytesRead<previousChunk.length)
    	{
    		readChunk(currentChunk);
    		switch(currentChunk.ID)
    		{
    		case MATNAME:
    			System.out.println("LOG: <fine> MATNAME");
                try
                {
                    materials.get(materials.size() - 1).name = winDataInputStream.readString(currentChunk.length - currentChunk.bytesRead);
                    System.out.println("LOG: <fine> Material name: " + materials.get(materials.size() - 1).name);
                    currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case MATDIFFUSE: // This holds the R G B color of our object
            	System.out.println("LOG: <fine> MATDIFFUSE");
                readColorChunk(materials.get(materials.size() - 1), currentChunk);
                break;

            case MATMAP: // This is the header for the texture info
            	System.out.println("LOG: <fine> MATMAP");
                processNextMaterialChunk(currentChunk);
                break;

            case MATMAPFILE: // This stores the file name of the material
            	System.out.println("LOG: <fine> MATMAPFILE");
                try
                {
                    String materialFileName = winDataInputStream.readString(currentChunk.length - currentChunk.bytesRead);
                    materialFileName = materialFileName.toLowerCase();
                    materials.get(materials.size() - 1).file = materialFileName;
                    LOG.fine("materialFileName: " + materialFileName);
                    currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                break;

            default:
            	try
            	{
            		winDataInputStream.skipBytes(currentChunk.length - currentChunk.bytesRead);
            		currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
            	}
            	catch(Exception e)
            	{
            		e.printStackTrace();
            	}
            	break;
            }
    		previousChunk.bytesRead += currentChunk.bytesRead;
    	}
    	currentChunk = previousChunk;
    }
    
    private void processNextObjectChunk(Object3ds object, Chunk previousChunk)
    {
    	currentChunk = new Chunk();
    	while(previousChunk.bytesRead<previousChunk.length)
    	{
    		readChunk(currentChunk);
    		switch(currentChunk.ID)
    		{
            case OBJECT_MESH:	// This lets us know that we are reading a new object
                System.out.println("LOG: <fine> OBJECT_MESH");
                processNextObjectChunk(object, currentChunk);
                break;

            case OBJECT_VERTICES: // This is the objects vertices
            	System.out.println("LOG: <fine> OBJECT_VERTICES");
                readVertices(object, currentChunk);
                break;

            case OBJECT_FACES: // This is the objects face information
            	System.out.println("LOG: <fine> OBJECT_FACES");
                readVertexIndices(object, currentChunk);
                break;

            case OBJECT_MATERIAL: // This holds the material name that the object has
            	System.out.println("LOG: <fine> OBJECT_MATERIAL");
                // This chunk holds the name of the material that the object has assigned to it.
                // This could either be just a color or a texture map.  This chunk also holds
                // the faces that the texture is assigned to (In the case that there is multiple
                // textures assigned to one object, or it just has a texture on a part of the object.
                // Since most of my game objects just have the texture around the whole object, and
                // they aren't multitextured, I just want the material name.

                // We now will read the name of the material assigned to this object
                readObjectMaterial(object, currentChunk);
                break;

            case OBJECT_UV: // This holds the UV texture coordinates for the object
            	System.out.println("LOG: <fine> OBJECT_UV");
                readUVCoordinates(object, currentChunk);
                break;

            default:
            	System.out.println("LOG: <fine> processNextObjectChunk default");
            	System.out.println("LOG: <fine> --> currentChunk.ID: " + currentChunk.ID);
                try
                {
                    winDataInputStream.skipBytes(currentChunk.length - currentChunk.bytesRead);
                    currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                }
                break;
    		}
    		previousChunk.bytesRead += currentChunk.bytesRead;
    	}
    	currentChunk = previousChunk;
    }
    
    private void readChunk(Chunk chunk)
    {
    	try
    	{
    		chunk.ID = winDataInputStream.readWinUnsignedShort();
    		chunk.bytesRead = WORD;
    		
    		chunk.length = winDataInputStream.readWinInt();
    		chunk.bytesRead += DWORD;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    private void readColorChunk(MaterialInfo3ds material, Chunk chunk)
    {
    	readChunk(tempChunk);
    	try
    	{
    		material.color[0] = winDataInputStream.readUnsignedByte();
    		material.color[1] = winDataInputStream.readUnsignedByte();
    		material.color[2] = winDataInputStream.readUnsignedByte();
    		tempChunk.bytesRead += 3;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	chunk.bytesRead += tempChunk.bytesRead;
    }
    
    private void readObjectMaterial(Object3ds object, Chunk previousChunk)
    {
    	System.out.println("LOG: <fine> readObjectMaterial");
    	
    	String materialName = null;
    	try
    	{
    		materialName = winDataInputStream.readNullTerminatedString();
    		previousChunk.bytesRead += materialName.length() +1;
    		System.out.println("LOG: <fine> materialName: "+materialName);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	int materialID = 0;
    	for(Iterator i=materials.iterator(); i.hasNext(); materialID++)
    	{
    		MaterialInfo3ds material = (MaterialInfo3ds)i.next();
    		if(materialName.equals(material.name))
    		{
    			System.out.println("LOG: <fine> materialID: "+materialID);
    			if(material.file!=null)
    			{
    				object.hasTexture = true;
    			}
    			break;
    		}
    	}
    	
    	try
    	{
    		int numberOfFaces = winDataInputStream.readWinUnsignedShort();
    		previousChunk.bytesRead += WORD;
    		System.out.println("LOG: <fine> numberOfFaces: "+numberOfFaces);
    		for(int i=0; i<numberOfFaces; i++)
    		{
    			int faceIndex = winDataInputStream.readWinUnsignedShort();
    			previousChunk.bytesRead += WORD;
    			object.faces[faceIndex].materialID = materialID;
    			System.out.println("LOG: <fine> faceIndex: "+faceIndex);
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    private void readUVCoordinates(Object3ds object, Chunk previousChunk)
    {
    	try
    	{
    		object.numberTextureVertices = winDataInputStream.readWinUnsignedShort();
    		previousChunk.bytesRead += 2;
    		System.out.println("LOG: <fine> object.numberTextureVertices: "+object.numberTextureVertices);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	for(int i=0; i<object.numberTextureVertices; i++)
    	{
    		try
    		{
    			object.vertices[i].u = winDataInputStream.readWinFloat();
    			object.vertices[i].v = winDataInputStream.readWinFloat();
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    	previousChunk.bytesRead += previousChunk.length - previousChunk.bytesRead;
    	System.out.println("LOG: <fine> bytesRead: "+previousChunk.bytesRead);
    }
    
    private void readVertexIndices(Object3ds object, Chunk previousChunk)
    {
    	try
    	{
    		object.numberOfFaces = winDataInputStream.readWinUnsignedShort();
    		previousChunk.bytesRead += 2;
    		System.out.println("LOG: <fine> NumberOfFaces: "+object.numberOfFaces);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	object.faces = new Face3ds[object.numberOfFaces];
    	for(int i=0; i<object.numberOfFaces; i++)
    		object.faces[i] = new Face3ds();
    	
    	for(int i=0; i<object.numberOfFaces; i++)
    	{
    		try
    		{
    			object.faces[i].vertexIndex[0] = winDataInputStream.readWinUnsignedShort();
    			object.faces[i].vertexIndex[1] = winDataInputStream.readWinUnsignedShort();
    			object.faces[i].vertexIndex[2] = winDataInputStream.readWinUnsignedShort();
    			winDataInputStream.skipBytes(2);
    			previousChunk.bytesRead += 8;
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    	System.out.println("LOG: <fine> bytesRead: "+previousChunk.bytesRead);
    	System.out.println("LOG: <fine> last Face: "+object.faces[object.numberOfFaces-1].vertexIndex[0]+" "+object.faces[object.numberOfFaces-1].vertexIndex[1]+" "+object.faces[object.numberOfFaces-1].vertexIndex[2]);
    }
    
    private void readVertices(Object3ds object, Chunk previousChunk)
    {
    	System.out.println("LOG: <fine> readVertices");
    	try
    	{
    		object.numberOfVertices = winDataInputStream.readWinUnsignedShort();
    		previousChunk.bytesRead += 2;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	object.vertices = new Vector3f[object.numberOfVertices];
    	for(int i=0; i<object.numberOfVertices; i++)
    		object.vertices[i] = new Vector3f();
    	
    	for(int i=0; i<object.numberOfVertices; i++)
    	{
    		try
    		{
    			object.vertices[i].x = winDataInputStream.readWinFloat();
    			object.vertices[i].y = winDataInputStream.readWinFloat();
    			object.vertices[i].z = winDataInputStream.readWinFloat();
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    	previousChunk.bytesRead += previousChunk.length - previousChunk.bytesRead;
    	System.out.println("LOG: <fine> bytesRead: "+previousChunk.bytesRead);
    	System.out.println("LOG: <fine> last Vertex: "+object.vertices[object.numberOfVertices-1].x+" "+object.vertices[object.numberOfVertices-1].y+" "++object.vertices[object.numberOfVertices-1].z);
    }
    
    public void setState(int state)
    {
    }
    
    public int getNumberOfPolygons()
    {
    	return 0;
    }
    
    public String toString()
    {
    	return	"\nmodelName: "+meshName+
    			"\nobject.size(): "+object.size()+
    			"\nobjects: "+objects+
    			"\nmaterials.size(): "+materials.size()+
    			"\nmaterials: "+materials+
    			"\nfileVersion: "+fileVersion;
    }
    
    private class Chunk
    {
    	public int ID;
    	public int length;
    	public int bytesRead;
    }
    
    private class Face3ds
    {
    	public int[] vertexIndex;
    	public int[] coordIndex;
    	public int materialID;
    	public Face3ds()
    	{
    		vertexIndex = new int[3];
    		coordIndex = new int[3];
    	}
    }
    
    private class MaterialInfo3ds
    {
    	public String name;
    	public String file;
    	public int[] color;
    	public Texture texture;
    	public float uTile;
    	public float vTile;
    	public float uOffset;
    	public float vOffset;
    	
    	public MaterialInfo3ds()
    	{
    		color = new int[3];
    	}
    	
    	public String toString()
    	{
    		return	"\nname: " + name +
    				"\nfile: " + file +
    				"\ntexture: " + texture;
    	}
    }
    
    private class Object3ds
    {
    	public String name;
    	public int numberOfVertices;
    	public int numberOfFaces;
    	public int numberTextureVertices;
    	public boolean hasTexture;
    	public Vector3f[] vertices;
    	public Vector3f[] normals;
    	public Face3ds[] faces;
    	
    	public String toString()
    	{
    		String output = "\nname: " + name +
    						"\nnumberOfVertices: " + numberOfVertices +
    						"\nnumberOfFaces: " + numberOfFaces +
    						"\nunmberTextureVertices: " + numberTextureVertices +
    						"\nhasTexture: " + hasTexture +
    						"\ncoords:";
    		for(int i=0; i<vertices.length; i++)
    		{
    			output += "\n["+vertices[i].x+" / "+vertices[i].y+" / "+vertices[i].z+"] ["+vertices[i].u+" / "+vertices[i].v+"]";
    		}
    		return output;
    	}
    }
}
*/