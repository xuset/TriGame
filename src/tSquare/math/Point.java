package tSquare.math;

import java.awt.geom.Point2D;

public class Point extends Point2D{
	public double x = 0;
	public double y = 0;
	
	public int intX() { return (int) x; }
	public int intY() { return (int) y; }
	public Point toInt() { return new Point((int) this.x, (int) this.y); }
	public double getX() { return x; }
	public double getY() { return y; }
	
	public boolean equals(Object o) {
		if (o instanceof Point)
			return equals((Point) o);
		return false;
	}
	public boolean equals(Point p) {
		if (x == p.x && y == p.y)
			return true;
		return false;
	}
	
	public Point() {
	}
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Point(java.awt.Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void set(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	public boolean isEqualTo(Point p) {
		if (p.x == this.x && p.y == this.y)
			return true;
		return false;
	}
	public boolean isEqualTo(double x, double y) {
		if (x == this.x && y == this.y)
			return true;
		return false;
	}
	public boolean isEqualTo(int x, int y) {
		if (x == this.x && y == this.y)
			return true;
		return false;
	}
	public static boolean isEqualTo(double x1, double y1, double x2, double y2) {
		if (x1 == x2 && y1 == y2)
			return true;
		return false;
	}
	
	public Point translate(double x, double y) {
		return new Point(this.x + x, this.y + y);
	}
	public Point translate(Point p) {
		return new Point(this.x + p.x, this.y + p.y);
	}
	
	public Point scale(double scaleX, double scaleY) {
		return new Point(this.x * scaleX, this.y * scaleY);
	}
	public Point scale(double scale) {
		return new Point(this.x * scale, this.y * scale);
	}
	public Point scale(Point p) {
		return new Point(this.x * p.x, this.y * p.y);
	}
	
	public double distance(Point p) {
		return java.awt.Point.distance(this.x, this.y, p.x, p.y);
	}
	public static double distance(double x1, double y1, double x2, double y2) {
		return java.awt.Point.distance(x1, y1, x2, y2);
	}
	
	public double degrees(Point p) {
		return Point.degrees(this.x, this.y, p.x, p.y);
	}
	public double degrees(double x, double y) {
		return Point.degrees(this.x, this.y, x, y);
	}
	public static double degrees(double x1, double y1, double x2, double y2) {
		double degrees = 0;
		int quadrant = DegreeMath.getQuadrant(x2, y2, x1, y1);
		if (quadrant == 0) {
			if (x1 == x2) {
				if (y2 > y1)
					degrees = 270;
				if (y2 < y1)
					degrees = 90;
			}
			if (y1 == y2) {
				if (x2 > x1)
					degrees = 0;
				if (x2 < x1)
					degrees = 180;
			}
		}
		if (quadrant == 1 || quadrant == 3)
			degrees = (Math.toDegrees(Math.atan(Math.abs((y2 - y1)/(x2 - x1)))) + (quadrant - 1)*90);
		if (quadrant == 2 || quadrant == 4)
			degrees = (Math.toDegrees(Math.atan(Math.abs((x2 - x1)/(y2 - y1)))) + (quadrant - 1)*90);
		return degrees;
	}
	@Override
	public void setLocation(double x, double y) {
		set(x, y);
	}
}
