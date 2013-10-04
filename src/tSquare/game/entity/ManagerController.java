package tSquare.game.entity;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import tSquare.game.Game;
import tSquare.game.GameIntegratable;
import tSquare.game.entity.Entity;

public class ManagerController implements GameIntegratable{
	private final LinkedHashMap<String, Manager<?>> managers = new LinkedHashMap<String, Manager<?>>();
	
	EntityCreater creator;
	Game game;
	
	public final HashMap<String, Manager<?>> getMap() { return managers; }
	
	public ManagerController(Game game) {
		creator = new EntityCreater(managers, game.getNetwork().getObjController());
		this.game = game;
	}
	
	public final Manager<?> put(String key, Manager<?> m) {
		return managers.put(key, m);
	}
	
	public final void performLogic() {
		for (Map.Entry<String, Manager<?>> entry : managers.entrySet()) {
			entry.getValue().performLogic();
		}
	}

	public final void draw() {
		for (Map.Entry<String, Manager<?>> entry : managers.entrySet()) {
			entry.getValue().draw();
		}
	}
	
	public final void completeAllListModifications() {
		for (Map.Entry<String, Manager<?>> entry : managers.entrySet()) {
			entry.getValue().getList().completeModifications();
		}
	}
	
	public final Manager<?> getManager(String managerKey) {
		return managers.get(managerKey);
	}
	
	public final Entity getEntity(String managerKey, long entityId) {
		Manager<?> manager = managers.get(managerKey);
		if (manager != null)
			return manager.getById(entityId);
		return null;
	}
}
