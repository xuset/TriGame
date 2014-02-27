package net.xuset.tSquare.math.point;

public abstract class PointR implements IPointR{

	@Override
	public boolean equals(IPointR p) {
		return equals(p.getX(), p.getY());
	}

	@Override
	public boolean equals(double x, double y) {
		return (getX() == x && getY() == y);
	}

	@Override
	public double calcDistance(IPointR p) {
		return PointR.distance(getX(), getY(), p.getX(), p.getY());
	}

	@Override
	public double calcDistance(double x, double y) {
		return PointR.distance(getX(), getY(), x, y);
	}

	@Override
	public double calcAngle(IPointR p) {
		return PointR.angle(getX(), getY(), p.getX(), p.getY());
	}

	@Override
	public double calcAngle(double x, double y) {
		return PointR.angle(getX(), getY(), x, y);
	}
	
	public static double angle(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y1 - y2;
		
		double angle = 0.0;
		if (dx == 0)
			return  Math.PI;
		angle = Math.atan(dy / dx);
		if (dx < 0)
			angle += Math.PI;
		
		return angle;
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		
		return Math.sqrt(dx * dx + dy * dy);
	}
}
