package org.cart.igd.util;

/* The specified movie is loaded into a JMF player, and 
   played continuously in a loop until stopMovie() is called.

   The player is not displayed, instead the user accesses
   the current frame in the movie by called getFrame(). It
   returns the image as a BufferedImage object of type
   BufferedImage.TYPE_3BYTE_BGR, and dimensions 
   FORMAT_SIZE x FORMAT_SIZE. The image has the current time
   in hours:minutes.seconds.milliseconds written on top of it.

   The original dimensions of the image in the movie can be 
   retrieved by calling getImageWidth() and getImageHeight().

   ----
   For best performance, the movie should be in MPEG-1 format
   with _no_ audio track. 

   If the movie does have an audio track, then the JMF player
   used here will be slow to start, and frame grabbing (using
   JMF's FrameGrabbingControl class) will be erratic -- 
   e.g. there may be several seconds when the frame does not 
   change.

   ----
   This code does not allow the user to retrieve a specific
   frame. I did want to do this (e.g. see the Quicktime version
   of this application), but I couldn't get JMF's 
   FramePositioningControl class to work in a reliable manner.
*/

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.net.URL;

import javax.media.*;
import javax.media.control.*;
import javax.media.format.*;
import javax.media.util.*;
import javax.imageio.*;

import org.cart.igd.core.Kernel;

public class JMFSnapper implements ControllerListener
{
	private static final int FORMAT_SIZE = 128;
	// size of BufferedImage; should be a power of 2, less than 512,
	// or older graphic cards may get upset when using it as a texture

	// used while waiting for the BufferToImage object to be initialized
	private static final int MAX_TRIES = 5;
	private static final int TRY_PERIOD = 2000;   // ms

	private Player p;
	private FrameGrabbingControl fg;
	private BufferToImage bufferToImage = null;

	private BufferedImage formatImg;    // for the frame image
	private int width, height;          // frame dimensions

	// used for waiting until the player has started
	private Object waitSync = new Object();
	private boolean stateTransitionOK = true;

	public JMFSnapper(String fnm)
	{
		Manager.setHint(Manager.PLUGIN_PLAYER, new Boolean(true));
		// utilise the native modular player so frame grabbing is available

		if(!(new File(fnm)).exists())
		{
			System.out.println("Cannot locate movie: " + fnm);
			System.exit(0);
		}
		
		// create a realized player
		try
		{
			URL url = new URL("file:" + fnm);
			p = Manager.createRealizedPlayer(url);
			System.out.println("Created player for: " + fnm);
		}
		catch(Exception e)
		{
			System.out.println("Failed to create player for " + fnm);
			System.exit(0);
		}
		
		p.addControllerListener(this);
		
		// create the frame grabber
		fg = (FrameGrabbingControl) p.getControl("javax.media.control.FrameGrabbingControl");
		if(fg==null)
		{
			System.out.println("Frame grabber could not be created");
			System.exit(0);
		}
		
		// specify the format of the returned BufferedImage object
		formatImg = new BufferedImage(FORMAT_SIZE, FORMAT_SIZE, BufferedImage.TYPE_3BYTE_BGR);
		
		// use a BufferedImage format that can support dynamic texturing in
		// OpenGL v.1.2 (and above) and D3D  
		
		// check if the player has a visual component
		if(p.getVisualComponent() == null)
		{
			System.out.println("No visual component found");
			System.exit(0);
		}
		
		// wait until the player has started
		System.out.println("Starting the player...");
		p.start();
		if(!waitForStart())
		{
			System.err.println("Failed to start the player.");
			System.exit(0);
		}
		
		waitForBufferToImage();
	}

	private boolean waitForStart()
	{
		synchronized(waitSync)
		{
			try
			{
				while(p.getState()!=Controller.Started && stateTransitionOK)
					waitSync.wait();
			}
			catch(Exception e) {}
		}
		return stateTransitionOK;
	}

