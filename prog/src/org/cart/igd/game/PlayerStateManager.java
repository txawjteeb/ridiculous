/**
 * @(#)PlayerStateManager.java
 *
 *
 * @author Vitaly Maximov
 * @version 1.00 2007/4/9
 */
package org.cart.igd.game;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PlayerStateManager
{
	boolean playerAuthenticated;
	boolean authenticationSuccess = true;
	String currentPlayerName;
	private PlayerState playerState;
	
    public PlayerStateManager() {
    	playerAuthenticated=false;
    }
    
    public boolean createProfile(String name, String pass){
    	//add a test whether name already exists
    	if(false){
    		return false;
    	}
    	
    	playerState=new PlayerState("default");
    	
    	return true;
    }
    
    /** open a */
    public boolean logIn(String name, String pass){
    	//put some security before proceeding
    	if(!authenticationSuccess){
    		return false;
    	}
    	
    	open(name);
    	currentPlayerName=name;
    	
    	return true;
    }
    
    public void logOut(){
    	save(playerState);
    }
    
    
    /** this will be moved to a more apropriate place later */
	public void save(PlayerState ps){
		/* Create a directory; all ancestor directories must exist*/
	    boolean success = (new File("save")).mkdir();
	    if (!success) {
	    	System.out.println("directory  creation failed");
	    }
	    
	    /* Create a directory; all non-existent ancestor directories are
	     * automatically created */
	    success = (new File("save")).mkdirs();
	    if (!success) {
	    	System.out.println("directory  creation failed");
	    }
		
		FileOutputStream fos=null;
	    ObjectOutputStream oos=null;
		   
	    try{
	    	fos= new FileOutputStream("save/"+ps.getName()+".dat");
	    	oos= new ObjectOutputStream(fos);
	    	oos.writeObject(ps);  //serializing
	    } catch (IOException e) {
	    	e.printStackTrace(); 
	    } finally {
	    	try {
	    		oos.flush();
	    		oos.close();
	    		fos.close();
	    	} catch (IOException e1) {
	    		e1.printStackTrace();
	    	}
		}
	    System.out.println("saved");
	}
	
	public PlayerState open(String name){
		FileInputStream fis=null;
		ObjectInputStream oIn=null;
		PlayerState ps=null;	   
		   try{
			   System.out.println("Loading...");
			   fis= new FileInputStream("save/"+name+".dat");
			   oIn = new ObjectInputStream(fis);
		   
			   //de-serializing
			   ps = (PlayerState) oIn.readObject();
			   //addPlayer(pl);

		   }catch(IOException e){
			   e.printStackTrace(); 
		   }catch(ClassNotFoundException e){
		       e.printStackTrace(); 
		   }finally{
			   try {
				   oIn.close();
				   fis.close();
			   } catch (IOException e1) {
				   e1.printStackTrace();
			   }
		   }
		   return ps;
	}
    
}