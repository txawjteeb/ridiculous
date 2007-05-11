package org.cart.igd.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * WinDataInputStream.java
 *
 * General Function: An extension of DataInputStream to gather Windows safe data.
 */
public class WinDataInputStream extends DataInputStream
{
	/**
	 * Constructor
	 *
	 * General Function: Creates an instance of WinDataInputStream.
	 *
	 * @param in The InputStream to read from.
	 */
	public WinDataInputStream(InputStream in)
	{
		super(in);
	}
	
	/**
	 * readNullTerminatedString
	 *
	 * General Function: Reads a null terminated String from the InputStream.
	 */
	public String readNullTerminatedString() throws IOException
	{
		StringBuffer stringBuffer = new StringBuffer();
		int input;
		
		do
		{
			input = in.read();
			if(input!=0) stringBuffer.append((char)input);
		}
		while(input!=0);
		
		return stringBuffer.toString().trim();
	}
	
	/**
	 * readString
	 *
	 * General Function: Returns a String of char values from InputStream.
	 *
	 * @param numOfChars The number of char values to read into the String.
	 */
	public String readString(int numOfChars) throws IOException
	{
		StringBuffer stringBuffer = new StringBuffer();
		int input;

		for(int i=0; i<numOfChars; i++)
		{
			input = in.read();
			if(input!=0) stringBuffer.append((char)input);
		}
		
		return stringBuffer.toString().trim();
	}
	
	/**
	 * readWinFloat
	 *
	 * General Function: Returns a Windows valid float from InputStream.
	 */
	public float readWinFloat() throws IOException
	{
		byte[] buffer = new byte[4];
		byte tempByte;

		buffer[0] = (byte)this.readUnsignedByte();
		buffer[1] = (byte)this.readUnsignedByte();
		buffer[2] = (byte)this.readUnsignedByte();
		buffer[3] = (byte)this.readUnsignedByte();

		tempByte = buffer[0];
		buffer[0] = buffer[3];
		buffer[3] = tempByte;

		tempByte = buffer[1];
		buffer[1] = buffer[2];
		buffer[2] = tempByte;

		return (new DataInputStream(new ByteArrayInputStream(buffer)).readFloat());
	}
	
	/**
	 * readWinInt
	 *
	 * General Function: Returns a Windows valid integer from InputStream.
	 */
	public int readWinInt() throws IOException
	{
		byte[] buffer = new byte[4];
		in.read(buffer, 0, 4);
		return (((buffer[3] & 0xff) << 24) | ((buffer[2] & 0xff) << 16) | ((buffer[1] & 0xff) << 8) | (buffer[0] & 0xff));
	}
	
	/**
	 * readWinUnsignedShort
	 *
	 * General Function: Returns a Windows valid unsigned short from InputStream.
	 */
	public int readWinUnsignedShort() throws IOException
	{
		byte[] buffer = new byte[2];
		in.read(buffer, 0, 2);
		return (((buffer[1] & 0xff) << 8) | (buffer[0] & 0xff));
	}
}
