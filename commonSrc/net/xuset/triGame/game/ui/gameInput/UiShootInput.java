package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.triGame.game.guns.GunType;

public class UiShootInput extends UiComponent implements IGunInput{
	private static final int initSize = 125;
	
	private final IPointW buttonCenter = new Point();
	private int buttonRadius = 0;
	private long timeShootRequested = 0;
	private boolean shootRequested = false;
	
	public UiShootInput() {
		super(0, 0, initSize, initSize);
	}
	
	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.action == MouseAction.PRESS || e.action == MouseAction.DRAG) {
			timeShootRequested = System.currentTimeMillis();
			shootRequested = true;
		}
	}

	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;
		
		boolean savedAntiAlias = g.isAntiAliasOn();
		g.setAntiAlias(true);
		
		buttonRadius = (int) (0.9 * getWidth() / 2);
		buttonCenter.setTo(getX() + buttonRadius, getY() + buttonRadius);
		
		if (System.currentTimeMillis() - timeShootRequested < 50) {
			//highlight the shoot button orange for 50ms after the button was pressed
			g.setColor(TsColor.orange);
			g.fillOval(getX(), getY(), buttonRadius * 2, buttonRadius * 2);
		}
		
		//g.setColor(TsColor.rgba(64, 64, 64, 128));
		g.setColor(TsColor.lightGray);
		g.fillOval(getX() + 1, getY() + 1, (buttonRadius - 1) * 2, (buttonRadius - 1) * 2);
		g.setColor(TsColor.orange);
		g.drawOval(getX(), getY(), buttonRadius * 2, buttonRadius * 2);
		
		g.setAntiAlias(savedAntiAlias);
	}

	@Override
	public boolean shootRequested() {
		if (shootRequested) {
			shootRequested = false;
			return true;
		}
		return false;
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
