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

import org.cart.igd.core.Kernel;
import org.cart.igd.input.UserInput;

/**
 * Display.java
 *
 * General Function:
 * Creates the window for OpenGL to draw onto.
 */
public class Display implements WindowListener
{
	/* Filler variable for DisplayModes */
	private static final int DONT_CARE = -1;
	
	/* Instance of Renderer class. */
	public Renderer renderer;
	
	/* JFrame to render to. */
	public static JFrame frame;
	
	/* GLCanvas attaches to the render frame and is manipulated by GL. */
	private GLCanvas glCanvas;
	
	/* FPSAnimator runs a managed loop for the engine. */
	private FPSAnimator animator;
	
	/* Flag for fullscreen. */
	private boolean fullscreen;
	
	/* Width of the Display window. */
	private int width
	
	/* Height of the Display window. */
	private int height;
	
	/* GraphicsDevice that is being used. */
	private GraphicsDevice usedDevice;
	
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of Display
	 */
	public Display( DisplaySettings disp )
	{	
		boolean tmpFs = disp.fullscreen;
		int tmpW = disp.w;
		int tmpH = disp.h;
		createDisplay( disp.title, tmpW,tmpH, tmpFs );
	}
	
	/**
	 * createDisplay
	 *
	 * General Function: Creates a public instance of Display with pre-specified settings.
	 */
	public void createDisplay( String title, int width, int height, boolean fullscreen )
	{
		/* Setup GLCapabilities for GLCanvas. */
		GLCapabilities glc = new GLCapabilities();
		glc.setDoubleBuffered(true);
		glc.setSampleBuffers(true);
		glc.setNumSamples(4);
		
		/* Initialize GLCanvas to be attached to Display frame. */
		glCanvas = new GLCanvas( glc );
		glCanvas.setSize( width, height );
		glCanvas.setBackground( Color.BLACK );
		glCanvas.setIgnoreRepaint( true );
		glCanvas.addGLEventListener( ( renderer = new Renderer() ) );
		
		/* Initialize the Display frame. */
		frame = new JFrame( title );
		frame.getContentPane().setLayout( new BorderLayout() );
		frame.getContentPane().add( glCanvas, BorderLayout.CENTER );
		frame.setResizable(false);
		frame.setLocation(0, 0);
		
		/* Set an invisible cursor of the window. */
		java.awt.image.BufferedImage tmp = frame.getGraphicsConfiguration().createCompatibleImage(1, 1, java.awt.Transparency.BITMASK);
		java.awt.Graphics2D g2 = tmp.createGraphics();
		g2.setBackground(new Color(0,0,0,0));
		g2.clearRect(0,0,1,1);
		java.awt.Cursor invisibleCursor = frame.getToolkit().createCustomCursor(tmp, new java.awt.Point(0,0), "Invisible");
		glCanvas.setCursor(invisibleCursor);
		
		this.fullscreen = fullscreen;
		this.width = width;
		this.height = height;
		
		/* Make the engine run at max speed. 100fps on Pentiums or 64fps on AMD64. */
		animator = new FPSAnimator( glCanvas, 100 );
		animator.setRunAsFastAsPossible( true );
		Kernel.displayRunning = true;
	}
	
	/**
	 * getRenderer
	 *
	 * General Function: Return an instance of Renderer.
	 */
	public Renderer getRenderer()
	{
		return renderer;
	}
	
	/**
	 * getGLCanvas
	 *
	 * General Function: Return an instance of GLCanvas.
	 */
	public Component getGLCanvas()
	{
		return glCanvas;
	}
	
	/**
	 * start
	 *
	 * General Function: Starts the engine through Display.
	 */
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
	
