package org.cart.igd.game;

/** 
 * GameEventList.java
 *
 * General Function:
 * This is an intermediary layer between Game Logic and User Input
 * acces this via user input and gui events to change the state of actions 
 * anything that is an action should be added here.
 **/
public class Global 
{
	public boolean isElephantRescued = false;
	public boolean isElephantMoviePlayed = false;
	public int nextMovieIndex = 1;
	
    public Global()
    {
    	
    }
}