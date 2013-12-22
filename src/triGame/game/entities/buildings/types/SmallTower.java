package triGame.game.entities.buildings.types;

import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import triGame.game.entities.projectiles.ProjectileManager;
import triGame.game.entities.zombies.ZombieTargeter;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class SmallTower extends Tower {
	
	private static final int initialShootDelay = 500;
	private static final int initialDamage = -15;
	
	public SmallTower(double x, double y, ParticleController pc, 
			ZombieTargeter targeter, ProjectileManager projectile, EntityKey key) {
		
		super(x, y, pc, targeter, projectile, INFO, key);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100),3, INFO.visibilityRadius, 50);
		if (owned()) {
			fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 50), 3, initialShootDelay, -50);
			damageUpgrade = new UpgradeItem(new ShopItem("Damage", 50), 3, initialDamage, -5);
			upgrades.addUpgrade(rangeUpgrade);
			upgrades.addUpgrade(fireRateUpgrade);
			upgrades.addUpgrade(damageUpgrade);
		}
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"small tower",	//spriteId
			"samll tower",	//Creator hash map key
			300,			//visibilityRadius
			"Just a little bit smaller than its predecessor",
			new ShopItem("Small Tower", 150),
			true,   //has a healthBar
			true,   //has an UpgradeManager
			true,   //is interactive
			10,     //zombie target selection weight
			100     //max health
	);
}
