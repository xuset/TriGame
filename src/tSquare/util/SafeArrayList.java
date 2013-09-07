package tSquare.util;

import java.util.ArrayList;

public class SafeArrayList<T> extends ArrayList<T>{
	private static final long serialVersionUID = 8025258780549245080L;
	
	private ArrayList<T> addList = new ArrayList<T>();
	private ArrayList<Object> removeList = new ArrayList<Object>();
	
	public boolean add_safe(T type) {
		return addList.add(type);
	}
	
	public boolean remove_safe(Object o) {
		return removeList.add(o);
	}
	
	public boolean completeModifications() {
		boolean a = true;
		boolean b = true;
		if (addList.size() != 0) {
			a = this.addAll(addList);
			addList.clear();
		}
		if (removeList.size() != 0) {
			b = this.removeAll(removeList);
			removeList.clear();
		}
		return a && b;
	}
}
