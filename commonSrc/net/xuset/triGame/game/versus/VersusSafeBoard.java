package net.xuset.triGame.game.versus;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.triGame.game.SafeBoard;


class VersusSafeBoard extends SafeBoard {
	private IRectangleR playableArea;
	private final IRectangleR bigArea;
	
	VersusSafeBoard(IRectangleR bigArea) {
		this.bigArea = bigArea;
		setPlayableArea(bigArea);
	}
	
	void setPlayableArea(IRectangleR playableArea) {
		this.playableArea = playableArea;
	}
	
	@Override
	public void update(int frameDelta) {
		
	}

	@Override
	public void draw(IGraphics g) {
		IRectangleR rect = g.getView();
		g.setColor(TsColor.black);
		int viewX = (int) rect.getX();
		int viewY = (int) rect.getY();
		int viewWidth = (int) rect.getWidth();
		int viewHeight = (int) rect.getHeight();
		
		int leftFill = (int) (bigArea.getX() - viewX);
		if (leftFill > 0)
			g.drawRect(0, 0, leftFill, viewHeight);
		
		int rightFill = (int) ((viewX + viewWidth) - (bigArea.getX() + bigArea.getWidth()));
		if (rightFill > 0)
			g.drawRect((int) (bigArea.getX() + bigArea.getWidth() - viewX), 0, rightFill, viewHeight);
		
		int topFill = (int) (bigArea.getY() - viewY);
		if (topFill > 0)
			g.drawRect(0, 0, viewWidth, topFill);
		
		int bottomFill = (int) ((viewY + viewHeight) - (bigArea.getY() + bigArea.getHeight()));
		if (bottomFill > 0)
			g.drawRect(0, (int) (bigArea.getY() + bigArea.getHeight() - viewY), viewWidth, bottomFill);
	}

	@Override
	public void addVisibilityForEntity(Entity e, double radius) { }

	@Override
	public void removeVisibility(Entity e) { }
	
	@Override
	public boolean insideSafeArea(double x, double y) {
		return playableArea.contains(x, y);
	}

	@Override
	public boolean insideSafeArea(double x, double y, double w, double h) {
		return playableArea.isInside(x, y, w, h);
	}

}
