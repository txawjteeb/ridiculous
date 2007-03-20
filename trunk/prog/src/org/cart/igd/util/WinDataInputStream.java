package org.cart.igd.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WinDataInputStream extends DataInputStream
{
	public WinDataInputStream(InputStream in)
	{
		super(in);
	}
	
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
	
	public int readWinInt() throws IOException
	{
		byte[] buffer = new byte[4];
		in.read(buffer, 0, 4);
		return (((buffer[3] & 0xff) << 24) | ((buffer[2] & 0xff) << 16) | ((buffer[1] & 0xff) << 8) | (buffer[0] & 0xff));
	}
	
	public int readWinUnsignedShort() throws IOException
	{
		byte[] buffer = new byte[2];
		in.read(buffer, 0, 2);
		return (((buffer[1] & 0xff) << 8) | (buffer[0] & 0xff));
	}
}
