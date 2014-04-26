package net.xuset.tSquare.math.rect;

import net.xuset.tSquare.math.point.IPointR;

public abstract class RectangleR implements IRectangleR {

	@Override 
	public boolean isInside(double x, double y, double w, double h) {
		return (x + w > getX() && x < getX() + getWidth() &&
				y + h > getY() && y < getY() + getHeight());
	}

	@Override 
	public boolean isInside(IRectangleR r) {
		return isInside(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	@Override
	public double getCenterX() {
		return getX() + getWidth() / 2;
	}
	
	@Override
	public double getCenterY() {
		return getY() + getHeight() / 2;
	}

	@Override
	public boolean contains(double x, double y) {
		return (x >= getX() && x <= getX() + getWidth() &&
				y >= getY() && y <= getY() + getHeight());
	}
	
	@Override
	public boolean contains(IPointR p) {
		return contains(p.getX(), p.getY());
	}
	
	@Override
	public boolean equals(IRectangleR rect) {
		return rect.getX() == getX() && rect.getY() == getY() &&
				rect.getWidth() == getWidth() && rect.getHeight() == getHeight();
	}

}
