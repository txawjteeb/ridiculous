package org.cart.igd.states;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import java.util.Random;

import org.cart.igd.Camera;
import org.cart.igd.Renderer;
import org.cart.igd.core.Kernel;
import org.cart.igd.discreet.*;
import org.cart.igd.entity.Entity;
import org.cart.igd.gl2d.GLGraphics;
import org.cart.igd.gui.Dialogue;
import org.cart.igd.gui.*;
import org.cart.igd.input.GameAction;
import org.cart.igd.input.PickingHandler;
import org.cart.igd.input.UserInput;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJAnimation;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.game.*;
import org.cart.igd.entity.*;
import org.cart.igd.sound.*;
//import org.cart.igd.media.CutscenePlayer;

/**
 * Handles most of the game playS
 **/
public class InGameState extends GameState
{	
	public String[] infoText = { "", "", "", "", "","","","" };
	
	public List<Entity> entities = Collections.synchronizedList(new ArrayList<Entity>());
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Entity> interactiveEntities = new ArrayList<Entity>();	
	public ArrayList<UnnecessaryExplore> unnecessaryExplores = new ArrayList<UnnecessaryExplore>();	

	public ArrayList<Entity> removeList = new ArrayList<Entity>();
	
	public Entity player;
	
	public  Camera camera;
	public int previousCameraZoom;
	private OBJModel playerSprite;
	private OBJModel partySnapper;
	private OBJModel bushModel;
	private OBJModel zoopaste;
	private OBJModel explorationBox,waterAffinity,foodAffinity;
	
	private MaxParser maxParser;
	private Model test3ds;
	private Model guard3ds;
	
	private final float GRAVITY = 0.025f;
	
	private int playerState = 0;
	
	public static final int GUI_GAME = 1;
	public static final int GUI_DIALOGUE = 0;
	
	/** Contain different gui states */
	public ArrayList<GUI> gui = new ArrayList<GUI>();
	
	
	
	private GameAction mouseWheelScroll;
	
	public Entity bush;
	public boolean nearBush = false;
	
	public Inventory inventory;
	public QuestLog questlog;
	//public CutscenePlayer cutscenePlayer;
	
	private Terrain terrain;
	
	private boolean loaded = false;
	boolean showInfoText = true;
	
	GLGraphics glg;

	public Sound backgroundMusic,throwPopper,popPopper,giveItem,openQuestLog, closeQuestLog,questLogMusic,freeAnimalTune;
	public Sound turnPage[] = new Sound[4];
	
	public GuardSquad guardSquad;
	public OBJAnimation flamingoWalk;
	public OBJAnimation flamingoIdle;
	//public OBJAnimation turtleIdle;
	
