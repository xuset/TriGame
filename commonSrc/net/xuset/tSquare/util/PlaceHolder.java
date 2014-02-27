package net.xuset.tSquare.util;

public class PlaceHolder<T>{
	private T place = null;
	
	public void set(T t) {
		if (place == null)
			place = t;
	}
	
	public T get() { return place; }
}
