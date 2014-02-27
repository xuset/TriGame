package net.xuset.tSquare.files;

public interface IFileFactory {
	boolean exists(String url);
	
	IFile open(String url);
}
