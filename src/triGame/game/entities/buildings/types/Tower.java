
package triGame.game.entities.buildings.types;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.zombies.Zombie;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class Tower extends Building {
	private static final int initialSpeed = 700;
	private static final int initialShootDelay = 500;
	private static final int initialDamage = -35;
	
	protected final ManagerService managers;
	protected UpgradeItem rangeUpgrade = null;
	protected UpgradeItem fireRateUpgrade = null;
	protected UpgradeItem damageUpgrade = null;
	protected UpgradeItem accuracyUpgrade = null;
	
	protected long lastShot = 0;
	
	@Override
	public int getVisibilityRadius() { return rangeUpgrade.getValue(); }
	
	protected int getFireRate() { return fireRateUpgrade.getValue(); }
	
	protected boolean readyToFire() { return System.currentTimeMillis() >= lastShot + getFireRate(); }
	
	public Tower(double x, double y, ParticleController pc, ManagerService managers, EntityKey key) {
		this(x, y, pc, managers, INFO, key);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100),3, INFO.visibilityRadius, 50);
		if (owned()) {
			fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 100), 3, initialShootDelay, -50);
			damageUpgrade = new UpgradeItem(new ShopItem("Damage", 100), 3, initialDamage, -10);
			accuracyUpgrade = new UpgradeItem(new ShopItem("Accuracy", 200), 3, initialSpeed, 100);
			upgrades.addUpgrade(rangeUpgrade);
			upgrades.addUpgrade(fireRateUpgrade);
			upgrades.addUpgrade(damageUpgrade);
			upgrades.addUpgrade(accuracyUpgrade);
		}
	}
	
	protected Tower(double x, double y, ParticleController pc, ManagerService managers, BuildingInfo info, EntityKey key) {
		super(info.spriteId, x, y, pc, info, key);
		this.managers = managers;
	}

	@Override
	public void performLogic(int frameDelta) {
		if (!owned() || managers.zombie.list.isEmpty())
			return;
		
		if (readyToFire()) {
			Entity target = targetEntity(); //TODO do not need to search every frame.
			shootAtTarget(target);
			lastShot = System.currentTimeMillis();
		}
		super.performLogic(frameDelta);
	}
	
	protected void shootAtTarget(Entity target) {
		if (target == null)
			return;
		
		final double targetX = target.getCenterX(), targetY = target.getCenterY();
		
		double dist = Point.distance(getCenterX(), getCenterY(), targetX, targetY);
		if (dist < getVisibilityRadius()) {
			turn(targetX, targetY);
			shoot();
		}
	}
	
	protected Entity targetEntity() {
		Zombie shortestZombie = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (Zombie z : managers.zombie.list) {
			int dist = (int) Point.distance(getCenterX(), getCenterY(), z.getX(), z.getY());
			if (dist < shortestDistance) {
				shortestDistance = dist;
				shortestZombie = z;
			}
		}
		return shortestZombie;
	}
	
	private void shoot() {
		int tSpeed = initialSpeed;
		if (accuracyUpgrade != null)
			tSpeed = accuracyUpgrade.getValue();
		managers.projectile.towerCreate((int) getCenterX(), (int) getCenterY(), getAngle(), tSpeed, damageUpgrade.getValue());
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"tower",    //spriteId
			"tower",    //Creator hash map key
			500,        //visibilityRadius
			"Need help defending yourself from waves of undead triangles?",
			new ShopItem("Tower", 500),
			true,   //has a healthBar
			true,   //has an UpgradeManager
			true,   //is interactive
			15,     //zombie target selection weight
			100     //max health
	);
}
