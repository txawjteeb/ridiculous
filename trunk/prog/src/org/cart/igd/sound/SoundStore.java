package org.cart.igd.sound;
 
import java.io.IOException; 
import java.io.InputStream; 
import java.nio.FloatBuffer; 
import java.nio.IntBuffer; 
import java.security.AccessController; 
import java.security.PrivilegedAction; 
import java.util.HashMap; 
 
import org.lwjgl.BufferUtils; 
import org.lwjgl.Sys; 
import org.lwjgl.openal.AL; 
import org.lwjgl.openal.AL10; 
import org.lwjgl.openal.OpenALException; 

public class SoundStore { 

    private static SoundStore store = new SoundStore(); 

    private boolean sounds; 
    private boolean music; 
    private boolean soundWorks; 
    private int sourceCount; 
    private HashMap loaded = new HashMap(); 

    private int currentMusic = -1; 

    private IntBuffer sources; 
    private int nextSource; 
    private boolean inited = false; 

    private float musicVolume = 1.0f; 

    private float soundVolume = 1.0f; 

    private float lastMusicGain = 1.0f; 

    private boolean paused; 

    private boolean deferred; 
     

    private FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }); 

    private FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3); 
     
     
    private SoundStore() { 
    } 
    
    public void clear() { 
        store = new SoundStore(); 
    } 
 
 
    public void setDeferredLoading(boolean deferred) { 
        this.deferred = deferred; 
    } 
     

    public boolean isDeferredLoading() { 
        return deferred; 
    } 
     

    public void setMusicOn(boolean music) { 
        if (soundWorks) { 
            this.music = music; 
            if (music) { 
                restartLoop(); 
            } else { 
                pauseLoop(); 
            } 
        } 
    } 
     

    public boolean isMusicOn() { 
        return music; 
    } 
 

    public void setMusicVolume(float volume) { 
        if (volume < 0) { 
            volume = 0; 
        } 
        musicVolume = volume; 
         
        if (soundWorks) { 
            if (volume == 0) { 
                volume = 0.001f; 
            } 
            AL10.alSourcef(sources.get(0), AL10.AL_GAIN, lastMusicGain * volume);  
        } 
    } 
     

    public void setSoundVolume(float volume) { 
        if (volume < 0) { 
            volume = 0; 
        } 
        soundVolume = volume; 
    } 
     

    public boolean soundWorks() { 
        return soundWorks; 
    } 
     
 
    public boolean musicOn() { 
        return music; 
    } 
 
  
    public float getSoundVolume() { 
        return soundVolume; 
    } 
     

    public float getMusicVolume() { 
        return musicVolume; 
    } 
     

    public int getSource(int index) { 
        return sources.get(index); 
    } 
     
 
    public void setSoundsOn(boolean sounds) { 
        if (soundWorks) { 
            this.sounds = sounds; 
        } 
    } 
     
    public boolean soundsOn() { 
        return sounds; 
    } 
     
  
    public void init() { 
        if (inited) { 
            return; 
        } 
        Log.info("Initialising sounds.."); 
        inited = true; 
         
        AccessController.doPrivileged(new PrivilegedAction() { 
            public Object run() { 
                try { 
                    AL.create(); 
                    soundWorks = true; 
                    sounds = true; 
                    music = true; 
                    Log.info("- Sound works"); 
                } catch (Exception e) { 
                    Log.error("Sound initialisation failure."); 
                    Log.error(e); 
                    soundWorks = false; 
                    sounds = false; 
                    music = false; 
                } 
                 
                return null; 
            }}); 
         
        if (soundWorks) { 
            sourceCount = 8; 
            sources = BufferUtils.createIntBuffer(sourceCount); 
            AL10.alGenSources(sources); 
             
            if (AL10.alGetError() != AL10.AL_NO_ERROR) { 
                sounds = false; 
                music = false; 
                soundWorks = false; 
                Log.error("- AL init failed"); 
            } else { 
                FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put( 
                        new float[] { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f }); 
                FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put( 
                        new float[] { 0.0f, 0.0f, 0.0f }); 
                FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put( 
                        new float[] { 0.0f, 0.0f, 0.0f }); 
                listenerPos.flip(); 
                listenerVel.flip(); 
                listenerOri.flip(); 
                AL10.alListener(AL10.AL_POSITION, listenerPos); 
                AL10.alListener(AL10.AL_VELOCITY, listenerVel); 
                AL10.alListener(AL10.AL_ORIENTATION, listenerOri); 
                 
                Log.info("- Sounds source generated"); 
            } 
        } 
    } 
 
  
    void stopSource(int index) { 
        AL10.alSourceStop(sources.get(index)); 
    } 
     
    int playAsSound(int buffer,float pitch,float gain,boolean loop) { 
        return playAsSoundAt(buffer, pitch, gain, loop, 0, 0, 0); 
    } 
     
    int playAsSoundAt(int buffer,float pitch,float gain,boolean loop,float x, float y, float z) { 
        gain *= soundVolume; 
        if (gain == 0) { 
            gain = 0.001f; 
        } 
        
        System.out.println("gain"+gain);
        if (soundWorks) { 
            if (sounds) { 
                int nextSource = findFreeSource(); 
                if (nextSource == -1) { 
                    return -1; 
                } 
                 
                AL10.alSourceStop(sources.get(nextSource)); 
                 
                AL10.alSourcei(sources.get(nextSource), AL10.AL_BUFFER, buffer); 
                AL10.alSourcef(sources.get(nextSource), AL10.AL_PITCH, pitch); 
                AL10.alSourcef(sources.get(nextSource), AL10.AL_GAIN, gain);  
                AL10.alSourcei(sources.get(nextSource), AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE); 
                 
                sourcePos.clear(); 
                sourceVel.clear(); 
                sourceVel.put(new float[] { 0, 0, 0 }); 
                sourcePos.put(new float[] { x, y, z }); 
                sourcePos.flip(); 
                sourceVel.flip(); 
                AL10.alSource(sources.get(nextSource), AL10.AL_POSITION, sourcePos); 
                AL10.alSource(sources.get(nextSource), AL10.AL_VELOCITY, sourceVel); 
                 
                AL10.alSourcePlay(sources.get(nextSource));  
                 
                return nextSource; 
            } 
        } 
         
        return -1; 
    } 

    boolean isPlaying(int index) { 
        int state = AL10.alGetSourcei(sources.get(index), AL10.AL_SOURCE_STATE); 
        return (state == AL10.AL_PLAYING); 
    } 
     

    private int findFreeSource() { 
        for (int i=1;i<sourceCount-1;i++) { 
            int state = AL10.alGetSourcei(sources.get(i), AL10.AL_SOURCE_STATE); 
             
            if (state != AL10.AL_PLAYING) { 
                return i; 
            } 
        } 
         
        return -1; 
    } 
     

    void playAsMusic(int buffer,float pitch,float gain, boolean loop) { 
        lastMusicGain = gain; 
        paused = false; 
         
        gain *= musicVolume; 
        if (gain == 0) { 
            gain = 0.001f; 
        } 

         
        if (soundWorks) { 
            if (currentMusic != -1) { 
                AL10.alSourceStop(sources.get(0)); 
            } 
             
            getMusicSource(); 
             
            AL10.alSourcei(sources.get(0), AL10.AL_BUFFER, buffer); 
            AL10.alSourcef(sources.get(0), AL10.AL_PITCH, pitch); 
            AL10.alSourcef(sources.get(0), AL10.AL_GAIN, gain);  
            AL10.alSourcei(sources.get(0), AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE); 
             
            currentMusic = sources.get(0); 
             
            if (!music) { 
                pauseLoop(); 
            } else { 
                AL10.alSourcePlay(sources.get(0));  
            } 
        } 
    } 
     
 
    public int getMusicSource() { 
        IntBuffer deleteMe = BufferUtils.createIntBuffer(1); 
        deleteMe.put(sources.get(0)); 
        deleteMe.flip(); 
        AL10.alDeleteSources(deleteMe); 
         
        IntBuffer musicChannel = BufferUtils.createIntBuffer(1); 
        AL10.alGenSources(musicChannel); 
        sources.put(0,musicChannel.get(0)); 
         
        return sources.get(0); 
    } 
     
 
    public void setMusicPitch(float pitch) { 
        if (soundWorks) { 
            AL10.alSourcef(sources.get(0), AL10.AL_PITCH, pitch); 
        } 
    } 
     
 
    public void pauseLoop() { 
        if ((soundWorks) && (currentMusic != -1)){ 
            paused = true; 
            AL10.alSourcePause(currentMusic); 
        } 
    } 
 
    
    public void restartLoop() { 
        if ((music) && (soundWorks) && (currentMusic != -1)){ 
            paused = false; 
            AL10.alSourcePlay(currentMusic); 
        } 
    } 
     

    public InternalSound getWAV(String ref) throws IOException { 
        if (!soundWorks) { 
            return new InternalSound(this, 0); 
        } 
        if (!inited) { 
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method."); 
        } 
        if (deferred) { 
            return new DeferredSound(ref, DeferredSound.WAV); 
        } 
         
        int buffer = -1; 
         
        if (loaded.get(ref) != null) { 
            buffer = ((Integer) loaded.get(ref)).intValue(); 
        } else { 
            Log.info("Loading: "+ref); 
            try { 
                IntBuffer buf = BufferUtils.createIntBuffer(1); 
                 
                InputStream in = ResourceLoader.getResourceAsStream(ref); 
                WaveData data = WaveData.create(in); 
                AL10.alGenBuffers(buf); 
                AL10.alBufferData(buf.get(0), data.format, data.data, data.samplerate); 
                 
                loaded.put(ref,new Integer(buf.get(0))); 
                buffer = buf.get(0); 
            } catch (Exception e) { 
                Log.error(e); 
                Sys.alert("Error","Failed to load: "+ref+" - "+e.getMessage()); 
                System.exit(0); 
            } 
        } 
         
        if (buffer == -1) { 
            throw new IOException("Unable to load: "+ref); 
        } 
         
        return new InternalSound(this, buffer); 
    } 
     
 
    public InternalSound getOgg(String ref) throws IOException { 
        if (!soundWorks) { 
            return new InternalSound(this, 0); 
        } 
        if (!inited) { 
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method."); 
        } 
        if (deferred) { 
            System.out.println("Return deferred for: "+ref); 
            return new DeferredSound(ref, DeferredSound.OGG); 
        } 
         
        int buffer = -1; 
         
        if (loaded.get(ref) != null) { 
            buffer = ((Integer) loaded.get(ref)).intValue(); 
        } else { 
            Log.info("Loading: "+ref); 
            try { 
                IntBuffer buf = BufferUtils.createIntBuffer(1); 
                 
                InputStream in = ResourceLoader.getResourceAsStream(ref); 
                 
                OggDecoder decoder = new OggDecoder(); 
                OggData ogg = decoder.getData(in); 
                 
                AL10.alGenBuffers(buf); 
                AL10.alBufferData(buf.get(0), ogg.channels > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16, ogg.data, ogg.rate); 
                 
                loaded.put(ref,new Integer(buf.get(0))); 
                                      
                buffer = buf.get(0); 
            } catch (Exception e) { 
                Log.error(e); 
                Sys.alert("Error","Failed to load: "+ref+" - "+e.getMessage()); 
                System.exit(0); 
            } 
        } 
         
        if (buffer == -1) { 
            throw new IOException("Unable to load: "+ref); 
        } 
         
        return new InternalSound(this, buffer); 
    } 
     

    public void poll(int delta) { 
        if (!soundWorks) { 
            return; 
        } 
        if (paused) { 
            return; 
        } 
         

    } 
     

    public static SoundStore get() { 
        return store; 
    } 
} 