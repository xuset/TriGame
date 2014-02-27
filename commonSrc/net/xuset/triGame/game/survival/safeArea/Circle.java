package net.xuset.triGame.game.survival.safeArea;

import net.xuset.tSquare.game.entity.Entity;

class Circle {
	public double firstX;
	public double firstY;
	public double radius;
	public double centerX;
	public double centerY;
	private double rSquare;
	Entity owner;
	public Circle(double centerX, double centerY, double radius, Entity owner) {
		this(centerX, centerY, radius);
		this.owner = owner;
	}
	public Circle(double centerX, double centerY, double radius) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		firstX = centerX - radius;
		firstY = centerY - radius;
		rSquare = radius * radius;
	}
	
	public boolean isInside(double x, double y) {
		double difX = centerX - x;
		double difY = centerY - y;
		if (difX * difX + difY * difY <= rSquare)
			return true;
		return false;
	}
}
