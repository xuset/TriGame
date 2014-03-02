package net.xuset.tSquare.imaging;

import java.util.HashMap;

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
	
	public AnimatedSprite(String url, IImage image, int rows, int columns, int total) {
		sprites = new Sprite[total];
		this.total = createContents(url, image, rows, columns, total);
	}
	
	public AnimatedSprite(String url, int rows, int columns, int total) {
		sprites = new Sprite[total];
		IImageFactory imgF = new ImageFactory();
		IImage img = imgF.loadImage(url);
		this.total = createContents(url, img, rows, columns, total);
	}
	
	private int createContents(String url, IImage image, int rows, int columns, int total) {
		IImageFactory imgFactory = new ImageFactory();
		int widthPer = image.getWidth() / columns;
		int heightPer = image.getHeight() / rows;
		int count = 0;

		for (int columnI = 0; columnI < columns && count < total; columnI++) {
			for (int rowI = 0; rowI < rows && count < total; rowI++) {
				int x = columnI * widthPer;
				int y = rowI * heightPer;
				IImage cropped = imgFactory.createCropped(image, x, y, widthPer, heightPer);
				sprites[count] = new Sprite(url + count, cropped, 1.0f);
				Sprite.add(sprites[count]);
				count++;
			}
		}
		return count;
	}
	
}
