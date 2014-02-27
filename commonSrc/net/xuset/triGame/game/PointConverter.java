package net.xuset.triGame.game;

import net.xuset.tSquare.math.rect.IRectangleR;

public class PointConverter {
	private final IRectangleR view;
	private final int blockSize;
	
	public PointConverter(IRectangleR view, int blockSize) {
		this.view = view;
		this.blockSize = blockSize;
	}
	
	public double screenToGameX(double x) {
		return x / blockSize + view.getX();
	}
	
	public double screenToGameY(double y) {
		return y / blockSize + view.getY();
	}
	
	public double gameToScreenX(double x) {
		return (x - view.getX()) * blockSize;
	}
	
	public double gameToScreenY(double y) {
		return (y - view.getY()) * blockSize;
	}
}
