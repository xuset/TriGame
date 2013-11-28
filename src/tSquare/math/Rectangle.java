package tSquare.math;


public class Rectangle extends java.awt.Rectangle{
	private static final long serialVersionUID = -7609527989117467150L;
	private double x = 0;
	private double y = 0;
	private double width = 0;
	private double height = 0;
	
	public Rectangle() {
	}
	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public double getX() { return x; }
	public double getY() { return y; }
	public double getWidth() { return width; }
	public double getHeight() { return height; }
	
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setWidth(double width) { this.width = width; }
	public void setHeight(double height) {this.height = height; }
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDimensions(double width, double height) {
		this.width = width;
		this.height = height;
	}
}
