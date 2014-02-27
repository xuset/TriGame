package net.xuset.tSquare.math.rect;

public abstract class RectangleW extends RectangleR implements IRectangleW {

	@Override 
	public void set(double x, double y) {
		setX(x);
		setY(y);
	}

	@Override 
	public void setDimensions(double width, double height) {
		setWidth(width);
		setHeight(height);
	}
	
	@Override
	public void setCenter(double x, double y) {
		setX(x - getWidth() / 2.0);
		setY(y - getHeight() / 2.0);
	}
	@Override
	public void setCenterRectBounded(double centerX, double centerY, IRectangleR rect) {
		int tempX = (int) (centerX - rect.getWidth() / 2);
		int tempY = (int) (centerY - rect.getHeight() / 2);
		
		if (centerX > getWidth() - rect.getWidth() / 2)
			tempX = (int) (getWidth() - rect.getWidth());
		if (centerX < rect.getWidth()/2.0)
			tempX = 0;
		
		if (centerY > getHeight() - rect.getHeight()/2.0)
			tempY = (int) (getHeight() - rect.getHeight());
		if (centerY < rect.getHeight()/2.0)
			tempY = 0;
		
		if (rect.getWidth() > getWidth())
			tempX = 0;
		if (rect.getHeight() > getHeight())
			tempY = 0;
		
		set(tempX, tempY);
	}
	
	@Override
	public void setFrame(double x, double y, double w, double h) {
		setX(x);
		setY(y);
		setWidth(w);
		setHeight(h);
	}

}
