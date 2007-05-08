package org.cart.igd.gui;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.core.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;
import org.cart.igd.game.*;
import org.cart.igd.sound.*;

public class MoviePlayer extends GUI{
	
	public InGameState igs = null;
	private UserInput input;
	private GameAction exitFromMovie = new GameAction("",false);
	private GameAction pauseMovie = new GameAction("",false);
	private GameAction fastForwardMovie = new GameAction("",false);
	public boolean paused = false;
	public boolean fastForward = false;
	public final int amountOfMovies = 1;
	
	public ArrayList <Movie> movies = new ArrayList<Movie>();
	
	public Movie movie;

	long timeToUpdate = 0;
	long updateTime = 100;
			
	public MoviePlayer(GameState gameState){
		super(gameState);
		input = Kernel.userInput;
		initInput();
		loadImages();
		loadSounds();		
	}
	
	public void playMovie(int movie){
		paused = false;
		fastForward = false;
		((InGameState)gameState).changeGuiState(3);
		this.movie = movies.get(movie);
		if(this.movie.finished){
			System.out.println("Could Not Locate Movie");
			((InGameState)gameState).changeGuiState(0);
		}
	}
	
	public void initInput()	{
		input.bindToKey(exitFromMovie, KeyEvent.VK_ENTER);
		input.bindToKey(pauseMovie, KeyEvent.VK_SPACE);
		input.bindToKey(fastForwardMovie, KeyEvent.VK_CONTROL);
	}
	
	public void loadImages(){
		String directory ;
		File dir;
		String[] frames;
		Texture movieFrames[];
		
		for(int j = 0;j<amountOfMovies;j++){
				directory = "data/movies/movie" + j + "/";
				dir = new File(directory);
				frames = dir.list();
				if(frames==null){
				} else{
					movieFrames = new Texture[frames.length-1];
						for(int i =0;i<frames.length-1;i++){
								movieFrames[i] = Kernel.display.getRenderer().loadImage(directory + i+ ".png");
								movies.add(new Movie(movieFrames));
						}
				}
		}
				
		
	}
	
	public void loadSounds(){
		try{
		} catch(Exception e){	
		}
		
	}
	
	public void update(long elapsedTime){
		timeToUpdate -= elapsedTime;
		if(fastForward){
			timeToUpdate -= elapsedTime*2;
		}
		if(timeToUpdate<=0&&!paused){
			if(movie!=null){
				movie.update();
			}
			timeToUpdate=updateTime;
		}
		if(movie!=null){
			if(movie.finished){
				movie.rewind();
				movie = null;
				((InGameState)gameState).changeGuiState(0);
			}
		}
	}
	
	public void handleInput(long elapsedTime){
		if(exitFromMovie.isActive()){
			movie.rewind();
			movie = null;
			((InGameState)gameState).changeGuiState(0);
		}
		if(pauseMovie.isActive()){
			if(paused){
				paused = false;
			} else paused = true;
		}
		if(fastForwardMovie.isPressed()){ //TODO: lol fix this vitaly
			fastForward = true;
		} else{
			fastForward = false;
		}

	}

	public void render(GLGraphics g){
			
		if(movie!=null){
			g.glgBegin();
			movie.render(g); // WHY IS IT NOT PRINTING?!?
			g.drawBitmapString("Space to Pause Movie",300,300);
			g.drawBitmapString("Enter to Skip Movie",300,200);
			g.drawBitmapString("Control to FastForward Movie",300,100);
			g.glgEnd();
		}
	}
	
	private class Movie {
		boolean finished = false;
		int frames;
		int currentFrame;
		Texture movie[];

		
		public Movie(Texture movie[]){
			this.movie = movie;
			this.frames = movie.length;
			this.currentFrame = 0;
		}

		public void update(){
			System.out.println("frames: " + frames + "    currentFrame: " + currentFrame );
			
			currentFrame++;
			if(currentFrame>frames-1){
				finished=true;
				currentFrame--;
			} 
		}
		
		public void render(GLGraphics g){
			g.glgBegin();
				g.drawImage(movie[currentFrame],0,0);
			g.glgEnd();
		}
		
		public void rewind(){
			finished = false;
			currentFrame = 0;
		}
	}
	
}