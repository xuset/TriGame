package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.MousePointer;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.triGame.game.guns.GunType;

public class UiShootInput extends UiComponent implements IGunInput{
	private static final int initSize = 125;
	
	private final RingDrawer ringDrawer = new RingDrawer(5, TsColor.orange);
	private final IPointW buttonCenter = new Point();
	private int buttonRadius = 0;
	private boolean shootRequested = false;
	private MousePointer pointer = null;
	
	public UiShootInput() {
		super(0, 0, initSize, initSize);
	}
	
	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.action == MouseAction.PRESS) {
			pointer = e.pointer;
			shootRequested = true;
		}
	}

	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;
		
		if (pointer == null || !pointer.isPressed())
			shootRequested = false;
		
		boolean savedAntiAlias = g.isAntiAliasOn();
		g.setAntiAlias(true);
		
		buttonRadius = (int) (0.9 * getWidth() / 2);
		buttonCenter.setTo(getX() + buttonRadius, getY() + buttonRadius);
		
		if (shootRequested) {
			drawPressed(g);
		} else {
			ringDrawer.setCenterAndRadius(buttonCenter, buttonRadius);
			ringDrawer.draw(g);
		}
		
		g.setAntiAlias(savedAntiAlias);
	}
	
	private void drawPressed(IGraphics g) {
		final int gutter = 5;
		g.setColor(TsColor.orange);
		g.fillOval(getX(), getY(), buttonRadius * 2, buttonRadius * 2);
		g.setColor(TsColor.darkGray);
		g.fillOval(getX() + gutter, getY() + gutter, (buttonRadius - gutter) * 2, (buttonRadius - gutter) * 2);
	}

	@Override
	public boolean shootRequested() {
		return shootRequested;
	}

	@Override
	public boolean changeGunRequested() {
		return false;
	}

	@Override
	public GunType getCurrentGunType() {
		return null;
	}
}
