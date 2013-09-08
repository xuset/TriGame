package triGame.game.projectile.gun;


import java.awt.event.KeyEvent;

import tSquare.system.Sound;
import tSquare.util.Observer;
import triGame.game.entities.Person;
import triGame.game.projectile.ProjectileContainer;
import triGame.game.projectile.ProjectilePistol;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;
import triGame.game.shopping.UpgradeManager;

public class GunPistol extends AbstractGun {
	public static final int KEY_CODE = KeyEvent.VK_1;
	public static final ShopItem FIRE_RATE_ITEM = new ShopItem("Fire rate", 100);
	public static final ShopItem DAMAGE_ITEM = new ShopItem("Damage", 100);
	public static final ShopItem UNLOCK = new ShopItem("Pistol", 0, true);
	private static final int initialShotDelay = 230;
	private static final int initialDamage = -35;
	private static final String gunName = "Pistol";
	
	private UpgradeItem fireRateUpgrade = new UpgradeItem(FIRE_RATE_ITEM, 3, initialShotDelay, -20);
	private Sound sound = Sound.get("media/Pistol_Shot.wav");
	
	public GunPistol(GunManager gunManager) {
		super(gunManager, KEY_CODE, gunName);
		setLocked(UNLOCK);
		unlock();
		semiShotDelay = initialShotDelay;
		UpgradeManager um = new UpgradeManager(gunManager.getShopManager(), gunName);
		um.addUpgrade(fireRateUpgrade);
		um.addUpgrade(new UpgradeItem(DAMAGE_ITEM, 3, initialDamage, -10));
		um.observer().watch(fireRateObserve);
		setUpgradeManager(um);
		automaticFire = false;
	}
	
	public void shoot(Person player, ProjectileContainer container) {
		ProjectilePistol.create(player, container, getUpgradeManager());
		sound.play();
	}
	
	private Observer.Change<UpgradeItem> fireRateObserve = new Observer.Change<UpgradeItem>() {
		@Override
		public void observeChange(UpgradeItem t) {
			if (t == fireRateUpgrade) {
				semiShotDelay = t.value;
			}
		}
	};

}
