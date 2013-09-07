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
	public static final ShopItem DAMAGE_ITEM = new ShopItem("Damage", 100);
	private static final ShopItem unlockItem = new ShopItem(NAME, 200);
	private static final int initialShotDelay = 700;
	
	private UpgradeItem fireRateUpgrade;
	private Sound sound = Sound.get("media/Pistol_Shot.wav");
	
	public GunShotgun(GunManager gunManager) {
		super(gunManager, KEY_CODE, NAME);
		semiShotDelay = 500;
		autoShotDelay = initialShotDelay;
		UpgradeManager um = new UpgradeManager(gunManager.getShopManager(), NAME);
		fireRateUpgrade = new UpgradeItem(FIRE_RATE_ITEM);
		um.addUpgrade(fireRateUpgrade);
		um.addUpgrade(DAMAGE_ITEM);
		setUpgradeManager(um);
		setLocked(unlockItem);
		automaticFire = true;
	}
	
	private int lastFireUpgradeCount = 0;
	public void shoot(Person player, ProjectileContainer container) {
		if (fireRateUpgrade.upgradeCount != lastFireUpgradeCount) {
			semiShotDelay = initialShotDelay - fireRateUpgrade.upgradeCount * 25;
			lastFireUpgradeCount = fireRateUpgrade.upgradeCount;
		}
		ProjectileShotgun.create(player, container, getUpgradeManager());
		sound.play();
	}
}
