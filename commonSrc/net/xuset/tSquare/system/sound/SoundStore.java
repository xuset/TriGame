package net.xuset.tSquare.system.sound;

import java.util.HashMap;

import net.xuset.tSquare.system.sound.SoundFactory;



public class SoundStore {
	private static final HashMap<String, ISound> sounds = new HashMap<String, ISound>();
	
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
	
	@SuppressWarnings("unused")
	public static ISound get(String name) {
		return new TEMPSOUND();
		//return sounds.get(name);
	}
	
	//TODO get rid of TEMPSOUND when sound is properly implemented
	private static class TEMPSOUND implements ISound {

		@Override
		public void play() {
			
		}

		@Override
		public void stop() {
			
		}

		@Override
		public void close() {
			
		}
		
	}
}
