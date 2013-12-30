package tSquare.game.entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.GameIntegratable;
import tSquare.game.entity.Entity;


public class Manager<T extends Entity> implements GameIntegratable{
	private String hashMapKey;
	private final CreationHandler creationHandler;

	public final ArrayList<T> list;
	public boolean updatesAllowed = true;
	
	protected void onRemove(Entity e) { }
	protected void onAdd(T t) { }
	
	public Manager(ManagerController controller, String hashMapKey) {
		creationHandler = controller.creator;
		this.hashMapKey = hashMapKey;
		this.list = new ArrayList<T>();
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
	public final void performLogic(int frameDelta) {
		for (Iterator<T> it = list.iterator(); it.hasNext();) {
			Entity e = it.next();
			if (e.removeRequested()) {
				it.remove();
				handleRemove(e);
			} else {
				e.performLogic(frameDelta);
				e.sendUpdates();
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		for (Entity e : list) {
			e.draw(g, rect);
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
