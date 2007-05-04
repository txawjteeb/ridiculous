package org.cart.igd.sound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;


public class OggDecoder {

	private int convsize = 4096 * 4;
	private byte[] convbuffer = new byte[convsize]; 
	

	public OggDecoder() {
	}
	

	public OggData getData(InputStream input) throws IOException {
		if (input == null) {
			throw new IOException("Failed to read OGG, source does not exist?");
		}
		ByteArrayOutputStream dataout = new ByteArrayOutputStream();
		
		SyncState oy = new SyncState(); 
		StreamState os = new StreamState(); 
		Page og = new Page(); 
		Packet op = new Packet(); 

		Info vi = new Info(); 
		Comment vc = new Comment(); 
		DspState vd = new DspState(); 
		Block vb = new Block(vd); 

		byte[] buffer;
		int bytes = 0;

		boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
	

		oy.init(); 

		while (true) {
			int eos = 0;

			int index = oy.buffer(4096);
			
			buffer = oy.data;
			try {
				bytes = input.read(buffer, index, 4096);
			} catch (Exception e) {
				Log.error("Failure reading in vorbis");
				Log.error(e);
				System.exit(0);
			}
			oy.wrote(bytes);
			if (oy.pageout(og) != 1) {
				if (bytes < 4096)
					break;

				Log.error("Input does not appear to be an Ogg bitstream.");
				System.exit(0);
			}


			os.init(og.serialno());

			vi.init();
			vc.init();
			if (os.pagein(og) < 0) {
			
				Log.error("Error reading first page of Ogg bitstream data.");
				System.exit(0);
			}

			if (os.packetout(op) != 1) {
				Log.error("Error reading initial header packet.");
				System.exit(0);
			}

			if (vi.synthesis_headerin(vc, op) < 0) {
				Log.error("This Ogg bitstream does not contain Vorbis audio data.");
				System.exit(0);
			}



			int i = 0;
			while (i < 2) {
				while (i < 2) {

					int result = oy.pageout(og);
					if (result == 0)
						break;


					if (result == 1) {
						os.pagein(og); 
			
						while (i < 2) {
							result = os.packetout(op);
							if (result == 0)
								break;
							if (result == -1) {
					
								Log.error("Corrupt secondary header.  Exiting.");
								System.exit(0);
							}
							vi.synthesis_headerin(vc, op);
							i++;
						}
					}
				}

				index = oy.buffer(4096);
				buffer = oy.data;
				try {
					bytes = input.read(buffer, index, 4096);
				} catch (Exception e) {
					Log.error("Failed to read Vorbis: ");
					Log.error(e);
					System.exit(0);
				}
				if (bytes == 0 && i < 2) {
					Log.error("End of file before finding all Vorbis headers!");
					System.exit(0);
				}
				oy.wrote(bytes);
			}

			convsize = 4096 / vi.channels;

	
			vd.synthesis_init(vi); 
			vb.init(vd); 
	

			float[][][] _pcm = new float[1][][];
			int[] _index = new int[vi.channels];
	
			while (eos == 0) {
				while (eos == 0) {

					int result = oy.pageout(og);
					if (result == 0)
						break; 
					if (result == -1) { 
						Log.error("Corrupt or missing data in bitstream; continuing...");
					} else {
						os.pagein(og); 
					
						while (true) {
							result = os.packetout(op);

							if (result == 0)
								break; 
							if (result == -1) { 
							
							} else {
							
								int samples;
								if (vb.synthesis(op) == 0) { 
									vd.synthesis_blockin(vb);
								}

				
								while ((samples = vd.synthesis_pcmout(_pcm,
										_index)) > 0) {
									float[][] pcm = _pcm[0];
								
									int bout = (samples < convsize ? samples
											: convsize);

							
									for (i = 0; i < vi.channels; i++) {
										int ptr = i * 2;
							
										int mono = _index[i];
										for (int j = 0; j < bout; j++) {
											int val = (int) (pcm[i][mono + j] * 32767.);
					
											if (val > 32767) {
												val = 32767;
										
											}
											if (val < -32768) {
												val = -32768;
										
											}
											if (val < 0)
												val = val | 0x8000;
				
											if (bigEndian) {
												convbuffer[ptr] = (byte) (val >>> 8);
												convbuffer[ptr + 1] = (byte) (val);
											} else {
												convbuffer[ptr] = (byte) (val);
												convbuffer[ptr + 1] = (byte) (val >>> 8);
											}
											ptr += 2 * (vi.channels);
										}
									}

									dataout.write(convbuffer, 0, 2 * vi.channels * bout);

									vd.synthesis_read(bout);
	
								}
							}
						}
						if (og.eos() != 0)
							eos = 1;
					}
				}
				if (eos == 0) {
					index = oy.buffer(4096);
					if (index >= 0) {
						buffer = oy.data;
						try {
							bytes = input.read(buffer, index, 4096);
						} catch (Exception e) {
							Log.error("Failure during vorbis decoding");
							Log.error(e);
							System.exit(0);
						}
					} else {
						bytes = 0;
					}
					oy.wrote(bytes);
					if (bytes == 0)
						eos = 1;
				}
			}



			os.clear();

	

			vb.clear();
			vd.clear();
			vi.clear();
		}


		oy.clear();
		
		OggData ogg = new OggData();
		ogg.channels = vi.channels;
		ogg.rate = vi.rate;
		
		byte[] data = dataout.toByteArray();
		ogg.data = ByteBuffer.allocateDirect(data.length);
		ogg.data.put(data);
		ogg.data.rewind();
		
		return ogg;
	}
}
