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
	public ArrayList<Animal> animals = new ArrayList<Animal>();
		
	public Entity player;
	
	private Camera camera;
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
	
	private GameAction mouseCameraRotate;
	private GameAction mouseWheelScroll;
	
	public Entity bush;
	public boolean nearBush = false;
	
	public Inventory inventory;
	public QuestLog questlog;
	
	private Terrain terrain;
	
	private boolean loaded = false;
	boolean showInfoText = true;
	
	GLGraphics glg;
	PickingHandler ph;
	
	public InGameState(GL gl)
	{
		super(gl);
		try
		{
			maxParser = new MaxParser();
			test3ds = new Model(maxParser.getObjectMesh("data/models/walk.3DS"));
			guard3ds = new Model(maxParser.getObjectMesh("data/models/guard_aw.3DS"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		terrain = new Terrain();
		terrain.load(gl);

		questlog = new QuestLog("Quest Log",20,10);
		questlog.createQuest(Inventory.FLAMINGO,"Flamingo", "Escaped!","You have escaped the zoo!",true);
		questlog.createQuest(Inventory.TURTLES,"Turtles", "Find and Talk","I must tell the Turtles about the zoo!",false);
		questlog.createQuest(Inventory.PANDA,"Panda","Find and Talk", "I must tell the Panda about the zoo!",false);
		questlog.createQuest(Inventory.KANGAROO,"Kangaroo","Find and Talk", "I must tell the Kangaroo about the zoo!",false);
		questlog.createQuest(Inventory.GIRAFFE,"Giraffe","Find and Talk", "I must tell the Giraffe about the zoo!",false);
		questlog.createQuest(Inventory.TIGER,"Tiger","Find and Talk", "I must tell the Tiger about the zoo!",false);
		questlog.createQuest(Inventory.PENGUIN,"Penguin","Find and Talk", "I must tell the Penguin about the zoo!",false);
		questlog.createQuest(Inventory.MEERKAT,"Meerkat","Find and Talk", "I must tell the Meerkat about the zoo!",false);
		questlog.createQuest(Inventory.WOODPECKER,"WoodPecker","Find and Talk", "I must tell the WoodPecker about the zoo!",false);
		questlog.createQuest(Inventory.ELEPHANT,"Elephant", "Find and Talk","I must tell the Elephant about the zoo!",false);


		inventory = new Inventory();

		
		playerSprite	= new OBJModel(gl, "data/models/flamingo_walking_cs",1.2f,false);
		partySnapper = new OBJModel(gl,"data/models/party_snapper");
		OBJModel partySnapper = new OBJModel(gl,"data/models/party_snapper");
		OBJModel treeModel = new OBJModel(gl,"data/models/tree2",8f,false);
		OBJModel guard = new OBJModel(gl, "data/models/guard_vm",1.5f,false);
		
		player			= new Player(new Vector3f(), 0f, 10f, playerSprite);
		camera			= new Camera(player, 10f, 4f);
		
		/* special entity where animals are hidden after rescue place rescued 
		 * animals in a position relative to this */
		bush = new Bush( new Vector3f(0,0,0), 20f, treeModel,this);	
		
		/* create and add test guard */
		entities.add(new Guard(new Vector3f(0f,0f,0f),0f,20f,guard,player,2f));
		
		/* create paths for the guard to follow*/
		((Guard)entities.get(0)).path.add(new Vector3f(30,0,30));
		((Guard)entities.get(0)).path.add(new Vector3f(-30,0,30));
		((Guard)entities.get(0)).path.add(new Vector3f(-30,0,-30));
		((Guard)entities.get(0)).path.add(new Vector3f(40,0,-40));
		
		/* add collectable object to the map */
				//0 reserverd
		items.add(new Item("Fish",Inventory.FISH,1,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,0f),true,true));
				
		items.add(new Item("Hotdog",Inventory.HOTDOG,1,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,10f),true,true));
				//3 reserved
				
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
		animals.add(new Animal("Turtles",Inventory.TURTLES,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-20f),this));
				
		animals.add(new Animal("Panda",Inventory.PANDA,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-30f),this));
				
		animals.add(new Animal("Panda",Inventory.KANGAROO,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-40f),this));
		
		animals.add(new Animal("Giraffe",Inventory.GIRAFFE,0f,5f,
				new OBJModel(gl,"data/models/giraffe_scaled_2_km", 4f,false), 
				new Vector3f(10f,0f,-50f),this));
		
		animals.add(new Animal("Tiger",Inventory.TIGER,0f,5f,
				new OBJModel(gl,"data/models/giraffe_scaled_2_km", 4f,false), 
				new Vector3f(10f,0f,-60f),this));
		
		animals.add(new Animal("Penguin",Inventory.PENGUIN,0f,5f,
				new OBJModel(gl,"data/models/giraffe_scaled_2_km", 4f,false), 
				new Vector3f(10f,0f,-70f),this));
				
		animals.add(new Animal("Meerkat",Inventory.MEERKAT,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-80f),this));
				
		animals.add(new Animal("WoodPecker",Inventory.WOODPECKER,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-90f),this));
				
		animals.add(new Animal("Elephant",Inventory.ELEPHANT,0f,3f,
				new OBJModel(gl,"data/models/meerkat_low_poly", 4f,false), 
				new Vector3f(10f,0f,-100f),this));
		
		
		/* add different gui segments */
		gui.add(new InGameGUI(this));
		gui.add(new Dialogue(this));
		gui.add(new PauseMenu(this));
		
		
		/* setup input */
		mouseCameraRotate = new GameAction("mouse rotation mode",true);
		Kernel.userInput.bindToMouse(mouseCameraRotate,MouseEvent.BUTTON2 );
		Kernel.userInput.bindToMouse(mouseCameraRotate,MouseEvent.BUTTON3 );	
		
		mouseWheelScroll = new GameAction("zoom out", false);
		
		Kernel.userInput.bindToMouse(mouseWheelScroll,UserInput.MOUSE_WHEEL_DOWN);
		
		/* Test 3ds Data */
		test3ds.printData();
	}
	
	public void init(GL gl, GLU glu)
	{
		glg = Kernel.display.getRenderer().getGLG();
		ph = new PickingHandler(gl,glu,animals,this);
		
		loaded = true;
	}
	
	/* post a string message do be displayed on index line*/
	public void addInfoText(int index, String txt){
		if(index<infoText.length){
			infoText[index] = txt;
		} 
	}
	
	public void rotateCamera(float amt){
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
	
	public void updateAnimals(){
		for(int i = 0;i<animals.size();i++){
			Animal animal = animals.get(i);
			animal.update(player.position);
		}
	}
	
	public void updateQuestLog(long elapsedTime){
		questlog.update(this,elapsedTime);
	}
	 
	public void update(long elapsedTime)
	{
		updateItems(elapsedTime);
		updateAnimals();
		updateQuestLog(elapsedTime);
		
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
		addInfoText(5,"# entities: "+entities.size());
		
		/* render all animals */
		for(int i = 0;i<animals.size();i++){
			Animal animal = animals.get(i);
			animal.display(gl);
		}
		addInfoText(3,"# animals: "+animals.size());
		
		/* guard debug text*/
		addInfoText(0,"guard 1  ref angle: "+((Guard)entities.get(0)).refAngleRad);
		addInfoText(1,"player yrot: "+ player.getFacingDirection());
		addInfoText(2,"guard1 yrot: "+entities.get(0).facingDirection);
		
		/* render all items in 3d space */
		for(int i = 0;i<items.size();i++){
			Item item = items.get(i);
			item.display3d(gl);
		}
		addInfoText(4,"# items: "+items.size());

		/* render the world map and sky*/
		terrain.render( gl, player);
		/* Render GUI */
		gui.get(currentGuiState).render( Kernel.display.getRenderer().getGLG() );		
		
		/* DRAW ALL 2D draw cursor and info text */

		if(!mouseCameraRotate.isActive())
		{
			glg.glgBegin();
			glg.drawImage(
					GLGraphics.Cursor, Kernel.userInput.mousePos[0], 
					Kernel.userInput.mousePos[1]-(GLGraphics.Cursor.imageHeight));
					glg.glgEnd();
		}

		
		if(showInfoText){
			int i = 0;
			for(String s: infoText){
				i++;
				glg.glgBegin();
				glg.drawBitmapString(s, 600 , 600 - (i*16));
				glg.glgEnd();
			}
		}
		
		

		
		/* PICK MODELS*/
		ph.pickModels();
	}
	
	/** called from in game gui mousePressed block */
	public void pick(){
		ph.mousePress(Kernel.userInput.mousePress[0], Kernel.userInput.mousePress[1]);
	}
	
	public synchronized void throwPartyPopper(){
		for(int i = 0;i<inventory.items.size();i++){
			Item item = inventory.items.get(i);
			if(item.id ==8 && item.amount>0){
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

		
		/** camera mouse rotation */
		if(mouseCameraRotate.isActive())
		{
			camera.arcRotateY( - Kernel.userInput.getXDif()*0.5f );
			/** camera angle change */
			camera.changeVerticalAngleDeg( Kernel.userInput.getYDif()*0.5f );
		} //camera snap back action
	}
}