package org.cart.igd.sound;
public class InternalSound { 

    private SoundStore store; 
    private int buffer; 
    private int index = -1; 
     

    InternalSound(SoundStore store, int buffer) { 
        this.store = store; 
        this.buffer = buffer; 
    } 
     

    protected InternalSound() { 
         
    } 
     

    public void stop() { 
        if (index != -1) { 
            store.stopSource(index); 
        } 
    } 
     

    public boolean isPlaying() { 
        if (index != -1) { 
            return store.isPlaying(index); 
        } 
         
        return false; 
    } 
     

    public void playAsSoundEffect(float pitch, float gain, boolean loop) { 
        index = store.playAsSound(buffer, pitch, gain, loop); 
    } 
 
 

    public void playAsSoundEffect(float pitch, float gain, boolean loop, float x, float y, float z) { 
        index = store.playAsSoundAt(buffer, pitch, gain, loop, x, y, z); 
    } 
     

    public void playAsMusic(float pitch, float gain, boolean loop) { 
        store.playAsMusic(buffer, pitch, gain, loop); 
    } 
     

    public static void pauseMusic() { 
        SoundStore.get().pauseLoop(); 
    } 
 

    public static void restartMusic() { 
        SoundStore.get().restartLoop(); 
    } 
} 
