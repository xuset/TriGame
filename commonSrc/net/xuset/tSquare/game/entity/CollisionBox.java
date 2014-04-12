package net.xuset.tSquare.game.entity;

import net.xuset.tSquare.math.rect.RectangleR;



public final class CollisionBox extends RectangleR{
	private final Entity entity;

	public double offsetX1 = 0.0;
	public double offsetY1 = 0.0;
	public double offsetX2 = 0.0;
	public double offsetY2 = 0.0;
	
	public CollisionBox(Entity e) {
		entity = e;
	}
	
	public final void setOffsetCorners(double x1, double y1, double x2, double y2) {
		setOffsetTopLeft(x1, y1);
		setOffsetBottomRight(x2, y2);
	}
	
	public final void setOffsetTopLeft(double x, double y) {
		offsetX1 = x;
		offsetY1 = y;
	}
	
	public final void setOffsetBottomRight(double x, double y) {
		offsetX2 = x;
		offsetY2 = y;
	}

	@Override
	public final double getHeight() {
		return entity.getHeight() + offsetY2 - offsetY1;
	}

	@Override
	public final double getWidth() {
		return entity.getWidth() + offsetX2 - offsetX1;
	}

	@Override
	public final double getX() {
		return entity.getX() + offsetX1;
	}

	@Override
	public final double getY() {
		return entity.getY() + offsetY1;
	}
	
}
