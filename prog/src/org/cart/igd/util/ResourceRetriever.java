package org.cart.igd.util;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

/**
 * ResourceRetriever.java
 *
 * General Function: Supplies functions for retrieving files inside or outside of the application's JAR.
 */
public class ResourceRetriever
{
	/**
	 * getResource
	 *
	 * General Function: Returns a URL of a file inside/outside of the JAR.
	 *
	 * @param filename The file name and path to the resource.
	 */
	public static URL getResource(final String filename) throws IOException
	{
        /* Try to load resource from jar */
        URL url = ResourceRetriever.class.getClassLoader().getResource(filename);
        
        /* If not found in jar, then load from disk */
        if(url == null)
        {
            return new URL("file", "localhost", filename);
        }
        else
        {
            return url;
        }
    }

	/**
	 * getResourceAsStream
	 *
	 * General Function: Returns an InputStream of a resource.
	 *
	 * @param filename The file anme and path of the resource.
	 */
    public static InputStream getResourceAsStream(final String filename) throws IOException
    {
        /* Try to load resource from jar */
        String convertedFileName = filename.replace('\\', '/');
        InputStream stream = ResourceRetriever.class.getClassLoader().getResourceAsStream(convertedFileName);
        
        /* If not found in jar, then load from disk */
        if(stream==null)
        {
            return new FileInputStream(convertedFileName);
        }
        else
        {
            return stream;
        }
    }
}
