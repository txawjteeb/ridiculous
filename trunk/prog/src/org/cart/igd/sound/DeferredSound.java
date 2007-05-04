package org.cart.igd.sound;

import java.io.IOException;

public class DeferredSound extends InternalSound   {
	public static final int OGG = 1;
	public static final int WAV = 2;
	public static final int MOD = 3;
	private int type;
	private String ref;
	private InternalSound target;

	public DeferredSound(String ref, int type) {
		this.ref = ref;
		this.type = type;
		
	}
	private void checkTarget() {
		if (target == null) {
			throw new RuntimeException("Attempt to use deferred sound before loading");
		}
	}
	public void load() throws IOException {

		SoundStore.get().setDeferredLoading(false);
		switch (type) {
		case OGG:
			target = SoundStore.get().getOgg(ref);
			break;
		case WAV:
			target = SoundStore.get().getWAV(ref);
			break;

		default:
			Log.error("Unrecognised sound type: "+type);
			break;
		}

	}
	public boolean isPlaying() {
		checkTarget();
		
		return target.isPlaying();
	}
	public void playAsMusic(float pitch, float gain, boolean loop) {
		checkTarget();
		target.playAsMusic(pitch, gain, loop);
	}
	public void playAsSoundEffect(float pitch, float gain, boolean loop) {
		checkTarget();
		target.playAsSoundEffect(pitch, gain, loop);
	}
	public void stop() {
		checkTarget();
		target.stop();
	}
	public String getDescription() {
		return ref;
	}

}
