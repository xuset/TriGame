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
		float viewX = (float) rect.getX();
		float viewY = (float) rect.getY();
		float viewWidth = (float) rect.getWidth();
		float viewHeight = (float) rect.getHeight();
		
		float leftFill = (float) (bigArea.getX() - viewX);
		if (leftFill > 0)
			g.fillRect(viewX, viewY, leftFill, viewHeight);
		
		float rightFill = (float) ((viewX + viewWidth) - (bigArea.getX() + bigArea.getWidth()));
		if (rightFill > 0)
			g.fillRect((float) (bigArea.getX() + bigArea.getWidth()), viewY, rightFill + 0.1f, viewHeight);
		
		float topFill = (float) (bigArea.getY() - viewY);
		if (topFill > 0)
			g.fillRect(viewX, viewY, viewWidth, topFill);
		
		float bottomFill = (float) ((viewY + viewHeight) - (bigArea.getY() + bigArea.getHeight()));
		if (bottomFill > 0)
			g.fillRect(viewX, (float) (bigArea.getY() + bigArea.getHeight()), viewWidth, bottomFill + 0.1f);
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
