package net.xuset.tSquare.math.rect;

public interface IRectangleW extends IRectangleR {

	
	void setX(double x);
	void setY(double y);
	void setWidth(double width);
	void setHeight(double height);
	void setCenter(double x, double y);
	void setCenterRectBounded(double x, double y, IRectangleR rect);
	
	void set(double x, double y);
	void setDimensions(double width, double height);
	void setFrame(double x, double y, double w, double h);
}
