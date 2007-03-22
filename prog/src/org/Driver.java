package org.cart.igd;

public class Driver
{
	public static Display display;
	
	public static void main(String[] args)
	{
		display = Display.createDisplay("Project Ridiculous");
		display.start();
	}
}
