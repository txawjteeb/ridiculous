/**
 * @(#)GUITextList.java
 *
 *
 * @author 
 * @version 1.00 2007/4/6
 */
package org.cart.igd;

import java.util.LinkedList;

public class GUITextList {
	int x;
	int y;
	int hSpace;
	int size;
	long textFadeTime =6000;
	long timeBeforeFade = 6000;
	
	private LinkedList<String> text = new LinkedList<String>();
	
	
    public GUITextList(int x,int y, int h,int size) {
    	this.x=x;
    	this.y=y;
    	this.hSpace=h;
    	this.size=size;
    }
    
    public void addText(String txt){
    	text.addFirst(txt);
    	timeBeforeFade = textFadeTime*2;
    }
    public synchronized void cleanUp(){
    	while(text.size()>size){
    		text.removeLast();
    	}
    }
    public synchronized void removeLast(){
    	if(text.size() >= 1){
    		text.removeLast();
    	}
    	
    }
    
    public void draw(GLGraphics g){
    	
    	int iText=0;
    	for(String t: text){
    		g.drawBitmapString(t,x,y-(iText*hSpace));
    		iText++;
    	}
    	cleanUp();
    	
    }
    public void update(long elapsedTime){
    	timeBeforeFade = timeBeforeFade - elapsedTime;
    	if(timeBeforeFade <= 0){
    		removeLast();
    		timeBeforeFade = textFadeTime;
    	}
    	System.out.println(timeBeforeFade);
    }
    
    
}