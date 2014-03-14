package net.xuset.tSquare.system.sound;

import java.util.HashMap;

import net.xuset.tSquare.system.sound.SoundFactory;



public class SoundStore {
	private static final HashMap<String, ISound> sounds = new HashMap<String, ISound>();
	private static boolean mute = false;
	
	private SoundStore() {
		
	}
	
	public static void add(String name) {
		ISound s = SoundFactory.instance.loadSound(name);
		if (s != null)
			sounds.put(name, s);
	}
	
	public static void add(String name, ISound sound) {
		sounds.put(name, sound);
	}
	
	public static ISound get(String name) {
		return sounds.get(name);
	}
	
	public static void setMuteOnAll(boolean mute) {
		SoundStore.mute = mute;
	}
	
	public static boolean isMuteOn() { return mute; }
}
