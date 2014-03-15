package net.xuset.tSquare.system.input;

public interface IInputListener<T> {
	T pollEvent();
	void addEvent(T e);
	void clearEvents();
	void clearListeners();
}
