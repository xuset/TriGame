package net.xuset.tSquare.system.input;

import net.xuset.tSquare.util.Observer;

public interface IInputListener<T> {
	void watch(Observer.Change<T> watcher);
	boolean unwatch(Observer.Change<T> watcher);
	void clearListeners();
}
