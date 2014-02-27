package net.xuset.triGame.game.input;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.guns.GunType;
import net.xuset.triGame.game.ui.gameInput.IGunInput;

public class TouchGunInput implements IGunInput, GameDrawable {

	private final int buttonRadius = 80;
	private final IPointW buttonCenter = new Point(0, 0);
	private boolean shootRequested = false;
	private long timeShootRequested = 0l;
	
	public TouchGunInput(IMouseListener mouse) {
		mouse.watch(new InputListener());
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
		return GunType.PISTOL;
	}

	@Override
	public void draw(IGraphics g) {
		int gutter = 50 + buttonRadius;
		IRectangleR view = g.getView();
		int x = (int) (view.getWidth() - gutter);
		int y = (int) (view.getHeight() - gutter);
		
		buttonCenter.setTo(x, y);
		x -= buttonRadius;
		y -= buttonRadius;
		
		if (System.currentTimeMillis() - timeShootRequested < 50) {
			//highlight the shoot button orange for 50ms after the button was pressed
			g.setColor(TsColor.orange);
			g.fillOval(x, y, buttonRadius * 2, buttonRadius * 2);
		}
		
		g.setColor(TsColor.rgba(64, 64, 64, 128));
		g.fillOval(x + 1, y + 1, (buttonRadius - 1) * 2, (buttonRadius - 1) * 2);
	}
	
	private class InputListener implements Observer.Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			double dist = buttonCenter.calcDistance(t.x, t.y);
			if (t.action == MouseAction.MOVE)
				return;
			if (dist < buttonRadius) {
				if (t.action == MouseAction.PRESS || t.action == MouseAction.DRAG) {
					timeShootRequested = System.currentTimeMillis();
					shootRequested = true;
				}
			}
		}
		
	}

}
