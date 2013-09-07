package tSquare.game.entity;

import tSquare.game.GameBoard;
import tSquare.game.Manager;
import tSquare.game.ManagerController;


public class EntityManager extends Manager<Entity>{
	public static final String HASHMAP_KEY = "entitie";
	
	public EntityManager(ManagerController controller, GameBoard gameBoard) {
		super(controller, gameBoard, HASHMAP_KEY);
	}
	
	public Entity createFromString(String parameters, long id) {
		String[] split = parameters.split(":");
		String spriteUrl = split[0];
		int x = Integer.parseInt(split[1]);
		int y = Integer.parseInt(split[2]);
		Entity e = new Entity(spriteUrl, x, y, this, id);
		add(e);
		return e;
	}
	
	public Entity create(String spriteId, int x, int y) {
		return Entity.create(spriteId, x, y, this);
	}
	
	
}
