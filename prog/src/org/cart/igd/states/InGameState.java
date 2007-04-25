package org.cart.igd.states;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.cart.igd.Camera;
import org.cart.igd.Display;
import org.cart.igd.core.Kernel;
import org.cart.igd.discreet.*;
import org.cart.igd.entity.Entity;
import org.cart.igd.gl2d.GLGraphics;
import org.cart.igd.gui.Dialogue;
import org.cart.igd.gui.GUI;
import org.cart.igd.gui.InGameGUI;
import org.cart.igd.gui.PauseMenu;
import org.cart.igd.input.GameAction;
import org.cart.igd.input.UserInput;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.util.*;
import org.cart.igd.math.*;
import org.cart.igd.entity.*;
import org.cart.igd.game.*;

public class InGameState extends GameState
{
	private OBJModel worldMap;
	private SkyDome skyDome;
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Animal> animals = new ArrayList<Animal>();
		
	public Entity player;
	
	
	private Camera camera;
	private OBJModel playerSprite;
	private OBJModel partySnapper;
	private OBJModel zoopaste;
	
	private MaxParser maxParser;
	private Model test3ds;
	private Model guard;
	
	private final float GRAVITY = 0.025f;
	
	private int playerState = 0;
	
	public static final int GUI_GAME = 1;
	public static final int GUI_DIALOGUE = 0;
	
	/** Contain different gui states */
	public ArrayList<GUI> gui = new ArrayList<GUI>();
	public int currentGuiState = 0;
	
	private GameAction mouseCameraRotate;
	
	private GameAction incCameraH;
	private GameAction decCameraH;
	
	private GameAction mouseWheelUp;
	private GameAction mouseWheelDown;
	
