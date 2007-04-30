package org.cart.igd.input;

import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.core.Kernel;
import org.cart.igd.entity.Entity;
import org.cart.igd.models.obj.OBJModel;

import com.sun.opengl.util.BufferUtil;


public class PickingHandler {
	private GL gl;
	private GLU glu;
	private OBJModel pickingBox;
	static final int BUFSIZE = 512;
	IntBuffer selectBuffer;
	int numHits;
	boolean inSelectionMode;
	
	int yCursor, xCursor;
	ArrayList<Entity> entities;
	
	public PickingHandler(GL gl,GLU glu,ArrayList<Entity> entities){
		this.gl=gl;
		this.glu = glu;
		this.entities = entities;
		this.pickingBox = new OBJModel(gl,"data/models/picking_box");
	}
	
	public void startPicking(){
		int selectBuf[]= new int[512];
		selectBuffer = BufferUtil.newIntBuffer(BUFSIZE);
		gl.glSelectBuffer(BUFSIZE, selectBuffer);
		
		gl.glRenderMode(GL.GL_SELECT);
		
		gl.glInitNames();
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		
		int viewport[] = new int[4];
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		
		glu.gluPickMatrix((double) xCursor,
				(double) (viewport[3] - yCursor),
				5.0,5.0, viewport, 0 );
				
		glu.gluPerspective(45f,
				(float)Kernel.display.getScreenWidth()/
				(float)Kernel.display.getScreenHeight()
				, 1, 100);
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}
	
	public void endPicking(){
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		processHits(numHits);
		inSelectionMode = false;
	}
	
	public void processHits(int nHits){
		if (numHits == 0){
			return;
		}
		System.out.println("# hits: " + numHits);
		
		int selectedNameId = -1;
		float smallestZ = -1f;
		
		boolean isFirstLoop = true;
		int offset = 0;
		
		for(int i = 0; i < numHits; i++){
			System.out.println("hit"+ (i+1));
			
			int numNames = selectBuffer.get(offset);
			offset++;
			
			float minZ = getDepth(offset);
			offset++;
			
			if(isFirstLoop){
				smallestZ = minZ;
				isFirstLoop = false;
			}
			else {
				if(minZ < smallestZ){
					smallestZ = minZ;
				}
			}
			
			float maxZ = getDepth(offset);
			offset++;
			
			System.out.println(" minZ: " + minZ+ 
					"; masZ: " + maxZ);
			
			int nameId;
			for( int j = 0; j < numNames; j++){
				nameId = selectBuffer.get(offset);
				System.out.println( idToString(nameId));
				if(j == (numNames-1)){
					if(smallestZ == minZ){
						selectedNameId = nameId;
					}
				}
				System.out.print(" ");
				offset++;
			}
			System.out.println();
		}
		System.out.println("Picked the " + idToString(selectedNameId));
	}//end process hits
	
	private float getDepth(int offset){
		long depth = (long) selectBuffer.get(offset);
		return (1f+((float)depth/ 0x7fffffff));
	}
	
	public void pickModels(){
		startPicking();
		for(Entity e: entities){
			gl.glPushName(e.id);
			drawEntity(e);
			gl.glPopName();
		}
		endPicking();
	}
	
	public void drawEntity(Entity e){
		gl.glPushMatrix();
		gl.glTranslatef( e.position.x, e.position.y, e.position.z );
		pickingBox.draw(gl);
		gl.glPopMatrix();
		
	}
	
	/** 
	 * call by handleInput method from InGameGui in the mouseClicked if block 
	 **/
	public void mousePress(int x, int y){
		xCursor = x;
		yCursor = y;
		inSelectionMode = true;
	}
	
	public String idToString(int nameID){
		return " "+entities.get(nameID).getName();
	}
}
