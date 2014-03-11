package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleW;
import net.xuset.tSquare.math.rect.Rectangle;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

public abstract class UiComponent {
	private final Observer<TsMouseEvent> mouseObserver = new Observer<TsMouseEvent>();
	private final IRectangleW rect = new Rectangle();
	private TsColor foreground = TsColor.black;
	private TsColor background = TsColor.white;
	private boolean isOpaque = false;
	private UiBorder border = new UiLineBorder();
	private boolean isVisible = true;
	
	public UiComponent(float x, float y, float w, float h) {
		rect.setFrame(x, y, w, h);
	}
	
	public float getX() { return (float) rect.getX(); }
	public float getY() { return (float) rect.getY(); }
	public float getWidth() { return (float) rect.getWidth(); }
	public float getHeight() { return (float) rect.getHeight(); }
	
	public TsColor getForeground() { return foreground; }
	public TsColor getBackground() { return background; }
	public void setForeground(TsColor c) { foreground = c; }
	public void setBackground(TsColor c) { background = c; }
	
	public void setVisibile(boolean isVisible) { this.isVisible = isVisible; }
	public boolean isVisible() { return isVisible; }
	
	public UiBorder getBorder() { return border; }
	public void setBorder(UiBorder border) { this.border = border; }
	
	public void setOpaque(boolean isOpaque) { this.isOpaque = isOpaque; }
	public boolean isOpaque() { return isOpaque; }
	
	public void addMouseListener(Change<TsMouseEvent> mouseListener) {
		mouseObserver.watch(mouseListener);
	}
	
	public void setSize(float w, float h) {
			rect.setDimensions(w, h);
	}
	
	public void draw(IGraphics g) {
		if (!isVisible())
			return;
		
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		
		if (getBorder().isVisible())
			getBorder().draw(g, getX(), getY(), getWidth(), getHeight());
	}
	
	public void setLocation(float x, float y) {
		rect.set(x, y);
	}
	
	public boolean contains(float x, float y) {
		return (x >= getX() && x <= getX() + getWidth() &&
				y >= getY() && y <= getY() + getHeight());
	}
	
	@SuppressWarnings("unused")
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		mouseObserver.notifyWatchers(e);
	}
}
