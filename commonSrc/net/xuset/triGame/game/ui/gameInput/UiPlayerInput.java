package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiComponent;

public class UiPlayerInput extends UiComponent implements IPlayerInput{
	private static final float initSize = 250;

	private final double movementThreshold = 0.05;
	private final IPointW uiMoveCenter = new Point();
	private int uiMoveRadius = 0;
	private double moveAngle = Math.PI/2;
	private double moveCoEfficient = 0.0;
	private boolean isMoving = false;
	private long lastMoveTime = 0l;
	
	public UiPlayerInput() {
		super(0, 0, initSize, initSize);
	}
	
	public boolean isBeingTouched() { return isMoving; }
	
	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);

		double distance = uiMoveCenter.calcDistance(e.x, e.y);
		
		switch(e.action) {
		case PRESS:
			if (distance < uiMoveRadius)
				isMoving = true;
			break;
		case RELEASE:
			isMoving = false;
			moveCoEfficient = 0;
			break;
		case DRAG:
			if (isMoving) {
				moveAngle = roundAngle(uiMoveCenter.calcAngle(e.x, e.y));
				
				double temp = distance / uiMoveRadius;
				
				if (temp < movementThreshold)
					temp = 0.0;
				temp *= 2;
				if (temp > 1.0)
					temp = 1.0;
				
				moveCoEfficient = temp;
				lastMoveTime = System.currentTimeMillis();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;
		
		super.draw(g);
		
		uiMoveRadius = (int) (getWidth() / 2.0 * 0.9);
		uiMoveCenter.setTo(getX() + getWidth() / 2, getY() + getHeight() / 2);
		
		boolean antiAlias = g.isAntiAliasOn();
		g.setAntiAlias(true);
		drawUiMove(g);
		drawUiFinger(g);
		g.setAntiAlias(antiAlias);
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
	
	private double roundAngle(double angle) {
		angle += Math.signum(angle) * Math.PI / 8;
		angle = (Math.PI / 4) * ( (int) (angle / (Math.PI / 4) ) );
		return angle;
	}

	@Override
	public double getMoveCoEfficient() {
		if (lastMoveTime + 250 < System.currentTimeMillis())
			moveCoEfficient = 0;
		return moveCoEfficient;
	}

	@Override
	public double getMoveAngle() {
		return moveAngle;
	}

}
