package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;

public class UiCheckBox extends UiComponent{
	public static interface UiCheckChange { void onChange(boolean newVal); }
	
	private final float gutter = 4.0f;
	
	private boolean isChecked = false;
	private UiCheckChange listener = null;
	
	public UiCheckBox() {
		super(0, 0, 25, 25);
		setForeground(new TsColor(0, 220, 220));
		setBackground(TsColor.darkGray);
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
		g.setColor(getBackground().shade(50));
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		if (isChecked())
			g.setColor(getForeground());
		else
			g.setColor(getBackground());
		g.fillRect(getX() + gutter, getY() + gutter,
				getWidth() - 2 * gutter, getHeight() - 2 * gutter);
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

	/*private void drawCheck(IGraphics g) {
		g.setColor(TsColor.black);
		g.drawLine(getX() + gutter, getY() + gutter,
				getX() + getWidth() - gutter, getY() + getHeight() - gutter);
		g.drawLine(getX() + getWidth() - gutter, getY() + gutter,
				getX() + gutter, getY() + getHeight() - gutter);
	}*/

}
