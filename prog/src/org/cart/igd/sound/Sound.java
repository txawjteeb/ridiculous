package org.cart.igd.sound;
public class Sound { 
	private InternalSound sound; 
    	
    public String sndFilepath = "";
	
	public Sound(String ref) throws Exception { 
		SoundStore.get().init(); 
		sndFilepath = ref;
		try { 
            if (ref.toLowerCase().endsWith(".ogg")) { 
                sound = SoundStore.get().getOgg(ref); 
            } else if (ref.toLowerCase().endsWith(".wav")) { 
                sound = SoundStore.get().getWAV(ref); 
            } 
             else { 
                throw new Exception("Only .xm, .mod and .ogg are currently supported."); 
            } 
        } catch (Exception e) { 
            Log.error(e); 
            throw new Exception("Failed to load sound: "+ref); 
        } 
    } 
     

   
     
    public void play(float pitch, float volume) { 
       sound.playAsSoundEffect(pitch, volume, false); 
    } 
 
    public void playAt(float x, float y, float z) { 
        sound.playAsSoundEffect(1.0f, 1.0f, false, x,y,z); 
    } 
     
    public void playAt(float pitch, float volume, float x, float y, float z) { 
        sound.playAsSoundEffect(pitch,volume, false, x,y,z); 
    } 
     
     public void playAtLoop(float x, float y, float z) { 
        sound.playAsSoundEffect(1.0f, 1.0f, true, x,y,z); 
    } 
     
    public void playSpec(float pitch, float gain, float x, float y, float z) { 
        sound.playAsSoundEffect(pitch, gain, false, x,y,z); 
    } 

    public void loop() { 
        loop(1.0f,1.0f); 
    } 
     
    public void loop(float pitch, float volume) { 
        sound.playAsSoundEffect(pitch, volume, true); 
    } 
     
    public boolean playing() { 
    System.out.println( sound.isPlaying());
        return sound.isPlaying(); 
    } 
     
    public void stop() { 
        sound.stop(); 
    } 
} 
