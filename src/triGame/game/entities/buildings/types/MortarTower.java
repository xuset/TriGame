package triGame.game.entities.buildings.types;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class MortarTower extends Tower {	
	private long lastShootTime = 0;

	public MortarTower(double x, double y, ParticleController pc,
			ManagerService managers, EntityKey key) {
		
		super(x, y, pc, managers, INFO, key);
		fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 100), 3, 1500, -150);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100), 3, INFO.visibilityRadius, 75);
		damageUpgrade = new UpgradeItem(new ShopItem("Damage", 100), 3, -100, -15);
		accuracyUpgrade = new UpgradeItem(new ShopItem("Accuracy", 100), 3, 250, 80);
		upgrades.addUpgrade(fireRateUpgrade);
		upgrades.addUpgrade(rangeUpgrade);
		upgrades.addUpgrade(damageUpgrade);
		upgrades.addUpgrade(accuracyUpgrade);
	}
	
	@Override
	protected void shootAtTarget(Entity target) {
		final int fireRate = fireRateUpgrade.getValue();
		final int range = rangeUpgrade.getValue();
		
		if (lastShootTime + fireRate > System.currentTimeMillis())
			return;
		
		double dist = Point.distance(getCenterX(), getCenterY(), target.getX(), target.getY());
		if (dist < range) {
			
			final double angle = Point.degrees(this.getCenterX(), this.getCenterY(),target.getCenterX(), target.getCenterY());
			final int x = (int) getCenterX();
			final int y = (int) getCenterY();
			final int speed = accuracyUpgrade.getValue();
			final int damage = damageUpgrade.getValue();
			
			managers.projectile.mortorCreate(x, y, angle, speed, damage);
			lastShootTime = System.currentTimeMillis();
		}
	}

	public static final BuildingInfo INFO = new BuildingInfo(
			"media/MortarTower.png",    //spriteId
			"mortarTower",    //Creator hash map key
			200,        //visibilityRadius
			"'BOOM,' says the mortar tower.",
			new ShopItem("Mortar tower", 500),
			true,    //has a healthBar
			true,    //has an UpgradeManager
			true,   //is interactive
			15,     //zombie target selection weight
			200     //max health
	);
}