	public InGameState(GL gl)
	{
		super(gl);
		try
		{
			maxParser = new MaxParser();
			
			test3ds = new Model(maxParser.getObjectMesh("data/models/walk.3DS"));
			
			backgroundMusic = new Sound("data/sounds/music/zoo_music.ogg");
			questLogMusic = new Sound("data/sounds/music/questlog_music.ogg");
			throwPopper = new Sound("data/sounds/effects/throw_popper.ogg");
			popPopper = new Sound("data/sounds/effects/pop_popper.ogg");
			giveItem = new Sound("data/sounds/effects/give_item.ogg");
			openQuestLog = new Sound("data/sounds/effects/open_quest_log.ogg");
			closeQuestLog = new Sound("data/sounds/effects/close_quest_log.ogg");
			freeAnimalTune = new Sound("data/sounds/music/free_animal_tune.ogg");
			for(int i = 0;i<turnPage.length;i++){
				turnPage[i] = new Sound("data/sounds/effects/turn_page-" + i + ".ogg");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		bushModel	= new OBJModel(gl,"data/models/bush");
		partySnapper = new OBJModel(gl,"data/models/party_snapper");
		OBJModel partySnapper = new OBJModel(gl,"data/models/party_snapper");
		OBJModel treeModel = new OBJModel(gl,"data/models/tree2",8f,false);
		
		explorationBox = new OBJModel(gl,"data/models/exploration_box",5f,false);
		waterAffinity = new OBJModel(gl,"data/models/water_affinity",5f,false);
		foodAffinity = new OBJModel(gl,"data/models/food_Affinity",5f,false);
		
		
		/* init container/gamelocic classes*/
		terrain = new Terrain();
		terrain.load(gl);

		questlog = new QuestLog("Quest Log",20,10);
		questlog.load();

		inventory = new Inventory(this);
		//cutscenePlayer = new CutscenePlayer();
		//cutscenePlayer.loadMovie("data/movies/flamingo_idle.avi");
		
		/* create objAnimation of a flamingo*/
		flamingoWalk = new OBJAnimation(gl,10,"data/models/flamingo",105);
		flamingoIdle = new OBJAnimation(gl,10,"data/models/flamingo",500);
		
		//turtleIdle = new OBJAnimation(gl,10,"data/models/flamingo",500);
		
		player			= new Player(new Vector3f(-20f,0f,-20f), 0f, .2f, flamingoWalk,flamingoIdle);
		camera			= new Camera(player, 10f, 4f);
		
		/* special entity where animals are hidden after rescue place rescued 
		 * animals in a position relative to this */
		bush = new Bush( new Vector3f(0,0,0), 20f, bushModel,this);	
		
		/* guards as a whole unit */
		guardSquad = new GuardSquad(this);

		
		

		/* add collectable object to the map */
		
		
		/*
		 public UnnecessaryExplore(Vector3f pos, float fD, float bsr, OBJModel model,InGameState igs,boolean display){
		super(pos, fD, bsr, model);
		this.display = display;
	}
		 */
		
		unnecessaryExplores.add(new UnnecessaryExplore("Nothing",new Vector3f(50f,0f,50f),0f,10f,explorationBox,this,true));
		unnecessaryExplores.add(new UnnecessaryExplore("Water",new Vector3f(-50f,0f,-50f),0f,10f,waterAffinity,this,true));
		unnecessaryExplores.add(new UnnecessaryExplore("Food",new Vector3f(50f,0f,-50f),0f,10f,foodAffinity,this,true));	
		unnecessaryExplores.add(new UnnecessaryExplore("Left",new Vector3f(0f,0f,-21f),0f,20f,foodAffinity,this,false));
		unnecessaryExplores.add(new UnnecessaryExplore("Right",new Vector3f(0f,0f,21f),0f,20f,foodAffinity,this,false));
		unnecessaryExplores.add(new UnnecessaryExplore("Up",new Vector3f(21f,0f,0f),0f,20f,foodAffinity,this,false));
		unnecessaryExplores.add(new UnnecessaryExplore("Down",new Vector3f(-21f,0f,0f),0f,20f,foodAffinity,this,false));
		
		
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
				
				
				
		items.add(new Item("Party Snapper Hidden",Inventory.POPPERS,50,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,80f),true,true));
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
				flamingoWalk, 
				new Vector3f(10f,0f,-20f),this,0,new Vector3f(10f,0f,10f)));
				
		interactiveEntities.add(new Animal("Panda",Inventory.PANDA,0f,3f,
				flamingoIdle, 
				new Vector3f(10f,0f,-30f),this,0,new Vector3f(10f,0f,5f)));
				
		interactiveEntities.add(new Animal("Kangaroo",Inventory.KANGAROO,0f,3f,
				flamingoWalk, 
				new Vector3f(10f,0f,-40f),this,Inventory.DISGUISEGLASSES,new Vector3f(5f,0f,10f)));
		
		interactiveEntities.add(new Animal("Giraffe",Inventory.GIRAFFE,0f,5f,
				flamingoWalk, 
				new Vector3f(10f,0f,-50f),this,Inventory.MEDICATION,new Vector3f(10f,0f,0f)));
				
		interactiveEntities.add(new Animal("Tiger",Inventory.TIGER,0f,5f,
				flamingoWalk, 
				new Vector3f(10f,0f,-60f),this,Inventory.ZOOPASTE,new Vector3f(0f,0f,10f)));
		
		interactiveEntities.add(new Animal("Penguin",Inventory.PENGUIN,0f,5f,
				flamingoWalk, 
				new Vector3f(10f,0f,-70f),this,Inventory.FISH,new Vector3f(-10f,0f,0f)));
				
		interactiveEntities.add(new Animal("Meerkat",Inventory.MEERKAT,0f,3f,
				flamingoWalk, 
				new Vector3f(10f,0f,-80f),this,Inventory.HOTDOG,new Vector3f(0f,0f,-10f)));
				
		interactiveEntities.add(new Animal("WoodPecker",Inventory.WOODPECKER,0f,3f,
				flamingoWalk, 
				new Vector3f(10f,0f,-90f),this,Inventory.PADDLEBALL,new Vector3f(-10f,0f,-10f)));
				
