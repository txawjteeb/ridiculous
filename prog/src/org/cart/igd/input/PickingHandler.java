package org.cart.igd.input;

import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.Renderer;
import org.cart.igd.core.Kernel;
import org.cart.igd.entity.Entity;
import org.cart.igd.game.Animal;
import org.cart.igd.gui.GUI;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.GameState;
import org.cart.igd.game.Inventory;
import com.sun.opengl.util.BufferUtil;


public class PickingHandler {
	private GL gl;
	private GLU glu;
	private OBJModel pickingBox;
	private GUI gs;
	static final int BUFSIZE = 512;
	IntBuffer selectBuffer;
	int numHits;
	public boolean inSelectionMode;
	
	int yCursor, xCursor;
	ArrayList<Entity> entities;
	
	public PickingHandler(GL gl,GLU glu,ArrayList<Entity> entities,GUI gs){
		this.gs = gs;
		this.gl=gl;
		this.glu = glu;
		this.entities = entities;
		this.pickingBox = new OBJModel(gl,"data/models/picking_box", 5f,false);
	}
	
	/* call every render cycle */
	public void pickModels()
	{
		if(!inSelectionMode)
			return;
		startPicking();
		drawEntities();
		endPicking();
		Inventory.canPick = false;
	}
	
	private void startPicking()
	{
		int viewport[] = new int[4];
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		
		int selectBuf[]= new int[512];
		selectBuffer = BufferUtil.newIntBuffer(BUFSIZE);
		gl.glSelectBuffer(BUFSIZE, selectBuffer);
		
		gl.glRenderMode(GL.GL_SELECT);
		
		gl.glInitNames();
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		
		glu.gluPickMatrix((double) xCursor,
				(double) yCursor,
				5.0,5.0, viewport, 0 );
				
		glu.gluPerspective(45f,
				(float)Kernel.display.getScreenWidth()/
				(float)Kernel.display.getScreenHeight()
				, 1, 100);
				
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}
	
	private void drawEntities()
	{
		int eid = 0;
		for(Entity e: entities)
		{
			e.id = eid;
			gl.glPushName(e.id);
			gl.glPushMatrix();
			gl.glTranslatef( e.position.x, e.position.y, e.position.z );
			pickingBox.draw(gl);
			gl.glPopMatrix();
			eid++;
		}
	}
	
	private void endPicking()
	{
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		
		processHits((numHits = gl.glRenderMode(GL.GL_RENDER)));
		inSelectionMode = false;
	}
	
	private void processHits(int nHits){
		/*
		if (numHits == 0){
			return;
		}
		System.out.println("# hits: " + numHits);
		
		int selectedNameId = -1;
		float smallestZ = -1f;
		*/
		if(nHits==0) return;
		
		int selectedNameId = -1;
		float smallestZ = -1f;
		
		boolean isFirstLoop = true;
		int offset = 0;
		
		for(int i = 0; i < numHits; i++){
			//System.out.println("hit"+ (i+1));
			
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
			
			//System.out.println(" minZ: " + minZ+ "; maxZ: " + maxZ);
			
			int nameId;
			for( int j = 0; j < numNames; j++){
				nameId = selectBuffer.get(offset);
				//System.out.println( idToString(nameId));
				if(j == (numNames-1)){
					if(smallestZ == minZ){
						selectedNameId = nameId;
						gs.picked = true;
						gs.pickedId = selectedNameId;
						Renderer.info[0]=("picked: "+selectedNameId);
					}
				}
				offset++;
			}
			Renderer.info[5]="selectedID: " + selectedNameId;
		}
		Renderer.info[6] = "picked obj loc: " + idToString( selectedNameId );
	}//end process hits
	
	private float getDepth(int offset){
		long depth = (long) selectBuffer.get(offset);
		return (1f+((float)depth/ 0x7fffffff));
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
		String retVal="";
		for (Entity e: entities){
			if(e.id == nameID);
				retVal = ""+e.getName();
		}
		return retVal;
	}
}
