package net.xuset.tSquare.util;

import java.util.LinkedList;

public class Observer<T> {
	public static interface Change<T> { void observeChange(T t); }
	
	private final LinkedList<Change<T>> watchers = new LinkedList<Change<T>>();
	private T var;
	
	public Observer(T t) {
		var = t;
	}
	
	public Observer() { }
	
	public void watch(Change<T> c) {
		watchers.add(c);
	}
	
	public void unwatch(Change<T> c) {
		watchers.remove(c);
	}
	
	public void unwatchAll() {
		watchers.clear();
	}
	
	public void notifyWatchers() {
		notifyWatchers(var);
	}
	
	public void notifyWatchers(T t) {
		for (Change<T> c : watchers) {
			c.observeChange(t);
		}
	}
}
