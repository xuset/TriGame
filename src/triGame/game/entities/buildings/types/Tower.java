
package triGame.game.entities.buildings.types;

import java.util.ArrayList;

import tSquare.game.entity.EntityKey;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.zombies.Zombie;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class Tower extends Building {
	
	private static final int initialShootDelay = 500;
	private static final int initialDamage = -30;
	
	protected final ManagerService managers;
	protected UpgradeItem rangeUpgrade = null;
	protected UpgradeItem fireRateUpgrade = null;
	protected UpgradeItem damageUpgrade = null;
	
	private long lastShot = 0;
	
	public int getVisibilityRadius() { return rangeUpgrade.getValue(); }
	
	public Tower(double x, double y, ManagerService managers, EntityKey key) {
		this(x, y, INFO, managers, key);
		if (owned()) {
			rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100),3, INFO.visibilityRadius, 50);
			fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 100), 3, initialShootDelay, -30);
			damageUpgrade = new UpgradeItem(new ShopItem("Damage", 100), 3, initialDamage, -10);
			upgrades.addUpgrade(rangeUpgrade);
			upgrades.addUpgrade(fireRateUpgrade);
			upgrades.addUpgrade(damageUpgrade);
		}
	}
	
	protected Tower(double x, double y, BuildingInfo info, ManagerService managers, EntityKey key) {
		super(info.spriteId, x, y, info, key);
		this.managers = managers;
	}
	
	private void shoot() {
		if (lastShot + fireRateUpgrade.getValue()  < System.currentTimeMillis()) {
			managers.projectile.create(getIntX(), getIntY(), getAngle(), 800, damageUpgrade.getValue());
			lastShot = System.currentTimeMillis();
		}
	}

	@Override
	public void performLogic(int frameDelta) {
		ArrayList<Zombie> zombies = managers.zombie.list;
		if (!owned() || zombies.size() == 0)
			return;
		Zombie shortestZombie = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (Zombie z : zombies) {
			int dist = (int) Point.distance(getX(), getY(), z.getX(), z.getY());
			if (dist < shortestDistance) {
				shortestDistance = dist;
				shortestZombie = z;
			}
		}
		if (shortestDistance < rangeUpgrade.getValue()) {
			this.setAngle(Point.degrees(this.getCenterX(), this.getCenterY(),shortestZombie.getCenterX(), shortestZombie.getCenterY()));
			this.shoot();
		}
		super.performLogic(frameDelta);
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"tower",    //spriteId
			"tower",    //Creator hash map key
			500,        //visibilityRadius
			"Need help defending yourself from waves of undead triangles?",
			new ShopItem("Tower", 500),
			true,   //has a healthBar
			true,    //has an UpgradeManager
			true
	);
}
