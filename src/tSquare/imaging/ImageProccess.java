package tSquare.imaging;


import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

//TODO [low] find better way to rotate images

public class ImageProccess {
	
	private static GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	
	public static void rotate(BufferedImage image, double degrees) {
	}
	
	public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height, BufferedImage output) {
		output.getGraphics().drawImage(image, 0, 0, width, height, x, y, x + width, y + height, null);
	    return output;
	}
	public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height) {
		return crop(image, x, y, width, height, createCompatiableImage(width, height));
	}
	
	public static BufferedImage scale(Image image, double scaleX, double scaleY) {
		int newWidth = (int) (image.getWidth(null) * scaleX);
		int newHeight = (int) (image.getHeight(null) * scaleY);
		BufferedImage bi = createCompatiableImage(newWidth, newHeight);
		bi.getGraphics().drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(null), image.getHeight(null), null);
		return bi;
	}
	
	public static BufferedImage createCompatiableImage(Image i) {
		BufferedImage new_image = gc.createCompatibleImage(i.getWidth(null),i.getHeight(null), Transparency.BITMASK);
		new_image.getGraphics().drawImage(i,0,0,null);
		return new_image;
	}
	
	public static BufferedImage createCompatiableImage(int width, int height) {
		return gc.createCompatibleImage(width, height, Transparency.BITMASK);
	}
	
	public static VolatileImage createVolatileImage(BufferedImage bi) {
		VolatileImage vImage = createVolatileImage(bi.getWidth(), bi.getHeight());
		vImage.getGraphics().drawImage(bi, 0, 0, null);
		return vImage;
	}
	
	public static VolatileImage createVolatileImage(int width, int height) {
		return gc.createCompatibleVolatileImage(width , height);
	}
	

	
	public static BufferedImage loadImage(String url) {
		File f = new File(url);
		try {
			if (f.exists() && f.isFile())
				return ImageIO.read(f);
			
			URL stream = ImageProccess.class.getResource("/" + url);
			if (stream == null) {
				return null;
			}
			return ImageIO.read(stream);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
