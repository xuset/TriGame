package triGame.game.projectile.gun;


import java.awt.event.KeyEvent;

import tSquare.system.Sound;
import triGame.game.entities.Person;
import triGame.game.projectile.ProjectileContainer;
import triGame.game.projectile.ProjectileShotgun;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;
import triGame.game.shopping.UpgradeManager;

public class GunShotgun extends AbstractGun {
	public static final int KEY_CODE = KeyEvent.VK_2;
	public static final String NAME = "Shotgun";
	public static final ShopItem FIRE_RATE_ITEM = new ShopItem("Fire rate", 100);
	public static final ShopItem RANGE_ITEM = new ShopItem("Range", 100);
	public static final ShopItem DAMAGE_ITEM = new ShopItem("Damage", 100);
	public static final ShopItem UNLOCK_ITEM = new ShopItem(NAME, 200);
	private static final int initialShotDelay = 700;
	private static final int initialDamage = -20;
	private static final int initialRange = 10; //in degrees
	
	private UpgradeItem fireRateUpgrade = new UpgradeItem(FIRE_RATE_ITEM, 3, initialShotDelay, -50);
	private Sound sound = Sound.get("media/Pistol_Shot.wav");
	
	public GunShotgun(GunManager gunManager) {
		super(gunManager, KEY_CODE, NAME);
		semiShotDelay = initialShotDelay - 200;
		autoShotDelay = initialShotDelay;
		UpgradeManager um = new UpgradeManager(gunManager.getShopManager(), NAME);
		um.addUpgrade(fireRateUpgrade);
		um.addUpgrade(new UpgradeItem(DAMAGE_ITEM, 3, initialDamage, -10));
		um.addUpgrade(new UpgradeItem(RANGE_ITEM, 3, initialRange, -2));
		setUpgradeManager(um);
		setLocked(UNLOCK_ITEM);
		automaticFire = true;
		description = "A 3-round burst should it";
	}
	
	private int lastFireUpgradeCount = 0;
	public void shoot(Person player, ProjectileContainer container) {
		if (fireRateUpgrade.upgradeCount != lastFireUpgradeCount) {
			autoShotDelay = initialShotDelay - fireRateUpgrade.upgradeCount * 50;
			semiShotDelay = 500 - fireRateUpgrade.upgradeCount * 50;
			lastFireUpgradeCount = fireRateUpgrade.upgradeCount;
		}
		ProjectileShotgun.create(player, container, getUpgradeManager());
		sound.play();
	}
}
