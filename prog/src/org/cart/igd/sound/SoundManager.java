package org.cart.igd.sound;

/**
 * SoundManager.java
 * 
 * General Purpose: player sounds at certain volumes based on 
 * settings and type of sound
 */
public class SoundManager
{
	/** background music volume */
	public float bgVol = 3f;
	
	/** special effects sound volume */
	public float seVol = 1f;
	
	/** dialogue voice volume */
	public float voVol = 8f;
	
	
	public SoundManager(float music,float sound,float voice){
		this.bgVol = music;
		this.seVol = sound;
		this.voVol = voice;
	}
	
	public void playVoice(Sound voice){
		if(voice != null){
			voice.play(1f, voVol);
		} else {
			System.out.println("SoundManager.playVoice(Sound voice) is null");
		}
	}
	
	public void playSound(Sound effect){
		if(effect != null){
			effect.play(1f, voVol);
		} else {
			System.out.println("SoundManager.playSound(Sound effect) is null");
		}
	}
	
	public void stopMusic(Sound music){
		if( music != null ){
			music.stop();
		} else {
			System.out.println("SoundManager.loopMusic(Sound music) is null");
		}
		
	}
	
	public void loopMusic(Sound music){
		if( music != null ){
			music.loop(1f, bgVol);
		} else {
			System.out.println("SoundManager.loopMusic(Sound music) is null");
		}
		
	}
	
}
