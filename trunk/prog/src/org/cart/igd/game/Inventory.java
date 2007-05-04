package org.cart.igd.game;

import java.util.*;


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
	
	public Inventory(){
		
	}
}

