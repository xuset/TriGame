package tSquare.util;

import java.util.LinkedList;

public class Observer<T> {
	public static interface Change<T> { void observeChange(T t); }
	
	private LinkedList<Change<T>> watchers;
	private T var;
	
	public Observer(T t) {
		this();
		var = t;
	}
	
	public Observer() {
		watchers = new LinkedList<Change<T>>();
	}
	
	public void watch(Change<T> c) {
		watchers.add(c);
	}
	
	public void unwatch(Change<T> c) {
		watchers.remove(c);
	}
	
	public void notifyWatchers() {
		notifiyWatchers(var);
	}
	
	public void notifiyWatchers(T t) {
		for (Change<T> c : watchers) {
			c.observeChange(t);
		}
	}
}
