package org.cart.igd.sound;

public class SoundSettings {
	/** background music volume */
	public float bgVol;
	
	/** special effects sound volume */
	public float seVol;
	
	/** dialogue voice volume */
	public float voVol;
	
	boolean mute = false;
	
	public SoundSettings(float music,float sound,float voice,boolean mute ){
		this.bgVol = music;
		this.seVol = sound;
		this.voVol = voice;
		this.mute = mute;
	}

	
}
