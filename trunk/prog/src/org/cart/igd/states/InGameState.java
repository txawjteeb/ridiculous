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
/*
 -Get all models in game
 -Finish all music
 -Get all music in game
 -Finish Menu
 -Get all animal cage solutions
 -Finish  AVI state 
 -Server printout 
 */

/**
 * InGameState.java
 *
 * General Function: Handles almost all of the game play.
 */
public class InGameState extends GameState
{	
	public String[] infoText = { "", "", "", "", "","","","" };
	
	/* Collection of all in-game entities. */
	public List<Entity> entities = Collections.synchronizedList(new ArrayList<Entity>());
	
	/* Collection of items. */
	public ArrayList<Item> items = new ArrayList<Item>();
	
	/* Collection of interactive entities in-game. */
	public ArrayList<Entity> interactiveEntities = new ArrayList<Entity>();
	
	/* Collection of unnecessary explorable objects. */
	public ArrayList<UnnecessaryExplore> unnecessaryExplores = new ArrayList<UnnecessaryExplore>();	

	/* Collection of entities to remove from the all-entity collection. */
	public ArrayList<Entity> removeList = new ArrayList<Entity>();
	
	/* The player entity. */
	public Entity player;
	
	/* The in-game camera. */
	public Camera camera;
	
	/* Previous camera zoom. */
	public int previousCameraZoom;
	
	/* Item Models*/
	private OBJModel partySnapper;
	private OBJModel sunglasses;
	private OBJModel hotdog;
	private OBJModel medicinebottle;
	private OBJModel zoopaste;
	private OBJModel bucketoffish;
	private OBJModel paddleball; 

	/* Bush OBJ Model Data. */
	private OBJModel bushModel;
	
	
	/* ExplorationBox OBJ Model Data. */
	private OBJModel explorationBox;
	
	/* WaterAffinity OBJ Model Data. */
	private OBJModel waterAffinity;
	
	/* FoodAffinity OBJ Model Data. */
	private OBJModel foodAffinity;
	
	/* Constant gravity variable. */
	private final float GRAVITY = 0.025f;
	
	/* Player's state. */
	private int playerState = 0;
	
	/* Game GUI index. */
	public static final int GUI_GAME = 1;
	
	/* Dialogue GUI index. */
	public static final int GUI_DIALOGUE = 0;
	
	/* Collection of GUI states */
	public ArrayList<GUI> gui = new ArrayList<GUI>();
	
	/* The GameAction that handles mouse wheel scrolling. */
	private GameAction mouseWheelScroll;
	
	/* Bush entity. */
	public Entity bush;
	
	/* Flag that says if the player is near the bush. */
	public boolean nearBush = false;
	
	/* Instance of Inventory object. */
	public Inventory inventory;
	
	/* Instance of QuestLog object. */
	public QuestLog questlog;
	
	/* Instance of Terrain object. */
	private Terrain terrain;
	
	private boolean loaded = false;
	private boolean showInfoText = true;
	
	/* Instance of GLGraphics object. */
	private GLGraphics glg;
	
	/* Collection of cages. */
	public ArrayList<Cage> cages = new ArrayList<Cage>();	

	public Sound backgroundMusic = null,throwPopper = null,popPopper = null,
		giveItem = null,openQuestLog = null, closeQuestLog = null,
		questLogMusic = null,freeAnimalTune = null;
	
	public Sound turnPage[] = new Sound[4];
	
	public GuardSquad guardSquad;
	public OBJAnimation flamingoWalk;
	public OBJAnimation flamingoIdle;
	public OBJAnimation turtleIdle;
	public OBJAnimation kangarooIdle;
	public OBJAnimation giraffeIdle;//NOT exported yet
	public OBJAnimation pandaIdle;
	public OBJAnimation tigerIdle;//NOT exported yet
	public OBJAnimation penguinIdle;
	public OBJAnimation meerkatIdle;
	public OBJAnimation woodpeckerIdle;
	public OBJAnimation elephantIdle;
	
