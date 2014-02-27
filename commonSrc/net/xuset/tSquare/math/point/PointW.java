package net.xuset.tSquare.math.point;

public abstract class PointW extends PointR implements IPointW {

	@Override
	public void translate(double dx, double dy) {
		setX(getX() + dx);
		setY(getY() + dy);
	}

	@Override
	public void translate(IPointR p) {
		translate(p.getX(), p.getY());
	}

	@Override
	public void scale(double sx, double sy) {
		setX(getX() * sx);
		setY(getY() * sy);
	}

	@Override
	public void scale(IPointR p) {
		scale(p.getX(), p.getY());
	}

	@Override
	public void setTo(double x, double y) {
		setX(x);
		setY(y);
	}

	@Override
	public void setTo(IPointR p) {
		setTo(p.getX(), p.getY());
	}
}
