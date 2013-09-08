package triGame.game.projectile.gun;


import java.awt.event.KeyEvent;

import tSquare.system.Sound;
import triGame.game.entities.Person;
import triGame.game.projectile.ProjectileContainer;
import triGame.game.projectile.ProjectileSub;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;
import triGame.game.shopping.UpgradeManager;

public class GunSub extends AbstractGun {
	public static final int KEY_CODE = KeyEvent.VK_3;
	public static final ShopItem FIRE_RATE_ITEM = new ShopItem("Fire rate", 100);
	public static final ShopItem DAMAGE_ITEM = new ShopItem("Damage", 100);
	public static final ShopItem UNLOCK_ITEM = new ShopItem("Sub", 200);
	private static final int initialShotDelay = 200;
	private static final String gunName = "Sub";
	
	private UpgradeItem fireRateUpgrade;
	private Sound sound = Sound.get("media/Pistol_Shot.wav");
	
	public GunSub(GunManager gunManager) {
		super(gunManager, KEY_CODE, gunName);
		semiShotDelay = initialShotDelay;
		autoShotDelay = initialShotDelay;
		UpgradeManager um = new UpgradeManager(gunManager.getShopManager(), gunName);
		fireRateUpgrade = new UpgradeItem(FIRE_RATE_ITEM);
		um.addUpgrade(fireRateUpgrade);
		um.addUpgrade(DAMAGE_ITEM);
		setUpgradeManager(um);
		setLocked(UNLOCK_ITEM);
	}
	
	private int lastFireUpgradeCount = 0;
	public void shoot(Person player, ProjectileContainer container) {
		if (fireRateUpgrade.upgradeCount != lastFireUpgradeCount) {
			semiShotDelay = initialShotDelay - fireRateUpgrade.upgradeCount * 15;
			autoShotDelay = initialShotDelay - fireRateUpgrade.upgradeCount * 15;
			lastFireUpgradeCount = fireRateUpgrade.upgradeCount;
		}
		ProjectileSub.create(player, container, getUpgradeManager());
		sound.play();
	}
}