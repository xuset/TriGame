package net.xuset.tSquare.imaging;


import java.util.HashMap;

public class Sprite {
	private static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	public final IImage image;
	public final String url;
	public final float scaleFactor;
	
	public final float getWidth() { return (image.getWidth() * scaleFactor); }
	public final float getHeight() { return (image.getHeight() * scaleFactor); }
	public final IImage createCopy() { return new ImageFactory().createCopy(image); }
	
	public Sprite(String url) {
		this.url = url;
		image = new ImageFactory().loadImage(url);
		scaleFactor = 1.0f;
	}
	
	public Sprite(String url, IImage image, float scaleFactor) {
		this.image = image;
		this.url = url;
		this.scaleFactor = scaleFactor;
	}
	
	public static Sprite add(String url) {
		Sprite s = Sprite.sprites.get(url);
		if (s != null)
			return s;
		
		Sprite cre = new Sprite(url);
		sprites.put(url, cre);
		return s;
	}
	
	public static Sprite add(Sprite s) {
		sprites.put(s.url, s);
		return s;
	}
	
	public static Sprite get(String url) {
		return Sprite.sprites.get(url);
	}
	
	public static boolean exists(String url) {
		if (Sprite.sprites.get(url) == null)
			return false;
		return true;
	}
	
	public void draw(float x, float y, IGraphics g) {
		g.drawImage(image, x, y);
	}
	public void draw(float dx, float dy, float dw, float dh, float sx, float sy, float sw, float sh, IGraphics g) {
		g.drawImage(image, dx, dy, dx + dw, dy + dh, sx, sy, sx + sw, sy + sh);
	}
	public void draw(float x, float y, double radians, IGraphics g) {
		g.drawImageRotate(image, x, y, radians);
	}
	public void draw(float x, float y, double scaleX, double scaleY, IGraphics g) {
		int newWidth = (int) (image.getWidth() * scaleX);
		int newHeight = (int) (image.getHeight() * scaleY);
		draw(x, y, newWidth, newHeight, 0, 0, image.getWidth(), image.getHeight(), g);
	}
}