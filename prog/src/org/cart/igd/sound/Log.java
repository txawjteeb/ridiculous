package org.cart.igd.sound;

import java.io.PrintStream;
import java.util.Date;


public final class Log {

	public static PrintStream out = System.out;
	private static boolean verbose = true;
	

	private Log() {
		
	}

	public static void setVerbose(boolean v) {
		verbose = v;
	}
	

	public static void error(String message, Throwable e) {
		error(message);
		error(e);
	}


	public static void error(Throwable e) {
		out.println(new Date()+" ERROR:" +e.getMessage());
		e.printStackTrace(out);
	}


	public static void error(String message) {
		out.println(new Date()+" ERROR:" +message);
	}


	public static void warn(String message) {
		out.println(new Date()+" WARN:" +message);
	}

	public static void info(String message) {
		if (verbose) {
			out.println(new Date()+" INFO:" +message);
		}
	}

	public static void debug(String message) {
		if (verbose) {
			out.println(new Date()+" DEBUG:" +message);
		}
	}
}
