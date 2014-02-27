package net.xuset.tSquare.imaging;

import android.graphics.Bitmap;

public class AndroidImage implements IImage {
	final Bitmap bitmap;
	
	public AndroidImage(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public AndroidImage(int w, int h) {
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
	}
	

	@Override
	public int getWidth() {
		return bitmap.getWidth();
	}

	@Override
	public int getHeight() {
		return bitmap.getHeight();
	}

	@Override
	public IGraphics getGraphics() {
		return new AndroidGraphics(bitmap);
	}

}
