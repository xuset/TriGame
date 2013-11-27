package tSquare.game.entity;

import objectIO.netObject.ObjController;

public final class EntityKey {
	public Manager<?> manager;
	public boolean allowUpdates = true;
	ObjController objController = null;
	long id;
	boolean owned = false;
	
	public boolean isOwned() { return owned; }
	
	EntityKey(Manager<?> manager, long id) {
		this(manager, id, false, null);
	}
	
	EntityKey(Manager<?> manager, long id, boolean allowUpdates, ObjController objController) {
		this.manager = manager;
		this.id = id;
		this.allowUpdates = allowUpdates;
		this.objController = objController;
	}
	
	EntityKey() {
		
	}
}
