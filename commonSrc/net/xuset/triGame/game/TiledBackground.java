package net.xuset.triGame.game;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleR;


public class TiledBackground implements GameDrawable{
	private double drawX = 0;
	private double drawY = 0;
	
	private Entity centerTo;
	
	TiledBackground() {
		
	}
	
	public void setCenter(Entity e) {
		centerTo = e;
	}
	
	private void positionBackground(IRectangleR rect) {
		double centerX = 0;
		double centerY = 0;
		if (centerTo != null) {
			centerX = centerTo.getCenterX();
			centerY = centerTo.getCenterY();
		}
		
		double pivotX = centerX - rect.getWidth()/2.0;
		double pivotY = centerY - rect.getHeight()/2.0;

		drawX = rect.getX() + (-1 * (pivotX - ((int) (pivotX))));
		drawY = rect.getY() + (-1 * (pivotY - ((int) (pivotY))));
	}
	
	@Override
	public void draw(IGraphics g) {
		positionBackground(g.getView());
		
		float gutter = 5/50.0f;
		double widthBlocks = g.getView().getWidth() + 1;
		double heightBlocks = g.getView().getHeight() + 1;
		
		g.setColor(TsColor.rgb(220, 220, 220));
		for (double x = drawX; x < drawX + widthBlocks; x += 1) {
			for (double y = drawY; y < drawY + heightBlocks; y += 1) {
				g.fillRect((float) (x + gutter), (float) (y + gutter),
						1.0f - 2 * gutter, 1.0f - 2 * gutter);
			}
		}
	}
}
