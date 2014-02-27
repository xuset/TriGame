package net.xuset.tSquare.math.rect;


public class Rectangle extends RectangleW{
	private double x = 0;
	private double y = 0;
	private double width = 0;
	private double height = 0;
	
	public Rectangle(IRectangleR rect) {
		setFrame(rect.getX(), rect.getY(),
				rect.getWidth(), rect.getHeight());
		
	}
	public Rectangle() {
		setFrame(0, 0, 0, 0);
	}
	public Rectangle(double x, double y, double width, double height) {
		setFrame(x, y, width, height);
	}
	
	@Override public double getX() { return x; }
	@Override public double getY() { return y; }
	@Override public double getWidth() { return width; }
	@Override public double getHeight() { return height; }
	
	@Override public void setX(double x) { this.x = x; }
	@Override public void setY(double y) { this.y = y; }
	@Override public void setWidth(double width) { this.width = width; }
	@Override public void setHeight(double height) {this.height = height; }
}
