package net.xuset.tSquare.files;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;

public class AssetFileFactory extends FileFactory {
	private final AssetManager assetManager;
	
	public AssetFileFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	@Override
	public boolean exists(String url) {
		try {
			InputStream is = assetManager.open(url);
			is.close();
			return true;
		} catch (IOException ex) {
			
		}
		return super.exists(url);
	}
	
	@Override
	public IFile open(String url) {
		try {
			InputStream is = assetManager.open(url);
			return new StreamFile(url, is, null);
		} catch (IOException e) {
			return super.open(url);
		}
	}

}
