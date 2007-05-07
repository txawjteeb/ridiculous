package org.cart.igd.internet;

import org.cart.igd.game.*;
import java.net.*;
import java.io.*;
import java.util.*;


public class Connect {
	BufferedReader in;
	PrintWriter	   out;
	Socket 		   socket;	
	
	public Connect(){
		
	}
	
	public boolean connectToServer(String ip, int port, String name){
		try {
			socket = new Socket(ip,port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter( socket.getOutputStream(), true );	
			out.println(name);
		} catch(Exception e){
			return false;
		}
		return true;
	}
	
	public void sendPacket(String packet){
		out.println(packet);
	}
	
	public void sendPacket(int packet){
		out.println("" + packet);
	}
	
	public void sendPsychData(Inventory inv){
		sendPacket(inv.PSYCH_FIRST_DIRECTION);
		sendPacket(inv.PSYCH_WASTED_POPPERS);
		sendPacket(inv.PSYCH_FIRST_CLICKED_QUADRANT_OF_SCREEN);
		sendPacket(inv.PSYCH_PREFERABLE_QUADRANT_OF_SCREEN);
		sendPacket(inv.PSYCH_TOTAL_CLICKS[0]);
		sendPacket(inv.PSYCH_TOTAL_CLICKS[1]);
		sendPacket(inv.PSYCH_TOTAL_CLICKS[2]);
		sendPacket(inv.PSYCH_TOTAL_CLICKS[3]);
		sendPacket(inv.PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_ONE);
		sendPacket(inv.PSYCH_AMOUNT_OF_DIALOGUE_CHOICE_TWO);
		sendPacket(inv.PSYCH_AMOUNT_OF_ITEMS_COLLECTED);
		sendPacket(inv.PSYCH_FOOD_WATER_AFFINITY);
		sendPacket(inv.PSYCH_TIME_IN_UNIMPORTANT_PLACES_ON_MAP);	
		sendPacket(inv.PSYCH_ENTERED_UNIMPORTANT_PLACES_ON_MAP);	
		sendPacket(inv.PSYCH_UNNECESSARY_CLICKS);	
		sendPacket(inv.PSYCH_FIRST_ANIMAL_TALKED_TO);
		sendPacket(inv.PSYCH_FIRST_ANIMAL_TALKED_TO_LETTER);	
		sendPacket(inv.PSYCH_FOUND_HIDDEN_POPPERS);	
		sendPacket(inv.PSYCH_FOUND_FAKE_SOLUTION);	
	}
}

