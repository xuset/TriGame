package triGame.game.versus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import triGame.game.SafeBoard;

public class VersusSafeBoard extends SafeBoard {
	private Rectangle2D playableArea;
	private final Rectangle2D bigArea;
	
	VersusSafeBoard(Rectangle2D bigArea) {
		this.bigArea = bigArea;
		setPlayableArea(bigArea);
	}
	
	void setPlayableArea(Rectangle2D playableArea) {
		this.playableArea = playableArea;
	}
	
	@Override
	public void performLogic(int frameDelta) {
		
	}

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		g.setColor(Color.black);
		int viewX = (int) rect.getX();
		int viewY = (int) rect.getY();
		int viewWidth = (int) rect.getWidth();
		int viewHeight = (int) rect.getHeight();
		
		int leftFill = (int) (bigArea.getX() - viewX);
		if (leftFill > 0)
			g.fillRect(0, 0, leftFill, viewHeight);
		
		int rightFill = (int) ((viewX + viewWidth) - (bigArea.getX() + bigArea.getWidth()));
		if (rightFill > 0)
			g.fillRect((int) (bigArea.getX() + bigArea.getWidth() - viewX), 0, rightFill, viewHeight);
		
		int topFill = (int) (bigArea.getY() - viewY);
		if (topFill > 0)
			g.fillRect(0, 0, viewWidth, topFill);
		
		int bottomFill = (int) ((viewY + viewHeight) - (bigArea.getY() + bigArea.getHeight()));
		if (bottomFill > 0)
			g.fillRect(0, (int) (bigArea.getY() + bigArea.getHeight() - viewY), viewWidth, bottomFill);
	}

	@Override
	public void addVisibilityForEntity(Entity e, int radius) { }

	@Override
	public void removeVisibility(Entity e) { }
	
	@Override
	public boolean insideSafeArea(int x, int y) {
		return playableArea.contains(x, y);
	}

	@Override
	public boolean insideSafeArea(int x, int y, int w, int h) {
		return playableArea.contains(x, y, w, h);
	}

}
