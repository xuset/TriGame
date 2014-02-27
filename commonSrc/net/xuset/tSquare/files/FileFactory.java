package net.xuset.tSquare.files;

import java.io.File;

public class FileFactory implements IFileFactory {
	public static final IFileFactory instance = new FileFactory();
	
	public FileFactory() {
		
	}

	@Override
	public boolean exists(String url) {
		File f = new File(url);
		if (f.isFile())
			return true;
		
		return (FileFactory.class.getResource(url) != null);
	}
	
	@Override
	public IFile open(String url) {
		File f = new File(url);
		if (f.exists() && f.isFile())
			return new DiskFile(f);
		
		IFile rscFile = UrlFile.loadFromResource(url);
		if (rscFile == null)
			System.err.println("Warning: File not found: " + url);
		return rscFile;
	}
}
