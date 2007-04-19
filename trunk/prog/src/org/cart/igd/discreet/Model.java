package org.cart.igd.discreet;

import javax.media.opengl.GL;

public class Model
{
	private ObjectMesh objectMesh;
	private long lastTime;
	private int currentKeyFrame = 0;
	
	public Model(ObjectMesh objectMesh)
	{
		this.objectMesh = objectMesh;
	}

	/*
	public void animate()
	{
		if(Kernel.profiler.currentTime-lastTime>=1000)
		{
			lastTime = Kernel.profiler.currentTime;
			currentKeyFrame++;
		}
		objectMesh.keyframes.get(currentKeyFrame).currentFrame++;
	}
	*/
	
	public float getMasterScale()
	{
		return objectMesh.masterScale;
	}
	
	public void render(GL gl)
	{		
		for(int i=0; i<objectMesh.getNumBlocks(); i++)
		{
			ObjectBlock objBlock = objectMesh.blocks.get(i);
			gl.glBegin(GL.GL_TRIANGLES);
			for(int k=0; k<objBlock.meshes.size(); k++)
			{
				TriangleMesh triMesh = objBlock.meshes.get(k);
				for(int j=0; j<triMesh.numFaces; j++)
		        {
        		    int v0 = triMesh.faces[j * 3];
        	    	int v1 = triMesh.faces[j * 3 + 1];
		            int v2 = triMesh.faces[j * 3 + 2];
            
		            //gl.glNormal3f( normals[v0 * 3], normals[v0 * 3 + 1], normals[v0 * 3 + 2] );
        		    //gl.glTexCoord2f( texCoords[i * 2], texCoords[i * 2 + 1] );
		            gl.glVertex3f( triMesh.vertices[v0 * 3], triMesh.vertices[v0 * 3 + 1], triMesh.vertices[v0 * 3 + 2] );
            
        		    //gl.glNormal3f( normals[v1 * 3], normals[v1 * 3 + 1], normals[v1 * 3 + 2] );
		            gl.glVertex3f( triMesh.vertices[v1 * 3], triMesh.vertices[v1 * 3 + 1], triMesh.vertices[v1 * 3 + 2] );
            
        		    //gl.glNormal3f( normals[v2 * 3], normals[v2 * 3 + 1], normals[v2 * 3 + 2] );
		            gl.glVertex3f( triMesh.vertices[v2 * 3], triMesh.vertices[v2 * 3 + 1], triMesh.vertices[v2 * 3 + 2] );
				}
			}
    		gl.glEnd();
		}
	}
	
	public void printData()
	{
		for(int i=0; i<objectMesh.keyframes.size(); i++)
		{
			KeyframeBlock kfBlock = objectMesh.keyframes.get(i);
			System.out.println("-[Start KeyframeBlock]----");
			kfBlock.printData();
			System.out.println("-[End KeyframeBlock]------\n");
		}
	}
}