
package triGame.game.entities.buildings.types;

import java.util.ArrayList;

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
	
	private long lastShot = 0;
	
	@Override
	public int getVisibilityRadius() { return rangeUpgrade.getValue(); }
	
	public Tower(double x, double y, ParticleController pc, ManagerService managers, EntityKey key) {
		this(x, y, pc, managers, INFO, key);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100),3, INFO.visibilityRadius, 50);
		if (owned()) {
			fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 100), 3, initialShootDelay, -50);
			damageUpgrade = new UpgradeItem(new ShopItem("Damage", 100), 3, initialDamage, -10);
			accuracyUpgrade = new UpgradeItem(new ShopItem("Accuracy", 200), 3, 1, 1);
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
	
	private void shoot() {
		if (lastShot + fireRateUpgrade.getValue()  < System.currentTimeMillis()) {
			int tSpeed = initialSpeed;
			if (accuracyUpgrade != null)
				tSpeed += 100 * accuracyUpgrade.getValue();
			managers.projectile.towerCreate((int) getCenterX(), (int) getCenterY(), getAngle(), tSpeed, damageUpgrade.getValue());
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
			int dist = (int) Point.distance(getCenterX(), getCenterY(), z.getX(), z.getY());
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
			true,   //has an UpgradeManager
			true,   //is interactive
			10,     //zombie target selection weight
			100     //max health
	);
}
