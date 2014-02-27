package net.xuset.tSquare.events;

import java.util.Iterator;
import java.util.LinkedList;

public class EventHandler{
	private LinkedList<Event> events = new LinkedList<Event>();
	
	public boolean addEvent(Event e) {
		return events.add(e);
	}
	
	public void handleEvents() {
		Iterator<Event> iterator = events.iterator();
		while (iterator.hasNext()) {
			Event next = iterator.next();
			if (next.isExpired())
				iterator.remove();
			else
				next.doAction();
		}
	}
}
