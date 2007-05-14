package org.cart.igd.sound;

import org.cart.igd.core.Kernel;

/**
 * SoundManager.java
 * 
 * General Purpose: player sounds at certain volumes based on 
 * settings and type of sound
 */
public class SoundManager
{
	/** background music volume */
	public float bgVol;
	
	/** special effects sound volume */
	public float seVol;
	
	/** dialogue voice volume */
	public float voVol;
	
	public boolean mute = false;
	
	
	public SoundManager(float music,float sound,float voice){
		this.bgVol = music;
		this.seVol = sound;
		this.voVol = voice;
	}
	
	public SoundManager(SoundSettings ss){
		this.bgVol = ss.bgVol;
		this.seVol = ss.seVol;
		this.voVol = ss.voVol;
		this.mute = ss.mute;
	}
	
	public void playVoice(Sound voice){
		if(voice != null){
			voice.play(1f, Kernel.soundSettings.voVol);
			System.out.println(voice.sndFilepath + "voice vol: "
					+Kernel.soundSettings.voVol);
		} else {
			System.out.println("SoundManager.playVoice(Sound voice) is null");
		}
	}
	
	public void playSound(Sound effect){
		if(effect != null){
			effect.play(1f, Kernel.soundSettings.seVol);
			System.out.println(effect.sndFilepath + "sound effect vol: "
					+Kernel.soundSettings.seVol);
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
			music.loop(1f, Kernel.soundSettings.bgVol);
			System.out.println(music.sndFilepath + "musci vol: "
					+Kernel.soundSettings.bgVol);
		} else {
			System.out.println("SoundManager.loopMusic(Sound music) is null");
		}
	}
	
}
