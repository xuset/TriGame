package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;

public class UiDivider extends UiComponent {

	public UiDivider() {
		super(0, 0, 200, 5);
		setForeground(TsColor.lightGray);
		setBackground(TsColor.darkGray);
	}
	
	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;
		super.draw(g);
		
		final float gutter = 1.0f;
		
		g.setColor(getBackground());
		g.fillRoundedRect(getX(), getY(),
				getWidth(), getHeight(),
				5, 5);
		
		g.setColor(getForeground());
		g.fillRoundedRect(getX() + gutter, getY() + gutter,
				getWidth() - 2 * gutter, getHeight() - 2 * gutter,
				5, 5);
	}

}
