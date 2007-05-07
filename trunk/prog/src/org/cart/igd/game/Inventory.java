package org.cart.igd.game;

import java.util.*;
import org.cart.igd.states.*;
import org.cart.igd.core.*;

public class Inventory{
	/* 
	 ids
	 0 flamingo
	 1 turtles
	 2 panda
	 3 kangaroo
	 4 giraffe
	 5 tiger
	 6 penguin
	 7 meerkat
	 8 woodpecker 
	 9 elephant
	 */
	 
	 
	 
	 /*
	  *Start Psychology Stuff
	  */
	  
	  ///
	  public int PSYCH_FIRST_DIRECTION = 0; // 1 is right,2 is forward, 3 is left, 4 is backwords
	  ///
	  public int PSYCH_WASTED_POPPERS = 0;
	  ///
	  public int PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN = 0;// 1 is up right, 2 is upleft, 3 is down left, 4 is down right
	  public int PSYCH_PREFERABLE_QUADRANT_OF_SCREEN = 0; // 1 is up right, 2 is upleft, 3 is down left, 4 is down right
	  public int PSYCH_TOTAL_CLICKS[] = new int[4];
	  ///
	  public int PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_ONE = 0;
	  public int PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_TWO = 0;
	  ///
	  public int PSYCH_AMOUNT_OF_ITEMS_COLLECTED = 0;
	  ///
	  public int PSYCH_FOOD_WATER_AFFINITY = 0; // 1 is food 2 is water
	  ///
	  public int PSYCH_TIME_IN_UNIMPORTANT_PLACES_ON_MAP = 0;
	  public int PSYCH_ENTERED_UNIMPORTANT_PLACES_ON_MAP = 0;
	  ///
	  public int PSYCH_UNNECESSARY_CLICKS = 0;
	  ///
	  public int PSYCH_FIRST_ANIMAL_TALKED_TO = 0;
	  public String PSYCH_FIRST_ANIMAL_TALKED_TO_LETTER = "a";
	  ///
	  public int PSYCH_FOUND_HIDDEN_POPPERS = 0;
	  ///
	  public int PSYCH_FOUND_FAKE_SOLUTION = 0;
	  ///
	  
	  
	  /*
	   *End Psychology Stuff
	   */
	
	public void analyzePreferableQuadrant(){
		int max = 0;
		int maxnumber = 0;
		for(int i = 0;i<PSYCH_TOTAL_CLICKS.length;i++){
			if(PSYCH_TOTAL_CLICKS[i]>max){
				max = PSYCH_TOTAL_CLICKS[i];
				maxnumber = i;
			}
		}
		PSYCH_PREFERABLE_QUADRANT_OF_SCREEN = maxnumber+1;
	}
	
	public void newClick(){ // fix for full screensize

		int maxX = Kernel.display.getScreenWidth();
		int maxY = Kernel.display.getScreenHeight();
		if(Kernel.userInput.mousePress[1]>maxY/2&&Kernel.userInput.mousePress[0]>maxX/2){
			PSYCH_TOTAL_CLICKS[0]++;
			if(PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN==0)PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN=1;
		}
		else if(Kernel.userInput.mousePress[1]>maxY/2&&Kernel.userInput.mousePress[0]<maxX/2){
			PSYCH_TOTAL_CLICKS[1]++;
			if(PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN==0)PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN=2;
		}
		else if(Kernel.userInput.mousePress[1]<maxY/2&&Kernel.userInput.mousePress[0]<maxX/2){
			PSYCH_TOTAL_CLICKS[2]++;
			if(PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN==0)PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN=3;
		}
		else if(Kernel.userInput.mousePress[1]<maxY/2&&Kernel.userInput.mousePress[0]>maxX/2){
			PSYCH_TOTAL_CLICKS[3]++;
			if(PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN==0)PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN=4;
		}
	}
	
	
	boolean first = true;
	int mousePress[] = new int[]{0,0};
	public void update(){
		if(igs.currentGuiState==0){
			if(Kernel.userInput.mousePress[0]!=mousePress[0]||Kernel.userInput.mousePress[1]!=mousePress[1]){
				mousePress[0]=Kernel.userInput.mousePress[0];
				mousePress[1]=Kernel.userInput.mousePress[1]; 
					if(first){
						first = false;
					} else{
						newClick();
						analyzePreferableQuadrant();
					}
					
			}
		}
	}

	
	public static final int NOT_TALKED_TO = 0;
	public static final int WAITING_FOR_ITEM = 1;
	public static final int READY_TO_SAVE = 2;
	public static final int JUST_GAVE_ITEM = 3;
	public static final int SAVED_IN_BUSH = 4;
	public static final int SAVED_IN_PARTY = 5;
	public static final int JUST_SAVED = 6;
	 
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

	public static final int FISH = 1; 
	public static final int HOTDOG = 2; 
	public static final int DISGUISEGLASSES = 4; 
	public static final int MEDICATION = 5; 
	public static final int PADDLEBALL = 6; 
	public static final int ZOOPASTE = 7; 
	public static final int POPPERS = 8;
	
	public static boolean canPick = true;
	public boolean pickedUpPoppers = false;
		
	public int currentItem = -1;
	public int currentCursor = 0;
	
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Animal> animals = new ArrayList<Animal>();
	
	public boolean hasItem(int id){
		for(int i = 0;i<items.size();i++){
			Item item = items.get(i);
			if(item.itemId==id){
				return true;
			}
		}
		return false;
	}
	
	public void setCurrentItem(int currentItem){
		this.currentItem = currentItem;
	}
	
	public void resetCurrentItem(){
		this.currentItem = -1;
	}
	
	public void setCurrentCursor(int currentCursor){
		this.currentCursor = currentCursor;
	}
	
	public void resetCurrentCursor(){
		this.currentCursor = 0;
	}
	
	// wont work for poppers
	public void useItem(int id){ 
		for(int i = 0;i<items.size();i++){
			Item item = items.get(i);
			if(item.itemId==id){
				item.amount--;
				return;
			}
		}
		return;
	}
	
	InGameState igs;
	public Inventory(InGameState igs){
		this.igs= igs;
	}
}

