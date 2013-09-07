package tSquare.game;


import java.util.LinkedHashMap;
import java.util.Map;

import tSquare.game.entity.Entity;

public class ManagerController extends LinkedHashMap<String, Manager<?>> implements GameIntegratable{
	private static final long serialVersionUID = -1352727451894266104L;
	
	Game game;
	
	public ManagerController(Game game) {
		this.game = game;
	}
	
	public void performLogic() {
		for (Map.Entry<String, Manager<?>> entry : this.entrySet()) {
			entry.getValue().performLogic();
		}
	}

	public void draw() {
		for (Map.Entry<String, Manager<?>> entry : this.entrySet()) {
			entry.getValue().draw();
		}
	}
	
	public void completeAllListModifications() {
		for (Map.Entry<String, Manager<?>> entry : this.entrySet()) {
			entry.getValue().getList().completeModifications();
		}
	}
	
	public Entity getEntity(String managerKey, long entityId) {
		Manager<?> manager = get(managerKey);
		if (manager != null)
			return manager.getById(entityId);
		return null;
	}
}
