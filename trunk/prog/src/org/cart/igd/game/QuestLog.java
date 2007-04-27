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
import java.util.*;

public class QuestLog{
	
	
	 	int mouseOverTime = 0;
		boolean mouseOver = false;
		int degree = 0;
		int mathDegrees[] = new int[]{20,10,8,5,2,1};
		int velDegrees[] = new int[]{12,10,8,6,4,2};
		int counter = 0;
		float alphaSwing = 1f;
		boolean left = false;
		boolean swinging;
		float alpha = 1f;
		float alphaText = 0f;

	public String name;
	public Texture texture;
	public int x;
	public int y;

	long timeToUpdate = 0;
	long updateTime = 45;



	ArrayList <Quest> quests = new ArrayList<Quest>();
	public boolean open = false;



	public QuestLog(String name,int x, int y){
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public void update(InGameState igs, long elapsedTime){
			timeToUpdate -= elapsedTime;
			if(timeToUpdate <= 0)
			{
				if(!open){
					mouseOver = false;
					if(Kernel.userInput.mousePos[0]>x &&Kernel.userInput.mousePos[0]<x+128&&Kernel.userInput.mousePos[1]>y&&Kernel.userInput.mousePos[1]<y+128){
							
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
						mouseOverTime = 0;
						if(alphaText>0f)alphaText-=0.1f;
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
					
					if(Kernel.userInput.mousePress[0]>x &&Kernel.userInput.mousePress[0]<x+128&&Kernel.userInput.mousePress[1]>y&&Kernel.userInput.mousePress[1]<y+128){
						Kernel.userInput.mousePress[0] = -100;
						Kernel.userInput.mousePress[1] = -100;
						open = true;
					} 
				} else{
					
				}
					
				
			timeToUpdate = updateTime;
		}

			
	}

	public void display(GLGraphics g, Texture texture1, Texture texture2){
		if(!open){
				if(mouseOver){
						g.drawImageRotateHueSize(texture1,x-13,y-13,degree, new float[]{1f,1f,alphaSwing,alpha}, new float[]{1.2f,1.2f});
				} else {	
						alphaSwing +=.05f;
						g.drawImageRotateHue(texture1,x,y,degree,new float[]{1f,1f,alphaSwing,alpha});
				}
				g.drawBitmapStringStroke(name,x,y+64,1,new float[]{1f,1f,.6f,alphaText},new float[]{0f,0f,0f,alphaText});
		} else{
				for(int i = 0;i<quests.size();i++){
					Quest quest = quests.get(i);
				//	g.drawBitmapStringStroke(quest.title,x,y+64,1,new float[]{1f,1f,.6f,alphaText},new float[]{0f,0f,0f,alphaText});
				//	for(int j = 0;j<quest.brokenInformation.length;j++){
				//			g.drawBitmapStringStroke(quest.brokenInformation[j],x+76,y+50-i*20,1,new float[]{1f,.6f,.6f,alpha},new float[]{0f,0f,0f,alpha});
					//	}
				}
				g.drawImageRotateHue(texture2,0,0,0,new float[]{1f,1f,1f,1f});
		}
		
	
	}
	
	class Quest{
		boolean done;
		String title;
		String information;
		String brokenInformation[];
		
		public Quest(String title, String information, boolean done){
			this.done = done;
			this.title = title;
			this.information = information;
			
			if(information.length()<40){
				brokenInformation = new String[] {information};
			} else {
				String wordsArray[] = new String[10];
				char c;
				int amountOfLines = 1;
				int lastSpace = 0;
				int lastAmountCopied = 0;
				for(int i = 0;i<information.length();i++){
					c = information.charAt(i);
					if(c ==' ')lastSpace = i;
					if(i<amountOfLines*40){
						
					}  else {
						wordsArray[amountOfLines-1] = information.substring(lastAmountCopied, lastSpace);
						lastAmountCopied = lastSpace+1;
						amountOfLines++;
					}
					if (i==information.length()-1){
						wordsArray[amountOfLines-1] = information.substring(lastAmountCopied, information.length());
						amountOfLines++;
					}
				}
				brokenInformation = new String[amountOfLines-1];
				for(int i = 0;i<amountOfLines-1;i++){
					brokenInformation[i] = wordsArray[i];
				}
			}
			
		}
	}
}
