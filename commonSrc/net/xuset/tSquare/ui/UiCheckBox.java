package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;

public class UiCheckBox extends UiComponent{
	public static interface UiCheckChange { void onChange(boolean newVal); }
	
	private final float gutter = 2.0f;
	
	private boolean isChecked = false;
	private UiCheckChange listener = null;
	
	public UiCheckBox() {
		super(0, 0, 10, 10);
	}
	
	public boolean isChecked() { return isChecked; }
	public void setChecked(boolean isChecked) { this.isChecked = isChecked; }
	
	public void setCheckChange(UiCheckChange listener) { this.listener = listener; }
	public UiCheckChange getCheckChange() { return listener; }
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
		if (!isVisible())
			return;
		g.setColor(getBackground().shade(-30));
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		g.setColor(getBackground());
		g.fillRect(getX() + gutter, getY() + gutter,
				getWidth() - 2 * gutter, getHeight() - 2 * gutter);
		
		if (isChecked())
			drawCheck(g);
	}
	
	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		if (e.action == MouseAction.PRESS) {
			isChecked = !isChecked;
			if (listener != null)
				listener.onChange(isChecked);
		}
	}

	private void drawCheck(IGraphics g) {
		g.setColor(getForeground());
		g.drawLine(getX() + gutter, getY() + gutter,
				getX() + getWidth() - gutter, getY() + getHeight() - gutter);
		g.drawLine(getX() + getWidth() - gutter, getY() + gutter,
				getX() + gutter, getY() + getHeight() - gutter);
	}

}
