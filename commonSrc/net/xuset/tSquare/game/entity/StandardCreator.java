package net.xuset.tSquare.game.entity;


public class StandardCreator<T extends Entity> extends Creator<T>{
	public static interface StandardFunc<T extends Entity> extends CreateFunc<T> {
		T create(String spriteId, double x, double y);
	}
	
	private final StandardFunc<? extends T> func;
	
	public StandardCreator(String classId, CreationHandler handler, StandardFunc<? extends T> func) {
		super(classId, handler, func);
		this.func = func;
	}
	
	public T create (String spriteId, double x, double y, Manager<T> manager) {
		T e = func.create(spriteId, x, y);
		networkCreate(e, manager);
		localCreate(e, manager);
		return e;
	}
	
	public T create(String spriteId, double x, double y) {
		return create(spriteId, x, y, null);
	}
}
