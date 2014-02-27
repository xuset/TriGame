package net.xuset.tSquare.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;

public class AssetFile implements IFile {
	private final String url;
	private final InputStream is;
	
	public AssetFile(String url, AssetManager aManager) throws IOException {
		this.url = url;
		is = aManager.open(url);
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
		return is;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public void close() {
		try {
			is.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
