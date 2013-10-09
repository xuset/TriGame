package tSquare.imaging;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

//TODO check Vimage if contents are available

public class Sprite {
	private static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	private Image image;
	private String url;
	
	public boolean useVolatileImage = false;
	public boolean useAntiAlias = false;
	
	public final int getWidth() { return image.getWidth(null); }
	public final int getHeight() { return image.getHeight(null); }
	public final String getUrl() { return url; }
	public final Image getImage() { return image; }
	public final BufferedImage getBuffered() { return ImageProccess.createCompatiableImage(image); }
	
	
	public Sprite(String url) {
		this(url, false);
	}
	public Sprite(String url, boolean useVImage) {
		File f = new File(url);
		try {
			BufferedImage image = ImageIO.read(f);
			createSprite(url, image, useVImage);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public Sprite(String url, BufferedImage image) {
		this(url, image, false);
	}
	public Sprite(String url, BufferedImage image, boolean useVImage) {
		createSprite(url, image, useVImage);
	}
	
	private void createSprite(String url, BufferedImage image, boolean useVImage) {
		if (useVImage)
			this.image = ImageProccess.createVolatileImage(image);
		else
			this.image = ImageProccess.createCompatiableImage(image);
		this.url = url;
		Sprite.sprites.put(url, this);
	}
	
	public static Sprite add(String url) {
		return add(url, false);
	}
	public static Sprite add(String url, boolean useVImage) {
		Sprite s = Sprite.sprites.get(url);
		if (s != null)
			return s;
		if (new File(url).isFile())
			return new Sprite(url, useVImage);
		return null;
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