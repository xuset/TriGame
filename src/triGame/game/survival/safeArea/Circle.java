package triGame.game.survival.safeArea;

import tSquare.game.entity.Entity;

class Circle {
	public int firstX;
	public int firstY;
	public int radius;
	public int centerX;
	public int centerY;
	private int rSquare;
	Entity owner;
	public Circle(int centerX, int centerY, int radius, Entity owner) {
		this(centerX, centerY, radius);
		this.owner = owner;
	}
	public Circle(int centerX, int centerY, int radius) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		firstX = centerX - radius;
		firstY = centerY - radius;
		rSquare = radius * radius;
	}
	
	public boolean isInside(int x, int y) {
		int difX = centerX - x;
		int difY = centerY - y;
		if (difX * difX + difY * difY <= rSquare)
			return true;
		return false;
	}
}
