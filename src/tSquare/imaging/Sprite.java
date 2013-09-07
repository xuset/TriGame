package tSquare.imaging;


import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import tSquare.game.GameBoard;
import tSquare.math.DegreeMath;
import tSquare.math.Point;

//TODO check Vimage if contents are available

public class Sprite {
	private static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	private double[] cachedRotationKeys;
	private BufferedImage image;
	private VolatileImage vImage;
	private String url;
	
	public BufferedImage[] cachedRotationImages;
	public int width = 0;
	public int height = 0;
	public boolean useCachedRotationImages = false;
	public boolean useVolatileImage = false;
	public boolean useAntiAlias = false;
	
	public String getUrl() { return url; }
	
	public Sprite(String url) {
		BufferedImage image = null;
		File f = new File(url);
		try {
			image = ImageIO.read(f);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		if (f.isFile()) {
			this.image = ImageProccess.createCompatiableImage(image);
			this.width = this.image.getWidth(null);
			this.height = this.image.getHeight(null);
			this.url = url;
			Sprite.sprites.put(url, this);
		}
	}
	public Sprite(String url, BufferedImage image) {
		this.image = ImageProccess.createCompatiableImage(image);
		this.width = this.image.getWidth(null);
		this.height = this.image.getHeight(null);
		this.url = url;
		Sprite.sprites.put(url, this);
	}
	
	public static Sprite add(String url) {
		Sprite s = Sprite.sprites.get(url);
		if (s != null)
			return s;
		if (new File(url).isFile())
			return new Sprite(url);
		return null;
	}
	
	public void changeImage(BufferedImage bi) {
		this.image = ImageProccess.createCompatiableImage(bi);
		this.width = bi.getWidth();
		this.height = bi.getHeight();
	}
	
	public void createCachedRotationImages(int numOfRotations) {
		double[] degreeArray = new double[numOfRotations];
		double degrees = 360 / numOfRotations;
		for (int i = 0; i < numOfRotations; i++)
			degreeArray[i] = degrees * i;
		createCachedRotationImages(degreeArray);
	}
	public void createCachedRotationImages(double[] degrees) {
		cachedRotationImages = new BufferedImage[degrees.length + 1];
		cachedRotationKeys = new double[degrees.length + 1];
		System.arraycopy(degrees, 0, cachedRotationKeys, 0, degrees.length);
		cachedRotationKeys[degrees.length] = 360;
		for (int i = 0; i < cachedRotationKeys.length; i++) {
			if (cachedRotationKeys[i] != 360)
				cachedRotationKeys[i] = DegreeMath.simplifyDegrees(cachedRotationKeys[i]);
			AffineTransform t1 = new AffineTransform();
			t1.translate(width/2, height/2);
			t1.rotate(Math.toRadians(-(cachedRotationKeys[i]) + 90), width/2, height/2);
			AffineTransformOp op1 = new AffineTransformOp(t1, AffineTransformOp.TYPE_BILINEAR);
			this.cachedRotationImages[i] = op1.filter(this.image, null);
			
			/*BufferedImage bi = new BufferedI mage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) bi.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			AffineTransform at = new AffineTransform();
			//at.translate(width/2, height/2);
			at.setToTranslation(width/2, height/2);
			g.transform(at);
			at.setToRotation(-(cachedRotationKeys[i]) + 90);
			g.transform(at);
			//at.translate(-width/2, -height/2);
			//at.setToTranslation(-width/2, -height/2);
			//g.transform(at);
			g.drawImage(image, 0, 0, null);
			at.setToTranslation(-width/2, -height/2);
			g.transform(at);
			
			
			
			//at.translate(width/2, height/2);
			//
			//at.rotate(-(cachedRotationKeys[i]) + 90);
			//at.translate(-width/2, -height/2);
			//at.translate(-width/2, -height/2);
			//g.transform(at);
			g.dispose();
			this.cachedRotationImages[i] = bi;*/
		}
	}
	
	public void scale(double scaleX, double scaleY) {
		this.width = (int) (image.getWidth() * scaleX);
		this.height = (int) (image.getHeight() * scaleY);
		this.image = ImageProccess.scale(this.image, scaleX, scaleY);
		if (cachedRotationImages != null) {
			for (int i = 0; i < cachedRotationImages.length; i++)
				cachedRotationImages[i] = ImageProccess.scale(cachedRotationImages[i], scaleX, scaleY);
		}
	}
	
	public void draw(Point p, GameBoard gameBoard) {
		draw(p.intX(), p.intY(), gameBoard);
	}
	public void draw(int x, int y, GameBoard gameBoard) {
		if (useVolatileImage && vImage != null)
			gameBoard.draw(vImage, x, y);
		else
			gameBoard.draw(this.image, x, y);
	}
	public void draw(Point p, double degrees, GameBoard gameBoard) {
		draw(p.intX(), p.intY(), degrees, gameBoard);
	}
	public void draw(int x, int y, double degrees, GameBoard gameBoard) {
		if (useCachedRotationImages && cachedRotationImages != null) {
			if (cachedRotationImages.length == 0) {
				draw(x, y, gameBoard);
				return;
			}
			degrees = DegreeMath.simplifyDegrees(degrees);
			int smallestDifferenceIndex = 0;
			double smallestDifference = Math.abs(degrees - cachedRotationKeys[0]);
			for (int i = 1; i < cachedRotationKeys.length; i++) {
				if (Math.abs(degrees - cachedRotationKeys[i]) < smallestDifference) {
					smallestDifference = Math.abs(degrees - cachedRotationKeys[i]);
					smallestDifferenceIndex = i;
				}
			}
			gameBoard.draw(cachedRotationImages[smallestDifferenceIndex], x - width/2, y - height/2);
		}
		else {
			AffineTransform t = new AffineTransform();
			t.translate(width/2, height/2);
			t.rotate(Math.toRadians(-degrees + 90), width/2, height/2);
			AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BILINEAR);
			gameBoard.draw(op.filter(this.image, null), x - width/2, y - height/2);
		}
	}
	
	public void createVolatileImage() {
			this.vImage = ImageProccess.createVolatileImage(image);
	}
	
	public static Sprite get(String url) {
		return Sprite.sprites.get(url);
	}
	
	public static boolean exists(String url) {
		if (Sprite.sprites.get(url) == null)
			return false;
		return true;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
}