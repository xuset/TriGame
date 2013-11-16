package triGame.game.entities.buildings.types;



import tSquare.game.entity.EntityKey;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.entities.Person;
import triGame.game.entities.buildings.Wall;
import triGame.game.shopping.ShopItem;

public class TrapDoor extends Wall {
	private final ManagerService managers;
	private boolean primed = false;
	
	public TrapDoor(double x, double y, ManagerService managers, EntityKey key) {
		super(INFO.spriteId, x, y, INFO, key);
		this.managers = managers;
	}

	@Override
	public void performLogic(int frameDelta) {
		Person player = managers.person.getPlayer();
		if (player == null)
			return;
		
		if (primed == false) {
			int posX = Params.roundToGrid(player.getCenterX());
			int posY = Params.roundToGrid(player.getCenterY());
			if (Point.isEqualTo(getX(), getY(), posX, posY))
				primed = true;
		} else if (collidedWith(player) == false) {
			remove();
			managers.building.getCreator(Barrier.INFO).create(getX(), getY());
		}
		super.performLogic(frameDelta);
	}
	
	public static WallInfo INFO = new WallInfo(
			"trapdoor",	//spriteId
			"tp",       //Creator hash map key
			"Not an ordinary wall. It has a special one-way function", 
			new ShopItem("Trap door", 25)
	);
}
