package net.xuset.tSquare.game.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;




public class Manager<T extends Entity> implements GameIntegratable{
	private String hashMapKey;
	private final CreationHandler creationHandler;

	public final List<T> list;
	public boolean updatesAllowed = true;

	@SuppressWarnings("unused")
	protected void onRemove(Entity e) { }
	@SuppressWarnings("unused")
	protected void onAdd(T t) { }
	
	public Manager(ManagerController controller, String hashMapKey) {
		this(controller, hashMapKey, new ArrayList<T>());
	}
	
	protected Manager(ManagerController controller, String hashMapKey, List<T> list) {
		creationHandler = controller.creator;
		this.hashMapKey = hashMapKey;
		this.list = list;
		controller.put(hashMapKey, this);
	}
	
	public final T getById(long id) {
		for (T type : list) {
			if (type.getId() == id)
				return type;
		}
		return null;
	}
	
	public final String getHashMapKey() {
		return hashMapKey;
	}
	
	public final boolean add(T t) {
		boolean r = list.add(t);
		if (updatesAllowed)
			t.syncWithController(creationHandler.objController);
		onAdd(t);
		return r;
	}
	
	@Override
	public final void update(int frameDelta) {
		for (Iterator<T> it = list.iterator(); it.hasNext();) {
			Entity e = it.next();
			if (e.removeRequested()) {
				it.remove();
				handleRemove(e);
			} else {
				e.update(frameDelta);
				e.sendUpdates();
			}
		}
	}
	
	@Override
	public void draw(IGraphics g) {
		for (Entity e : list) {
			e.draw(g);
		}
	}
	
	public final void updateList() {
		for (Iterator<T> it = list.iterator(); it.hasNext();) {
			Entity e = it.next();
			if (e.removeRequested()) {
				it.remove();
				handleRemove(e);
			} else {
				e.sendUpdates();
			}
		}
	}
	
	private final void handleRemove(Entity e) {
		creationHandler.removeOnNetwork(e, this);
		e.handleRemove();
		onRemove(e);
	}
}
