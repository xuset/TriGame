package tSquare.system;
import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//TODO ability to repeat sound without reinitialization or lag

public class Sound {
	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	private String url;
	private Clip clip;
	
	public static Sound get(String soundId) {
		Sound s = sounds.get(soundId);
		if (s == null) {
			Sound newSound = new Sound(soundId, soundId);
			sounds.put(soundId, newSound);
			return newSound;
		} else {
			return s;
		}
	}
	
	public Sound(String soundId, String url) {
		this.url = url;
		try
	    {
	        clip = AudioSystem.getClip();
	        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(url));
	        clip.open(audioStream);
	    }
	    catch (Exception exc)
	    {
	        exc.printStackTrace(System.out);
	    }
	}
	
	public void play() {
		/*clip.stop();
		clip.flush();
		clip.setMicrosecondPosition(0l);
		clip.start();*/
	}
	
	
	
	public void remove() {
		sounds.remove(this.url);
		clip.drain();
		clip.close();
	}

}
