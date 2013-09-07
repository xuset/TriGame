package tSquare.util;

public class SafeContainer<T> {
	protected SafeArrayList<T> list;
	
	public SafeArrayList<T> getList() { return list; }
	
	public SafeContainer() {
		list = new SafeArrayList<T>();
	}
	public SafeContainer(SafeArrayList<T> list) {
		this.list = list;
	}
	
	public boolean add(T type) {
		return list.add_safe(type);
	}
	
	public boolean remove(T type) {
		return list.remove_safe(type);
	}
	
	public T get(int i) {
		return list.get(i);
	}
	
	public boolean completeListModifications() {
		return list.completeModifications();
	}
	
	
}