	/**
	 * stop
	 *
	 * General Function: Stops the engine and kills Display.
	 */
	public void stop()
	{
		Kernel.displayRunning = false;
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
	
	/**
	 * findDisplayMode
	 *
	 * General Function: Finds a DisplayMode to use.
	 */
	private DisplayMode findDisplayMode( DisplayMode[] displayModes, int requestedWidth, int requestedHeight, int requestedDepth, int requestedRefreshRate )
	{
		DisplayMode displayMode = findDisplayModeInternal( displayModes, requestedWidth, requestedHeight, requestedDepth, requestedRefreshRate );

		if(displayMode==null)
		{
			displayMode = findDisplayModeInternal( displayModes, requestedWidth, requestedHeight, DONT_CARE, DONT_CARE );
		}
		if(displayMode==null)
		{
			displayMode = findDisplayModeInternal( displayModes, requestedWidth, DONT_CARE, DONT_CARE, DONT_CARE );
		}
		if(displayMode==null)
		{
			displayMode = findDisplayModeInternal( displayModes, DONT_CARE, DONT_CARE, DONT_CARE, DONT_CARE );
		}
		
		return displayMode;
	}
	
	/**
	 * findDisplayModeInternal
	 *
	 * General Function: Finds a DisplayMode, using the java object DisplayMode.
	 */
	private DisplayMode findDisplayModeInternal( DisplayMode[] displayModes, int requestedWidth, int requestedHeight, int requestedDepth, int requestedRefreshRate ) 
	{
		DisplayMode displayModeToUse = null;
		for(int i=0; i<displayModes.length; i++)
		{
			DisplayMode displayMode = displayModes[i];
			if(	(requestedWidth==DONT_CARE || displayMode.getWidth()==requestedWidth) &&
				(requestedHeight==DONT_CARE || displayMode.getHeight()==requestedHeight ) &&
				(requestedHeight==DONT_CARE || displayMode.getRefreshRate()==requestedRefreshRate) &&
				(requestedDepth==DONT_CARE || displayMode.getBitDepth()==requestedDepth))
			{
				displayModeToUse = displayMode;
			}
		}

		return displayModeToUse;
	}

	/**
	 * getTitle
	 *
	 * General Function: Returns the title of the Display frame.
	 */
	public String getTitle()
	{
		return frame.getTitle();
	}
	
	/**
	 * setTitle
	 *
	 * General Function: Assigns a new String variable as the Display frame's title.
	 */
	public void setTitle( String title )
	{
		frame.setTitle( title );
	}
	
	/**
	 * getScreenWidth
	 *
	 * General Function: Returns the GLCanvas width.
	 */
	public int getScreenWidth()
	{
		return glCanvas.getWidth();
	}
	
	/**
	 * getScreenHeight
	 *
	 * General Function: Returns the GLCanvas height.
	 */
	public int getScreenHeight()
	{
		return glCanvas.getHeight();
	}
	
	/**
	 * windowActivated
	 *
	 * General Function: Required method from WindowListener.
	 */
	public void windowActivated(WindowEvent e)
	{
	}
	
	/**
	 * windowDeactivated
	 *
	 * General Function: Required method from WindowListener.
	 */
	public void windowDeactivated(WindowEvent e)
	{
	}
	
	/**
	 * windowDeiconified
	 *
	 * General Function: Required method from WindowListener.
	 */
	public void windowDeiconified(WindowEvent e)
	{
	}
	
	/**
	 * windowIconified
	 *
	 * General Function: Required method from WindowListener.
	 */
	public void windowIconified(WindowEvent e)
	{
	}
	
	/**
	 * windowClosing
	 *
	 * General Function: Required method from WindowListener. Also stops the engine.
	 */
	public void windowClosing(WindowEvent e)
	{
		stop();
	}
	
	/**
	 * windowClosed
	 *
	 * General Function: Required method from WindowListener.
	 */
	public void windowClosed(WindowEvent e)
	{
	}
	
	/**
	 * windowOpened
	 *
	 * General Function: Required method from WindowListener.
	 */
	public void windowOpened(WindowEvent e)
	{
	}

}
