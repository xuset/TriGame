package tSquare.imaging;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Sprite {
	private static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	public final BufferedImage image;
	public final String url;
	
	public final int getWidth() { return image.getWidth(); }
	public final int getHeight() { return image.getHeight(); }
	public final BufferedImage createCopy() { return ImageProccess.createCompatiableImage(image); }
	
	public Sprite(String url) {
		BufferedImage img = loadImage(url);
		this.image = ImageProccess.createCompatiableImage(img);
		this.url = url;
	}
	
	public Sprite(String url, BufferedImage image) {
		this.image = ImageProccess.createCompatiableImage(image);
		this.url = url;
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
	
	public static Sprite get(String url) {
		return Sprite.sprites.get(url);
	}
	
	public static boolean exists(String url) {
		if (Sprite.sprites.get(url) == null)
			return false;
		return true;
	}
	
	public void draw(int x, int y, Graphics g) {
		g.drawImage(image, x, y, null);
	}
	public void draw(int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh, Graphics g) {
		g.drawImage(image, dx, dy, dx + dw, dy + dh, sx, sy, sx + sw, sy + sh, null);
	}
	public void draw(int x, int y, double degrees, Graphics2D g2d) {
		AffineTransform t = new AffineTransform();
		t.setToRotation(Math.toRadians(-degrees + 90), x + (getWidth() / 2), y + (getHeight() / 2));
		draw(x, y, t, g2d);
	}
	public void draw(int x, int y, double degrees, double scaleX, double scaleY, Graphics2D g2d) {
		//TODO this does not work when scale is used
		AffineTransform t = new AffineTransform();
		t.translate(-x * scaleX + x, -y * scaleY + y);
		t.scale(scaleX, scaleY);
		t.rotate(Math.toRadians(-degrees + 90), x + (getWidth()/2), y + (getHeight()/2));
		draw(x, y, t, g2d);
	}
	public void draw(int x, int y, AffineTransform trans, Graphics2D g2d) {
		AffineTransform saveAT = g2d.getTransform();
		g2d.transform(trans);
		g2d.drawImage(image, x, y, null);
        g2d.setTransform(saveAT);
	}
}