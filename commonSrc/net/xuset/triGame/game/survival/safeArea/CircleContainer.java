package net.xuset.triGame.game.survival.safeArea;


import java.util.Iterator;
import java.util.LinkedList;

import net.xuset.tSquare.game.entity.Entity;



class CircleContainer {
	public LinkedList<Circle> circles = new LinkedList<Circle>();
	
	public void addCircle(double centerX, double centerY, double radius, Entity e) {
		circles.add(new Circle(centerX, centerY, radius, e));
	}
	
	public void removebyCenter(int centerX, int centerY) {
		Iterator<Circle> i = circles.iterator();
		while (i.hasNext()) {
			Circle c = i.next();
			if (c.centerX == centerX && c.centerY == centerY) {
				i.remove();
				break;
			}
		}
	}
	
	public boolean removeByEntity(Entity owner) {
		Iterator<Circle> i = circles.iterator();
		boolean found = false;
		while(i.hasNext()) {
			Circle c = i.next();
			if (c.owner != null && c.owner.equals(owner)) {
				i.remove();
				found = true;
			}
		}
		return found;
	}
	
	public boolean isInsideACircle(double x, double y) {
		for (Circle c : circles) {
			if (c.isInside(x, y))
				return true;
		}
		return false;
	}
	
	double[] getDimensions() {
		Circle first = circles.getFirst();
		double lowest = first.centerY + first.radius;
		double highest = first.centerY - first.radius;
		double leftest = first.centerX - first.radius;
		double rightest = first.centerX + first.radius;
		
		for (Circle c : circles) {
			double low = c.centerY + c.radius;
			if (low > lowest)
				lowest = low;
			double high = c.centerY - c.radius;
			if (high < highest)
				highest = high;
			double left = c.centerX - c.radius;
			if (left < leftest)
				leftest = left;
			double right = c.centerX + c.radius;
			if (right > rightest)
				rightest = right;
		}
		return new double[] { leftest , highest , rightest - leftest , lowest - highest };
	}
}
