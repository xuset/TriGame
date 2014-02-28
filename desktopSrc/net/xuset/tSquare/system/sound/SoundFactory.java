package net.xuset.tSquare.system.sound;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.files.IFile;
import net.xuset.tSquare.system.sound.ISound;
import net.xuset.tSquare.system.sound.ISoundFactory;

public class SoundFactory implements ISoundFactory{
	public static final ISoundFactory instance = new SoundFactory();
	
	private SoundFactory() {
		
	}

	@Override
	public ISound loadSound(IFile file) {
		
		try {
			return new JavaXSound(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public ISound loadSound(String url) {
		IFile f = FileFactory.instance.open(url);
		if (f == null)
			return null;
		return loadSound(f);
	}
}
