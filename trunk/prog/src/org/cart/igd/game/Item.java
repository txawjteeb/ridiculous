package org.cart.igd.game;

import org.cart.igd.models.obj.OBJModel;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.cart.igd.math.Vector3f;
import org.cart.igd.entity.*;
import org.cart.igd.gl2d.*;
import org.cart.igd.util.*;
import org.cart.igd.input.*;
import org.cart.igd.states.*;
import org.cart.igd.core.*;

public class Item extends Entity {
	/*
	 states 
	 0 in map
	 1 in inventory
	 2 used
	 */
	 
	 /*
	 taken from dialogue
	 */
	 
	 int mouseOverTime = 0;
		boolean mouseOver = false;
		int degree = 0; // positive turns it counterclockwise
		int mathDegrees[] = new int[]{20,10,8,5,2,1};
		int velDegrees[] = new int[]{12,10,8,6,4,2};
		int counter = 0;
		float alphaSwing = 1f;
		boolean left = false;
		boolean swinging;
		float alpha = 1f;
		float alphaText = 0f;
		
	/*
	 taken from dialogue
	 */
	 
	public static final int MAX_POPPERS = 50;
	public Texture texture;
	public String name;
	public int id;
	public int state = 0;
	public int amount;
	public boolean turn;
	public boolean bounce;
	
	boolean up = true;
	float difference;
	
	int x, y;
	
	long timeToUpdate = 0;
	long updateTime = 30;
	
	public Item(String name,int id, int amount,float fd, float bsr, OBJModel model, Vector3f location, boolean turn, boolean bounce){
		super(location,fd,bsr, model);
		this.name = name;
		this.id = id;
		this.amount = amount;
		this.turn = turn;
		this.bounce = bounce;
	}
	
	public Item(String name,int id, int amount,float fd, float bsr, OBJModel model, Vector3f location){
		super(location,fd,bsr, model);
		this.name = name;
		this.id = id;
		this.amount = amount;
		this.turn = false;
		this.bounce = false;
	}
	
	public void update(Vector3f playerPosition,InGameState igs,long elapsedTime){
		if(amount<1){
			boolean found = false;
			for(int i = 0;i<igs.inventory.items.size();i++){
						Item item = igs.inventory.items.get(i);
						if(found){
							item.x-=70;
						} else if(item.equals(this)){
							found = true;
						}
			}
			igs.items.remove(this);
			igs.inventory.items.remove(this);
		}
		if(state == 0){ // in 3d world
			float xDiff = Math.abs(playerPosition.x - this.position.x);
			float zDiff = Math.abs(playerPosition.z - this.position.z);
			if(xDiff < boundingSphereRadius && zDiff<boundingSphereRadius){
				boolean alreadyHasPoppers = false;
				if(id ==8){
					for(int i = 0;i<igs.inventory.items.size();i++){
						Item item = igs.inventory.items.get(i);
						if(item.id==8){
							item.amount =MAX_POPPERS;
							alreadyHasPoppers = true;
							break;
						}
					}
				}
				
				
				if(alreadyHasPoppers){
					igs.items.remove(this);
				} else {
					x = 200+igs.inventory.items.size()*70;
					 y = 10;
					 igs.inventory.items.add(this);
					 state = 1;
				}
				 
			}
			if(bounce){
					difference+=.1f;
				if(up){
					position.y+=.1f;
					if(difference>1f){
						difference = 0f;
						up = false;
					}
				} else{
					position.y-=.1f;
					if(difference>1f){
						difference = 0f;
						up = true;
					}
				}
			}
			if(turn)facingDirection+=4f;
		} else if(state == 1){ // in inventory
			if(id==8 && amount ==0){
				boolean found = false;
				for(int i=0;i<igs.inventory.items.size();i++){
					Item item = igs.inventory.items.get(i);
					if(found)item.x-=70;
					else if(item.equals(this)){
						found = true;
					}
				}
				igs.inventory.items.remove(this);
				igs.items.remove(this);
			}
			
			
			timeToUpdate -= elapsedTime;
			if(timeToUpdate <= 0)
			{
			
					mouseOver = false;
					if(Kernel.userInput.mousePos[0]>x &&Kernel.userInput.mousePos[0]<x+64&&Kernel.userInput.mousePos[1]>y&&Kernel.userInput.mousePos[1]<y+64){
						mouseOver = true;
						mouseOverTime++;
						if(alphaText<1f)alphaText+=0.1f;
						if(!swinging){
							degree = 0;
							left = true;
							swinging = true;	
						} 
							counter = 0;
							alphaSwing = .6f;
					} else{
						if(alphaText>0f)alphaText-=0.1f;
						mouseOverTime = 0;
					}
					
					if(swinging){
						if(left){
							degree+=velDegrees[counter];
							if(degree>mathDegrees[counter]){
								counter++;
								left = false;
							}
						}else{
							degree-=velDegrees[counter];
							if(degree<-mathDegrees[counter]){
								counter++;
								left = true;
							}
						}
						if(counter>mathDegrees.length-1){
							swinging = false;
							degree = 0;
						}
					}
				
				timeToUpdate = updateTime;
				}
		}	
	}

	public void display2d(GLGraphics g, Texture texture){
		if(state == 1){
			if(mouseOver){
						g.drawImageRotateHueSize(texture,x,y,degree, new float[]{1f,alphaSwing,alphaSwing,alpha}, new float[]{1.3f,1.3f});
						g.drawBitmapStringStroke(name,x,y+73,1,new float[]{1f,1f,.6f,alphaText},new float[]{0f,0f,0f,alphaText});
					} else {	
						alphaSwing +=.05f;
						g.drawImageRotateHue(texture,x,y,degree,new float[]{1f,alphaSwing,alphaSwing,alpha});
						g.drawBitmapStringStroke(name,x,y+64,1,new float[]{1f,1f,.6f,alphaText},new float[]{0f,0f,0f,alphaText});
			}
			g.drawBitmapStringStroke(""+amount,x,y,1,new float[]{.6f,1f,.6f,1f},new float[]{0f,0f,0f,1f});
				
		}
	}
	
	public void display3d(GL gl){
		if(state == 0){
			super.render(gl);
		}
	}

}