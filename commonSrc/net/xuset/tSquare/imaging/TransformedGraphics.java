package net.xuset.tSquare.imaging;

import net.xuset.tSquare.math.rect.IRectangleR;

public class TransformedGraphics implements IGraphics{
	private final IGraphics g;
	private final IRectangleR view;
	
	public TransformedGraphics(IGraphics g) {
		this(g, g.getView());
	}
	
	public TransformedGraphics(IGraphics g, IRectangleR view) {
		this.g = g;
		this.view = view;
	}

	@Override
	public void drawImage(IImage image, float dx, float dy, float dw, float dh, float sx,
			float sy, float sw, float sh) {
		
		g.drawImage(
				image,
				transformX(dx),
				transformY(dy),
				dw, dh, sx, sy, sw, sh);
		
	}

	@Override
	public void drawImage(IImage image, float x, float y) {
		g.drawImage(
				image,
				transformX(x),
				transformY(y));
		
	}

	@Override
	public void drawImageRotate(IImage image, float x, float y, double radians) {
		g.drawImageRotate(image,
				transformX(x),
				transformY(y),
				radians);
		
	}

	@Override
	public void drawRect(float x, float y, float w, float h) {
		g.drawRect(
				transformX(x),
				transformY(y),
				w, h);
		
	}

	@Override
	public void drawRoundedRect(float x, float y, float w, float h, int rx, int ry) {
		g.drawRoundedRect(
				transformX(x),
				transformY(y),
				w, h, rx, ry);
		
	}

	@Override
	public void drawOval(float x, float y, float w, float h) {
		g.drawOval(
				transformX(x),
				transformY(y),
				w, h);
	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		g.drawLine(
				transformX(x1),
				transformY(y1),
				transformX(x2),
				transformY(y2));
	}

	@Override
	public void fillRect(float x, float y, float w, float h) {
		g.fillRect(
				transformX(x),
				transformY(y),
				w, h);
	}

	@Override
	public void fillRoundedRect(float x, float y, float w, float h, int rx, int ry) {
		g.fillRoundedRect(
				transformX(x),
				transformY(y),
				w, h, rx, ry);
	}

	@Override
	public void fillOval(float x, float y, float w, float h) {
		g.fillOval(
				transformX(x),
				transformY(y),
				w, h);
	}

	@Override
	public void fillTriangle(float x, float y, float w, float h) {
		g.fillTriangle(
				transformX(x),
				transformY(y),
				w, h);
	}

	@Override
	public void setFont(IFont font) {
		g.setFont(font);
	}

	@Override
	public void drawText(float x, float y, String text) {
		g.drawText(
				transformX(x),
				transformY(y),
				text);
	}

	@Override
	public float getTextWidth(String text) {
		return g.getTextWidth(text);
	}
	
	@Override
	public float getTextHeight() {
		return g.getTextHeight();
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
	
	protected float transformX(float x) {
		return (float) (x - getView().getX());
	}
	
	protected float transformY(float y) {
		return (float) (y - getView().getY());
	}
}
