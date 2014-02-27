package net.xuset.tSquare.imaging;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageFactoryBackend {
	public static IImage createEmpty(int w, int h) {
		return new AndroidImage(w, h);
	}
	
	public static IImage loadFromStream(InputStream is) {
		Bitmap bm = BitmapFactory.decodeStream(is);
		return new AndroidImage(bm);
	}
}
