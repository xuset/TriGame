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
			g.setColor(TsColor.orange);
			g.fillOval(getX(), getY(), buttonRadius * 2, buttonRadius * 2);
		}

		g.setColor(TsColor.gray);
		//g.setColor(TsColor.rgba(64, 64, 64, 128));
		g.fillOval(getX() + 1, getY() + 1, (buttonRadius - 1) * 2, (buttonRadius - 1) * 2);
		g.setColor(TsColor.orange);
		g.drawOval(getX(), getY(), buttonRadius * 2, buttonRadius * 2);
		
		g.setAntiAlias(savedAntiAlias);
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
