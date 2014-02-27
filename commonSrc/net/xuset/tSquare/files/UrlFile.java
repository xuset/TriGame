package net.xuset.tSquare.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class UrlFile implements IFile {
	private final URL url;
	private InputStream inputStream = null;
	
	public static UrlFile loadFromResource(String url) {
		if (!url.startsWith("/"))
			url = "/" + url;
		
		URL stream = UrlFile.class.getResource(url);
		if (stream == null)
			return null;
		
		return new UrlFile(stream);
	}
	
	public UrlFile(URL url) {
		this.url = url;
	}

	@Override
	public String getName() {
		return url.getFile();
	}

	@Override
	public String getPath() {
		return url.getPath();
	}

	@Override
	public InputStream getInputStream() {
		if (inputStream == null) {
			try {
				inputStream = url.openStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public void close() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
