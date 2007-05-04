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
import org.cart.igd.Renderer;
import org.cart.igd.core.Kernel;
import org.cart.igd.discreet.*;
import org.cart.igd.entity.Entity;
import org.cart.igd.gl2d.GLGraphics;
import org.cart.igd.gui.Dialogue;
import org.cart.igd.gui.GUI;
import org.cart.igd.gui.InGameGUI;
import org.cart.igd.gui.PauseMenu;
import org.cart.igd.input.GameAction;
import org.cart.igd.input.PickingHandler;
import org.cart.igd.input.UserInput;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.game.*;
import org.cart.igd.entity.*;

public class InGameState extends GameState
{	
	public String[] infoText = { "", "", "", "", "","","","" };
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Item> items = new ArrayList<Item>();
	//public ArrayList<Animal> animals = new ArrayList<Animal>();
	public ArrayList<Entity> interactiveEntities = new ArrayList<Entity>();	
	
	public Entity player;
	
	public  Camera camera;
	public int previousCameraZoom;
	private OBJModel playerSprite;
	private OBJModel partySnapper;
	private OBJModel zoopaste;
	
	private MaxParser maxParser;
	private Model test3ds;
	private Model guard3ds;
	
	private final float GRAVITY = 0.025f;
	
	private int playerState = 0;
	
	public static final int GUI_GAME = 1;
	public static final int GUI_DIALOGUE = 0;
	
	/** Contain different gui states */
	public ArrayList<GUI> gui = new ArrayList<GUI>();
	public int currentGuiState = 0;
	
	
	private GameAction mouseWheelScroll;
	
	public Entity bush;
	public boolean nearBush = false;
	
	public Inventory inventory;
	public QuestLog questlog;
	
	private Terrain terrain;
	
	private boolean loaded = false;
	boolean showInfoText = true;
	
	GLGraphics glg;

	
	public InGameState(GL gl)
	{
		super(gl);
		try
		{
			maxParser = new MaxParser();
			test3ds = new Model(maxParser.getObjectMesh("data/models/walk.3DS"));
			guard3ds = new Model(maxParser.getObjectMesh("data/models/guard_aw.3DS"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		playerSprite	= new OBJModel(gl, "data/models/flamingo_walking_cs",1.2f,false);
		partySnapper = new OBJModel(gl,"data/models/party_snapper");
		OBJModel partySnapper = new OBJModel(gl,"data/models/party_snapper");
		OBJModel treeModel = new OBJModel(gl,"data/models/tree2",8f,false);
		OBJModel guard = new OBJModel(gl, "data/models/guard_vm",1.5f,false);
		
		/* init container/gamelocic classes*/
		terrain = new Terrain();
		terrain.load(gl);

		questlog = new QuestLog("Quest Log",20,10);
		questlog.load();

		inventory = new Inventory();
		
		player			= new Player(new Vector3f(), 0f, 10f, playerSprite);
		camera			= new Camera(player, 10f, 4f);
		
		/* special entity where animals are hidden after rescue place rescued 
		 * animals in a position relative to this */
		bush = new Bush( new Vector3f(0,0,0), 20f, treeModel,this);	
		
		/* create and add test guard */
		entities.add(new Guard(new Vector3f(0f,0f,0f),0f,20f,guard,player,2f));
		
		/* create paths for the guard to follow*/
		((Guard)entities.get(0)).path.add(new Vector3f(5,0,5));
		((Guard)entities.get(0)).path.add(new Vector3f(-5,0,-5));
		((Guard)entities.get(0)).path.add(new Vector3f(-5,0,5));
		((Guard)entities.get(0)).path.add(new Vector3f(5,0,-5));
		
		/* add collectable object to the map */
		items.add(new Item("Fish",Inventory.FISH,1,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,0f),true,true));
				
		items.add(new Item("Hotdog",Inventory.HOTDOG,1,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,10f),true,true));
				
		items.add(new Item("Disguise Glasses",Inventory.DISGUISEGLASSES,1,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,20f),true,true));
				
		items.add(new Item("Medication",Inventory.MEDICATION,1,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,30f),true,true));
				
		items.add(new Item("Paddle Ball",Inventory.PADDLEBALL,1,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,40f),false,false));
				
		items.add(new Item("Zoo Paste",Inventory.ZOOPASTE,1,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,50f),false,false));
				
		items.add(new Item("Party Snapper",Inventory.POPPERS,50,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,60f),true,true));
