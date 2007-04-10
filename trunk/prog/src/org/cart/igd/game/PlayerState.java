/**
 * @(#)PlayerStateManager.java
 *
 *
 * @author Vitaly Maximov
 * @version 1.00 2007/4/9
 */

package org.cart.igd.game;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerState implements Serializable
{
	/**
	 * increment the Id when structural changes made any related files
	 */
	private static final long serialVersionUID = 2L;

	/* all the in game objectives that need to be completed or asigned*/
	ArrayList<GameObjective> gameObjectives = new ArrayList<GameObjective>();
	
	int mapX,mapY;
	private String name = "test";
	
	public PlayerState (String name){
		this.name = name;
	}
	
	private boolean[] animalsRescued=new boolean[9];
	private long gameTime;
	
	public String getName(){
		return name;
	}
	
	
	
}
