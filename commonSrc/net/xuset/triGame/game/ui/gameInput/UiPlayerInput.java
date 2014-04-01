package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.MousePointer;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiComponent;

public class UiPlayerInput extends UiComponent implements IPlayerInput{
	
	private final static float initSize = 250;
	private final double movementThreshold = 0.1;
	
	private final IPointW uiMoveCenter = new Point(0, 0);
	private int uiMoveRadius = 0;
	
	private double moveAngle = Math.PI/2;
	private boolean isMoving = false;
	private MousePointer pointer = null;
	
	public boolean isBeingTouched() { return isMoving; }
	
	public UiPlayerInput() {
		super(0, 0, initSize, initSize);
	}

	@Override
	public double getMoveCoEfficient() {
		if (!isMoving)
			return 0.0;
		
		double dist = uiMoveCenter.calcDistance(pointer.getX(), pointer.getY());
		dist /= initSize / 2;
		dist *= 3;
		
		if (dist < movementThreshold)
			dist = 0.0;
		else if (dist > 1.0)
			dist = 1.0;
		return dist;
	}

	@Override
	public double getMoveAngle() {
		return moveAngle;
	}
	
	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.action == MouseAction.PRESS) {
			double dist = uiMoveCenter.calcDistance(e.x, e.y);
			if (dist < uiMoveRadius) {
				isMoving = true;
				pointer = e.pointer;
			}
		}
	}
	
	private boolean useDiagonal(double angle) {
		final double minDiagonalRange = Math.PI / 10; // must be <= PI / 8
		angle += Math.signum(angle) * Math.PI / 4;
		double range = angle % (Math.PI / 2);
		return Math.abs(range) <= minDiagonalRange;
	}

	private double roundAngle(double angle) {
		double denominator = useDiagonal(angle) ? 4 : 2;
		angle += Math.signum(angle) * Math.PI / (denominator * 2);
		angle = (Math.PI / denominator) * ( (int) (angle / (Math.PI / denominator) ) );
		return angle;
	}
	
	private boolean isMouseActive() {
		if (pointer == null || !pointer.isPressed()) {
			return false;
		}
		
		return isMoving;
	}
	
	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;
		super.draw(g);
		
		uiMoveRadius = (int) (getWidth() / 2.0 * 0.9);
		uiMoveCenter.setTo(getX() + getWidth() / 2, getY() + getHeight() / 2);
		isMoving = isMouseActive();
		if (isMoving)
			moveAngle = roundAngle(uiMoveCenter.calcAngle(pointer.getX(), pointer.getY()));
		
		boolean isAntiAlias = g.isAntiAliasOn();
		g.setAntiAlias(true);
		drawUiMove(g);
		drawUiFinger(g);
		g.setAntiAlias(isAntiAlias);
	}

	private void drawUiMove(IGraphics g) {
		int x = (int) (uiMoveCenter.getX() - uiMoveRadius);
		int y = (int) (uiMoveCenter.getY() - uiMoveRadius);
		int diam = uiMoveRadius * 2;
		
		g.setColor(TsColor.rgba(64, 64, 64, 128));
		g.fillOval(x, y, diam, diam);
		g.setColor(TsColor.white);
		g.drawOval(x, y, diam, diam);
	}
	
	private void drawUiFinger(IGraphics g) {
		double moveCoEfficient = getMoveCoEfficient();
		double fingerDist = moveCoEfficient * uiMoveRadius;
		int x = (int) (uiMoveCenter.getX() + Math.cos(moveAngle) * fingerDist);
		int y = (int) (uiMoveCenter.getY() - Math.sin(moveAngle) * fingerDist);
		
		int radius = uiMoveRadius / 4;
		x -= radius;
		y -= radius;
		
		if (moveCoEfficient > movementThreshold) {
			g.setColor(TsColor.white);
			g.fillOval(x, y, radius * 2, radius * 2);
		}
		
		g.setColor(TsColor.darkGray);
		g.fillOval(x + 2, y + 2, (radius - 2) * 2, (radius - 2) * 2);
		
	}
}
