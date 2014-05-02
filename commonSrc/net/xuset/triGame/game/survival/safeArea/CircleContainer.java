package net.xuset.triGame.game.survival.safeArea;


import java.util.ArrayList;
import java.util.Iterator;

import net.xuset.tSquare.game.entity.Entity;



final class CircleContainer {
	final ArrayList<Circle> circles = new ArrayList<Circle>();
	
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
		for (int i = 0; i < circles.size(); i++) {
			if (circles.get(i).isInside(x, y))
				return true;
		}
		return false;
	}
}
