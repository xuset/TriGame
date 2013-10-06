package tSquare.game.entity;

import objectIO.markupMsg.MarkupMsg;
import tSquare.game.GameBoard;


public class EntityManager extends Manager<Entity>{
	public static final String HASHMAP_KEY = "entitie";
	
	public EntityManager(ManagerController controller, GameBoard gameBoard) {
		super(controller, gameBoard, HASHMAP_KEY);
	}
	
	public Entity create(String spriteId, int x, int y) {
		return Entity.create(spriteId, x, y, this);
	}

	@Override
	public Entity createFromMsg(MarkupMsg msg, long entityId) {
		double x = msg.getAttribute("x").getDouble();
		double y = msg.getAttribute("y").getDouble();
		String spriteId = msg.getAttribute("spriteId").getString();
		Entity e = new Entity(spriteId, x, y, this, entityId);
		add(e);
		return e;
	}
	
	
}
