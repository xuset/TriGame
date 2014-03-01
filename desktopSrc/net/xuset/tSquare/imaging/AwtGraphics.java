package net.xuset.tSquare.imaging;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.math.rect.Rectangle;

public class AwtGraphics implements IGraphics {
	public final Graphics g;
	private final IRectangleR rect;
	private boolean antiAlias = false;
	
	public AwtGraphics(Graphics g, float width, float height) {
		this.g = g;
		rect = new Rectangle(0, 0, width, height);
	}

	@Override
	public void drawImage(IImage image, float dx, float dy, float dw, float dh,
			float sx, float sy, float sw, float sh) {
		g.drawImage(
				((AwtImage) image).bufferedImg,
				(int) dx, (int) dy, (int) (dx + dw), (int) (dy + dh),
				(int) sx, (int) sy, (int) (sx + sw), (int) (sy + sh),
				null);
	}

	@Override
	public void drawImage(IImage image, float x, float y) {
		g.drawImage(
				((AwtImage) image).bufferedImg,
				(int) x, (int) y,
				null);
	}

	@Override
	public void drawImageRotate(IImage image, float x, float y, double radians) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform savedAt = g2d.getTransform();
		
		AffineTransform at = new AffineTransform();
		at.setToRotation(-radians + Math.PI/2, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
		g2d.setTransform(at);
		
		g2d.drawImage(
				((AwtImage) image).bufferedImg,
				(int) x, (int) y,
				null);
		
		g2d.setTransform(savedAt);
	}

	@Override
	public void drawRect(float x, float y, float w, float h) {
		g.drawRect((int) x, (int) y, (int) w, (int) h);
	}

	@Override
	public void drawOval(float x, float y, float w, float h) {
		g.drawOval((int) x, (int) y, (int) w, (int) h);
;	}

	@Override
	public void fillRect(float x, float y, float w, float h) {
		g.fillRect((int) x, (int) y, (int) w, (int) h);
		
	}

	@Override
	public void fillOval(float x, float y, float w, float h) {
		g.fillOval((int) x, (int) y, (int) w, (int) h);
	}
	
	@Override
	public void setColor(TsColor c) {
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()));
	}

	@Override
	public void setColor(int color) {
		g.setColor(new Color(color, true));
	}

	@Override
	public void setColor(int red, int green, int blue) {
		g.setColor(new Color(red, green, blue));
	}


	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
	
	@Override
	public void setFont(IFont f) {
		g.setFont(new Font(f.getName(), translateTsTypeFace(f.getTypeFace()), f.getSize()));
				
	}

	//TODO eventually remove the antiAlias. The caller of the function should be able
	//to choose whether antiAlias should be used
	@Override
	public void drawText(float x, float y, String text) {
		Graphics2D g2d = (Graphics2D) g;
		Object savedState = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawString(text, (int) x, (int) y);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, savedState);
	}

	@Override
	public float getTextWidth(String text) {
		return g.getFontMetrics().stringWidth(text);
	}
	
	@Override
	public float getTextHeight() {
		return g.getFontMetrics().getHeight();
	}

	@Override
	public IRectangleR getView() {
		return rect;
	}

	@Override
	public void clear() {
		g.setColor(Color.black);
		g.fillRect(0, 0, (int) rect.getWidth(), (int) rect.getHeight());
	}
	
	@Override
	public void dispose() {
		g.dispose();
	}
	
	private int translateTsTypeFace(TsTypeFace tf) {
		switch(tf) {
		case BOLD:
			return Font.BOLD;
		case ITALICS:
			return Font.ITALIC;
		default:
			return Font.PLAIN;
		}
	}

	@Override
	public void drawRoundedRect(float x, float y, float w, float h, int rx, int ry) {
		g.drawRoundRect((int) x, (int) y, (int) w, (int) h, rx, ry);
	}

	@Override
	public void fillRoundedRect(float x, float y, float w, float h, int rx, int ry) {
		g.fillRoundRect((int) x, (int) y, (int) w, (int) h, rx, ry);
	}

	@Override
	public boolean isAntiAliasOn() {
		return antiAlias;
	}

	@Override
	public void setAntiAlias(boolean antiAlias) {
		this.antiAlias = antiAlias;
		Object value = antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF;
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, value);
	}

	@Override
	public void fillTriangle(float x, float y, float w, float h) {
		Polygon p = new Polygon();
		
		p.addPoint(
				(int) (x + w / 2),
				(int) y);
		p.addPoint(
				(int) x,
				(int) (y + h));
		p.addPoint(
				(int) (x + w),
				(int) (y + h));
		
		g.fillPolygon(p);
	}

	@Override
	public float getWidthUnits(IImage image) {
		return image.getWidth();
	}

	@Override
	public float getHeightUnits(IImage image) {
		return image.getHeight();
	}

}
