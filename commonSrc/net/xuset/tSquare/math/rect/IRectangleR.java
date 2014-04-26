package net.xuset.tSquare.math.rect;

import net.xuset.tSquare.math.point.IPointR;

public interface IRectangleR {
	double getX();
	double getY();
	double getWidth();
	double getHeight();
	
	double getCenterX();
	double getCenterY();
	
	boolean isInside(double x, double y, double w, double h);
	boolean isInside(IRectangleR r);
	boolean contains(double x, double y);
	boolean contains(IPointR p);
	
	boolean equals(IRectangleR rect);
}
