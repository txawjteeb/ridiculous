package org.cart.igd.sound;
public class Music {

	private static Music currentMusic;
	private InternalSound sound;
	private boolean playing;
	

	public Music(String ref) throws Exception {
		SoundStore.get().init();
		
		try {
			if (ref.toLowerCase().endsWith(".ogg")) {
				sound = SoundStore.get().getOgg(ref);
			} else if (ref.toLowerCase().endsWith(".wav")) {
				sound = SoundStore.get().getWAV(ref);
			} else {
				throw new Exception("Only .xm, .mod and .ogg are currently supported.");
			}
		} catch (Exception e) {
			Log.error(e);
			throw new Exception("Failed to load sound: "+ref);
		}
	}


	public void loop() {
		loop(1.0f,1.0f);
	}
	

	public void play() {
		play(1.0f,1.0f);
	}


	public void play(float pitch, float volume) {
		if (currentMusic != null) {
			currentMusic.playing = false;
		}
		
		currentMusic = this;
		sound.playAsMusic(pitch, volume, false);
		playing = true;
	}


	public void loop(float pitch, float volume) {
		if (currentMusic != null) {
			currentMusic.stop();
			currentMusic.playing = false;
		}
		
		currentMusic = this;
		sound.playAsMusic(pitch, volume, true);
		playing = true;
	}
	

	public void pause() {
		playing = false;
		InternalSound.pauseMusic();
	}
	

	public void stop() {
		pause();
	}
	

	public void resume() {
		playing = true;
		InternalSound.restartMusic();
	}
	

	public boolean playing() {
		return (currentMusic == this) && (playing);
	}
}
