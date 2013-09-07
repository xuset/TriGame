package tSquare.game;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityCreater;
import tSquare.system.Network;
import tSquare.util.SafeArrayList;
import tSquare.util.SafeContainer;


public abstract class Manager<T extends Entity> extends SafeContainer<T> implements GameIntegratable{
	
	private ManagerController controller;
	private Game game;
	private String hashMapKey;
	
	public GameBoard gameBoard;

	public abstract T createFromString(String parameters, long id);
	
	public Game getGameInstance() { return game; }
	public Network getNetwork() { return game.network; }
	public EntityCreater getEntityCreater() { return game.entityCreater; }
	public int getDelta() { return game.getDelta(); }
	public long getUserId() { return game.getUserId(); }
	
	public Manager(ManagerController controller, GameBoard gameBoard, String hashMapKey) {
		this(controller, gameBoard, hashMapKey, new SafeArrayList<T>());
	}
	
	public Manager(ManagerController controller, GameBoard gameBoard, String hashMapKey, SafeArrayList<T> list) {
		this.controller = controller;
		this.game = controller.game;
		this.gameBoard = gameBoard;
		this.hashMapKey = hashMapKey;
		this.list = list;
		controller.put(hashMapKey, this);
	}
	
	public Manager(String hashMapKey, Manager<T> manager) {
		this.controller = manager.controller;
		this.game = manager.game;
		this.gameBoard = manager.gameBoard;
		this.hashMapKey = hashMapKey;
		this.list = manager.list;
		controller.put(hashMapKey, this);
	}
	
	public T getById(long id) {
		for (T type : list) {
			if (type.getId() == id)
				return type;
		}
		return null;
	}
	
	public String getHashMapKey() {
		return hashMapKey;
	}
	
	public long getUniqueId() {
		return game.idGenerator.getId();
	}
	
	public boolean remove(Entity e) {
		return list.remove_safe(e);
	}
	
	public void performLogic() {
		for (Entity e : list) {
			e.performLogic();
		}
		completeListModifications();
	}
	
	public void draw() {
		for (Entity e : list) {
			e.draw();
		}
	}
}
