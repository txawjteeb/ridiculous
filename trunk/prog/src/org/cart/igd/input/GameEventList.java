/**
 * @(#)GameEventList.java
 *
 *
 * @author Vitaly Maximov
 * @version 1.00 2007/3/27
 */

package org.cart.igd.input;

/** 
 * This is an intermediary layer between Game Logic and User Input
 * acces this via user input and gui events to change the state of actions 
 * anything that is an action should be added here
 **/
public class GameEventList 
{
	/*** control events ***/
	public GameEvent moveForward = new GameEvent("Move Forward");
	public GameEvent strafeLeft = new GameEvent("Move Forward");
	public GameEvent turnLeft = new GameEvent("Move Forward");
	
	/*** game logic events ***/
	public GameEvent pandaRescued = new GameEvent("Panda is Rescued");
	public GameEvent pandaJoinParty = new GameEvent("Bring panda into active party");
	public GameEvent pandaLeaveParty = new GameEvent("Panda leaves party and goes to the bush");
	
    public GameEventList() {
    }
}