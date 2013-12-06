package tSquare.imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class AnimatedSprite {
	private static final HashMap<String, AnimatedSprite> storedSprites = new HashMap<String, AnimatedSprite>();
	
	public static AnimatedSprite ADD(String key, AnimatedSprite as) {
		storedSprites.put(key, as);
		return as;
	}
	
	public static AnimatedSprite GET(String key) {
		return storedSprites.get(key);
	}
	
	public final Sprite[] sprites;
	public final int total;
	
	public AnimatedSprite(String url, BufferedImage image, int rows, int columns, int total) {
		sprites = new Sprite[total];
		this.total = createContents(url, image, rows, columns, total);
	}
	
	public AnimatedSprite(String url, int rows, int columns, int total) {
		sprites = new Sprite[total];
		BufferedImage img = loadImage(url);
		this.total = createContents(url, img, rows, columns, total);
	}
	
	private int createContents(String url, BufferedImage image, int rows, int columns, int total) {
		int widthPer = image.getWidth() / columns;
		int heightPer = image.getHeight() / rows;
		int count = 0;

		for (int columnI = 0; columnI < columns && count < total; columnI++) {
			for (int rowI = 0; rowI < rows && count < total; rowI++) {
				int x = columnI * widthPer;
				int y = rowI * heightPer;
				BufferedImage cropped = ImageProccess.crop(image, x, y, widthPer, heightPer);
				sprites[count] = new Sprite(url + count, cropped);
				Sprite.add(sprites[count]);
				count++;
			}
		}
		return count;
	}
	
	private BufferedImage loadImage(String url) {
		File f = new File(url);
		try {
			if (f.exists() && f.isFile())
				return ImageIO.read(f);
			
			URL stream = getClass().getResource("/" + url);
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
