package tSquare.game.entity;

public class LocationCreator<T extends Entity> extends Creator<T> {
	public static interface LocationFunc<T extends Entity> extends CreateFunc<T> {
		T create(double x, double y);
	}
	
	private final LocationFunc<? extends T> func;

	public LocationCreator(String classId, CreationHandler handler, LocationFunc<? extends T> func) {
		super(classId, handler, func);
		this.func = func;
	}
	
	public T create(double x, double y, Manager<T> manager) {
		T e = func.create(x, y);
		networkCreate(e, manager);
		localCreate(e, manager);
		return e;
	}
	
	public T create(double x, double y) {
		return create(x, y, null);
	}
}
