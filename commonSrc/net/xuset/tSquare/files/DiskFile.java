package net.xuset.tSquare.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.xuset.tSquare.files.IFile;



public class DiskFile implements IFile {
	private final File file;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	
	DiskFile(File file) {
		this.file = file;
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public String getPath() {
		return file.getPath();
	}

	@Override
	public InputStream getInputStream() {
		if (inputStream == null) {
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() {
		if (outputStream == null) {
			try {
				outputStream = new FileOutputStream(file);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
		}

		return outputStream;
	}
	
	@Override
	public void close() {
		closeInputStream();
		closeOutputStream();
	}
	
	private void closeInputStream() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void closeOutputStream() {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
