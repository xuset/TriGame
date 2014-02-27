package net.xuset.tSquare.math.point;

public class Point extends PointW {
	double x, y;
	
	public Point() {
		setTo(0, 0);
	}
	
	public Point(double x, double y) {
		setTo(x, y);
	}
	
	public Point(IPointR p) {
		setTo(p);
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

}
