package net.xuset.tSquare.game.entity;

public class NoArgCreator<T extends Entity> extends Creator<T> {
	
	
	public NoArgCreator(String classId, CreationHandler handler, CreateFunc<? extends T> createFunc) {
		super(classId, handler, createFunc);
	}
	
	public T create(Manager<T> manager) {
		T t = createFunc.create(null);
		networkCreate(t, manager);
		localCreate(t, manager);
		return t;
	}

	public T create() {
		return create(null);
	}

}

	
