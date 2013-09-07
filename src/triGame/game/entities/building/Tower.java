
package triGame.game.entities.building;

import java.util.ArrayList;

import tSquare.math.Point;
import tSquare.system.Sound;
import triGame.game.entities.zombies.Zombie;
import triGame.game.projectile.ProjectileTower;
import triGame.game.shopping.ShopItem;

public class Tower extends Building {
	public static final String IDENTIFIER = "tower";
	public static final String SPRITE_ID = "tower";
	public static final ShopItem NEW_TOWER = new ShopItem("Tower", 500);
	
	private static final int initialShootDelay = 500;
	private static final int initialRange = 500;
	
	protected int range = initialRange;
	protected int shootDelay = initialShootDelay;
	
	//private BuildingManager manager;
	private Sound shotSound = new Sound("tower", "media/Pistol_Shot.wav");
	private long lastShot = 0;
	
	public Tower(int x, int y, BuildingManager manager, long ownerId, long entityId) {
		super(SPRITE_ID, x, y, manager, ownerId, entityId, initialRange);
		this.manager = manager;
	}
	
	protected Tower(String spriteId, int x, int y, BuildingManager manager, long ownerId, long entityId, int range) {
		super(spriteId, x, y, manager, ownerId, entityId, range);
		this.manager = manager;
	}
	
	public static Tower create(int x, int y, BuildingManager manager) {
		Tower t = new Tower(x, y, manager, manager.getUserId(), manager.getUniqueId());
		t.createOnNetwork(true);
		manager.add(t);
		return t;
	}
	
	private void shoot() {
		if (lastShot + shootDelay < System.currentTimeMillis()) {
			shotSound.play();
			ProjectileTower.create(this, manager.getProjectils());
			lastShot = System.currentTimeMillis();
		}
	}
	
	public void performLogic() {
		ArrayList<Zombie> zombies = manager.getGameInstance().zombieManager.getList();
		if (zombies.size() == 0)
			return;
		Zombie shortestZombie = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (Zombie z : zombies) {
			int dist = (int) Point.distance(x, y, z.getX(), z.getY());
			if (dist < shortestDistance) {
				shortestDistance = dist;
				shortestZombie = z;
			}
		}
		if (shortestDistance < range) {
			this.setAngle(Point.degrees(this.getCenterX(), this.getCenterY(),shortestZombie.getCenterX(), shortestZombie.getCenterY()));
			this.shoot();
		}
		varContainer.update();
	}
	
	public static Tower createFromString(String parameters, BuildingManager manager, long ownerId, long id) {
		String[] parameter = parameters.split(":");
		int x = Integer.parseInt(parameter[0]);
		int y = Integer.parseInt(parameter[1]);
		Tower t = new Tower(x, y, manager, ownerId, id);
		return t;
	}
	
	public String createToString() {
		return createToStringHeader() + (int) x + ":" + (int) y;
	}
	
	public String getIdentifier() {
		return IDENTIFIER;
	}
}
