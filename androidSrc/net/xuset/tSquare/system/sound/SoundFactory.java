package net.xuset.tSquare.system.sound;

import net.xuset.tSquare.files.AssetFile;
import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.files.IFile;

public class SoundFactory implements ISoundFactory {
	public static final ISoundFactory instance = new SoundFactory();
	
	private SoundFactory() {
		
	}

	@Override
	public ISound loadSound(IFile file) {
		if (file instanceof AssetFile)
			return new AndroidSound((AssetFile) file);
		else
			return new AndroidSound(file.getPath());
	}

	@Override
	public ISound loadSound(String url) {
		IFile f = new FileFactory().open(url);
		if (f != null) {
			return loadSound(f);
		}
		return null;
	}

}
