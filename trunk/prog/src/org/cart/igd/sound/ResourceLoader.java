package org.cart.igd.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {


	public static InputStream getResourceAsStream(String ref) {
		InputStream in = ResourceLoader.class.getClassLoader().getResourceAsStream(ref);
		
		if (in == null) {
			File file = new File(ref);
			try {
				if (System.getProperty("jnlp.slick.webstart", "false").equals("false")) {
					in = new FileInputStream(file);
					return new BufferedInputStream(in);
				} else {
					Log.error("Resource not found: "+ref);
					throw new RuntimeException("Resource not found: "+ref);
				}
			} catch (IOException e) {
				Log.error("Resource not found: "+ref);
				throw new RuntimeException("Resource not found: "+ref);
			} 
		}
		
		return new BufferedInputStream(in);
	}
}
