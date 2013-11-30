package tSquare.system;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	private static final HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	private final String url;
	private Clip clip;
	
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
		try
	    {
	        clip = AudioSystem.getClip();
	        AudioInputStream audioStream = loadSound(url);
	        clip.open(audioStream);
	    }
	    catch (Exception exc)
	    {
	        exc.printStackTrace(System.out);
	    }
	}
	
	private AudioInputStream loadSound(String url) {
		File f = new File(url);
		try {
			if (f.exists() && f.isFile())
				return AudioSystem.getAudioInputStream(f);
			
			URL stream = getClass().getResource("/" + url);
			if (stream == null) {
				return null;
			}
			return AudioSystem.getAudioInputStream(stream);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public void play() {
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