	public Entity bush;
	public boolean nearBush = false;
	
	
	public InGameState(GL gl)
	{
		super(gl);
		try
		{
			maxParser = new MaxParser();
			test3ds = new Model(maxParser.getObjectMesh("data/models/walk.3DS"));
			guard = new Model(maxParser.getObjectMesh("data/models/guard_aw.3DS"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		
		
							
		playerSprite	= new OBJModel(gl, "data/models/flamingo_walking_cs");
		worldMap		= new OBJModel(gl, "data/models/zoo_map_km");
		skyDome			= new SkyDome(0, 90, 300f, new ColorRGBA( 0, 51, 51 ), gl);
		player			= new Player(new Vector3f(), 0f, 10f, playerSprite);
		camera			= new Camera(player, 10f, 4f);
		partySnapper = new OBJModel(gl,"data/models/party_snapper");
		OBJModel treeModel = new OBJModel(gl,"data/models/party_snapper");
		
		bush = new Bush( new Vector3f(0,0,0), 20f, playerSprite,this);
	
		/*
		Model guardo = null;
		try{
			guardo = new Model(maxParser.getObjectMesh("data/models/guard_km.3DS"));
		}catch(Exception e){}
		*/
		OBJModel guardo = new OBJModel(gl, "data/models/guard_vm");
		
		entities.add(new Guard(new Vector3f(0f,0f,0f),0f,20f,guardo,player));
		entities.add(new Guard(new Vector3f(30f,0f,-20f),0f,20f,guard,player));//3ds guard
		entities.add(new Guard(new Vector3f(0f,0f,0f),0f,20f,guardo,player));
		entities.add(new Guard(new Vector3f(-30f,0f,-20f),0f,20f,guardo,player));
		
		/* test walking guard*/
		((Guard)entities.get(0)).path.add(new Vector3f(50,0,30));
		((Guard)entities.get(0)).path.add(new Vector3f(-50,0,20));
		((Guard)entities.get(0)).path.add(new Vector3f(-100,0,100));
		((Guard)entities.get(0)).path.add(new Vector3f(100,0,-20));

		/* test walking guard */
		((Guard)entities.get(1)).path.add(new Vector3f(-30,0,30));
		((Guard)entities.get(1)).path.add(new Vector3f( 30,0, 30));
		
		/* test walking guard */
		((Guard)entities.get(2)).path.add(new Vector3f(30,0,-30));
		((Guard)entities.get(2)).path.add(new Vector3f( 20,0, 30));
		
		/* test walking guard */
		((Guard)entities.get(3)).path.add(new Vector3f(-30,0,39));
		((Guard)entities.get(3)).path.add(new Vector3f( 35,0, 35));
		
		
		/* add collectable object to the map */
				//0 reserverd
		items.add(new Item("Fish",1,1,0f,1f,
				treeModel,
				new Vector3f(-15f,0f,-15f),true,true));
		items.add(new Item("Hotdog",2,1,0f,1f,
				treeModel,
				new Vector3f(-15f,0f,-15f),true,true));
				//3 reserved
		items.add(new Item("Disguise Glasses",4,1,0f,1f,
				treeModel,
				new Vector3f(-15f,0f,-15f),true,true));
		items.add(new Item("Medication",5,1,0f,1f,
				treeModel,
				new Vector3f(-15f,0f,-15f),true,true));
		items.add(new Item("Paddle Ball",6,1,0f,1f,
				treeModel,
				new Vector3f(-15f,0f,15f),false,false));
		items.add(new Item("Zoo Paste",7,1,0f,1f,
				treeModel,
				new Vector3f(15f,0f,15f),false,false));
		items.add(new Item("Party Snapper",8,1,0f,1f,
				treeModel,
				new Vector3f(-15f,0f,-15f),true,true));
	
		
					
		animals.add(new Animal("Giraffe",4,0f,5f,new OBJModel(gl,"data/models/giraffe_scaled_2_km"), new Vector3f(10f,0f,0f),this));
		
		
		/* add different gui segments */
		gui.add(new InGameGUI(this));
		gui.add(new Dialogue(this));
		gui.add(new PauseMenu(this));
		
		mouseCameraRotate = new GameAction("mouse rotation mode",true);
		Kernel.userInput.bindToMouse(mouseCameraRotate,MouseEvent.BUTTON2 );
		Kernel.userInput.bindToMouse(mouseCameraRotate,MouseEvent.BUTTON3 );
		
		//one press actions continuous = false;
		incCameraH = new GameAction("camera h++", false);
		decCameraH = new GameAction("camera h--", false);
		Kernel.userInput.bindToKey(incCameraH, KeyEvent.VK_UP);
		Kernel.userInput.bindToKey(decCameraH, KeyEvent.VK_DOWN);
		
		
		mouseWheelUp = new GameAction("zoom in", false);
		mouseWheelDown = new GameAction("zoom out", false);
		
		Kernel.userInput.bindToMouse(mouseWheelUp,  UserInput.MOUSE_WHEEL_UP);
		Kernel.userInput.bindToMouse(mouseWheelDown,UserInput.MOUSE_WHEEL_DOWN);
		
		/* Test 3ds Data */
		test3ds.printData();
	}
	
	public void init(GL gl, GLU glu)
	{
		
	}
	
	public void rotateCamera(float amt){
		camera.facingOffset += amt;
	}
	
	/** 
	 * moved some of the character movement input due to a faster update 
	 * which made character jitter forward when walking 
	 **/
	 
	public void updateItems(){
		for(int i = 0;i<items.size();i++){
			Item item = items.get(i);
			item.update(player.position);
		}
	}
	
	public void updateAnimals(){
		for(int i = 0;i<animals.size();i++){
			Animal animal = animals.get(i);
			animal.update(player.position);
		}
	}
	 
	public void update(long elapsedTime)
	{
		updateItems();
		updateAnimals();
		
		((Bush)bush).update(elapsedTime);

		

		/* W/S - Move player forward/back. Resets camera offset to back view*/
		if(Kernel.userInput.keys[KeyEvent.VK_W])
		{
			player.walkForward(elapsedTime);
			
			if(!mouseCameraRotate.isActive() && camera.facingOffset!=0f)
			{
				if(player.position.x==player.lastPosition.x && 
						player.position.z==player.lastPosition.z)
					{
						camera.moveToBackView(8f);
					}
			}
		}
			
		else if(Kernel.userInput.keys[KeyEvent.VK_S])
			player.walkBackward(elapsedTime);
		
		/* D/A - Rotate the player on y axis */
		if(Kernel.userInput.keys[KeyEvent.VK_D])
			player.turnRight(elapsedTime);
		else if(Kernel.userInput.keys[KeyEvent.VK_A])
			player.turnLeft(elapsedTime);
		
		/* Q/E Strafe left and right */
		if(Kernel.userInput.keys[KeyEvent.VK_Q])
			player.strafeLeft(elapsedTime);
		else if(Kernel.userInput.keys[KeyEvent.VK_E])
			player.strafeRight(elapsedTime);
		
		for(int i = 0; i<entities.size(); i++){
			Entity entity = entities.get(i);
			entity.update(elapsedTime);
		}
		
		player.lastPosition.x = player.position.x;
		player.lastPosition.y = player.position.y;
		player.lastPosition.z = player.position.z;
		if(playerState==1)
		{
			if(player.position.y<5f)
				player.position.y+=0.5f;
			else if(player.position.y>=5f)
				playerState=2;
		}
		else if(playerState==2)
		{
			if(player.position.y>0f)
				player.position.y -= (float)elapsedTime * GRAVITY;
			else if(player.position.y<0f)
				player.position.y = 0f;
			if(player.position.y==0f)
				playerState = 0;
		}
		
		
		
		gui.get(currentGuiState).update(elapsedTime);
	}
	
	/** safely change gui defaul gui to dialogue gui and viasa versa */
	public void changeGuiState( int guiState){
		if(gui.size()>= guiState){
			currentGuiState =guiState;
		} else {
			System.out.println(
				"InGameState.changeGuiState(int guiState) ->no such guiState"
					+ gui.size());
		}
	}
	
	public synchronized void display(GL gl, GLU glu)
	{
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		/* Setup Camera */
		camera.lookAt(glu, player);
		
		
		/* render the bush */
		bush.render(gl);
		
		/* Render Player Model */
		
		//test3ds.render(gl);
		//playerSprite.draw(gl);
		
		player.render(gl);

		Iterator itr = entities.iterator();
		/* render all entities */
		while(itr.hasNext())
		{
			Entity e = (Entity)itr.next();
			e.render(gl);
		}	
			
		for(int i = 0;i<items.size();i++){
			Item item = items.get(i);
			item.display3d(gl);
		}
		
		for(int i = 0;i<animals.size();i++){
			Animal animal = animals.get(i);
				animal.display(gl);
		}
		
		/* Render SkyDome */
		gl.glPushMatrix();
			gl.glDisable(GL.GL_LIGHTING);
			gl.glDisable(GL.GL_LIGHT0);
			gl.glDisable(GL.GL_CULL_FACE);
			gl.glTranslatef(player.position.x, player.position.y-30f, 
					player.position.z);
			skyDome.render(gl);
			gl.glEnable(GL.GL_CULL_FACE);
			gl.glEnable(GL.GL_LIGHTING);
			gl.glEnable(GL.GL_LIGHT0);
		gl.glPopMatrix();

		/* Render Land Map */
		gl.glPushMatrix();
			gl.glTranslatef(0f, 0f, 0f);
			gl.glScalef(500f, 500f, 500f);
			worldMap.draw(gl);
		gl.glPopMatrix();
		
		/* Render GUI */
		gui.get(currentGuiState).render( Kernel.display.getRenderer().getGLG() );
		
		if(!mouseCameraRotate.isActive())
		{
			GLGraphics glg = Kernel.display.getRenderer().getGLG();
			glg.glgBegin();
			glg.drawImage(
					GLGraphics.Cursor, Kernel.userInput.mousePos[0], 
					Kernel.userInput.mousePos[1]-(GLGraphics.Cursor.imageHeight));
			glg.glgEnd();
		}
		
	}
	
	public synchronized void throwPartyPopper()
	{
		entities.add(new PartySnapper(
				new Vector3f(player.position.x, player.position.y, player.position.z),
				player.facingDirection, 
				0f,
				partySnapper)
		);
	}
	
	public void handleInput(long elapsedTime)
	{
		gui.get(currentGuiState).handleInput(elapsedTime);
		camera.zoom((float)Kernel.userInput.getMouseWheelMovement()*2f);
		
		if(mouseWheelDown.isActive())
		{
			camera.zoom(mouseWheelDown.getAmount());
		}
		
		/* Check for Escape key to end program */
		if(Kernel.userInput.keys[KeyEvent.VK_ESCAPE]) Kernel.display.stop();
		
		/* PAGEUP/PAGEDOWN - Inc./Dec. how far above the ground the camera is. */
		if(Kernel.userInput.keys[KeyEvent.VK_PAGE_UP])
			camera.changeVerticalAngleDeg( 1);
		else if(Kernel.userInput.keys[KeyEvent.VK_PAGE_DOWN])
			camera.changeVerticalAngleDeg(-1);
		
		/* HOME/END - Inc./Dec. distance from camera to player. */
		if(Kernel.userInput.keys[KeyEvent.VK_HOME])
			camera.zoom(-1);
		else if(Kernel.userInput.keys[KeyEvent.VK_END])
			camera.zoom( 1);
			
		if(Kernel.userInput.keys[KeyEvent.VK_SPACE])
		{
			if(playerState==0)
				playerState=1;
		}
		
		if(Kernel.userInput.keys[KeyEvent.VK_CONTROL])
		{
		
			throwPartyPopper();

		}

		
		/** camera mouse rotation */
		if(mouseCameraRotate.isActive())
		{
			camera.arcRotateY( - Kernel.userInput.getXDif()*0.5f );
			/** camera angle change */
			camera.changeVerticalAngleDeg( Kernel.userInput.getYDif()*0.5f );
		} //camera snap back action
	}
}