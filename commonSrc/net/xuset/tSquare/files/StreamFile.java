package net.xuset.tSquare.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamFile implements IFile {
	private final String url;
	private final InputStream input;
	private final OutputStream output;
	
	public StreamFile(String url, InputStream input, OutputStream output) {
		this.url = url;
		this.input = input;
		this.output = output;
	}

	@Override
	public String getName() {
		return url;
	}

	@Override
	public String getPath() {
		return url;
	}

	@Override
	public InputStream getInputStream() {
		return input;
	}

	@Override
	public OutputStream getOutputStream() {
		return output;
	}

	@Override
	public void close() {
		closeInput();
		closeOutput();
	}
	
	private void closeInput() {
		if (input != null) {
			try {
				input.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void closeOutput() {
		if (output != null) {
			try {
				output.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
