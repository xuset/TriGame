package net.xuset.tSquare.game.entity;


import java.util.LinkedHashMap;
import java.util.Map;

import net.xuset.objectIO.netObject.NetObjUpdater;
import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.util.HashMapKeyCollision;



public final class ManagerController implements GameIntegratable{
	public final LinkedHashMap<String, Manager<?>> managers = new LinkedHashMap<String, Manager<?>>();
	
	public final CreationHandler creator;
	
	public ManagerController(NetObjUpdater objController) {
		creator = new CreationHandler(objController, this);
	}
	
	public Manager<?> put(String key, Manager<?> m) {
		if (managers.containsKey(key))
			throw new HashMapKeyCollision(key + " has already been used");
		return managers.put(key, m);
	}
	
	@Override
	public void update(int frameDelta) {
		for (Map.Entry<String, Manager<?>> entry : managers.entrySet()) {
			entry.getValue().update(frameDelta);
		}
	}

	@Override
	public void draw(IGraphics g) {
		for (Map.Entry<String, Manager<?>> entry : managers.entrySet()) {
			entry.getValue().draw(g);
		}
	}
	
	public Manager<?> getManager(String managerKey) {
		return managers.get(managerKey);
	}
	
	public Entity getEntity(String managerKey, long entityId) {
		Manager<?> manager = managers.get(managerKey);
		if (manager != null)
			return manager.getById(entityId);
		return null;
	}
}
