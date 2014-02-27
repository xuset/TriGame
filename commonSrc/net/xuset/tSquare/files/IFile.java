package net.xuset.tSquare.files;

import java.io.InputStream;
import java.io.OutputStream;

public interface IFile {
	public String getName();
	public String getPath();
	
	public InputStream getInputStream();
	public OutputStream getOutputStream();
	public void close();
}
