package triGame.game.entities.wall;



import tSquare.math.Point;
import triGame.game.entities.Person;
import triGame.game.shopping.ShopItem;

public class TrapDoor extends AbstractWall {
	public static final String SPRITE_ID = "trapdoor";
	public static final String IDENTIFIER = "tp";
	public static final ShopItem NEW_TRAP_DOOR = new ShopItem("Trap door", 25);
	
	private boolean primed = false;
	
	public TrapDoor(int x, int y, WallManager manager, long entityId) {
		super(SPRITE_ID, x, y, manager, entityId, IDENTIFIER);
	}
	
	public void performLogic() {
			if (primed == false) {
				Person p = manager.getPlayer();
				Point playerPosition = manager.objectGrid.roundToGrid(p.getCenter());
				if (playerPosition.isEqualTo(x, y))
					primed = true;
			}
			if (primed == true && collidedWith(manager.getPlayer()) == false) {
				this.remove();
				Barrier.create((int) x, (int) y, manager);
			}
	}
	
	public static TrapDoor create(int x, int y, WallManager manager) {
		if (manager.objectGrid.isBlockOpen(x, y)) {
			TrapDoor td = new TrapDoor(x, y, manager, manager.getUniqueId());
			td.createOnNetwork(false);
			manager.add(td);
			return td;
		}
		return null;
	}
	
	public static TrapDoor createFromString(String parameters, WallManager manager, long entityId) {
		String[] parameter = parameters.split(":");
		int x = Integer.parseInt(parameter[1]);
		int y = Integer.parseInt(parameter[2]);
		TrapDoor td = new TrapDoor(x, y, manager, entityId);
		return td;
	}
}