		interactiveEntities.add(new Animal("Elephant",Inventory.ELEPHANT,0f,3f,
				flamingoWalk, 
				new Vector3f(10f,0f,-100f),this,0,new Vector3f(-10f,0f,-5f)));

		
		/* add interactive terrain items*/
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-20f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.TURTLES,Inventory.FLAMINGO,this));
				
				
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(30f,0f,-20f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.TURTLES,Inventory.FLAMINGO,this,true));
				
				
						
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-30f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.PANDA,Inventory.TURTLES,this));
				
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-40f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.KANGAROO,Inventory.TURTLES,this));
				
				
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-50f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.GIRAFFE,Inventory.TURTLES,this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-60f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.TIGER,new int[]{Inventory.GIRAFFE,Inventory.PANDA},this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-70f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.PENGUIN,Inventory.TIGER,this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-80f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.MEERKAT,new int[]{Inventory.GIRAFFE,Inventory.KANGAROO},this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-90f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.WOODPECKER,Inventory.MEERKAT,this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-100f), 0f, 3f,
				new OBJModel(gl,"data/models/save_animal_thing", 2f,false),Inventory.ELEPHANT,new int[]{Inventory.WOODPECKER,Inventory.PENGUIN},this));
		
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
		gui.add(new MoviePlayer(this));
		//backgroundMusic.loop(1f,.5f);//TODO: enable when sound fixed
		
		guardSquad.init( gl, glu );
		
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
				((Animal)e).update(elapsedTime);
			} else {
				e.update(elapsedTime);
			}
		}
	}
	
	public void updateUnnecessaryExplores(long elapsedTime){
		for(int i = 0;i<unnecessaryExplores.size();i++){
			UnnecessaryExplore unnecessaryExplore = unnecessaryExplores.get(i);
			unnecessaryExplore.update(player.position,elapsedTime);
		}
	}
	
	
	public void updateQuestLog(long elapsedTime){
		questlog.update(this,elapsedTime);
	}
	 
	public Animal getAnimal(String name){
		for( Entity e : interactiveEntities ){
			if(e instanceof Animal){
				Animal a = (Animal)e;
				if(a.name.equals(name)){
					return a;
				}
			}
		}
		return null;
	}
	
	public void removePartyAnimals(){
		for( Entity e : interactiveEntities ){
			if(e instanceof Animal){
				Animal a = (Animal)e;
				if(a.state==Inventory.SAVED_IN_PARTY){
					a.state=Inventory.SAVED_IN_BUSH;
				}
			}
		}
		inventory.animals.clear();
		inventory.currentCursor=Inventory.FLAMINGO;	
	}
	 
	public void update(long elapsedTime)
	{
		player.update(elapsedTime);

		
		/* reset guads be removing Noise entities TODO: make sure to call this once */
		if(Kernel.userInput.keys[KeyEvent.VK_R]) guardSquad.reset();
		guardSquad.reset = true;
		
		inventory.update();
		gui.get(currentGuiState).update(elapsedTime);

		updateItems(elapsedTime);
		updateInteractiveEntities(elapsedTime);
		updateQuestLog(elapsedTime);
		updateUnnecessaryExplores(elapsedTime);
		
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
		
		entities.removeAll(removeList);
		removeList.clear();
		
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
		//if(!cutscenePlayer.isStopped)
		//{
		//	cutscenePlayer.render(glg);
		//	return;
		//}
		
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		
		/* Setup Camera */
		camera.lookAt(glu, player);
		
		/* render the bush */
		bush.render(gl);
		

		//objAnimation.render(gl);
		

		/* Render Player Model */
		if(currentGuiState!=4){
			player.render(gl);
		}
		
		
		/* render all entities: partySnappers, guards*/
		synchronized ( entities ){
			for( Entity e: entities )
			{
				e.render(gl);
			}	
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
		
		for(int i = 0;i<unnecessaryExplores.size();i++){
			UnnecessaryExplore unnecessaryExplore = unnecessaryExplores.get(i);
			unnecessaryExplore.display(gl);
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
				throwPopper.play((new Random()).nextFloat() + .5f,(new Random()).nextFloat() + .5f);//TODO: re-enable when sound is fixed
				entities.add(new PartySnapper(
					new Vector3f(player.position.x, player.position.y, player.position.z),
					player.facingDirection, 
					0f,
					partySnapper,this)
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
		
		if(currentGuiState==0){
			if(mouseWheelScroll.isActive())
			{
				camera.zoom(mouseWheelScroll.getAmount());
			}
			
		
			/* Check for Escape key to end program */
			if(Kernel.userInput.keys[KeyEvent.VK_ESCAPE]) Kernel.display.stop();
			
			if(Kernel.userInput.keys[KeyEvent.VK_0])
			{
				Kernel.userInput.keys[KeyEvent.VK_0] = false;
			//	cutscenePlayer.playMovie();
			}
			
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
				Kernel.userInput.keys[KeyEvent.VK_CONTROL]=false;
			}
			
			if(Kernel.userInput.keys[KeyEvent.VK_END]){
				((MoviePlayer)gui.get(3)).playMovie(0);
				//this.changeGameState("MiniGame");
			}
			
			if(Kernel.userInput.keys[KeyEvent.VK_M]){
				//((MoviePlayer)gui.get(3)).playMovie(0);
				this.changeGameState("MiniGame");
			}	
		}
		

	}
}