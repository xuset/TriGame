package net.xuset.triGame.game.survival.safeArea;

import net.xuset.tSquare.game.entity.Entity;

final class Circle {
	final double firstX;
	final double firstY;
	final double radius;
	final double centerX;
	final double centerY;
	final double rSquare;
	final Entity owner;
	
	public Circle(double centerX, double centerY, double radius, Entity owner) {
		this.owner = owner;
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		firstX = centerX - radius;
		firstY = centerY - radius;
		rSquare = radius * radius;
	}
	
	public Circle(double centerX, double centerY, double radius) {
		this(centerX, centerY, radius, null);
	}
	
	public boolean isInside(double x, double y) {
		double difX = centerX - x;
		double difY = centerY - y;
		if (difX * difX + difY * difY <= rSquare)
			return true;
		return false;
	}
}
