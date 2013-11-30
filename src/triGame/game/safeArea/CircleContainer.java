package triGame.game.safeArea;


import java.util.Iterator;
import java.util.LinkedList;

import tSquare.game.entity.Entity;

class CircleContainer {
	public LinkedList<Circle> circles = new LinkedList<Circle>();
	
	public void addCircle(int centerX, int centerY, int radius, Entity e) {
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
	
	public boolean isInsideACircle(int x, int y) {
		for (Circle c : circles) {
			if (c.isInside(x, y))
				return true;
		}
		return false;
	}
	
	int[] getDimensions() {
		Circle first = circles.getFirst();
		int lowest = first.centerY + first.radius;
		int highest = first.centerY - first.radius;
		int leftest = first.centerX - first.radius;
		int rightest = first.centerX + first.radius;
		
		for (Circle c : circles) {
			int low = c.centerY + c.radius;
			if (low > lowest)
				lowest = low;
			int high = c.centerY - c.radius;
			if (high < highest)
				highest = high;
			int left = c.centerX - c.radius;
			if (left < leftest)
				leftest = left;
			int right = c.centerX + c.radius;
			if (right > rightest)
				rightest = right;
		}
		return new int[] { leftest , highest , rightest - leftest , lowest - highest };
	}
}
