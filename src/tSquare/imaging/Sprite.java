package tSquare.imaging;


import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import tSquare.game.GameBoard;

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
	
	public void draw(int x, int y, GameBoard gameBoard) {
		gameBoard.draw(image, x, y);
	}
	public void draw(int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, GameBoard gameBoard) {
		gameBoard.draw(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
	}
	public void draw(int x, int y, double degrees, GameBoard gameBoard) {
		AffineTransform t = new AffineTransform();
		t.translate(getWidth()/2, getHeight()/2);
		t.rotate(Math.toRadians(-degrees + 90), getWidth()/2, getHeight()/2);
		AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BILINEAR);
		gameBoard.draw(op.filter((BufferedImage) image, null), x - getWidth()/2, y - getHeight()/2);
	}
	public void draw(int x, int y, AffineTransform trans, GameBoard gameBoard) {
		AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BILINEAR);
		gameBoard.draw(op.filter((BufferedImage) image, null), x, y);
		//TODO may want to create a BufferedImage to output the contents of op.filter so its allocated on the stack and not the heap
	}
	
	/*AffineTransform t1 = new AffineTransform();
	t1.translate(width/2, height/2);
	t1.rotate(Math.toRadians(-(cachedRotationKeys[i]) + 90), width/2, height/2);
	AffineTransformOp op1 = new AffineTransformOp(t1, AffineTransformOp.TYPE_BILINEAR);
	this.cachedRotationImages[i] = op1.filter(this.image, null);
	
	BufferedImage bi = new BufferedI mage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
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