package net.xuset.tSquare.system.sound;

import net.xuset.tSquare.files.IFile;

public interface ISoundFactory {
	ISound loadSound(String url);
	ISound loadSound(IFile file);
}
