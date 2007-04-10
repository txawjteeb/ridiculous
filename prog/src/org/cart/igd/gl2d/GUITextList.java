/**
 * @(#)GUITextList.java
 *
 *
 * @author Vitaly Maximov
 * @version 1.01 2007/4/9
 *
 * Purpose: This object holds a list of string messages that are printed
 * to the screen and are removed after a period of time, or when list overflows
 */
package org.cart.igd.gl2d;

import java.util.LinkedList;


public class GUITextList
{
	int x;
	int y;
	int hSpace;
	int size;
	long textFadeTime =6000;
	long timeBeforeFade = textFadeTime;
	
	private LinkedList<String> text = new LinkedList<String>();
	
	
    public GUITextList(int x,int y, int h,int size) {
    	this.x=x;
    	this.y=y;
    	this.hSpace=h;
    	this.size=size;
    }
    
    public GUITextList(int x,int y, int h,int size,int time) {
    	this(x,y,h,size);
    	this.textFadeTime=time;
    }
    
    public void addText(String txt){
    	text.addFirst(txt);
    	timeBeforeFade = textFadeTime;
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
    		g.drawBitmapString(t,x,y+(iText*hSpace));
    		iText++;
    	}
    	cleanUp();	
    }
    
    public void update(long elapsedTime){
    	timeBeforeFade = timeBeforeFade - elapsedTime;
    	if(timeBeforeFade <= 0){
    		addText("");//add blank
    		timeBeforeFade = textFadeTime;
    		
    	}
    }
}