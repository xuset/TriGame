package tSquare.game.entity;

import objectIO.markupMsg.MarkupMsg;
import tSquare.math.IdGenerator;

public abstract class Creator<T extends Entity> {
	private final CreationHandler handler;
	
	protected final String classId;
	
	public boolean createOnNetwork = true;
	public boolean allowUpdates = true;
	
	protected abstract T parseMsg(MarkupMsg msg, EntityKey key);
	
	public Creator(String classId, CreationHandler handler) {
		handler.activateCreator(classId, this);
		this.classId = classId;
		this.handler = handler;
	}
	
	@SuppressWarnings("unchecked") //just don't add something your'e not supposed to.
	protected final Entity createFromMsg(MarkupMsg msg, EntityKey key) {
		T t = parseMsg(msg, key);
		localCreate(t, (Manager<T>) key.manager);
		return t;
	}
	
	protected final void localCreate(T t, Manager<T> manager) {
		if (manager != null) {
			manager.add(t);
		}
	}
	
	protected final void networkCreate(EntityKey key, MarkupMsg args) {
		if (createOnNetwork)
			handler.createOnNetwork(key, args, this);
	}
	
	protected final EntityKey getNewKey(Manager<T> manager) {
		EntityKey key = new EntityKey();
		key.manager = manager;
		key.id = IdGenerator.getNext();
		key.allowUpdates = allowUpdates;
		key.owned = true;
		if (allowUpdates && createOnNetwork)
			key.objController = handler.objController;
		else
			key.objController = null;
		return key;
	}
}
