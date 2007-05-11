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

/**
 * PickingHandler.java
 *
 * General Function: Handles the GL object picking.
 */
public class PickingHandler
{
	/* Copy of the GL instance. */
	private GL gl;
	
	/* Copy of the GLU instance. */
	private GLU glu;
	
	/* Picking box OBJ model data. */
	private OBJModel pickingBox;
	
	/* Instance of the GUI state. */
	private GUI gs;
	
	/* Size of the buffer. */
	private static final int BUFSIZE = 512;
	
	/* IntBuffer for selectable 3d object. */
	private IntBuffer selectBuffer;
	
	/* The number of hits reported. */
	private int numHits;
	
	/* The flag for selection mode. */
	public boolean inSelectionMode;
	
	/* The cursor y position. */
	private int yCursor;
	
	/* The cursor x position. */
	private int xCursor;
	
	/* The collection of entities to render in the picking buffer. */
	private ArrayList<Entity> entities;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of PickingHandler.
	 *
	 * @param gl
	 * @param glu
	 * @param entities
	 * @param gs
	 */
	public PickingHandler(GL gl,GLU glu,ArrayList<Entity> entities,GUI gs)
	{
		this.gs = gs;
		this.gl=gl;
		this.glu = glu;
		this.entities = entities;
		this.pickingBox = new OBJModel(gl,"data/models/picking_box", 5f,false);
	}
	
	/**
	 * pickModels
	 *
	 * General Function: Called every render cycle.
	 */
	public void pickModels()
	{
		if(!inSelectionMode)
		{
			return;
		}
		startPicking();
		drawEntities();
		endPicking();
		Inventory.canPick = false;
	}
	
	/**
	 * startPicking
	 *
	 * General Function: Sets up GL for picking mode.
	 */
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
	
	/**
	 * drawEntities
	 *
	 * General Function: Renders entities to the picking buffer.
	 */
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
	
	/**
	 * endPicking
	 *
	 * General Function: Sets GL back to prior picking settings.
	 */
	private void endPicking()
	{
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		
		processHits((numHits = gl.glRenderMode(GL.GL_RENDER)));
		inSelectionMode = false;
	}
	
	/**
	 * processHits
	 *
	 * General Function: Handles the picking results and hits.
	 *
	 * @param nHits The number of picking hits to process.
	 */
	private void processHits(int nHits)
	{
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
	}
	
	/**
	 * getDepth
	 *
	 * General Function: Returns the selection buffer depth.
	 *
	 * @param offset The buffer offset to use.
	 */
	private float getDepth(int offset)
	{
		long depth = (long) selectBuffer.get(offset);
		return (1f+((float)depth/ 0x7fffffff));
	}
	
	/**
	 * mousePress
	 *
	 * General Function: Call by handleInput method from InGameGui in the mouseClicked if block.
	 *
	 * @param x The mouse x position.
	 * @param y The mouse y position.
	 */
	public void mousePress(int x, int y)
	{
		xCursor = x;
		yCursor = y;
		inSelectionMode = true;
	}
	
	/**
	 * idToString
	 *
	 * General Function: Converts a name id to the actual entity name.
	 *
	 * @param nameID The name id to find.
	 */
	public String idToString(int nameID)
	{
		String retVal="";
		for(Entity e: entities)
		{
			if(e.id == nameID)
			{
				retVal = ""+e.getName();
			}
		}
		return retVal;
	}
}
