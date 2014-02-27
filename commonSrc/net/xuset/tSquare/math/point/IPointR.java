package net.xuset.tSquare.math.point;

public interface IPointR{
	double getX();
	double getY();
	boolean equals(IPointR p);
	boolean equals(double x, double y);
	double calcDistance(IPointR p);
	double calcDistance(double x, double y);
	double calcAngle(IPointR p);
	double calcAngle(double x, double y);
}
