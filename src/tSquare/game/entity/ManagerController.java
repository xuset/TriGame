package tSquare.game.entity;


import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.Map;

import objectIO.netObject.ObjControllerI;
import tSquare.game.GameIntegratable;
import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.util.HashMapKeyCollision;

public final class ManagerController implements GameIntegratable{
	public final LinkedHashMap<String, Manager<?>> managers = new LinkedHashMap<String, Manager<?>>();
	
	public final CreationHandler creator;
	
	public ManagerController(ObjControllerI objController) {
		creator = new CreationHandler(objController, this);
	}
	
	public Manager<?> put(String key, Manager<?> m) {
		if (managers.containsKey(key))
			throw new HashMapKeyCollision(key + " has already been used");
		return managers.put(key, m);
	}
	
	@Override
	public void performLogic(int frameDelta) {
		for (Map.Entry<String, Manager<?>> entry : managers.entrySet()) {
			entry.getValue().performLogic(frameDelta);
		}
	}

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		for (Map.Entry<String, Manager<?>> entry : managers.entrySet()) {
			entry.getValue().draw(g, rect);
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
