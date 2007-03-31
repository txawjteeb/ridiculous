package org.cart.igd;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.sun.opengl.util.FPSAnimator;
import org.cart.igd.input.UserInput;

public class Display implements WindowListener
{
	//private static final int DEFAULT_WIDTH = 640;
	//private static final int DEFAULT_HEIGHT = 480;
	
	private static final int DONT_CARE = -1;
	
	public static Renderer renderer;
	
	public static JFrame frame;
	private GLCanvas glCanvas;
	private FPSAnimator animator;
	private boolean fullscreen;
	private static int width, height;
	private GraphicsDevice usedDevice;
	
	public static Display createDisplay( String title )
	{
		boolean fullscreen = false;
		
		Object[] p = new Object[] { "640 by 480", "800 by 600", "1024 by 768", "1280 by 960", "1280 by 1024" };
		String s = (String)JOptionPane.showInputDialog(null, "Preferred Resolution:", "Myriad", JOptionPane.QUESTION_MESSAGE, null, p, p[1] );
		int w = Integer.parseInt(s.substring(0, s.indexOf(" ")));
		int h = Integer.parseInt(s.substring(s.indexOf(" ")+4));
		
		return new Display( title, w, h, fullscreen );
	}
	
	private Display( String title, int width, int height, boolean fullscreen )
	{
		GLCapabilities glc = new GLCapabilities();
		glc.setDoubleBuffered(true);
		glc.setSampleBuffers(true);
		glc.setNumSamples(4);
		
		glCanvas = new GLCanvas( glc );
		glCanvas.setSize( width, height );
		glCanvas.setBackground( Color.BLACK );
		glCanvas.setIgnoreRepaint( true );
		glCanvas.addGLEventListener( ( renderer = new Renderer() ) );
		
		frame = new JFrame( title );
		frame.getContentPane().setLayout( new BorderLayout() );
		frame.getContentPane().add( glCanvas, BorderLayout.CENTER );
		frame.setResizable(false);
		frame.setLocation(0, 0);

//		InvisiCursor
		/*
		java.awt.image.BufferedImage tmp = frame.getGraphicsConfiguration().createCompatibleImage(1, 1, java.awt.Transparency.BITMASK);
		java.awt.Graphics2D g2 = tmp.createGraphics();
		g2.setBackground(new Color(0,0,0,0));
		g2.clearRect(0,0,1,1);
		java.awt.Cursor invisibleCursor = frame.getToolkit().createCustomCursor(tmp, new java.awt.Point(0,0), "Invisible");
		glCanvas.setCursor(invisibleCursor);
		*/
		
		this.fullscreen = fullscreen;
		Display.width = width;
		Display.height = height;
		
		animator = new FPSAnimator( glCanvas, 60 );
		animator.setRunAsFastAsPossible( false );
	}
	
	public Component getGLCanvas()
	{
		return glCanvas;
	}
	
	public void start()
	{
		try
		{
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setUndecorated( fullscreen );
			frame.addWindowListener( this );
			
			if(fullscreen)
			{
				usedDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
				usedDevice.setFullScreenWindow( frame );
				usedDevice.setDisplayMode
					(
						findDisplayMode
							(
								usedDevice.getDisplayModes(),
								width,
								height,
								usedDevice.getDisplayMode().getBitDepth(),
								usedDevice.getDisplayMode().getRefreshRate()
							)
					);
			}
			else
			{
				frame.setSize( frame.getContentPane().getPreferredSize() );
				frame.setLocation
					(
						( screenSize.width - frame.getWidth() ) / 2,
						( screenSize.height - frame.getHeight() ) / 2
					);
				frame.setVisible( true );
			}
			
			glCanvas.requestFocus();
			
			animator.start();
			
			frame.pack();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		try
		{
			animator.stop();
			if( fullscreen )
			{
				usedDevice.setFullScreenWindow( null );
				usedDevice = null;
			}
			frame.dispose();
		}
		catch(Exception e) { e.printStackTrace(); }
		finally { System.exit( 0 ); }
	}
	
	private DisplayMode findDisplayMode( DisplayMode[] displayModes, int requestedWidth, int requestedHeight, int requestedDepth, int requestedRefreshRate )
	{
		DisplayMode displayMode = findDisplayModeInternal( displayModes, requestedWidth, requestedHeight, requestedDepth, requestedRefreshRate );

		if(displayMode == null)
			displayMode = findDisplayModeInternal( displayModes, requestedWidth, requestedHeight, DONT_CARE, DONT_CARE );
			
		if(displayMode == null)
			displayMode = findDisplayModeInternal( displayModes, requestedWidth, DONT_CARE, DONT_CARE, DONT_CARE );
		
		if(displayMode == null)
			displayMode = findDisplayModeInternal( displayModes, DONT_CARE, DONT_CARE, DONT_CARE, DONT_CARE );
		
		return displayMode;
	}
	
	private DisplayMode findDisplayModeInternal( DisplayMode[] displayModes, int requestedWidth, int requestedHeight, int requestedDepth, int requestedRefreshRate ) {
		DisplayMode displayModeToUse = null;
		for(int i=0; i<displayModes.length; i++)
		{
			DisplayMode displayMode = displayModes[i];
			if(	(requestedWidth == DONT_CARE || displayMode.getWidth() == requestedWidth) &&
				(requestedHeight == DONT_CARE || displayMode.getHeight() == requestedHeight ) &&
				(requestedHeight == DONT_CARE || displayMode.getRefreshRate() == requestedRefreshRate) &&
				(requestedDepth == DONT_CARE || displayMode.getBitDepth() == requestedDepth))
					displayModeToUse = displayMode;
		}
		
		return displayModeToUse;
	}

	public String getTitle() { return frame.getTitle(); }
	
	public void setTitle( String title ) { frame.setTitle( title ); }
	
	public static int getScreenWidth() { return width; }
	
	public static int getScreenHeight() { return height; }
	
	public void windowActivated(WindowEvent e) {  }
	public void windowDeactivated(WindowEvent e) {  }
	public void windowDeiconified(WindowEvent e) {  }
	public void windowIconified(WindowEvent e) {  }
	public void windowClosing(WindowEvent e) { stop(); }
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

}
