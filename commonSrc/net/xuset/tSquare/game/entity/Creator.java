package net.xuset.tSquare.game.entity;

public class Creator<T extends Entity> {
	public static interface CreateFunc<T extends Entity> { T create(EntityKey key); }
	
	private final CreationHandler handler;

	protected final CreateFunc<? extends T> createFunc;
	protected final String classId;
	
	public boolean createOnNetwork = true;
	
	public Creator(String classId, CreationHandler handler, CreateFunc<? extends T> createFunc) {
		this.classId = classId;
		this.handler = handler;
		this.createFunc = createFunc;
		handler.activateCreator(this);
	}
	
	@SuppressWarnings("unchecked") //just don't add something your'e not supposed to.
	protected Entity createFromMsg(EntityKey key, Manager<?> manager) {
		T t = createFunc.create(key);
		localCreate(t, (Manager<T>) manager);
		return t;
	}
	
	protected final void localCreate(T t, Manager<T> manager) {
		if (manager != null) {
			manager.add(t);
		}
	}
	
	protected final void networkCreate(Entity e, Manager<?> manager) {
		if (createOnNetwork && !e.isCreatedOnNetwork()) {
			handler.createOnNetwork(e, this, manager);
		}
	}
	
	public final <E extends T> void createOnNetwork(E e, Manager<?> manager) {
		if (!e.isCreatedOnNetwork()) {
			handler.createOnNetwork(e, this, manager);
		}
		
	}
}
