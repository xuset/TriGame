package net.xuset.tSquare.ui;

import net.xuset.tSquare.system.input.mouse.TsMouseEvent;

public class UiSlider extends UiProgressBar {
	public static interface SliderChange { void onChange(double newPosition); }
	
	private SliderChange listener = null;

	public UiSlider() {
		super(100, 15);
	}
	
	public void setSliderListener(SliderChange listener) { this.listener = listener; }
	public SliderChange getSliderListener() { return listener; }

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.pointer.isPressed()) {
			setProgress(x / getWidth());
			if (listener != null)
				listener.onChange(getProgress());
		}
	}

	/*private void drawBar(IGraphics g) {
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
		
	}*/
}
