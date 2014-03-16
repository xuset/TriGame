package net.xuset.tSquare.system.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.xuset.tSquare.files.IFile;
import net.xuset.tSquare.system.sound.ISound;

public class JavaXSound implements ISound {
	private final Clip clip;
	
	JavaXSound(IFile file) throws IOException, LineUnavailableException, UnsupportedAudioFileException{
		InputStream is = file.getInputStream();
		BufferedInputStream buffIs = new BufferedInputStream(is);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(buffIs);
		
		Clip temp;
		temp = AudioSystem.getClip();
		temp.open(audioStream);
		clip = temp;
	}

	@Override
	public void play() {
		if (!SoundStore.isMuteOn()) {
			clip.setFramePosition(0);
			if (!clip.isActive()) {
				clip.start();
			}
		}
	}

	@Override
	public void stop() {
		clip.stop();
		clip.setMicrosecondPosition(0l);
	}

	@Override
	public void close() {
		clip.close();
	}

}