	/** create a sound manager with current sound settings */
	private SoundManager sm = new SoundManager(Kernel.soundSettings);
	
	public InGameState(GL gl)
	{
		super(gl);
		
		/* load common sounds */
		try{
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
		}catch (Exception e){
			e.printStackTrace();
		}
		
		bushModel	= new OBJModel(gl,"bush");
		partySnapper = new OBJModel(gl,"party_popper_bh");
		
		sunglasses= new OBJModel(gl,"disguiseglasses");
		hotdog= new OBJModel(gl,"hot_dog_bh");
		medicinebottle= new OBJModel(gl,"medicine_bottle_bh");
		zoopaste= new OBJModel(gl,"tooth_paste_bh");
		bucketoffish= new OBJModel(gl,"bucket_of_fish_bh");
		paddleball= new OBJModel(gl,"paddle-ball");
		
		OBJModel partySnapper = new OBJModel(gl,"party_popper_bh");
		
		explorationBox = new OBJModel(gl,"exploration_box",5f,false);
		waterAffinity = new OBJModel(gl,"water_affinity",5f,false);
		foodAffinity = new OBJModel(gl,"food_Affinity",5f,false);
		
		
		/* init container/gamelocic classes*/
		terrain = new Terrain(this);
		terrain.load(gl);

		questlog = new QuestLog("Quest Log",20,10);
		questlog.load();

		inventory = new Inventory(this);
		
		/* create objAnimations */

		flamingoWalk = new OBJAnimation(gl,1,"flamingo_walking_",105,2f);
		flamingoIdle = new OBJAnimation(gl,1,"flamingo_idle_",500,2f);
		turtleIdle = new OBJAnimation(gl,1,"turtle_idle_",300,4f);
		kangarooIdle = new OBJAnimation(gl,1,"kangaroo_idle_",300,4f);
		giraffeIdle = new OBJAnimation(gl,10,"giraffe_idle_",1000,12f);
		tigerIdle = new OBJAnimation(gl,1,"tiger_idle_",1000,40f);
		penguinIdle = new OBJAnimation(gl,1,"penguin_idle_",300,4f);
		pandaIdle = new OBJAnimation(gl,1,"panda_idle_",300,4f);
		meerkatIdle = new OBJAnimation(gl,1,"meerkat_idle_",300,2f);
		woodpeckerIdle = new OBJAnimation(gl,1,"woodpecker_idle_",300,4f);
		elephantIdle = new OBJAnimation(gl,1,"elephant_idle_",300,22f);
		
		player = new Player(new Vector3f(-20f,0f,-20f), 0f, .2f, 
				flamingoWalk,flamingoIdle);
		camera = new Camera(player, 10f, 4f);
		
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
				bucketoffish,
				new Vector3f(-15f,0f,100f),true,true));
				
		items.add(new Item("Hotdog",Inventory.HOTDOG,1,0f,1f,
				hotdog,
				new Vector3f(-15f,0f,10f),true,true));
				
		items.add(new Item("Disguise Glasses",Inventory.DISGUISEGLASSES,1,0f,1f,
				sunglasses,
				new Vector3f(-15f,0f,20f),true,true));
				
		items.add(new Item("Medication",Inventory.MEDICATION,1,0f,1f,
				medicinebottle,
				new Vector3f(-15f,0f,30f),true,true));
				
		items.add(new Item("Paddle Ball",Inventory.PADDLEBALL,1,0f,1f,
				paddleball,
				new Vector3f(-15f,0f,40f),false,false));
				

				
		items.add(new Item("Party Snapper",Inventory.POPPERS,50,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,60f),true,true));
				
		items.add(new Item("Party Snapper Hidden",Inventory.POPPERS,50,0f,1f,
				partySnapper,
				new Vector3f(-15f,0f,80f),true,true));
		items.add(new Item("Zoo Paste",Inventory.ZOOPASTE,1,0f,1f,
				zoopaste,
				new Vector3f(-15f,0f,50f),false,false));
		/* add animals to the map */

		interactiveEntities.add(new Animal("Turtles",Inventory.TURTLES,0f,30f,
				turtleIdle, 
				new Vector3f(-80f,3f,180f),this,0,new Vector3f(10f,0f,10f)));
		
				
		interactiveEntities.add(new Animal("Panda",Inventory.PANDA,0f,3f,
				pandaIdle, 
				new Vector3f(30f,0f,-180f),this,0,new Vector3f(10f,0f,5f)));
				
		interactiveEntities.add(new Animal("Kangaroo",Inventory.KANGAROO,180f,20f,
				kangarooIdle, 
				new Vector3f(130f,3f,-75f),this,Inventory.DISGUISEGLASSES,new Vector3f(5f,0f,10f)));
		
		interactiveEntities.add(new Animal("Giraffe",Inventory.GIRAFFE,180f,5f,
				giraffeIdle, 
				new Vector3f(-27f,5f,208f),this,Inventory.MEDICATION,new Vector3f(10f,0f,0f)));
				
		interactiveEntities.add(new Animal("Tiger",Inventory.TIGER,0f,5f,
				tigerIdle, 
				new Vector3f(40f,3f,210f),this,Inventory.ZOOPASTE,new Vector3f(0f,0f,10f)));
		
		interactiveEntities.add(new Animal("Penguin",Inventory.PENGUIN,90f,5f,
				penguinIdle, 
				new Vector3f(-80f,9f,-181f),this,Inventory.FISH,new Vector3f(-10f,0f,0f)));
				
		interactiveEntities.add(new Animal("Meerkat",Inventory.MEERKAT,0f,9f,
				meerkatIdle, 
				new Vector3f(130f,0f,130f),this,Inventory.HOTDOG,new Vector3f(0f,2f,-10f)));
				
		interactiveEntities.add(new Animal("WoodPecker",Inventory.WOODPECKER,180f,3f,
				woodpeckerIdle, 
				new Vector3f(190f,8f,-25f),this,Inventory.PADDLEBALL,new Vector3f(-10f,0f,-10f)));
				
		interactiveEntities.add(new Animal("Elephant",Inventory.ELEPHANT,180f,40f,
				elephantIdle, 
				new Vector3f(160f,5f,80f),this,0,new Vector3f(-10f,0f,-5f)));
 
 
		/* add all cages*/
		cages.add(new Cage(9,new Vector3f(160f,6f,80f),45f,21f,new OBJModel(gl,"9",70.2f,false),this));
		cages.add(new Cage(8,new Vector3f(200f,13f,-20f),20f,14f,new OBJModel(gl,"8",70.2f,false),this));
		cages.add(new Cage(7,new Vector3f(130f,-2f,130f),1f,1.2f,new OBJModel(gl,"7",10f,false),this));
		cages.add(new Cage(6,new Vector3f(-80f,6f,-180f),1f,14f,new OBJModel(gl,"6",40.2f,false),this));
		
		
		cages.add(new Cage(5,new Vector3f(39f,8f,210f),1f,14f,new OBJModel(gl,"5",40.2f,false),this));
		cages.add(new Cage(4,new Vector3f(-30f,5f,210f),1f,14f,new OBJModel(gl,"4",40.2f,false),this));
		
		cages.add(new Cage(3,new Vector3f(130f,.1f,-80f),90f,14f,new OBJModel(gl,"3",40.2f,false),this));
		cages.add(new Cage(2,new Vector3f(30f,0f,-180f),1f,14f,new OBJModel(gl,"2",40.2f,false),this));
		cages.add(new Cage(1,new Vector3f(-80f,2f,180f),1f,14f,new OBJModel(gl,"1",40.2f,false),this));
		cages.add(new Cage(0,new Vector3f(0f,8f,0f),1f,14f,new OBJModel(gl,"0",40.2f,false),this));
	
		/* add interactive terrain items*/
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(-80f,2f,170f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.TURTLES,Inventory.FLAMINGO,this));
				
				
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(30f,0f,-20f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.TURTLES,Inventory.FLAMINGO,this,true));
				
				
						
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-30f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.PANDA,Inventory.TURTLES,this));
				
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-40f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.KANGAROO,Inventory.TURTLES,this));
				
				
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-50f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.GIRAFFE,Inventory.TURTLES,this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-60f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.TIGER,new int[]{Inventory.GIRAFFE,Inventory.PANDA},this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-70f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.PENGUIN,Inventory.TIGER,this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-80f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.MEERKAT,new int[]{Inventory.GIRAFFE,Inventory.KANGAROO},this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-90f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.WOODPECKER,Inventory.MEERKAT,this));
		interactiveEntities.add(new TerrainEntity(
				new Vector3f(20f,0f,-100f), 0f, 3f,
				new OBJModel(gl,"save_animal_thing", 2f,false),Inventory.ELEPHANT,new int[]{Inventory.WOODPECKER,Inventory.PENGUIN},this));
		
		mouseWheelScroll = new GameAction("zoom out", false);
		
		Kernel.userInput.bindToMouse(mouseWheelScroll,UserInput.MOUSE_WHEEL_DOWN);
	}
	
	public void init(GL gl, GLU glu)
	{
		glg = Kernel.display.getRenderer().getGLG();
		
		/* add different gui segments */
		gui.add(new InGameGUI(this,gl,glu));
		gui.add(new Dialogue(this));
		gui.add(new PauseMenu(this));
		
		if(backgroundMusic != null){
			backgroundMusic.loop(1f,.5f);//TODO: enable when sound fixed
		} else {
			System.out.println("backgroundMusic loop is null");
		}
	
		
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
				if(throwPopper != null){
					throwPopper.play((new Random()).nextFloat() 
						+ .5f,(new Random()).nextFloat() + .5f);
				}
				
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
				Kernel.userInput.keys[KeyEvent.VK_CONTROL] = false;
			}
			
			if(Kernel.userInput.keys[KeyEvent.VK_M]){
				//((MoviePlayer)gui.get(3)).playMovie(0);
				this.changeGameState("MiniGame");
			}
			
			entityMover();
		}
		

	}
	
	/*
	 entity placement	 
	 */
	 int currentId =0;	
	 	
	public void entityMover(){
		System.out.println("Current ID = " + currentId);
		if(Kernel.userInput.keys[KeyEvent.VK_H]){
				currentId++;
		} else if(Kernel.userInput.keys[KeyEvent.VK_G]){
				currentId--;
		}
		
		if(Kernel.userInput.keys[KeyEvent.VK_RIGHT]){
				if(entities.size()>currentId){
					entities.get(currentId).position.x+=1f;
					
				}
		} else if(Kernel.userInput.keys[KeyEvent.VK_LEFT]){
				if(entities.size()>currentId){
					entities.get(currentId).position.x-=1f;
				}
		} else if(Kernel.userInput.keys[KeyEvent.VK_UP]){
				if(entities.size()>currentId){
					entities.get(currentId).position.z+=1f;
				}
		} else if(Kernel.userInput.keys[KeyEvent.VK_DOWN]){
				if(entities.size()>currentId){
					entities.get(currentId).position.z-=1f;
				}
		} else if(Kernel.userInput.keys[KeyEvent.VK_Y]){
				if(entities.size()>currentId){
					entities.get(currentId).position.y+=1f;
				}
		} else if(Kernel.userInput.keys[KeyEvent.VK_T]){
				if(entities.size()>currentId){
					entities.get(currentId).position.y-=1f;
				}
		}
		if(entities.size()>currentId){
				System.out.println("Entity is at location " + entities.get(currentId).position.x + " " + entities.get(currentId).position.y + " " + entities.get(currentId).position.z);
		}
		
	}
	
	

	 
	 
	
	
}
