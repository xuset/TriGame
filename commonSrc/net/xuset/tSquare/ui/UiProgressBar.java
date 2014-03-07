package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;

public class UiProgressBar extends UiComponent {
	private final float gutter = 2.0f;
	private double progress = 0.0;
	private Axis orientation = Axis.X_AXIS;

	public UiProgressBar() {
		super(0, 0, 60, 10);
		setBackground(TsColor.darkGray);
		setForeground(TsColor.cyan);
	}

	public double getProgress() { return progress; }
	
	public void setProgress(double progress) {
		if (progress < 0.0 || progress > 1.0)
			throw new IllegalArgumentException("progress must be between 0 and 1 inclusively");
		this.progress = progress;
	}
	
	public Axis getOrientation() { return orientation; }
	public void setOrientation(Axis orientation) { this.orientation = orientation; }
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
		if (!isVisible())
			return;
		
		g.setColor(getBackground().darker());
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		g.setColor(getBackground().lighter());
		g.fillRect(getX(), getY(), getWidth() - gutter, getHeight() - gutter);
		
		drawProgress(g);
	}
	
	private void drawProgress(IGraphics g) {
		float w, h;
		
		if (orientation == Axis.X_AXIS) {
			w = (float) (progress * (getWidth() - 2 * gutter));
			h = getHeight() - 2 * gutter;
		} else {
			w = getWidth() - 2 * gutter;
			h = (float) (progress * (getHeight() - 2 * gutter));
		}
		
		g.setColor(getForeground());
		g.fillRect(getX() + gutter, getY() + gutter, w, h);
	}
}
