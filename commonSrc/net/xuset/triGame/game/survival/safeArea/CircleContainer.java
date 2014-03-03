package net.xuset.triGame.game.survival.safeArea;


import java.util.Iterator;
import java.util.LinkedList;

import net.xuset.tSquare.game.entity.Entity;



final class CircleContainer {
	final LinkedList<Circle> circles = new LinkedList<Circle>();
	
	void addCircle(double centerX, double centerY, double radius, Entity e) {
		circles.add(new Circle(centerX, centerY, radius, e));
	}
	
	void removebyCenter(int centerX, int centerY) {
		Iterator<Circle> i = circles.iterator();
		while (i.hasNext()) {
			Circle c = i.next();
			if (c.centerX == centerX && c.centerY == centerY) {
				i.remove();
				break;
			}
		}
	}
	
	boolean removeByEntity(Entity owner) {
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
	
	boolean isInsideACircle(double x, double y) {
		for (Circle c : circles) {
			if (c.isInside(x, y))
				return true;
		}
		return false;
	}
}
