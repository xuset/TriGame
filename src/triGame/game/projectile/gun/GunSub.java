package triGame.game.projectile.gun;


import java.awt.event.KeyEvent;

import tSquare.system.Sound;
import tSquare.util.Observer;
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
	private static final int initialShotDelay = 180;
	private static final int initialDamage = -20;
	private static final String gunName = "Sub";
	
	private UpgradeItem fireRateUpgrade = new UpgradeItem(FIRE_RATE_ITEM, 3, initialShotDelay, -15);
	private Sound sound = Sound.get("media/Pistol_Shot.wav");
	
	public GunSub(GunManager gunManager) {
		super(gunManager, KEY_CODE, gunName);
		semiShotDelay = initialShotDelay;
		autoShotDelay = initialShotDelay;
		UpgradeManager um = new UpgradeManager(gunManager.getShopManager(), gunName);
		um.addUpgrade(fireRateUpgrade);
		um.addUpgrade(new UpgradeItem(DAMAGE_ITEM, 3, initialDamage, -10));
		um.observer().watch(fireRateObserve);
		setUpgradeManager(um);
		setLocked(UNLOCK_ITEM);
		description = "A rapid fire sub-machine gun to defend against evil red triangles";
	}
	
	public void shoot(Person player, ProjectileContainer container) {
		ProjectileSub.create(player, container, getUpgradeManager());
		sound.play();
	}
	
	private Observer.Change<UpgradeItem> fireRateObserve = new Observer.Change<UpgradeItem>() {
		@Override
		public void observeChange(UpgradeItem t) {
			if (t == fireRateUpgrade) {
				semiShotDelay = t.value;
				autoShotDelay = t.value;
			}
		}
	};
}
