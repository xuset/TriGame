package net.xuset.triGame.game.input;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.ui.gameInput.IPlayerInput;

public class TouchPlayerInput implements IPlayerInput, GameDrawable {
	private final double movementThreshold = 0.05;
	
	private final IPointW uiMoveCenter = new Point(0, 0);
	private int uiMoveRadius = 200;
	
	private double moveAngle = Math.PI/2;
	private double moveCoEfficient = 0.0;
	private boolean isMoving = false;
	
	public TouchPlayerInput(IMouseListener mouse) {
		mouse.watch(new InputListener());
	}

	@Override
	public double getMoveCoEfficient() {
		return moveCoEfficient;
	}

	@Override
	public double getMoveAngle() {
		return moveAngle;
	}
	
	private class InputListener implements Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent e) {
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
					else
						temp = 1.0;
					
					moveCoEfficient = temp;
				}
				break;
			default:
				break;
			}
		}
	}
	
	private double roundAngle(double angle) {
		angle += Math.signum(angle) * Math.PI / 8;
		angle = (Math.PI / 4) * ( (int) (angle / (Math.PI / 4) ) );
		return angle;
	}
	
	

	
	
	@Override
	public void draw(IGraphics g) {
		IRectangleR view = g.getView();
		int gutter = 50 + uiMoveRadius;
		uiMoveCenter.setTo(gutter, view.getHeight() - gutter);
		
		drawUiMove(g);
		drawUiFinger(g);
	}

	private void drawUiMove(IGraphics g) {
		int x = (int) (uiMoveCenter.getX() - uiMoveRadius);
		int y = (int) (uiMoveCenter.getY() - uiMoveRadius);
		int diam = uiMoveRadius * 2;
		
		g.setColor(TsColor.rgba(64, 64, 64, 128));
		g.fillOval(x, y, diam, diam);
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

}
