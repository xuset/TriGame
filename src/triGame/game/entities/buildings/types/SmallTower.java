package triGame.game.entities.buildings.types;

import tSquare.game.entity.EntityKey;
import triGame.game.ManagerService;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class SmallTower extends Tower {
	
	private static final int initialShootDelay = 600;
	private static final int initialDamage = -20;
	
	public SmallTower(double x, double y, ManagerService managers, EntityKey key) {
		super(x, y, INFO, managers, key);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100),3, INFO.visibilityRadius, 50);
		if (owned()) {
			fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 100), 3, initialShootDelay, -30);
			damageUpgrade = new UpgradeItem(new ShopItem("Damage", 100), 3, initialDamage, -10);
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
			new ShopItem("Small Tower", 250),
			true,   //has a healthBar
			true,    //has an UpgradeManager
			true
	);
}
