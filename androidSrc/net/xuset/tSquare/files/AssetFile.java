package net.xuset.tSquare.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

public class AssetFile implements IFile {
	private final String url;
	private final AssetManager aManager;
	private InputStream is;
	
	public AssetFile(String url, AssetManager aManager) {
		this.url = url;
		this.aManager = aManager;
	}
	
	public AssetFileDescriptor getFileDescriptor() {
		try {
			return aManager.openFd(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
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
		if (is == null) {
			try {
				is = aManager.open(url);
			} catch (IOException e) {
				is = null;
				e.printStackTrace();
			}
		}
		
		return is;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public void close() {
		if (is != null) {
			try {
				is.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		is = null;
	}

}
