package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.triGame.game.entities.projectiles.ProjectileManager;
import net.xuset.triGame.game.entities.zombies.ZombieTargeter;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.UpgradeItem;


public class SmallTower extends Tower {
	
	private static final int initialShootDelay = 500;
	private static final int initialDamage = -15;
	
	public SmallTower(double x, double y, ParticleController pc, 
			ZombieTargeter targeter, ProjectileManager projectile, EntityKey key) {
		
		super(x, y, pc, targeter, projectile, INFO, key);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100),3, INFO.visibilityRadius, 1);
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
			6,			//visibilityRadius
			"Just a little bit smaller than its predecessor",
			new ShopItem("Small Tower", 150),
			true,   //has a healthBar
			true,   //has an UpgradeManager
			true,   //is interactive
			10,     //zombie target selection weight
			100     //max health
	);
}