	/* Wait for the BufferToImage object to be initialized.
     Movies with an audio track may take several seconds to
     initialize this object, so this method makes
     up to MAX_TRIES attempts.
	*/	
	private void waitForBufferToImage()
	{
		int tryCount = MAX_TRIES;
		System.out.println("Initializing BufferToImage...");
		while(tryCount > 0)
		{
			if(hasBufferToImage())   // initialization succeeded
				break;
			
			// initialization failed so wait a while and try again
			try
			{
				System.out.println("Waiting...");
				Thread.sleep(TRY_PERIOD);
			}
			catch(InterruptedException e)
			{
				System.out.println(e);
			}
			tryCount--;
		}
		
		if(tryCount==0)
		{
			System.out.println("Giving Up");
			System.exit(0);
		}
	}

	/*  The BufferToImage object is initialized here, so that when 
      getFrame() is called later, the snap can be quickly changed to 
      an image.

      The object is initialized by taking a snap, which
      may be an actual picture or be 'empty'.

      An 'empty' snap is a Buffer object with no video information,
      as detected by examining its component VideoFormat data. 

      An 'empty' snap is caused by the delay in the player, which 
      although in its started state still takes several seconds to 
      start playing the movie. This delay occurs when the movie has a 
      video and audio track.

      There's no delay if the movie only has a video track.
	*/	
	private boolean hasBufferToImage()
	{
		Buffer buf = fg.grabFrame();     // take a snap
		if(buf == null)
		{
			System.out.println("No grabbed frame");
			return false;
		}
    
		// there is a buffer, but check if it's empty or not
		VideoFormat vf = (VideoFormat) buf.getFormat();
		if(vf == null)
		{
			System.out.println("No video format");
			return false;
		}

		System.out.println("Video format: " + vf);
		width = vf.getSize().width;     // extract the image's dimensions
		height = vf.getSize().height;

		// initialize bufferToImage with the video format info.
		bufferToImage = new BufferToImage(vf);
		return true;
	}
	
	public synchronized void stopMovie()
	{
		p.close();
	}

	/* Grab a frame from the movie.
     The frame must be converted from Buffer object to Image,
     and finally to BufferedImage. The current time is written
     on top of the image when it's converted to a BufferedImage.
	*/	
	public synchronized Texture getFrame()
	{
		// grab the current frame as a buffer object
		Buffer buf = fg.grabFrame();
		if(buf == null)
		{
			System.out.println("No grabbed buffer");
			return null;
		}
    
		// convert buffer to image
		Image im = bufferToImage.createImage(buf);
		if(im == null)
		{
			System.out.println("No grabbed image");
			return null;
		}

		// convert the image to a BufferedImage
		Graphics g = formatImg.getGraphics();
		g.drawImage(im, 0, 0, FORMAT_SIZE, FORMAT_SIZE, null);
		g.dispose();
		
		return Kernel.display.renderer.loadImageFromBuffer(formatImg);
		
		//return formatImg;
	}
	
	private String timeNow()
	{ 
		SimpleDateFormat sdf = new SimpleDateFormat ("HH:mm.ss.SSS");
		Calendar now = Calendar.getInstance();
		return ( sdf.format(now.getTime()) );
	}

	public void controllerUpdate(ControllerEvent evt)
	{
		if(evt instanceof StartEvent)
		{
			synchronized(waitSync)
			{
				stateTransitionOK = true;
				waitSync.notifyAll();
			}
		}
		else if(evt instanceof ResourceUnavailableEvent)
		{
			synchronized(waitSync)
			{
				stateTransitionOK = false;
				waitSync.notifyAll();
			}
		}
		else if(evt instanceof EndOfMediaEvent)
		{
			p.setMediaTime(new Time(0));
			p.start();
		} 
	}
	
	public int getImageWidth()
	{
		return width;
	}

	public int getImageHeight()
	{
		return height;
	}
}