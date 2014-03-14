package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;

public class UiSlider extends UiComponent {
	public static interface SliderChange { void onChange(double newPosition); }
	
	private double position = 0.0;
	private SliderChange listener = null;

	public UiSlider() {
		super(0, 0, 100, 10);
		setForeground(new TsColor(0, 220, 220));
	}
	
	public void setSliderListener(SliderChange listener) { this.listener = listener; }
	public SliderChange getSliderListener() { return listener; }
	
	public double getPosition() { return position; }
	public void setPosition(double position) {
		if (position > 1.0 || position < 0.0)
			throw new IllegalArgumentException("Position must between 0.0 and 1.0 inclusive");
		this.position = position;
	}
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
		if (!isVisible())
			return;
		drawBar(g);
		drawMarker(g);
	}

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.action == MouseAction.PRESS || e.action == MouseAction.DRAG) {
			position = x / getWidth();
			if (listener != null)
				listener.onChange(position);
		}
	}

	private void drawBar(IGraphics g) {
		final float gutter = 1.0f;
		
		g.setColor(getBackground().darker());
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		g.setColor(getBackground());
		g.fillRect(getX() + gutter, getY() + gutter,
				getWidth() - 2 * gutter, getHeight() - 2 * gutter);
		
		g.setColor(getForeground());
		float w = (float) ((getWidth() - 2 * gutter) * position);
		g.fillRect(getX() + gutter, getY() + gutter, w, getHeight() - 2 * gutter);
	}
	
	private void drawMarker(IGraphics g) {
		final float markerW = 5.0f;
		final float markerH = getHeight();
		
		float markerX = (float) (getX() + getWidth() * position - markerW / 2);
		float markerY = getY();
		
		float middleX = markerX + markerW / 2;
		float middleY = markerY + markerH;
		
		g.setColor(getForeground().lighter());
		g.drawLine(markerX, markerY, middleX, middleY);
		g.drawLine(markerX + markerW, markerY, middleX, middleY);
		
	}
}
