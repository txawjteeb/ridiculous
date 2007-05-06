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

	long timeToUpdate = 20;
	long updateTime = 0;



	ArrayList <Quest> quests = new ArrayList<Quest>();
	ArrayList <QuestPopUp> questPopUps = new ArrayList<QuestPopUp>();
	public boolean open = false;
	public boolean swingingBook = false;
		int degreeBook = 0;
		int mathDegreesBook[] = new int[]{10,8,6,4,2,1};
		int velDegreesBook[] = new int[]{6,5,4,3,2,1};
		int counterBook = 0;
		boolean leftBook = true;
		float swingBookAlpha = 0f;



	public QuestLog(String name,int x, int y){
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	public void load(){
		createNewQuest(Inventory.FLAMINGO,"Flamingo", "Escaped!","You have escaped the zoo!",true,140,520,9);
		createNewQuest(Inventory.TURTLES,"Turtles", "Find and Talk","I must tell the Turtles about the zoo!",false,300,530,-5);
		createNewQuest(Inventory.PANDA,"Panda","Find and Talk", "I must tell the Panda about the zoo!",false,100,400,12);
		createNewQuest(Inventory.KANGAROO,"Kangaroo","Find and Talk", "I must tell the Kangaroo about the zoo!",false,220,390,8);
		createNewQuest(Inventory.GIRAFFE,"Giraffe","Find and Talk", "I must tell the Giraffe about the zoo!",false,340,410,-4);
		createNewQuest(Inventory.TIGER,"Tiger","Find and Talk", "I must tell the Tiger about the zoo!",false,130,270,-10);
		createNewQuest(Inventory.PENGUIN,"Penguin","Find and Talk", "I must tell the Penguin about the zoo!",false,300,265,7);
		createNewQuest(Inventory.MEERKAT,"Meerkat","Find and Talk", "I must tell the Meerkat about the zoo!",false,120,120,-6);
		createNewQuest(Inventory.WOODPECKER,"WoodPecker","Find and Talk", "I must tell the WoodPecker about the zoo!",false,220,155,8);
		createNewQuest(Inventory.ELEPHANT,"Elephant", "Find and Talk","I must tell the Elephant about the zoo!",false,340,120,-7);
	}

	public void update(InGameState igs, long elapsedTime){
			timeToUpdate -= elapsedTime;
			if(timeToUpdate <= 0){
				//if(!open){
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
						if(open){
							//igs.backgroundMusic.loop(1f,.5f);
							//igs.questLogMusic.stop();
							igs.closeQuestLog.play();
							open=false;
							swingingBook = true;
							degreeBook = 0;
							counterBook=0;
							leftBook=true;
							swingBookAlpha=1f;
						}
						else {
							if(igs.inventory.currentItem!=-1) {
								for(int i = 0;i<igs.inventory.items.size();i++){
									Item item = igs.inventory.items.get(i);
									item.selected = false;
								}
								 igs.inventory.currentItem =-1;
							}
							//igs.backgroundMusic.stop();
							//igs.questLogMusic.loop(1f,.5f);
							igs.openQuestLog.play();
							swingBookAlpha = 0f;
							leftBook=true;
							degreeBook = 0;
							counterBook=0;
							swingingBook = true;
							open = true;
						}
					} 
			//	} else{
					
			//	}
					
				
			timeToUpdate = updateTime;
			for(int i = 0;i<quests.size();i++){
					quests.get(i).update(igs);
			}
		}
		
		if(swingingBook){
			if(leftBook){
					degreeBook+=velDegreesBook[counterBook];
							if(degreeBook>mathDegreesBook[counterBook]){
								counterBook++;
								leftBook = false;
							}
						}else{
							degreeBook-=velDegreesBook[counterBook];
							if(degreeBook<-mathDegreesBook[counterBook]){
								counterBook++;
								leftBook = true;
							}
						}
						if(counterBook>mathDegrees.length-1){
							swingingBook = false;
							degreeBook = 0;
							counterBook=0;
			}
			if(!open){
				swingBookAlpha-=.05f;
			} else{
				swingBookAlpha+=.05f;
			}
		}
			
		Iterator i = questPopUps.iterator();
		while(i.hasNext()){
			QuestPopUp qpu = (QuestPopUp)i.next();
			if(qpu.update()){
				i.remove();
			}
		}
	}


	
	public void display(GLGraphics g, Texture questLogIco, Texture questLog,Texture[] questLogAnimals){
		if(!open){
				if(mouseOver){
						g.drawImageRotateHueSize(questLogIco,x-13,y-13,degree, new float[]{1f,1f,alphaSwing,alpha}, new float[]{1.2f,1.2f});
				} else {	
						alphaSwing +=.05f;
						g.drawImageRotateHue(questLogIco,x,y,degree,new float[]{1f,1f,alphaSwing,alpha});
				}
				g.drawBitmapStringStroke(name,x,y+64,1,new float[]{1f,1f,.6f,alphaText},new float[]{0f,0f,0f,alphaText});
			if(swingingBook){
				g.drawImageRotateHue(questLog,0,0,degreeBook,new float[]{1f,1f,1f,swingBookAlpha});
			}
		} else{
			g.drawImageRotateHue(questLog,0,0,degreeBook,new float[]{1f,1f,1f,swingBookAlpha});
				for(int i = 0;i<quests.size();i++){
					quests.get(i).draw(g,questLogAnimals,degreeBook,swingBookAlpha);
				}
			if(mouseOver){
						g.drawImageRotateHueSize(questLogIco,x-13,y-13,degree, new float[]{1f,1f,alphaSwing,alpha}, new float[]{1.2f,1.2f});
				} else {	
						alphaSwing +=.05f;
						g.drawImageRotateHue(questLogIco,x,y,degree,new float[]{1f,1f,alphaSwing,alpha});
				}
				g.drawBitmapStringStroke(name,x,y+64,1,new float[]{1f,1f,.6f,alphaText},new float[]{0f,0f,0f,alphaText});
				
		}
		for(int i = 0;i<questPopUps.size();i++){
			questPopUps.get(i).draw(g);
		}
		
	
	}
	
	
	public void createNewQuest(int id,String title, String subtitle, String information, boolean done, int x, int y, int degree){
			quests.add(new Quest(id,title, subtitle,information,done,x,y,degree));
	}
	
	
	public void updateQuest(int id,String title, String subtitle, String information){
		
		//huge problem here, it works, but never understood the problem
		boolean duplicate = false;
		int i = 0;
		for(;i<quests.size();i++){
			if(quests.get(i).id==id){
				duplicate = true;
				break;
			}
		}
		
		if(duplicate){
			Quest quest = quests.get(i);
			quest.title = title;
			quest.subtitle = subtitle;
			quest.information = information;
			quest.done = false;
			quest.breakUpInformation();
			questPopUps.add(new QuestPopUp());
		}
		
	}

	public void finishQuest(int id,String title, String subtitle, String information){
		
		//huge problem here, it works, but never understood the problem
		boolean duplicate = false;
		int i = 0;
		for(;i<quests.size();i++){
			if(quests.get(i).id==id){
				duplicate = true;
				break;
			}
		}
		
		if(duplicate){
			Quest quest = quests.get(i);
			quest.title = title;
			quest.subtitle = subtitle;
			quest.information = information;
			quest.done = true;
			quest.breakUpInformation();
			questPopUps.add(new QuestPopUp());
		}
		
	}

	
	class Quest{
		int id;
		int x,y;
		boolean selected = false;
		boolean done = false;
		String title;
		String subtitle;
		String information;
		String brokenInformation[];
		boolean mouseOverTitle = false;
		int degree;
		
		public Quest(int id, String title, String subtitle, String information, boolean done,int x, int y, int degree){
			this.id = id;
			this.done = done;
			this.title = title;
			this.subtitle = subtitle;
			this.information = information;
			breakUpInformation();
			this.x = x;
			this.y = y;
			this.degree = degree;
		}
		
		
		
		public void breakUpInformation(){
			if(information.length()<25){
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
					if(i<amountOfLines*25){
						
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

		
		public void draw(GLGraphics g, Texture[] questLogAnimals,int degreeBook, float swingBookAlpha){
			if(mouseOverTitle){
				g.drawImageRotateHueSize(questLogAnimals[id],x-20,y-20,degree+degreeBook, new float[]{1f,1f,1f,swingBookAlpha}, new float[]{1.0f,1.0f});
				if(!done){
					g.drawImageRotateHueSize(questLogAnimals[11],x-20,y-20,degree+degreeBook, new float[]{1f,1f,1f,swingBookAlpha}, new float[]{1.0f,1.0f});
				}
			} else {
				g.drawImageRotateHueSize(questLogAnimals[id],x,y,degree+degreeBook, new float[]{1f,1f,1f,swingBookAlpha}, new float[]{.7f,.7f});
				if(!done){
					g.drawImageRotateHueSize(questLogAnimals[11],x,y,degree+degreeBook, new float[]{1f,1f,1f,swingBookAlpha}, new float[]{.7f,.7f});
				}
			}
			if(selected){
				g.drawBitmapStringStrokeSize(subtitle,600,600,1,new float[]{1f,1f,.6f,alpha},new float[]{0f,0f,0f,alpha}, new float[]{1.4f,1.4f},14);
					for(int j = 0;j<brokenInformation.length;j++){
						int adder = 0;
						if(j==0)adder = 30;
							g.drawBitmapStringStroke(brokenInformation[j],500+adder,580-j*20,1,new float[]{1f,1f,.6f,alpha},new float[]{0f,0f,0f,alpha});
					}
					if(done){
						g.drawBitmapStringStrokeSize("Finished",600,110,1,new float[]{.3f,1f,.3f,alpha},new float[]{0f,0f,0f,alpha}, new float[]{2.0f,2.0f},20);
					} else {
						g.drawBitmapStringStrokeSize("Unfinished",600,110,1,new float[]{1f,.3f,.3f,alpha},new float[]{0f,0f,0f,alpha}, new float[]{2.0f,2.0f},20);
					}
			}
			

		}
		
		
		
		public void update(InGameState igs){
			if(igs.questlog.open){
				if(mouseOverTitle){
					if(Kernel.userInput.mousePos[0]>x &&Kernel.userInput.mousePos[0]<x+128&&Kernel.userInput.mousePos[1]>y&&Kernel.userInput.mousePos[1]<y+128){
						mouseOverTitle = true;
					} else mouseOverTitle = false;
					if(Kernel.userInput.mousePress[0]>x &&Kernel.userInput.mousePress[0]<x+128&&Kernel.userInput.mousePress[1]>y&&Kernel.userInput.mousePress[1]<y+128&&!selected){
						for(int i = 0;i<igs.questlog.quests.size();i++){
							igs.questlog.quests.get(i).selected = false;
						}
						Kernel.userInput.mousePress[0] = -100;
						Kernel.userInput.mousePress[1] = -100;
						selected = true;
						igs.turnPage[(new Random()).nextInt(4)].play();
					}
				} else {
					if(Kernel.userInput.mousePos[0]>x &&Kernel.userInput.mousePos[0]<x+100&&Kernel.userInput.mousePos[1]>y&&Kernel.userInput.mousePos[1]<y+100){
					mouseOverTitle = true;
					} else mouseOverTitle = false;
					if(Kernel.userInput.mousePress[0]>x &&Kernel.userInput.mousePress[0]<x+100&&Kernel.userInput.mousePress[1]>y&&Kernel.userInput.mousePress[1]<y+100){
						selected = true;
					}
				}	
			}
			
		}
	}
	
	
	private class QuestPopUp{
		int x; int y;
		boolean up;
		float alpha;
		public QuestPopUp(){
			x = 10;
			y = 50;
			up = true;
			alpha = 0f;
		}
		
		public void draw(GLGraphics g){
			g.drawBitmapStringStrokeSize("Quest Log Updated",x,y,1,new float[]{1f,.3f,.3f,alpha},new float[]{0f,0f,0f,alpha}, new float[]{1.0f,1.0f},20);
		}
		
		public boolean update(){
			if(up){
				y++;
				alpha +=.03f;
				if(y> 100){
					up = false;
				}
			} else{
				y--;
				alpha -=.03f;
			}
			if(y<0){
				return true;
			}
			return false;
		}
	}
}
