package net.xuset.tSquare.imaging;

import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.math.rect.IRectangleW;
import net.xuset.tSquare.math.rect.Rectangle;



public class WindowGraphics implements IGraphics{
	
	private IRectangleR view;
	private IRectangleW subView = new Rectangle();
	private IGraphics g;
	
	public WindowGraphics() {
		
	}
	
	public WindowGraphics(IRectangleR view, IGraphics g) {
		reset(view, g);
	}
	
	public void reset(IRectangleR newView, IGraphics newG) {
		view = newView;
		g = newG;
		subView.setFrame(0, 0, newView.getWidth(), newView.getHeight());
	}
 
	private float transformX(float x) {
		return (float) (x + view.getX());
	}

	private float transformY(float y) {
		return (float) (y + view.getY());
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
	public void drawRoundedRect(float x, float y, float w, float h, float rx, float ry) {
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
	public void fillRoundedRect(float x, float y, float w, float h, float rx, float ry) {
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
		return subView;
	}

	@Override
	public float getWidthUnits(IImage image) {
		return g.getWidthUnits(image);
	}

	@Override
	public float getHeightUnits(IImage image) {
		return g.getHeightUnits(image);
	}

	@Override
	public void setErase(boolean erase) {
		g.setErase(erase);
	}
	
}
