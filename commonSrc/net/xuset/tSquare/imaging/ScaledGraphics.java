package net.xuset.tSquare.imaging;

import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.math.rect.Rectangle;

public class ScaledGraphics implements IGraphics {
	private final IGraphics g;
	private final float scale;
	private final IRectangleR view;
	private boolean scaleImages = false;
	
	public ScaledGraphics(IGraphics g, float scale) {
		this(g, scale, false);
	}
	
	public ScaledGraphics(IGraphics g, float scale, boolean scaleImages) {
		this.g = g;
		this.scale = scale;
		this.scaleImages = scaleImages;
		view = new Rectangle(
				g.getView().getX() / scale,
				g.getView().getY() / scale,
				g.getView().getWidth() / scale,
				g.getView().getHeight() / scale);
	}
	
	public boolean isScalingImages() { return scaleImages; }
	public void setScaleImages(boolean scaleImages) { this.scaleImages = scaleImages; }

	@Override
	public void drawImage(IImage image, float dx, float dy, float dw, float dh, float sx,
			float sy, float sw, float sh) {

		g.drawImage(
				image,
				(dx * scale),
				(dy * scale),
				(dw * scale),
				(dh * scale),
				sx, sy, sw, sh);
	}

	@Override
	public void drawImage(IImage image, float x, float y) {
		if (scaleImages) {
			drawImage(image,
					x, y, image.getWidth(), image.getHeight(),
					0, 0, image.getWidth(), image.getHeight());
		} else {
			g.drawImage(image,
					(x * scale), (y * scale));
		}
	}

	@Override
	public void drawImageRotate(IImage image, float x, float y, double radians) {
		g.drawImageRotate(
				image,
				(x * scale),
				(y * scale),
				radians);
	}

	@Override
	public void drawRect(float x, float y, float w, float h) {
		g.drawRect(
				(x * scale),
				(y * scale),
				(w * scale),
				(h * scale));
	}

	@Override
	public void drawRoundedRect(float x, float y, float w, float h, float rx, float ry) {
		g.drawRoundedRect(
				(x * scale),
				(y * scale),
				(w * scale),
				(h * scale),
				rx, ry);
	}

	@Override
	public void drawOval(float x, float y, float w, float h) {
		g.drawOval(
				(x * scale),
				(y * scale),
				(w * scale),
				(h * scale));

	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		g.drawLine(
				(x1 * scale),
				(y1 * scale),
				(x2 * scale),
				(y2 * scale));

	}

	@Override
	public void fillRect(float x, float y, float w, float h) {
		g.fillRect(
				(x * scale),
				(y * scale),
				(w * scale),
				(h * scale));

	}

	@Override
	public void fillRoundedRect(float x, float y, float w, float h, float rx, float ry) {
		g.fillRoundedRect(
				(x * scale),
				(y * scale),
				(w * scale),
				(h * scale),
				rx * scale, ry * scale);

	}

	@Override
	public void fillOval(float x, float y, float w, float h) {
		g.fillOval(
				(x * scale),
				(y * scale),
				(w * scale),
				(h * scale));

	}

	@Override
	public void fillTriangle(float x, float y, float w, float h) {
		g.fillTriangle(
				(x * scale),
				(y * scale),
				(w * scale),
				(h * scale));
	}

	@Override
	public void setFont(IFont font) {
		g.setFont(new TsFont(
				font.getName(),
				(int) (font.getSize() * scale),
				font.getTypeFace()));
	}

	@Override
	public void drawText(float x, float y, String text) {
		g.drawText(
				(x * scale),
				(y * scale),
				text);
	}

	@Override
	public float getTextWidth(String text) {
		return g.getTextWidth(text) / scale;
	}
	
	@Override
	public float getTextHeight() {
		return g.getTextHeight() / scale;
	}

	@Override
	public void setColor(int color) {
		g.setColor(color);
	}

	@Override
	public void setColor(TsColor color) {
		g.setColor(color);
	}

	@Override
	public void setColor(int red, int green, int blue) {
		g.setColor(red, green, blue);
	}

	@Override
	public void setAntiAlias(boolean antiAlias) {
		g.setAntiAlias(antiAlias);
	}
	
	@Override
	public boolean isAntiAliasOn() {
		return g.isAntiAliasOn();
	}

	@Override
	public void clear() {
		g.clear();
	}

	@Override
	public void dispose() {
		g.dispose();
	}

	@Override
	public IRectangleR getView() {
		return view;
	}

	@Override
	public float getWidthUnits(IImage image) {
		return image.getHeight() / scale;
	}

	@Override
	public float getHeightUnits(IImage image) {
		return image.getHeight() / scale;
	}

	@Override
	public void setErase(boolean erase) {
		g.setErase(erase);
	}

}
