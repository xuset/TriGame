package net.xuset.tSquare.system.sound;

import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.files.IFile;

public class SoundFactory implements ISoundFactory {
	public static final ISoundFactory instance = new SoundFactory();
	
	private SoundFactory() {
		
	}

	@Override
	public ISound loadSound(String url) {
		IFile f = new FileFactory().open(url);
		if (f != null)
			loadSound(f);
		return null;
	}

	@Override
	public ISound loadSound(IFile file) {
		return new AndroidSound(file);
	}

}
