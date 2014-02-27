package net.xuset.tSquare.imaging;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageFactoryBackend {
	private static GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

	
	static IImage createEmpty(int w, int h) {
		return new AwtImage(gc.createCompatibleImage(w, h, Transparency.BITMASK));
	}
	
	static IImage loadFromStream(InputStream is) {
		try {
			return new AwtImage(ImageIO.read(is));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
