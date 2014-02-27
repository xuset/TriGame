package net.xuset.tSquare.math.point;

public interface IPointW extends IPointR{
	public void translate(double dx, double dy);
	public void translate(IPointR p);
	public void scale(double sx, double sy);
	public void scale(IPointR p);
	public void setTo(double x, double y);
	public void setTo(IPointR p);
	public void setX(double x);
	public void setY(double y);
	
}
