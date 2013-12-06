package tSquare.system;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	private static final HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	private final String url;
	private Clip clip;
	public final boolean isLoaded;
	
	public static Sound get(String soundId) {
		return sounds.get(soundId);
	}
	
	public static Sound add(String soundId) {
		Sound s = sounds.get(soundId);
		if (s == null) {
			Sound newSound = new Sound(soundId);
			sounds.put(soundId, newSound);
			return newSound;
		} else {
			return s;
		}
	}
	
	public Sound(String url) {
		this.url = url;
		boolean errored = false;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream audioStream = loadSound(url);
			clip.open(audioStream);
		} catch (Exception ex) {
			errored = true;
			ex.printStackTrace();
		} finally {
			isLoaded = !errored;
		}
	}
	
	private AudioInputStream loadSound(String url) throws Exception {
		File f = new File(url);
		if (f.exists() && f.isFile())
			return AudioSystem.getAudioInputStream(f);
		InputStream stream = getClass().getResourceAsStream("/" + url);
		if (stream == null)
			throw new Exception(url + " was not found!");
		BufferedInputStream bStream = new BufferedInputStream(stream);
		return AudioSystem.getAudioInputStream(bStream);
	}
	
	public void play() {
		if (!isLoaded)
			return;
		
		clip.stop();
		clip.setMicrosecondPosition(0l);
		clip.start();
	}
	
	
	
	public void remove() {
		sounds.remove(this.url);
		clip.drain();
		clip.close();
	}

}