/*
 	public static final int FLAMINGO = 0;
	public static final int TURTLES = 1; 
	public static final int PANDA = 2; 
	public static final int KANGAROO = 3; 
	public static final int GIRAFFE = 4; 
	public static final int TIGER = 5; 
	public static final int PENGUIN = 6; 
	public static final int MEERKAT = 7; 
	public static final int WOODPECKER = 8;
	public static final int ELEPHANT = 9;  
*/
		/* add animals to the map */
		interactiveEntities.add(new Animal("Turtles",Inventory.TURTLES,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-20f),this));
				
		interactiveEntities.add(new Animal("Panda",Inventory.PANDA,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-30f),this));
				
		interactiveEntities.add(new Animal("Panda",Inventory.KANGAROO,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-40f),this));
		
		interactiveEntities.add(new Animal("Giraffe",Inventory.GIRAFFE,0f,5f,
				new OBJModel(gl,"data/models/giraffe_scaled_2_km", 4f,false), 
				new Vector3f(10f,0f,-50f),this));
			((Animal)interactiveEntities.get(3)).state = Animal.SAVED_BUSH;//test gui buttons
				
		interactiveEntities.add(new Animal("Tiger",Inventory.TIGER,0f,5f,
				new OBJModel(gl,"data/models/giraffe_scaled_2_km", 4f,false), 
				new Vector3f(10f,0f,-60f),this));
		
		interactiveEntities.add(new Animal("Penguin",Inventory.PENGUIN,0f,5f,
				new OBJModel(gl,"data/models/giraffe_scaled_2_km", 4f,false), 
				new Vector3f(10f,0f,-70f),this));
				
		interactiveEntities.add(new Animal("Meerkat",Inventory.MEERKAT,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-80f),this));
				
		interactiveEntities.add(new Animal("WoodPecker",Inventory.WOODPECKER,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-90f),this));
				
		interactiveEntities.add(new Animal("Elephant",Inventory.ELEPHANT,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-100f),this));

		/* add interactive terrain items*/
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(-10f,10f,-10), 0f, 3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 10f,false) 
				,10));
		
		
		mouseWheelScroll = new GameAction("zoom out", false);
		
		Kernel.userInput.bindToMouse(mouseWheelScroll,UserInput.MOUSE_WHEEL_DOWN);
		
		/* Test 3ds Data */
		test3ds.printData();
	}
	
	public void init(GL gl, GLU glu)
	{
		glg = Kernel.display.getRenderer().getGLG();
		
		/* add different gui segments */
		gui.add(new InGameGUI(this,gl,glu));
		gui.add(new Dialogue(this));
		gui.add(new PauseMenu(this));
		
		loaded = true;
	}
	
	public void rotateCamera(float amt)
	{
		camera.facingOffset += amt;
	}
	
	/** 
	 * moved some of the character movement input due to a faster update 
	 * which made character jitter forward when walking 
	 **/
	public void updateItems(long elapsedTime){
		for(int i = 0;i<items.size();i++){
			Item item = items.get(i);
			item.update(player.position,this, elapsedTime);
		}
	}
	
	public void updateInteractiveEntities(long elapsedTime){
		for( Entity e : interactiveEntities ){
			if(e instanceof Animal){
				((Animal)e).update(player.position);
			} else {
				e.update(elapsedTime);
			}
		}
	}
	
	public void updateQuestLog(long elapsedTime){
		questlog.update(this,elapsedTime);
	}
	 
	public void update(long elapsedTime)
	{
		updateItems(elapsedTime);
		updateInteractiveEntities(elapsedTime);
		updateQuestLog(elapsedTime);
		
		((Bush)bush).update(elapsedTime);
		
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
	
	/** select differen gui subsets */
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
		player.render(gl);
		
		/* render all entities */
		Iterator itr = entities.iterator();
		while(itr.hasNext())
		{
			Entity e = (Entity)itr.next();
			e.render(gl);
		}	
		Renderer.info[1]="# entities: "+entities.size();
		
		
		/* render all animals */
		for( Entity e : interactiveEntities ){
			int animCount = 0;
			int terrCount = 0;
			if( e instanceof Animal){
				animCount ++;
				((Animal)e).display(gl);
			}
			if(e instanceof TerrainEntity){
				e.render(gl);
				terrCount++;
			}
			
			Renderer.info[2]="# animals: "+animCount;
			Renderer.info[3]="# terrObj: "+terrCount;
		}
		
		
		/* render all items in 3d space */
		for(int i = 0;i<items.size();i++){
			Item item = items.get(i);
			item.display3d(gl);
		}
		Renderer.info[4]="# items: "+items.size();
		
		/* render the world map and sky*/
		terrain.render( gl, player);
		
		/* Render GUI */
		gui.get(currentGuiState).render( Kernel.display.getRenderer().getGLG() );	
	}
	
	public synchronized void throwPartyPopper(){
		for(int i = 0;i<inventory.items.size();i++){
			Item item = inventory.items.get(i);
			if(item.itemId ==8 && item.amount>0){
				entities.add(new PartySnapper(
					new Vector3f(player.position.x, player.position.y, player.position.z),
					player.facingDirection, 
					0f,
					partySnapper)
				);
				item.amount--;
			}
		}
	}
	
	public void handleInput(long elapsedTime)
	{
		gui.get(currentGuiState).handleInput(elapsedTime);
		previousCameraZoom = (int)camera.distance;
		camera.zoom((float)Kernel.userInput.getMouseWheelMovement()*2f);
		
		if(mouseWheelScroll.isActive())
		{
			camera.zoom(mouseWheelScroll.getAmount());
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
		
		if(Kernel.userInput.keys[KeyEvent.VK_CONTROL]){
			throwPartyPopper();
		}
	}
}